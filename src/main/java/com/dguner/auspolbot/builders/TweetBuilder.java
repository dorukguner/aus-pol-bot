package com.dguner.auspolbot.builders;

import com.dguner.auspolbot.db.entities.Bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TweetBuilder {

    public boolean shouldMakeUpdateTweet(Bill oldBill, Bill newBill) {
        return !oldBill.getStatus().equals(newBill.getStatus()) || !Objects.equals(oldBill.getSponsor(), newBill.getSponsor()) || !Objects.equals(oldBill.getChamber(), newBill.getChamber());
    }

    public List<String> buildNewBillThread(Bill bill) {
        boolean fieldHasBeenAdded = false;
        List<String> strings = new ArrayList<>();
        StringBuilder title = new StringBuilder(bill.getTitle())
                .append(" has been introduced to the ")
                .append(bill.getChamber())
                .append(".")
        .append(System.getProperty("line.separator"));
        strings.add(title.toString());

        if (bill.getSponsor() != null && !bill.getSponsor().isEmpty()) {
            fieldHasBeenAdded = true;
            StringBuilder sponsor = new StringBuilder("This bill is sponsored by ")
                    .append(bill.getSponsor())
                    .append(System.getProperty("line.separator"));
            strings.add(sponsor.toString());
        }

        if (bill.getPortfolio() != null && !bill.getPortfolio().isEmpty()) {
            StringBuilder portfolio = new StringBuilder();
            if (fieldHasBeenAdded) {
                portfolio.append(" and ");
            } else {
                portfolio.append("This bill ");
            }

            portfolio.append("is a part of parliament's ")
                    .append(bill.getPortfolio())
                    .append(" portfolio.")
                    .append(System.getProperty("line.separator"));
            strings.add(portfolio.toString());
        }

        strings.add(buildLink(bill.getLink()));

        return accumulateTweets(strings);
    }

    private String buildStatusChangeString(String oldStatus, String newStatus) {
        switch (newStatus) {
            case "Assent":
                return "The bill has assented both houses and will be acted upon" + System.getProperty("line.separator");
            case "Passed Both Houses":
                return "The bill has passed both houses and is waiting on the signature of the Governor" + System.getProperty("line.separator");
            case "Act":
                return "The bill has become an act" + System.getProperty("line.separator");
            case "Not Proceeding":
                return "The bill has not passed " + oldStatus + " and will not be proceeding" + System.getProperty("line.separator");
            default:
                return "The bill has been moved from " + oldStatus + " to " + newStatus + System.getProperty("line.separator");

        }
    }

    private String buildPropertyChangeString(String propertyName, String oldProperty, String newProperty) {
        StringBuilder propertyChangeString = new StringBuilder();
        if (oldProperty != null) {
            propertyChangeString.append("The bill's ")
                    .append(propertyName)
                    .append(" has changed from ")
                    .append(oldProperty)
                    .append(" to ")
                    .append(newProperty);
        } else {
            propertyChangeString.append("A new ")
                    .append(propertyName)
                    .append(" has been added: ")
                    .append(newProperty);
        }

        propertyChangeString.append(System.getProperty("line.separator"));
        return propertyChangeString.toString();
    }

    public List<String> buildBillUpdateThread(Bill oldBill, Bill newBill) {
        List<String> strings = new ArrayList<>();
        StringBuilder title = new StringBuilder(newBill.getTitle())
                .append(" has been updated. The following changes have been made:")
                .append(System.getProperty("line.separator"));
        strings.add(title.toString());


        if (!Objects.equals(oldBill.getStatus(), newBill.getStatus())) {
            strings.add(buildStatusChangeString(oldBill.getStatus(), newBill.getStatus()));
        }

        if (!Objects.equals(newBill.getSponsor(), oldBill.getSponsor())) {
            strings.add(buildPropertyChangeString("sponsor", oldBill.getSponsor(), newBill.getSponsor()));
        }

        if (!Objects.equals(newBill.getPortfolio(), oldBill.getPortfolio())) {
            strings.add(buildPropertyChangeString("parliament portfolio", oldBill.getPortfolio(), newBill.getPortfolio()));
        }

        strings.add(buildLink(newBill.getLink()));

        return accumulateTweets(strings);
    }

    private List<String> accumulateTweets(List<String> strings) {
        List<String> tweets = new ArrayList<>();
        StringBuilder accumulatedTweet = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            if (accumulatedTweet.toString().length() + string.length() >= 275) {
                tweets.add(accumulatedTweet.toString().trim());
                accumulatedTweet = new StringBuilder(string);
            } else {
                accumulatedTweet.append(string);
            }

            if (i >= strings.size() - 1) {
                tweets.add(accumulatedTweet.toString().trim());
            }
        }

        return tweets;
    }

    private String buildLink(String link) {
            return System.getProperty("line.separator") +
                    "The official APH page for this bill can be found at: " +
                    "https://www.aph.gov.au" +
                    link;
    }
}
