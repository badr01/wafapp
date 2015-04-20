package com.ilem.Models;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by laassiri on 16/04/15.
 */
public class Backups {
    @Id
    @ObjectId // automapping key to object _id
    private String key;
    private Date date;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private List<Site> sites;

    public Backups() {
    }

    public Backups(Date date, List<Site> sites) {
        this.date = date;
        this.sites = sites;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
}
