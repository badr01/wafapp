package com.ilem.RestService;



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
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response produceJSON(@Context HttpServletRequest request) throws URISyntaxException {
        HttpSession session = request.getSession();
        session.invalidate();
        return Response.seeOther(new URI("../")).build();
    }
}
