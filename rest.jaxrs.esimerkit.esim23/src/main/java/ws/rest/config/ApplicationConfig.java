package ws.rest.config;
import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ws.rest.source.EmployeeResource.class);
        classes.add(ws.rest.providers.JacksonXmlJakartaProvider.class);
        classes.add(ws.rest.providers.JacksonXmlTextJakartaProvider.class);
        return classes;
    }
}