package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import org.codehaus.jettison.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 25/03/15.
 */

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
public class LogsService {
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
