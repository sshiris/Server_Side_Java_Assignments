package rest.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//Here we set the path to base URL + /greet
@Path("/greet")
public class GreetingServer {
    
    //@GET annotation indicates that the method responds to HTTP GET request.
    //@Produces annotation indicates that the method can deliver the specific MIME type.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String jsonTextGreet() {
        return "{ \"message\": \"Hello there!\"}";
    }
    
    //@GET annotation indicates that the method responds to HTTP GET request.
    //@Produces annotation indicates that the method can deliver the specific MIME type.
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String plainTextGreet() {
        return "Hello there from Plain Text";
    }
    
    //@GET annotation indicates that the method responds to HTTP GET request.
    //@Produces annotation indicates that the method can deliver the specific MIME type.
    @GET
    @Produces(MediaType.TEXT_XML)
    public String xmlGreet() {
        return "<?xml version=\"1.0\"?><response><greet>Hello there from XML</greet></response>";
    }
    
    //@GET annotation indicates that the method responds to HTTP GET request.
    //@Produces annotation indicates that the method can deliver the specific MIME type.
    //The browser requests per default the HTML MIME type.
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String htmlGreet() {
        return "<html><title>Response</title><body><h1>Hello there from HTML</h1></body></html>";
    }
}