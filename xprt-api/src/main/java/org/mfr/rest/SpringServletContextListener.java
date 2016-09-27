package org.mfr.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringServletContextListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	String path=event.getServletContext().getRealPath("/");
    	System.setProperty("servlet.path", path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
