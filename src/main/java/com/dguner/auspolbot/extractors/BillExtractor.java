package com.dguner.auspolbot.extractors;

import com.dguner.auspolbot.db.entities.Bill;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BillExtractor {
    /**
     * Extracts the list of bills found in the given doc
     *
     * @param doc
     * @return
     */
    public List<Bill> extractBills(Document doc) {
        List<Bill> bills = new ArrayList<>();
        Elements titleElements = doc.getElementsByClass("medium-11 small-8 columns");
        for (Element titleElement : titleElements) {
            Element parent = titleElement.parent();
            if (parent != null) {
                Element info = parent.nextElementSibling();
                bills.add(mapInfoToBill(titleElement, info));
            }

        }
        return bills;
    }

    private Bill mapInfoToBill(Element titleElement, Element info) {
        String title = titleElement.text().trim();
        String link = titleElement.selectFirst("a").attr("href");
        String id = link.split("\\?bId=")[1];
        String status = null;
        String date = null;
        String chamber = null;
        String sponsor = null;
        String portfolio = null;
        String summary = null;
        String field = null;
        for (Element infoElement : info.selectFirst("dl").children()) {
            String text = infoElement.text().trim();
            if (field == null) {
                field = text.toLowerCase();
            } else {
                switch (field) {
                    case "status":
                        status = text;
                        break;
                    case "date":
                        date = text;
                        break;
                    case "chamber":
                        chamber = text;
                        break;
                    case "sponsor":
                        sponsor = text;
                        break;
                    case "portfolio":
                        portfolio = text;
                        break;
                    case "summary":
                        summary = text;
                        break;
                }

                field = null;
            }

        }

        return new Bill(id, title, status, date, chamber, sponsor, portfolio, summary);
    }
}
