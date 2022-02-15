package com.dguner.auspolbot.listeners;

import com.dguner.auspolbot.builders.TweetBuilder;
import com.dguner.auspolbot.configuration.TwitterConfiguration;
import com.dguner.auspolbot.db.entities.Bill;
import com.dguner.auspolbot.db.repositories.BillRepository;
import com.dguner.auspolbot.extractors.BillExtractor;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@EnableConfigurationProperties(TwitterConfiguration.class)
public class BillListener implements Runnable {
    private final BillRepository billRepository;
    private final BillExtractor billExtractor;
    private final TwitterClient twitterClient;
    private final TweetBuilder tweetBuilder;
    private final TwitterConfiguration twitterConfiguration;
    private static final String URL = "https://www.aph.gov.au/Parliamentary_Business/Bills_Legislation/Bills_Search_Results?st=1&sr=1&q=&drt=1&drv=7&drvH=7&pnuH=0&f=02%2F07%2F2019&to=15%2F09%2F2100&pf=dd%2Fmm%2Fyyyy&pto=dd%2Fmm%2Fyyyy&bs=1&pbh=1&bhor=1&ra=1&np=1&pmb=1&g=1&ps=100&page=";

    public BillListener(BillRepository billRepository, TwitterConfiguration twitterConfiguration) {
        this.billRepository = billRepository;
        this.twitterConfiguration = twitterConfiguration;
        billExtractor = new BillExtractor();
        tweetBuilder = new TweetBuilder();
        twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken(twitterConfiguration.getAccessToken())
                .accessTokenSecret(twitterConfiguration.getAccessTokenSecret())
                .apiKey(twitterConfiguration.getApiKey())
                .apiSecretKey(twitterConfiguration.getApiSecretKey()).build());
    }

    @Override
    public void run() {
        Document doc;
        try {
            List<Bill> billsToSave = new ArrayList<>();
            List<List<String>> threads = new ArrayList<>();
            Iterable<Bill> existingBills = billRepository.findAll();
            int pageNumber = 1;
            while (true) {
                doc = Jsoup.connect(URL + pageNumber).get();
                List<Bill> bills = billExtractor.extractBills(doc);

                // Title is a primary key for bills, so any new bill with the same title as an existing one is one that already exists.
                // If the new Page is different to the old Page the bill has been moved to the new page and should be treated as new.
                // We then use equals to compare every field and find the fields that have changed to tweet and update the bill record
                for (Bill bill : bills) {
                    Optional<Bill> existingBill = StreamSupport.stream(existingBills.spliterator(), false).filter(b -> b.getId().equals(bill.getId())).findAny();
                    if (existingBill.isEmpty()) {
                        billsToSave.add(bill);
                        threads.add(tweetBuilder.buildNewBillThread(bill));
                    } else if (!existingBill.get().equals(bill)) {
                        billsToSave.add(bill);
                        if (tweetBuilder.shouldMakeUpdateTweet(existingBill.get(), bill)) {
                            threads.add(tweetBuilder.buildBillUpdateThread(existingBill.get(), bill));
                        }
                    }
                }

                Element nextPageButton = doc.selectFirst("a[title=\"Next page\"]");
                if (nextPageButton != null) {
                    pageNumber++;
                } else {
                    break;
                }
            }

            billRepository.saveAll(billsToSave);

            if (twitterConfiguration.isShouldTweet()) {
                for (List<String> thread : threads) {
                    String prevTweetId = null;
                    for (int i = 0; i < thread.size(); i++) {
                        String tweet = thread.get(i);
                        if (thread.size() > 1) {
                            tweet = (i + 1) + "/" + thread.size() + " " + tweet;
                        }

                        if (prevTweetId == null) {
                            prevTweetId = twitterClient.postTweet(tweet).getId();
                        } else {
                            prevTweetId = twitterClient.postTweet(tweet, prevTweetId).getId();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
