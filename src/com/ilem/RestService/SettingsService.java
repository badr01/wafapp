package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 15/04/15.
 */
@Path("/settings")
public class SettingsService {
    public static Logger log = LogManager.getLogger(SettingsService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/settings and returns a JSON array of Settings object
    public Settings getSettingsJSON(){
        log.debug("Entering getSettingsJSON()");
        Settings setting=MongoConnection.getInstance().getSettings();
        log.debug("Leaving getSettingsJSON(): returned {}",setting);
        return setting;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //method handling POST requests to /api/site,it takes a Settings object as param and returns a success response status
    public Response saveSettingsJSON(Settings settings){
        log.debug("Entering saveSettingsJSON()");
        MongoConnection.getInstance().dropSettings();
        Response res=Response.status(200).entity(MongoConnection.getInstance().saveSettings(settings)).build();
        log.debug("Leaving saveSettingsJSON(): returned {}",res);
       return res;
    }
}
