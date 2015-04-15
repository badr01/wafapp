package com.ilem.DBAccess;

import com.ilem.Models.Settings;
import com.ilem.Models.Site;
import com.mongodb.*;
import com.mongodb.util.JSON;
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

/**
 * Created by laassiri on 25/03/15.
 */
public class MongoConnection {
    private DB db =null;
    private Jongo jongo = null;
    private MongoCollection sites = null;
    private MongoCollection settings = null;
    private DBCollection errorLogs = null;
    private DBCollection accessLogs = null;
    {
    try{
    this.db = new MongoClient("localhost", 27017).getDB("WAF");
    }catch(UnknownHostException e) {
        e.printStackTrace();
    }
        this.jongo = new Jongo(db);
        this.sites = jongo.getCollection("sites");
        this.settings = jongo.getCollection("settings");
        this.errorLogs = db.getCollection("naxsi_error");
        this.accessLogs = db.getCollection("naxsi_access");

    }

    private static MongoConnection ourInstance = new MongoConnection();

    public static MongoConnection getInstance() {
        return ourInstance;
    }

    private MongoConnection() {}


    //get sites from db
    public List<Site> getSites(){
        MongoCursor<Site> cursor = sites.find().as(Site.class);
        List<Site> allSites=new ArrayList<>();
        while (cursor.hasNext()){
            allSites.add(cursor.next());
        }
        return allSites;
    }



    //get a settings from db
    public Settings getSettings(){
        return settings.findOne().as(Settings.class);
    }
    //create a new settings or modify a new ones from db
    public String saveSettings(Settings settings){
        return sites.save(settings).toString();
    }


    //get a specific site from db
    public Site getSite(String site){
        return sites.findOne("{nomDomaine: '"+site+"'}").as(Site.class);
    }
    //create a new site or modify a new one from db
    public String saveSite(Site site){
        return sites.save(site).toString();
    }



    //delete site from db
    public String removeSite(String site){
       return sites.remove("{nomDomaine: '"+site+"'}").toString();
    }


    //get the error logs from db
    public JSONArray getExtLogs(String from,String to){
        BasicDBObject addToset = new BasicDBObject("rule_id","$rule_id");
        addToset.put("zone","$zone");
        addToset.put("var_name","$var_name");
        addToset.put("content","$content");
        BasicDBObject rules = new BasicDBObject("$addToSet",addToset);
        BasicDBObject _id = new BasicDBObject("worker_id","$worker_id");
        _id.put("host","$host");
        _id.put("path","$path");
        _id.put("time","$time");
        _id.put("client_ip","$client_ip");
        BasicDBObject group = new BasicDBObject("_id",_id);
        BasicDBObject grp =new BasicDBObject("$group",group);
        group.put("rules",rules);
        BasicDBObject log_type = new BasicDBObject("log_type","NAXSI_EXLOG");
        if(from!=null||to!=null)log_type.putAll((Map) dateQueryBuild(from,to));
        BasicDBObject match =new BasicDBObject("$match",log_type);
        List<DBObject> pipeline=new ArrayList<>();
        pipeline.add(match);
        pipeline.add(grp);

      JSON json =new JSON();

        AggregationOutput cursor = errorLogs.aggregate(pipeline);
        JSONArray AllJson=null;
        String serialize = json.serialize(cursor.results());
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }


    //get the access logs from db
    public JSONArray getAccessLogs(String from,String to){

        BasicDBObject query=dateQueryBuild(from,to);

        JSON json =new JSON();
        DBCursor cursor = accessLogs.find(query);
        JSONArray AllJson=null;
        String serialize = json.serialize(cursor);
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }

    //convert iso date to java Date object
    public static Date isoDate(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date=null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //building the query to adapt to dates from and to
    public static BasicDBObject dateQueryBuild(String from,String to ){
        if(from!=null||to!=null){
            BasicDBObject match = new BasicDBObject();
            if(from!=null){match.append("$gte", isoDate(from));}
            if(to!=null){match.append("$lt",isoDate(to));}
            return new BasicDBObject("time",match);
        }
        return null;
    }
}

