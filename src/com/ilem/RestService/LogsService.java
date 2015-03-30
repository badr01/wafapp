package com.ilem.RestService;

import com.ilem.DBAccess.MongoConnection;
import org.codehaus.jettison.json.JSONArray;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public String errorJSON(){
        return MongoConnection.getInstance().getExtLogs().toString();
    }

    @GET
    @Path("/access")
    @Produces(MediaType.APPLICATION_JSON)
    public String accessJSON(){
        return MongoConnection.getInstance().getAccessLogs().toString();
    }
}
