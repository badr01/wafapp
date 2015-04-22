package com.ilem.SystemOps;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;


/**
 * Created by laassiri on 17/04/15.
 */
public class StartupServlet implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Logger log = LogManager.getLogger(StartupServlet.class.getName());
        log.info("application initialization ");
        log.debug("Entering contextInitialized(ServletContextEvent={})", servletContextEvent);
        if (MongoConnection.getInstance().countSettings() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            Settings settings = null;
            try {
                settings = mapper.readValue(servletContextEvent.getServletContext().getResourceAsStream("WEB-INF/initialSettings.json"), Settings.class);
            } catch (IOException e) {
                log.error("IO error while reading resource stream", e);
            }
            MongoConnection.getInstance().saveSettings(settings);
        }
        log.debug("Leaving contextInitialized()");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Logger log = LogManager.getLogger(StartupServlet.class.getName());
        log.info("Stopping the application");
    }
}
