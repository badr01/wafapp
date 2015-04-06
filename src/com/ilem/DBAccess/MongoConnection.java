package com.ilem.DBAccess;

import com.ilem.Models.Site;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.net.UnknownHostException;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;

/**
 * Created by laassiri on 25/03/15.
 */
public class MongoConnection {
    private DB db =null;
    private Jongo jongo = null;
    private MongoCollection sites = null;
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
        this.errorLogs = db.getCollection("naxsi_error");
        this.accessLogs = db.getCollection("naxsi_access");

    }

    private static MongoConnection ourInstance = new MongoConnection();

    public static MongoConnection getInstance() {
        return ourInstance;
    }

    private MongoConnection() {}

    public List<Site> getSites(){
        MongoCursor<Site> cursor = sites.find().as(Site.class);
        List<Site> allSites=new ArrayList<>();
        while (cursor.hasNext()){
            allSites.add(cursor.next());
        }
        return allSites;
    }
    public Site getSite(String site){
        return sites.findOne("{nomDomaine: '"+site+"'}").as(Site.class);
    }
    public String saveSite(Site site){
        return sites.save(site).toString();
    }
    public String removeSite(String site){
       return sites.remove("{nomDomaine: '"+site+"'}").toString();
    }
    public JSONArray getExtLogs(){
        BasicDBObject query = new BasicDBObject("log_type","NAXSI_EXLOG");
        JSON json =new JSON();
        DBCursor cursor = errorLogs.find(query);
        JSONArray AllJson=null;
        String serialize = json.serialize(cursor);
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }
    public JSONArray getAccessLogs(){
       // BasicDBObject query = new BasicDBObject("log_type","NAXSI_EXLOG");
        JSON json =new JSON();
        DBCursor cursor = accessLogs.find();
        JSONArray AllJson=null;
        String serialize = json.serialize(cursor);
        try {
            AllJson = new JSONArray(serialize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AllJson;
    }
}
