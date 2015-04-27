package com.ilem.DBAccess;

import com.ilem.Models.Backups;
import com.ilem.Models.Settings;
import com.ilem.Models.Site;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jongo.Oid.withOid;

/**
 * Created by laassiri on 25/03/15.
 */
public class MongoConnection {
    public static Logger log = LogManager.getLogger(MongoConnection.class.getName());
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
            log.error("Database connection error", e);
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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            log.error("Error while parsing date", e);
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
        BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject("time", -1));
        BasicDBObject limit = new BasicDBObject("$limit",300);
        pipeline.add(match);
        pipeline.add(grp);
        pipeline.add(sort);
        pipeline.add(limit);
        return pipeline;
    }
    //-----------------------------------------------------------------------

    //------------------------Websites db operations--------------------
    //get sites from db
    public List<Site> getSites() {
        log.debug("Entering getSites()");
        MongoCursor<Site> cursor = sites.find().as(Site.class);
        List<Site> allSites = new ArrayList<>();
        while (cursor.hasNext()) {
            allSites.add(cursor.next());
        }
        log.debug("Leaving getSites(): returned {}",allSites);
        return allSites;
    }

    //get a specific site from db
    public Site getSite(String id) {
        log.debug("Entering getSite(id={})",id);
        Site site=sites.findOne(withOid(id)).as(Site.class);
        log.debug("Leaving getSite(id):returned {}", site);
        return site;
    }

    //create a new site or modify a new one from db
    public String saveSite(Site site) {
        log.debug("Entering saveSite(site={})",site);
        String result=sites.save(site).toString();
        log.debug("Leaving saveSite(site): returned {}",result);
        return result;
    }

    //insert an array of sites to db
    public String saveSites(List<Site> sitesList) {
        log.debug("Entering saveSites(sitesList={})",sitesList);
        String result=sites.insert(sitesList.toArray()).toString();
        log.debug("Leaving saveSites(sitesList):returned {}",result);
        return result;
    }

    //delete site from db
    public String removeSite(String id) {
        log.debug("Entering removeSite(id={})",id);
        String result = sites.remove(withOid(id)).toString();
        log.debug("Leaving removeSite(id): returned {}",result);
        return result;
    }

    //remove allsites from db
    public void removeAllSites() {
        log.debug("Entering removeAllSites()");
        sites.drop();
        log.debug("Leaving removeAllSites()");
    }
    //--------------------------------------------------------------------------------------------------

    //------------------------------settings db operations--------------------------------------
    //get a settings from db
    public Settings getSettings() {
        log.debug("Entering getSettings()");
        Settings setting=settings.findOne().as(Settings.class);
        log.debug("Leaving getSettings():returned {}",setting);
        return setting ;
    }

    //create a new settings or modify a new ones from db
    public String saveSettings(Settings setting) {
        log.debug("Entering saveSettings(settings={})", setting);
        String result=settings.save(setting).toString();
        log.debug("Leaving saveSettings(settings): returned {}",result);
        return result;
    }

    //drop setting collection from db
    public void dropSettings() {
        log.debug("Entering dropSettings()");
        settings.drop();
        log.debug("Leaving dropSettings()");

    }

    //count setting collection records in db
    public long countSettings() {
        log.debug("Entering countSettings()");
        long l =settings.count();
        log.debug("Leaving countSettings(): returned {}",l);
        return l;



    }

    //-------------------------------------------------------------------------------------------------------

    //------------------------------------Logs db operations--------------------------------------------------
    //get the error logs from db
    public JSONArray getExtLogs(String from, String to) {
        log.debug("Entering getExtLogs(from={},to={})", from, to);
        JSON json = new JSON();
        AggregationOutput cursor = errorLogs.aggregate(dateAggBuild(from, to));
        JSONArray AllJson = null;
        String serialize = json.serialize(cursor.results());
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            log.error("error while serializing JSON array", e);
        }
        log.debug("Leaving getExtLogs(from,to):returned {} element", AllJson.length());
        return AllJson;
    }

    //get the access logs from db
    public JSONArray getAccessLogs(String from, String to) {
        log.debug("Entering getAccessLogs(from={},to={})", from, to);

        BasicDBObject query = dateQueryBuild(from, to);
        BasicDBObject sort = new BasicDBObject("time", -1);
        JSON json = new JSON();
        DBCursor cursor = accessLogs.find(query).sort(sort).limit(300);
        JSONArray AllJson = null;
        String serialize = json.serialize(cursor);
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        log.debug("Leaving getAccessLogs(from,to):returned {} element", AllJson.length());

        return AllJson;
    }
    //--------------------------------------------------------------------------------------------------

    //-----------------------------------------------------Backups db operations------------------------
    //get backups list from db
    public List<Backups> getBackups(boolean isProjected) {
        log.debug("Entering getBackups(isProjected={})",isProjected);
        MongoCursor<Backups> cursor;
        if (isProjected) {
            cursor = backups.find().limit(10).sort("{date:-1}").as(Backups.class);
        } else {
            cursor = backups.find().projection("{date:1}").limit(10).sort("{date:-1}").as(Backups.class);
        }
        List<Backups> allBackups = new ArrayList<>();
        while (cursor.hasNext()) {
            allBackups.add(cursor.next());
        }
        log.debug("Leaving getBackups(isProjected): returned {} element",allBackups.size());

        return allBackups;
    }

    //save backups to db
    public String saveBackups(Backups backup) {
        log.debug("Entering saveBackups(backup={})",backup);
        String result=backups.save(backup).toString();
        log.debug("Leaving saveBackups(backup): returned {}",result);
        return result;
    }

    //get backup by key
    public Backups getbackup(String key) {
        log.debug("Entering getbackup(key={})",key);
        Backups backup=backups.findOne(withOid(key)).as(Backups.class);
        log.debug("Leaving getbackup(key): returned {}",backup);
        return backup;
    }
}
//-------------------------------------------------------------------------------------------------------
