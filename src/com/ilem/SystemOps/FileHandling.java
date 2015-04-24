package com.ilem.SystemOps;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;
import com.ilem.Models.Site;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

//class for updating or creating files from database
public class FileHandling {
    public static Logger log = LogManager.getLogger(FileHandling.class.getName());

    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
        Velocity.init(p);
    }

    public static Settings settings;

    public static void updateNginx() {
        log.debug("Entering updateNginx()");
        settings = MongoConnection.getInstance().getSettings();
        List<Site> list = MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/nginx_conf.vm");
        VelocityContext context = new VelocityContext();
        context.put("sites", list);
        context.put("settings", settings);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        BufferedWriter bw = null;
        try {
            File NginxConfDir = new File(settings.getNginxConfDir());
            if (!NginxConfDir.exists()) {
                NginxConfDir.mkdirs();
            }
            File sortie = new File(NginxConfDir, "nginx.conf");

            bw = new BufferedWriter(new FileWriter(sortie));

            bw.write(writer.toString());

            bw.close();
        } catch (IOException e) {
            log.error("IO error while writing to file", e);
        }
        log.debug("Leaving updateNginx()");
    }

    public static void updateHaproxy() {
        log.debug("Entering updateHaproxy()");
        settings = MongoConnection.getInstance().getSettings();
        List<Site> list = MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/haproxy_conf.vm");
        VelocityContext context = new VelocityContext();
        context.put("sites", list);
        context.put("rate", "100");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        BufferedWriter bw = null;
        try {
            File HaproxyConfDir = new File(settings.getHaproxyConfDir());
            if (!HaproxyConfDir.exists()) {
                HaproxyConfDir.mkdirs();
            }
            File sortie = new File(HaproxyConfDir, "haproxy.cfg");

            bw = new BufferedWriter(new FileWriter(sortie));

            bw.write(writer.toString());

            bw.close();
        } catch (IOException e) {
            log.error("IO error while writing to file", e);
        }
        log.debug("Leaving updateHaproxy()");
    }

    public static void updateEnabledSites() {
        log.debug("Entering updateEnabledSites()");
        settings = MongoConnection.getInstance().getSettings();
        List<Site> list = MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/site.vm");
        Template t1 = Velocity.getTemplate("templates/error_html.vm");
        VelocityContext context = new VelocityContext();
        VelocityContext context1 = new VelocityContext();

        for (Iterator<Site> i = list.iterator(); i.hasNext(); ) {
            Site site = i.next();
            context.put("port", site.getPort());
            context.put("server_name", site.getNomDomaine());
            context.put("ip_address", site.getIp());
            context.put("ishttps", site.isHttps());
            context.put("settings", settings);
            context1.put("message", site.getMsgError());
            context1.put("server_name", site.getNomDomaine());
            StringWriter writer = new StringWriter();
            StringWriter writer1 = new StringWriter();
            t.merge(context, writer);
            t1.merge(context1, writer1);


            BufferedWriter bw = null;
            BufferedWriter bw1 = null;
            try {
                File sitesEnabledDir = new File(settings.getSitesEnabledDir());
                if (!sitesEnabledDir.exists()) {
                    sitesEnabledDir.mkdirs();
                }
                File errorHtmlDir = new File(settings.getNginxConfDir(), "html");
                if (!errorHtmlDir.exists()) {
                    errorHtmlDir.mkdirs();
                }

                File sortie = new File(sitesEnabledDir, site.getNomDomaine() + ".conf");
                File sortie1 = new File(errorHtmlDir, site.getNomDomaine() + "_erreur.html");
                bw = new BufferedWriter(new FileWriter(sortie));
                bw1 = new BufferedWriter(new FileWriter(sortie1));

                bw.write(writer.toString());
                bw1.write(writer1.toString());

                bw.close();
                bw1.close();
            } catch (IOException e) {
                log.error("IO error while writing to file", e);
            }
            if (site.isHttps()) {
                File SiteCertDir = new File(settings.getSiteCertDir());
                if (!SiteCertDir.exists()) {
                    SiteCertDir.mkdirs();
                }
                File cert = new File(SiteCertDir, "cert_" + site.getNomDomaine() + ".pem");
                File pkey = new File(SiteCertDir, "key_" + site.getNomDomaine() + ".key");
                BufferedWriter bwcert = null;
                BufferedWriter bwkey = null;
                try {
                    bwkey = new BufferedWriter(new FileWriter(pkey));
                    bwcert = new BufferedWriter(new FileWriter(cert));

                    bwkey.write(site.getPkey());
                    bwcert.write(site.getCert());

                    bwkey.close();
                    bwcert.close();
                } catch (IOException e) {
                    log.error("IO error while writing to file", e);
                }
            }
        }
        log.debug("Leaving updateEnabledSites()");

    }

    public static void updateNaxsiRules() {
        log.debug("Entering updateNaxsiRules()");

        settings = MongoConnection.getInstance().getSettings();
        List<Site> list = MongoConnection.getInstance().getSites();
        Template t = Velocity.getTemplate("templates/rules.vm");
        VelocityContext context = new VelocityContext();

        for (Iterator<Site> i = list.iterator(); i.hasNext(); ) {
            Site site = i.next();
            context.put("wls", site.getWlList());
            context.put("isLearning", site.isMode());
            StringWriter writer = new StringWriter();
            t.merge(context, writer);

            BufferedWriter bw = null;
            try {
                File NaxsiConfDir = new File(settings.getNaxsiConfDir());
                if (!NaxsiConfDir.exists()) {
                    NaxsiConfDir.mkdirs();
                }
                File sortie = new File(NaxsiConfDir, "naxsi_" + site.getNomDomaine() + ".rules");

                bw = new BufferedWriter(new FileWriter(sortie));

                bw.write(writer.toString());

                bw.close();
            } catch (IOException e) {
                log.error("IO error while writing to file", e);
            }
        }
        log.debug("Leaving updateNaxsiRules()");

    }
}
