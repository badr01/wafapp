package com.ilem.Models;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.ArrayList;

/**
 * Created by laassiri on 15/04/15.
 */

public class Settings {
    @Id
    @ObjectId // automapping key to object _id
    private String key;
    private String sitesEnabledDir;
    private String nginxConfDir;
    private String naxsiConfDir;
    private String haproxyConfDir;
    private String siteCertDir;
    private ArrayList<Wl> whitelistRules;

    public Settings() {
    }

    public ArrayList<Wl> getWhitelistRules() {
        return whitelistRules;
    }

    public void setWhitelistRules(ArrayList<Wl> whitelistRules) {
        this.whitelistRules = whitelistRules;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNaxsiConfDir() {
        return naxsiConfDir;
    }

    public void setNaxsiConfDir(String naxsiConfDir) {
        this.naxsiConfDir = naxsiConfDir;
    }


    public String getHaproxyConfDir() {
        return haproxyConfDir;
    }

    public void setHaproxyConfDir(String haproxyConfDir) {
        this.haproxyConfDir = haproxyConfDir;
    }

    public String getSiteCertDir() {
        return siteCertDir;
    }

    public void setSiteCertDir(String siteCertDir) {
        this.siteCertDir = siteCertDir;
    }

    public String getNginxConfDir() {
        return nginxConfDir;
    }

    public void setNginxConfDir(String nginxConfDir) {
        this.nginxConfDir = nginxConfDir;
    }

    public String getSitesEnabledDir() {
        return sitesEnabledDir;
    }

    public void setSitesEnabledDir(String sitesEnabledDir) {
        this.sitesEnabledDir = sitesEnabledDir;
    }
}
