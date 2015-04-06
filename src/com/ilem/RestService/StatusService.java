package com.ilem.RestService;

import com.ilem.FileHandlilng.FileHandle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by laassiri on 24/03/15.
 */

@Path("/status")
public class StatusService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Status produceJSON(){
        FileHandle.updateNginx();
        FileHandle.updateHaproxy();
        FileHandle.updateEnabledSites();
        return new Status();
    }

    static class Status{
        private String nginx="Eteint";
        private String haproxy="Eteint";

        public String getNginx() {
            return nginx;
        }

        public void setNginx(String nginx) {
            this.nginx = nginx;
        }

        public String getHaproxy() {
            return haproxy;
        }

        public void setHaproxy(String haproxy) {
            this.haproxy = haproxy;
        }

        @Override
        public String toString() {
            return new StringBuffer(" nginx : ").append(this.nginx)
                    .append(" haproxy : ").append(this.haproxy)
                    .toString();
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON(Status status){
       System.out.println(status.toString());
        return Response.status(200).entity(status.toString()).build();
    }
}
