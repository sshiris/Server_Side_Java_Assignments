package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;


public class FileHandler {

	private static String destinationDir;

	public static void setDestinationDir(String destinationDir) {
		FileHandler.destinationDir = destinationDir;
	}

	public static String getDestinationDir() {
		return FileHandler.destinationDir;
	}

//This methods saves the uploaded file  on the server
	public static String saveFile(InputStream uploadedInputStream, String fileName) {
          StringBuilder feedback = new StringBuilder();
          File destinationFile = new File(destinationDir + fileName) ;
          
        try {
            OutputStream outpuStream = new FileOutputStream(destinationFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            
            outpuStream.close();
            
              feedback.append(destinationFile.getAbsolutePath() + " exists? " +
                      destinationFile.exists() + ", size=" + destinationFile.length());
                      
        } catch (FileNotFoundException e) {
            
            feedback.append(e.getLocalizedMessage());
        } catch (IOException e) {
        
            feedback.append(e.getLocalizedMessage());
            
        }
                      
                      
                      return feedback.toString();
    }
	public static Response download(String fileName) {     
        
        StringBuilder feedback = new StringBuilder();
        
      Response response = null;
      
      //Here we define a locale independent number format
      NumberFormat numberFormat = NumberFormat.getInstance();
      numberFormat.setGroupingUsed(true);
       
      //Here we create a file object, which refers to the requested file
      File file = new File(destinationDir + fileName);
      
      if (file.exists()) {
        ResponseBuilder builder = Response.ok(file);
        builder.header("Content-Disposition", "attachment; filename=" + file.getName());
        builder.header("Content-Length", numberFormat.format(file.length()));
        response = builder.build();
         
      } else {
          
           feedback.append(fileName + " not found on the server!");
         
        response = Response.status(404).
                entity("File not found: " + feedback.toString()).
                type(MediaType.APPLICATION_JSON).
                build();
      }
        
      return response;
    }
 
}