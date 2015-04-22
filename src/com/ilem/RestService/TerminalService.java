package com.ilem.RestService;

import com.ilem.SystemOps.ShellOps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by laassiri on 24/03/15.
 */

@Path("/terminal")
public class TerminalService {
    public static Logger log = LogManager.getLogger(TerminalService.class.getName());
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //method handling GET requests to /api/terminal?cmd=######
    public Output executeCmdJSON(@QueryParam("cmd") String cmd){
        log.debug("Entering executeCmdJSON(cmd={})",cmd);
        Output obj=new Output();
        obj.output="Command not allowed!";
        if(ShellOps.ops.get(cmd)!=null){
        obj.output=ShellOps.Execute(ShellOps.ops.get(cmd));
        }
        obj.nginxStatus= ShellOps.Execute(ShellOps.ops.get("NGINX_STATUS")).equals(" * nginx is running\n");
        obj.haproxyStatus= ShellOps.Execute(ShellOps.ops.get("HAPROXY_STATUS")).equals("haproxy is running.\n");
        log.debug("Leaving executeCmdJSON(cmd): returned {}",obj);
        return obj;
    }
    public class Output{
       public String output;
       public boolean nginxStatus;
       public boolean haproxyStatus;
    }
    }



