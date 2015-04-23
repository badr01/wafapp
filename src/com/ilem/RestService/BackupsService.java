package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Backups;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by laassiri on 16/04/15.
 */
@Path("/backups")
public class BackupsService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/backups and returns a JSON array of Backups object
    public List<Backups> getBackupsJSON(){
        List<Backups> list=MongoConnection.getInstance().getBackups(false);
        return list;
    }

    @GET
    @Path("/restore/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/Backups/restore/{key} and returns a success Response status
    public Response restoreJSON( @PathParam("key") String key){
        Backups backup= MongoConnection.getInstance().getbackup(key);
        MongoConnection.getInstance().removeAllSites();
        return Response.status(200).entity(MongoConnection.getInstance().saveSites(backup.getSites())).build();
    }


}
