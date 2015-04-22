package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Backups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by laassiri on 16/04/15.
 */
@Path("/backups")
public class BackupsService {
    public static Logger log = LogManager.getLogger(BackupsService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/backups and returns a JSON array of Backups object
    public List<Backups> getBackupsJSON(){
        log.debug("Entering getBackupsJSON()");
        List<Backups> list=MongoConnection.getInstance().getBackups(false);
        log.debug("Leaving getBackupsJSON()");
        return list;
    }

    @GET
    @Path("/restore/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/Backups/restore/{key} and returns a success Response status
    public Response restoreJSON( @PathParam("key") String key){
        log.debug("Entering restoreJSON()");
        Backups backup= MongoConnection.getInstance().getbackup(key);
        MongoConnection.getInstance().removeAllSites();
        log.debug("Leaving restoreJSON():{}",Response.status(200).entity(MongoConnection.getInstance().saveSites(backup.getSites())).build());
        return Response.status(200).entity(MongoConnection.getInstance().saveSites(backup.getSites())).build();
    }


}
