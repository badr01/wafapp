package com.ilem.SystemOps;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Site;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by laassiri on 06/04/15.
 */
public class FileHandling {
    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }
    public static void updateNginx(){
        List<Site> list =MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/nginx_conf.vm");
        VelocityContext context = new VelocityContext();
        context.put("sites", list);
           StringWriter writer = new StringWriter();
            t.merge(context, writer);

            File sortie = new File("/home/laassiri/nginx.conf");
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(sortie));

                bw.write(writer.toString());

                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static void updateHaproxy(){

        List<Site> list =MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/haproxy_conf.vm");
        VelocityContext context = new VelocityContext();
        context.put("sites", list);
        context.put("rate", "100");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        File sortie = new File("/home/laassiri/haproxy.conf");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(sortie));

            bw.write(writer.toString());

            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void updateEnabledSites(){
        List<Site> list =MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/site.vm");
        VelocityContext context = new VelocityContext();

        for(Iterator<Site> i=list.iterator();i.hasNext();){
            Site site=i.next();
            context.put("port", site.getPort());
            context.put("server_name", site.getNomDomaine());
            context.put("ip_address", site.getIp());
            context.put("ishttps",site.isHttps());
            StringWriter writer = new StringWriter();
            t.merge(context, writer);

            File sortie = new File("/home/laassiri/"+site.getNomDomaine()+".conf");
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(sortie));

                bw.write(writer.toString());

                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void updateNaxsiRules(){

    }
}
