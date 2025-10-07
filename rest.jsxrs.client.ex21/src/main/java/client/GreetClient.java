package client;

import java.net.URI;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

public class GreetClient {
    public static void main(String[] args) {
        // Create a client configuration
        ClientConfig clientConfig = new ClientConfig();
        
        // Build the client
        Client client = ClientBuilder.newClient(clientConfig);
        
        // Define the base URI of the REST service
        WebTarget target = client.target(getBaseURI());
        
        // Make a request and get full Response object
        String response = target
            .path("rest")
            .path("ws")
            .path("greet")
            .request()
            .accept(MediaType.TEXT_PLAIN)
            .get(Response.class)
            .toString();
        System.out.println("Response: " + response);
        
        // Get plain text response
        String plainAnswer = target
            .path("rest")
            .path("ws")
            .path("greet")
            .request()
            .accept(MediaType.TEXT_PLAIN)
            .get(String.class);
        System.out.println("Plain text response: " + plainAnswer);
        
        // Get JSON response
        String jsonAnswer = target
            .path("rest")
            .path("ws")
            .path("greet")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get(String.class);
        System.out.println("Plain text JSON: " + jsonAnswer);
        
        // Get XML response
        String xmlAnswer = target
            .path("rest")
            .path("ws")
            .path("greet")
            .request()
            .accept(MediaType.TEXT_XML)
            .get(String.class);
        System.out.println("XML text response: " + xmlAnswer);
        
        // Get HTML response
        String htmlAnswer = target
            .path("rest")
            .path("ws")
            .path("greet")
            .request()
            .accept(MediaType.TEXT_HTML)
            .get(String.class);
        System.out.println("HTML text response: " + htmlAnswer);
    }
    
    // Helper method to build base URI
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/rest-example").build();
    }
}