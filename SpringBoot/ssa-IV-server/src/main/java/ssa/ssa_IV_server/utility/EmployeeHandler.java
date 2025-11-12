package ssa.ssa_IV_server.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class EmployeeHandler {
	private static final String SERVER_UPLOAD_DIR;
	

    
    static {
    	String uploadDir = System.getenv("SERVER_UPLOAD_DIR");
    	
    	if(uploadDir == null || uploadDir.isEmpty()) {
    		uploadDir = System.getProperty("user.home")+File.separator+"server-uploads";
    		
    	}
    	SERVER_UPLOAD_DIR = uploadDir + File.separator;
        createDirectory();
    }
    
    private static void createDirectory() {
    	File uploadDir = new File(SERVER_UPLOAD_DIR);
    	
    	if (!uploadDir.exists() ) {
            uploadDir.mkdirs();
    
            System.out.println("Created server upload/download directory: " + uploadDir.getAbsolutePath() );
        }
    }
    
    public static ResponseEntity<Object> uploadImage(MultipartFile file) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid filename");
            }
            
            // Security: prevent directory traversal
            if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
                return ResponseEntity.badRequest().body("Invalid filename: directory traversal not allowed");
            }
            
            Path destination = Paths.get(SERVER_UPLOAD_DIR, originalFilename);           
            file.transferTo(destination.toFile());
            return ResponseEntity.ok("Uploaded: " + originalFilename + " to " + destination.toAbsolutePath());            
        }  catch (IOException e) {
            System.err.println("IO error during upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: IO error - " + e.getMessage());
        }
    }
    
    public static ResponseEntity<Object> downloadImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty() || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return ResponseEntity.badRequest().body("Invalid filename");
        }
        
        Path filePath = Paths.get(SERVER_UPLOAD_DIR, filename);
        if (!Files.exists(filePath)) {
            System.out.println("File not found: " + filePath.toAbsolutePath());
            return ResponseEntity.notFound().build();
        }
        
        try {
            String contentType = Files.probeContentType(filePath);
            MediaType mediaType = MediaType.IMAGE_JPEG;
            
            if (contentType != null) {
                mediaType = MediaType.parseMediaType(contentType);
            }
            
            File file = filePath.toFile();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            System.err.println("Download error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Download failed: " + e.getMessage());
        }
    }
}