package client;

import java.net.URI;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;

public class EmployeeClient {
    public static void main(String[] args) {
        // Configure JAX-RS client
        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        
        // Set target URI
        WebTarget webTarget = client.target(getBaseURI());
        
        System.out.println("Testing Employee REST Service...\n");
        
        // Get single employee as TEXT_XML
        System.out.println("1. Single Employee (TEXT_XML):");
        String xmlResponse = webTarget.path("rest").path("employee")
                                    .request()
                                    .accept(MediaType.TEXT_XML)
                                    .get(String.class); 
        System.out.println(xmlResponse);
        System.out.println("---");
        
        // Get all employees as APPLICATION_XML
        System.out.println("2. All Employees (APPLICATION_XML):");
        String xmlAppResponse = webTarget.path("rest").path("employee")
                                       .request()
                                       .accept(MediaType.APPLICATION_XML)
                                       .get(String.class);
        System.out.println(xmlAppResponse);
        System.out.println("---");
        
        // Get all employees as JSON
        System.out.println("3. All Employees (JSON):");
        String jsonResponse = webTarget.path("rest").path("employee")
                                     .request()
                                     .accept(MediaType.APPLICATION_JSON)
                                     .get(String.class);
        System.out.println(jsonResponse);
        
        client.close();
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/rest-example").build();
    }
}