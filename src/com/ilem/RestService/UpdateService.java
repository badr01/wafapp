package com.ilem.RestService;

import com.ilem.SystemOps.FileHandling;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 14/04/15.
 */
@Path("/update")
public class UpdateService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/status
    public Response produceJSON(){
        FileHandling.updateNginx();
        FileHandling.updateHaproxy();
        FileHandling.updateEnabledSites();
        FileHandling.updateNaxsiRules();
        return Response.status(200).build();
    }
}