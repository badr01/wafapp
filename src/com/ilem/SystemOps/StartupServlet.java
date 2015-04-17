package com.ilem.SystemOps;

import com.ilem.Models.Settings;
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

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
   ObjectMapper mapper = new ObjectMapper();
        Settings settings = null;

        try {
            settings = mapper.readValue(servletContextEvent.getServletContext().getResourceAsStream("WEB-INF/data.json"), Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(settings.getHaproxyConfDir());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
