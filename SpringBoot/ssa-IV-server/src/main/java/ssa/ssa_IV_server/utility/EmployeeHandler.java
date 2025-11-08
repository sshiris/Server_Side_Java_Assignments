package ssa.ssa_IV_server.utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ssa.ssa_IV_server.model.Employee;

public class EmployeeHandler {
	private static final String imageDir = "public/images/";
	
	static {new File(imageDir).mkdirs();};

	public static ResponseEntity<Object> uploadImage(MultipartFile file) throws IOException{
		File dest = new File(imageDir + file.getOriginalFilename());
		file.transferTo(dest);
		return ResponseEntity.ok("uploaded: "+file.getOriginalFilename());
	}
	
	public static ResponseEntity<Object> downloadImage(String filename) throws IOException{
		File file = new File(imageDir + filename);
		if(!file.exists()) return ResponseEntity.notFound().build();
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(null);
	}
}
