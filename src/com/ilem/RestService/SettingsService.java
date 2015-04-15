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
    //method handling GET requests to /api/settings and returns a JSON array of Site object
    public Settings getSettingsJSON(){

        return MongoConnection.getInstance().getSettings();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //method handling POST requests to /api/site,it takes a Site object as param and returns a success response status
    public Response postJSON(Settings settings){

        return Response.status(200).entity(MongoConnection.getInstance().saveSettings(settings)).build();
    }
}
