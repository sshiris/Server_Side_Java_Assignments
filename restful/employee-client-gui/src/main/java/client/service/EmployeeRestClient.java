package client.service;

import model.Employee;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.SSLContext;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRestClient {
    
    // Server configuration
    private static final String BASE_URL = "https://localhost:8443/employees_restful/rest";
    private static final String TRUSTSTORE_PATH = "truststores/my_truststore";
    private static final String TRUSTSTORE_PASSWORD = "Autumn2025";
    
    private Client client;
    private Gson gson;
    private String username;
    private String password;
    
    /**
     * Constructor with credentials
     */
    public EmployeeRestClient(String username, String password) {
        this.username = username;
        this.password = password;
        this.gson = new Gson();
        this.client = createSecureClient();
    }
    
    /**
     * Create HTTPS client with SSL certificate validation and basic auth
     */
    private Client createSecureClient() {
        try {
            // Set system properties for SSL
            System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_PATH);
            System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);
            
            // Configure SSL using Jersey
            SslConfigurator sslConfig = SslConfigurator.newInstance()
                    .trustStoreFile(TRUSTSTORE_PATH)
                    .trustStorePassword(TRUSTSTORE_PASSWORD);
            
            SSLContext sslContext = sslConfig.createSSLContext();
            
            // Setup basic authentication with provided credentials
            HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(username, password);
            
            // Build secure client
            return ClientBuilder.newBuilder()
                    .sslContext(sslContext)
                    .register(auth)
                    .build();
            
        } catch (Exception e) {
            System.err.println("Error creating secure client: " + e.getMessage());
            e.printStackTrace();
            return ClientBuilder.newClient();
        }
    }
    
    /**
     * Get all employees (PUBLIC endpoint - no auth needed but will send credentials)
     */
    public List<Employee> getAllEmployees() {
        try {
            WebTarget target = client.target(BASE_URL).path("employees");
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonData = response.readEntity(String.class);
                Type listType = new TypeToken<ArrayList<Employee>>(){}.getType();
                return gson.fromJson(jsonData, listType);
            }
            System.err.println("❌ GET /employees failed: HTTP " + response.getStatus());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error getting all employees: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get employee by ID (PUBLIC endpoint)
     */
    public Employee getEmployeeById(int id) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees").path(String.valueOf(id));
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Employee.class);
            }
            System.err.println("❌ GET /employees/" + id + " failed: HTTP " + response.getStatus());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error getting employee: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create new employee (ADMIN endpoint - requires authentication)
     */
    public Employee createEmployee(Employee employee) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees");
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(employee, MediaType.APPLICATION_JSON));
            
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Employee.class);
            } else if (response.getStatus() == 401) {
                System.err.println("❌ Unauthorized (401): You don't have permission to create employees");
                System.err.println("💡 Only admin users can create employees");
                return null;
            }
            System.err.println("❌ POST /employees failed: HTTP " + response.getStatus());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error creating employee: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Update employee (ADMIN endpoint - requires authentication)
     */
    public Employee updateEmployee(int id, Employee employee) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees").path(String.valueOf(id));
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(employee, MediaType.APPLICATION_JSON));
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Employee.class);
            } else if (response.getStatus() == 401) {
                System.err.println("❌ Unauthorized (401): You don't have permission to update employees");
                return null;
            }
            System.err.println("❌ PUT /employees/" + id + " failed: HTTP " + response.getStatus());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error updating employee: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Delete employee (ADMIN endpoint - requires authentication)
     */
    public boolean deleteEmployee(int id) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees").path(String.valueOf(id));
            Response response = target.request().delete();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else if (response.getStatus() == 401) {
                System.err.println("❌ Unauthorized (401): You don't have permission to delete employees");
                return false;
            }
            System.err.println("❌ DELETE /employees/" + id + " failed: HTTP " + response.getStatus());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Search employees (PUBLIC endpoint)
     */
    public List<Employee> searchEmployees(String searchTerm) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees").path("search")
                    .queryParam("q", searchTerm);
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonData = response.readEntity(String.class);
                Type listType = new TypeToken<ArrayList<Employee>>(){}.getType();
                return gson.fromJson(jsonData, listType);
            }
            System.err.println("❌ GET /employees/search failed: HTTP " + response.getStatus());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Error searching employees: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Upload employee image (ADMIN endpoint - requires authentication)
     */
    public boolean uploadEmployeeImage(int employeeId, File imageFile) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees")
                    .path(String.valueOf(employeeId)).path("image");
            
            FileInputStream fis = new FileInputStream(imageFile);
            Response response = target.request()
                    .post(Entity.entity(fis, MediaType.APPLICATION_OCTET_STREAM));
            
            fis.close();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else if (response.getStatus() == 401) {
                System.err.println("❌ Unauthorized (401): You don't have permission to upload images");
                return false;
            }
            System.err.println("❌ POST /employees/" + employeeId + "/image failed: HTTP " + response.getStatus());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error uploading image: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Download employee image (PUBLIC endpoint)
     */
    public boolean downloadEmployeeImage(int employeeId, File destinationFile) {
        try {
            WebTarget target = client.target(BASE_URL).path("employees")
                    .path(String.valueOf(employeeId)).path("image");
            Response response = target.request(MediaType.APPLICATION_OCTET_STREAM).get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                InputStream is = response.readEntity(InputStream.class);
                
                // Create directory if needed
                destinationFile.getParentFile().mkdirs();
                
                // Save file
                FileOutputStream fos = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();
                return true;
            }
            System.err.println("❌ GET /employees/" + employeeId + "/image failed: HTTP " + response.getStatus());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error downloading image: " + e.getMessage());
            return false;
        }
    }
}