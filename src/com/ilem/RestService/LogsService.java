package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Created by laassiri on 25/03/15.
 */

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
public class LogsService {
    public static Logger log = LogManager.getLogger(LogsService.class.getName());
    @GET
    @Path("/error")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/logs/error
    public String errorJSON(@QueryParam("from") String from,@QueryParam("to") String to){
        return MongoConnection.getInstance().getExtLogs(from,to).toString();
    }

    @GET
    @Path("/access")
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/logs/access
    public String accessJSON(@QueryParam("from") String from,@QueryParam("to") String to){
        return MongoConnection.getInstance().getAccessLogs(from,to).toString();
    }
}
