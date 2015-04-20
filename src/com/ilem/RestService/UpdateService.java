package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Backups;
import com.ilem.SystemOps.FileHandling;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by laassiri on 14/04/15.
 */
@Path("/update")
public class UpdateService {
    public static Logger log = Logger.getLogger(UpdateService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/status
    public Response produceJSON() {
        FileHandling.updateEnabledSites();
        FileHandling.updateNginx();
        FileHandling.updateHaproxy();
        FileHandling.updateNaxsiRules();
        Backups backup = new Backups(new Date(),MongoConnection.getInstance().getSites());
        MongoConnection.getInstance().saveBackups(backup);
        return Response.status(200).build();
    }
}
