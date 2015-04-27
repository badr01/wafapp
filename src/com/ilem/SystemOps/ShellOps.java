package com.ilem.SystemOps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Created by laassiri on 14/04/15.
 */
public class ShellOps {
    public static Logger log = LogManager.getLogger(ShellOps.class.getName());
    //list of permitted shell commands
    public static final HashMap<String,String> ops=new HashMap<>();
    static{
        ops.put("NGINX_STATUS","service nginx status");
        ops.put("HAPROXY_STATUS","service haproxy status");
        ops.put("NGINX_START","service nginx start");
        ops.put("HAPROXY_START","service haproxy start");
        ops.put("NGINX_STOP","service nginx stop");
        ops.put("HAPROXY_STOP","service haproxy stop");
        ops.put("NGINX_RESTART","service nginx restart");
        ops.put("HAPROXY_RESTART","service haproxy restart");
        ops.put("NGINX_TEST","service nginx configtest");
        ops.put("HAPROXY_TEST","service haproxy checkconf");
    }
    //execute command and get output
    public static String Execute(String Commande){

    StringBuffer output = new StringBuffer();


    log.debug("Entering Execute(commande={})", Commande);

    Process p;
    try {

        p = Runtime.getRuntime().exec(Commande);

        p.waitFor();

        BufferedReader readerInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        BufferedReader readerError = new BufferedReader(new InputStreamReader(
                p.getErrorStream()));

        String line;
        while ((line = readerInput.readLine()) != null) {
            output.append(line + "\n");

        }
        readerInput.close();
        while ((line = readerError.readLine()) != null) {
            output.append(line + "\n");

        }
        readerError.close();
        p.destroy();

    }catch (IOException e) {
        log.error("Unexpected IO error while reading shell command output",e);
    }
    catch (InterruptedException e) {
        log.error("error while waiting for process to terminate",e);
    }
        log.debug("Leaving Execute: returned {}", output.toString());
        return output.toString();
    }
}
