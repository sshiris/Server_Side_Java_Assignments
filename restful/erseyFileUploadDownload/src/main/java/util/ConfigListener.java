package util;
import java.io.File;

import helper.FileHandler;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
public class ConfigListener implements ServletContextListener {
    
        private String destinationDir;
        private String separator=File.separator;
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextListener) {
      
        ServletContext servletContext = servletContextListener.getServletContext();
        destinationDir=servletContext.getRealPath(servletContext.getInitParameter("destination_dir")) + separator;
       
        File file = new File(destinationDir);
        if(!file.exists())
            file.mkdirs();
        
        FileHandler.setDestinationDir(destinationDir);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}