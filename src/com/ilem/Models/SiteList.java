package com.ilem.Models;


import java.util.HashMap;


/**
 * Created by laassiri on 25/03/15.
 */
public class SiteList {

    public HashMap<String,Site> list = new HashMap<>();

    private static SiteList ourInstance = new SiteList();

    public static SiteList getInstance() {
        return ourInstance;
    }

    private SiteList() {
    }
}
