package util;
import java.io.File;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import helper.FileHandler;
public class ConfigListener implements ServletContextListener {
private String destinationDir;
private final String separator = File.separator;
@Override
public void contextInitialized(ServletContextEvent servletContextEvent) {
ServletContext servletContext = servletContextEvent.getServletContext();
// Retrieve the context parameter from web.xml
destinationDir = servletContext.getRealPath(servletContext.getInitParameter("destination_dir")) + separator;
File directory = new File(destinationDir);
if (!directory.exists()) {
directory.mkdirs();
}
// Set the destination directory in the FileHandler utility
FileHandler.setDestinationDir(destinationDir);
}
@Override
public void contextDestroyed(ServletContextEvent servletContextEvent) {
}
}