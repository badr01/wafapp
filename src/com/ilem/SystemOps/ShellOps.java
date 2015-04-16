package com.ilem.SystemOps;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by laassiri on 14/04/15.
 */
public class ShellOps {
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
    Logger log = Logger.getLogger(ShellOps.class.getName());

    log.info("La commande lancée est : " + Commande);

    Process p;
    try {

        p = Runtime.getRuntime().exec(Commande);

        p.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");

        }
        reader.close();
        log.info("Le retour de la commande lancée est : "
                + output.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }

    return output.toString();
    }
}
