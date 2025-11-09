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
    // Define server-side folders with absolute path resolution
    // Using a dedicated directory in user's home for uploads
	private static final String BASE_DIR = "/Users/iris/SSAs/SpringBoot/ssa-IV-server";
    private static final String SERVER_UPLOAD_DIR = BASE_DIR + File.separator + "uploads" + File.separator;
    private static final String SERVER_DOWNLOAD_DIR = BASE_DIR + File.separator + "downloads" + File.separator;
    
    static {
        // Create both directories when class loads
        createDirectory(SERVER_UPLOAD_DIR);
        createDirectory(SERVER_DOWNLOAD_DIR);
    }
    
    private static void createDirectory(String path) {
        try {
            Path dirPath = Paths.get(path);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("Created directory: " + dirPath.toAbsolutePath());
            } else {
                System.out.println("Directory exists: " + dirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + path);
            e.printStackTrace();
        }
    }

    public static ResponseEntity<Object> uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or null");
        }
        
        try {
            String originalFilename = file.getOriginalFilename();
            
            // Validate filename
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid filename");
            }
            
            // Security: prevent directory traversal
            if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
                return ResponseEntity.badRequest().body("Invalid filename: directory traversal not allowed");
            }
            
            Path destination = Paths.get(SERVER_UPLOAD_DIR, originalFilename);
            
            // Create parent directories if they don't exist
            Files.createDirectories(destination.getParent());
            
            file.transferTo(destination.toFile());
            System.out.println("✅ File saved to: " + destination.toAbsolutePath());
            System.out.println("✅ File exists: " + Files.exists(destination));
            System.out.println("✅ File size: " + Files.size(destination) + " bytes");
            
            // List all files in directory
            System.out.println("📁 Files in directory:");
            Files.list(destination.getParent()).forEach(p -> {
                try {
                    System.out.println("   - " + p.getFileName() + " (" + Files.size(p) + " bytes)");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            
            return ResponseEntity.ok("Uploaded: " + originalFilename + " to " + destination.toAbsolutePath());
            
        } catch (IllegalStateException e) {
            System.err.println("❌ File transfer error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: Cannot transfer file - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ IO error during upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: IO error - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error during upload: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
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
            MediaType mediaType = MediaType.IMAGE_JPEG; // default
            
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
    
    // Helper method to list files in server upload directory
    public static void listServerFiles() {
        try {
            File uploadDir = new File(SERVER_UPLOAD_DIR);
            File[] files = uploadDir.listFiles();
            System.out.println("Server upload directory: " + uploadDir.getAbsolutePath());
            if (files != null && files.length > 0) {
                System.out.println("Files on server:");
                for (File file : files) {
                    System.out.println("  - " + file.getName() + " (" + file.length() + " bytes)");
                }
            } else {
                System.out.println("No files in server upload directory");
            }
        } catch (Exception e) {
            System.err.println("Error listing server files: " + e.getMessage());
        }
    }
}