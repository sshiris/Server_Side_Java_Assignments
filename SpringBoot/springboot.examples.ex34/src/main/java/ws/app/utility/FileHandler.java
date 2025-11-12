package ws.app.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	private static String publicDir;

	public static void setPublicDir(String uploadDir) {
		FileHandler.publicDir = uploadDir;
	}

	public static String getUploadDir() {
		return FileHandler.publicDir;
	}

	public static String getFileList() {
		String[] fileList = new File(FileHandler.publicDir).list();
		StringBuilder stringBuilder = new StringBuilder();
		for (String file : fileList)
			stringBuilder.append(file + System.lineSeparator());
		return "The file list:" + System.lineSeparator() + stringBuilder.toString();
	}

	public static ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) {
		StringBuilder feedback = new StringBuilder();
		ResponseEntity<Object> responseEntity = null;
		File uploadFile = new File(FileHandler.publicDir + file.getOriginalFilename());
		try {
//uploadFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(uploadFile);
			fout.write(file.getBytes());
			fout.close();
			feedback.append(uploadFile.getAbsolutePath() + " (" + String.format("%.2f", uploadFile.length() / 1024.0)
					+ " KB) was uploaded successfully");
			responseEntity = ResponseEntity.ok().contentLength(feedback.length())
					.contentType(MediaType.parseMediaType("application/txt")).body(feedback);
		} catch (IOException e) {
// feedback.append("Uploading " + file.getOriginalFilename() + " failed!");
			responseEntity = ResponseEntity.badRequest().contentLength(e.toString().length())
					.contentType(MediaType.parseMediaType("application/txt")).body(e.toString());
		}
// return uploadFile.getAbsolutePath() + " was upload successfully";
// return feedback.toString();
		return responseEntity;
	}

//This methods returns the content of requested file as part of a Response object
	public static ResponseEntity<Object> downloadFile(String fileName) {
		StringBuilder feedback = new StringBuilder();
//Here we define a locale independent number format
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setGroupingUsed(true);
//Here we create a file object, which refers to the requested file
		File fileObj = new File(FileHandler.publicDir + fileName);
		ResponseEntity<Object> responseEntity = null;
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(fileObj));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileObj.getName()));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			responseEntity = ResponseEntity.ok().headers(headers).contentLength(fileObj.length())
					.contentType(MediaType.parseMediaType("application/txt")).body(resource);
		} catch (FileNotFoundException e) {
			feedback.append(fileName + " not found on the server!");
			responseEntity = ResponseEntity.status(404).contentType(MediaType.parseMediaType("application/txt"))
					.body("File not found: " + feedback.toString());
		}
		return responseEntity;
	}
}
