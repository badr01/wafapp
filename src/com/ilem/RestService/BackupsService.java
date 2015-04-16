package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Backups;
import com.ilem.Models.Settings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by laassiri on 16/04/15.
 */
@Path("/backups")
public class BackupsService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/settings and returns a JSON array of Site object
    public List<Backups> getBackupsJSON(){

        return MongoConnection.getInstance().getBackups(false);
    }
}
