package com.ilem.RestService;



import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by laassiri on 07/04/15.
 */
@Path("/logout")
public class LogOutService {
    public static Logger log = Logger.getLogger(LogOutService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/logout which deauthenticate the user
    public Response produceJSON(@Context HttpServletRequest request) throws URISyntaxException {
        HttpSession session = request.getSession();
        session.invalidate();
        return Response.seeOther(new URI("../")).build();
    }
}
