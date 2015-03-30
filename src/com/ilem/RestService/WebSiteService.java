package com.ilem.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Site;
import com.ilem.Models.SiteList;

import java.util.Collection;


/**
 * Created by laassiri on 25/03/15.
 */

@Path("/site")
public class WebSiteService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Site> getListJSON(){

        return MongoConnection.getInstance().getSites();
    }
    @GET
    @Path("/{site}")
    @Produces(MediaType.APPLICATION_JSON)
    public Site getJSON( @PathParam("site") String site){
        return MongoConnection.getInstance().getSite(site);
    }
    @DELETE
    @Path("/{site}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJSON( @PathParam("site") String site){

        return Response.status(200).entity(MongoConnection.getInstance().removeSite(site)).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postJSON(Site site){

        return Response.status(200).entity(MongoConnection.getInstance().saveSite(site)).build();
    }
}
