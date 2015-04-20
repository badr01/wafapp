package com.ilem.DBAccess;

import com.ilem.Models.Backups;
import com.ilem.Models.Settings;
import com.ilem.Models.Site;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;


import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.jongo.Oid.withOid;

/**
 * Created by laassiri on 25/03/15.
 */
public class MongoConnection {
    public static Logger log = Logger.getLogger(MongoConnection.class.getName());
    private static MongoConnection ourInstance = new MongoConnection();
    private DB db = null;
    private Jongo jongo = null;
    private MongoCollection sites = null;
    private MongoCollection backups = null;
    private MongoCollection settings = null;
    private DBCollection errorLogs = null;
    private DBCollection accessLogs = null;
    {
        try {
            this.db = new MongoClient("localhost", 27017).getDB("WAF");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.jongo = new Jongo(db);
        this.sites = jongo.getCollection("sites");
        this.backups = jongo.getCollection("backups");
        this.settings = jongo.getCollection("settings");
        this.errorLogs = db.getCollection("naxsi_error");
        this.accessLogs = db.getCollection("naxsi_access");

    }

    private MongoConnection() {
    }

    public static MongoConnection getInstance() {
        return ourInstance;
    }

//-------------------------Helper methods------------------------------------------
    //convert iso date to java Date object
    public static Date isoDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    //building the query to adapt to dates from and to
    public static BasicDBObject dateQueryBuild(String from, String to) {
        if (from != null || to != null) {
            BasicDBObject match = new BasicDBObject();
            if (from != null) {
                match.append("$gte", isoDate(from));
            }
            if (to != null) {
                match.append("$lt", isoDate(to));
            }
            return new BasicDBObject("time", match);
        }
        return null;
    }

     //building the agregation query to adapt to dates from and to
    public static List<DBObject> dateAggBuild(String from, String to) {

        BasicDBObject addToset = new BasicDBObject("rule_id", "$rule_id");
        addToset.put("zone", "$zone");
        addToset.put("var_name", "$var_name");
        addToset.put("content", "$content");
        BasicDBObject rules = new BasicDBObject("$addToSet", addToset);
        BasicDBObject _id = new BasicDBObject("worker_id", "$worker_id");
        _id.put("host", "$host");
        _id.put("path", "$path");
        _id.put("time", "$time");
        _id.put("client_ip", "$client_ip");
        BasicDBObject group = new BasicDBObject("_id", _id);
        BasicDBObject grp = new BasicDBObject("$group", group);
        group.put("rules", rules);
        BasicDBObject log_type = new BasicDBObject("log_type", "NAXSI_EXLOG");
        if (from != null || to != null) log_type.putAll((Map) dateQueryBuild(from, to));
        BasicDBObject match = new BasicDBObject("$match", log_type);
        List<DBObject> pipeline = new ArrayList<>();
        pipeline.add(match);
        pipeline.add(grp);
        return pipeline;
    }
//-----------------------------------------------------------------------

//------------------------Websites db operations--------------------
    //get sites from db
    public List<Site> getSites() {
        MongoCursor<Site> cursor = sites.find().as(Site.class);
        List<Site> allSites = new ArrayList<>();
        while (cursor.hasNext()) {
            allSites.add(cursor.next());
        }
        return allSites;
    }
    //get a specific site from db
    public Site getSite(String site) {
        return sites.findOne(withOid(site)).as(Site.class);
    }

    //create a new site or modify a new one from db
    public String saveSite(Site site) {
        return sites.save(site).toString();
    }
    //insert an array of sites to db
    public String saveSites(List<Site> sitesList) {
        return sites.insert(sitesList.toArray()).toString();
    }

    //delete site from db
    public String removeSite(String site) {
        return sites.remove(withOid(site)).toString();
    }

    //remove allsites from db
    public void removeAllSites() {
        sites.drop();
    }
//--------------------------------------------------------------------------------------------------

//------------------------------settings db operations--------------------------------------
    //get a settings from db
    public Settings getSettings() {
        return settings.findOne().as(Settings.class);
    }

    //create a new settings or modify a new ones from db
    public String saveSettings(Settings setting) {
        return settings.save(setting).toString();
    }

    //drop setting collection from db
    public void dropSettings() {
         settings.drop();
    }

    //drop setting collection from db
    public long countSettings() {
         return settings.count();
    }

//-------------------------------------------------------------------------------------------------------

//------------------------------------Logs db operations--------------------------------------------------
    //get the error logs from db
    public JSONArray getExtLogs(String from, String to) {
        JSON json = new JSON();
        AggregationOutput cursor = errorLogs.aggregate(dateAggBuild(from,to));
        JSONArray AllJson = null;
        String serialize = json.serialize(cursor.results());
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }

    //get the access logs from db
    public JSONArray getAccessLogs(String from, String to) {

        BasicDBObject query = dateQueryBuild(from, to);

        JSON json = new JSON();
        DBCursor cursor = accessLogs.find(query);
        JSONArray AllJson = null;
        String serialize = json.serialize(cursor);
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }
//--------------------------------------------------------------------------------------------------

//-----------------------------------------------------Backups db operations------------------------
    //get backups list from db
    public List<Backups> getBackups(boolean b) {
        MongoCursor<Backups> cursor;
        if(b){
            cursor = backups.find().limit(10).sort("{date:-1}").as(Backups.class);
        }else {
            cursor = backups.find().projection("{date:1}").limit(10).sort("{date:-1}").as(Backups.class);
        }
        List<Backups> allBackups = new ArrayList<>();
        while (cursor.hasNext()) {
            allBackups.add(cursor.next());
        }
        return allBackups;
    }
    //save backups to db
    public String saveBackups(Backups backup) {
        return backups.save(backup).toString();
    }

    //get backup by key
    public Backups getSetting(String key) {
        return backups.findOne(withOid(key)).as(Backups.class);
    }
}
//-------------------------------------------------------------------------------------------------------
