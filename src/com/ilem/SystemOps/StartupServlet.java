package com.ilem.SystemOps;

import com.ilem.DBAccess.MongoConnection;
import com.ilem.Models.Settings;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by laassiri on 17/04/15.
 */
public class StartupServlet implements ServletContextListener {
    public static Logger log = Logger.getLogger(StartupServlet.class.getName());
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        if(MongoConnection.getInstance().countSettings()==0) {
            ObjectMapper mapper = new ObjectMapper();
            Settings settings = null;
            try {
                settings = mapper.readValue(servletContextEvent.getServletContext().getResourceAsStream("WEB-INF/initialSettings.json"), Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MongoConnection.getInstance().saveSettings(settings);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
