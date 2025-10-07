package rest.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greet")
public class GreetingServer {
    
    @GET
    @Path("/json")  // Add different paths
    @Produces(MediaType.APPLICATION_JSON)
    public String jsonTextGreet() {
        return "{ \"message\": \"Hello there!\"}";
    }
    
    @GET
    @Path("/text")  // Add different paths
    @Produces(MediaType.TEXT_PLAIN)
    public String plainTextGreet() {
        return "Hello there from Plain Text";
    }
    
    @GET
    @Path("/xml")   // Add different paths
    @Produces(MediaType.TEXT_XML)
    public String xmlGreet() {
        return "<?xml version=\"1.0\"?><response><greet>Hello there from XML</greet></response>";
    }
    
    @GET
    @Path("/html")  // Add different paths
    @Produces(MediaType.TEXT_HTML)
    public String htmlGreet() {
        return "<html><title>Response</title><body><h1>Hello there from HTML</h1></body></html>";
    }
}