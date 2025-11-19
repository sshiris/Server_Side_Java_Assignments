package ws.app.controller;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import ws.app.utility.FileHandler;
 
@RestController
@RequestMapping(value = "files")
public class FileUploadDownloadController {
@Autowired
private ServletContext context;
 
@PostConstruct
public void init() {
String publicDir = context.getRealPath(context.getInitParameter("destination_dir")) + File.separator;
File file = new File(publicDir);
if (!file.exists())
file.mkdirs();
FileHandler.setPublicDir(publicDir);
}
 
@GetMapping(value = "/list")
public String getFiles() {
return FileHandler.getFileList();
}
 
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
return FileHandler.fileUpload(file);
}
 
@GetMapping(value = "/download/{filename}")
public ResponseEntity<Object> downloadFileByPath(@PathVariable("filename") String fileName) {
return FileHandler.downloadFile(fileName);
}
 
@PostMapping(value = "/download")
public ResponseEntity<Object> downloadFileByQuery(@RequestParam("filename") String fileName) {
return FileHandler.downloadFile(fileName);
}
}