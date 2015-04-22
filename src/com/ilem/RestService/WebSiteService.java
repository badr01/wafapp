package com.ilem.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Site;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by laassiri on 25/03/15.
 */

@Path("/site")
public class WebSiteService {
    public static Logger log = LogManager.getLogger(WebSiteService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/site and returns a JSON array of Site object
    public List<Site> getSiteListJSON(){
        log.debug("Entering getSiteListJSON()");
        List<Site> col=MongoConnection.getInstance().getSites();
        log.debug("Leaving getSiteListJSON():returned {}",col);
        return col;
    }
    @GET
    @Path("/{site}")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/site/{site} and returns a Site object
    public Site getJSON( @PathParam("site") String key){

        log.debug("Entering executeCmdJSON(site={})",key);
        Site site=MongoConnection.getInstance().getSite(key);
        log.debug("Leaving executeCmdJSON(site):returned {}",site);
        return site;
    }
    @DELETE
    @Path("/{site}")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling DELETE requests to /api/site/{site}, it takes a string as param and returns a success response status
    public Response deleteJSON( @PathParam("site") String key){
        log.debug("Entering executeCmdJSON(site={})",key);


        return Response.status(200).entity(MongoConnection.getInstance().removeSite(key)).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //method handling POST requests to /api/site,it takes a Site object as param and returns a success response status
    public Response postJSON(Site site){


        return Response.status(200).entity(MongoConnection.getInstance().saveSite(site)).build();
    }
}
