package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 15/04/15.
 */
@Path("/settings")
public class SettingsService {
    public static Logger log = Logger.getLogger(SettingsService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/settings and returns a JSON array of Settings object
    public Settings getSettingsJSON(){

        return MongoConnection.getInstance().getSettings();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //method handling POST requests to /api/site,it takes a Settings object as param and returns a success response status
    public Response postJSON(Settings settings){
        MongoConnection.getInstance().dropSettings();
       return Response.status(200).entity(MongoConnection.getInstance().saveSettings(settings)).build();
    }
}
