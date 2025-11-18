package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientProperties;

public class RESTClient {
    
    public static void main(String[] args) throws IOException {
        // Create directories if they don't exist
        new File("uploads").mkdirs();
        new File("downloads").mkdirs();
        
        // Get the list of files on the server
        getServerFileList();
        
        String uploadDirPath = "uploads" + File.separator;
        
        // Test upload (make sure you have a file named "emp1.jpg" in uploads folder)
        String uploadFileName = "emp1.jpg";
        uploadFile(uploadDirPath, uploadFileName);
        
        String downloadDirPath = "downloads" + File.separator;
        
        // Test download (use a file that exists on your server)
        String downloadFileName = "emp1.jpg";
        downloadFile(downloadDirPath, downloadFileName);
    }
    
    private static URI getBaseURI() {
        // Update this to match your server URL
        return UriBuilder.fromUri("http://localhost:8080/rename/rest/file/service/").build();
    }
    
    private static void getServerFileList() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());
        Response response = target.path("files").request()
                .accept(MediaType.TEXT_PLAIN).get(); // Changed to TEXT_PLAIN since your server returns text
        
        System.out.println("Server file list:");
        System.out.println(response.readEntity(String.class));
    }
    
    public static void uploadFile(String uploadDirPath, String fileName) {
        Client client = ClientBuilder.newClient();
        
        // Set REQUEST_ENTITY_PROCESSING to CHUNKED for uploading possible large files
        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
        
        WebTarget target = client.target(getBaseURI());
        String filePathName = uploadDirPath + fileName;
        File uploadFile = new File(filePathName);
        
        if (!uploadFile.exists()) {
            System.out.println("File not found: " + filePathName);
            return;
        }
        
        try (InputStream fileInStream = new FileInputStream(uploadFile)) {
            // Send the request and receive the response
            Response response = target.path("upload").path("{filename}")
                    .resolveTemplate("filename", uploadFile.getName())
                    .request(MediaType.APPLICATION_XML)
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));
            
            // Print received message from the server
            System.out.println("Server response: " + response.readEntity(String.class));
        } catch (FileNotFoundException e) {
            System.out.println("Upload file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Upload error: " + e.getMessage());
        }
    }
    
    public static void downloadFile(String downloadPath, String fileName) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());
        
        Response response = target.path("download")
                .queryParam("filename", fileName)
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        
        // Check the response status from the server
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // Read the InputStream from the server
            InputStream downloadInputStream = response.readEntity(InputStream.class);
            
            // Prepare for saving the file
            String savePathName = downloadPath + fileName;
            File downloadFile = new File(savePathName);
            
            try (FileOutputStream outputStream = new FileOutputStream(downloadFile)) {
                int read = 0;
                byte[] bytes = new byte[1024];
                
                // Save the received content to the file
                while ((read = downloadInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                
                System.out.println("Client response: " + downloadFile.getAbsolutePath() + 
                    " exists? " + downloadFile.exists() + " size=" + downloadFile.length());
            } catch (IOException e) {
                System.out.println("Download error: " + e.getMessage());
            }
        } else {
            System.out.println("Download failed. Server returned: " + response.getStatus());
            System.out.println("Response: " + response.readEntity(String.class));
        }
    }
}