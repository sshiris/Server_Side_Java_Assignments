package rest.server.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import rest.server.GreetingServer;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest/ws/")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Explicitly register your REST resource classes
        classes.add(GreetingServer.class);
        return classes;
    }
}