package com.dguner.auspolbot.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Bill {
    @Id
    private String id;
    private String title;
    private String status;
    private String date;
    private String chamber;
    private String sponsor;
    private String portfolio;
    private String summary;

    public Bill() {
    }

    public Bill(String id, String title, String status, String date, String chamber, String sponsor, String portfolio, String summary) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.date = date;
        this.chamber = chamber;
        this.sponsor = sponsor;
        this.portfolio = portfolio;
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return "/Parliamentary_Business/Bills_Legislation/Bills_Search_Results/Result?bId=" + id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(id, bill.id) &&
                Objects.equals(title, bill.title) &&
                Objects.equals(status, bill.status) &&
                Objects.equals(date, bill.date) &&
                Objects.equals(chamber, bill.chamber) &&
                Objects.equals(sponsor, bill.sponsor) &&
                Objects.equals(portfolio, bill.portfolio) &&
                Objects.equals(summary, bill.summary);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", chamber='" + chamber + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", portfolio='" + portfolio + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
