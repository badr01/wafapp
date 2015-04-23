package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 15/04/15.
 */
@Path("/settings")
public class SettingsService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/settings and returns a JSON array of Settings object
    public Settings getSettingsJSON(){
        Settings setting=MongoConnection.getInstance().getSettings();
        return setting;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //method handling POST requests to /api/site,it takes a Settings object as param and returns a success response status
    public Response saveSettingsJSON(Settings settings){
        MongoConnection.getInstance().dropSettings();
        Response res=Response.status(200).entity(MongoConnection.getInstance().saveSettings(settings)).build();
       return res;
    }
}
