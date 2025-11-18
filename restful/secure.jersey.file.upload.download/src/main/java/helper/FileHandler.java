package helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
public class FileHandler {
private static String destinationDir;
public static void setDestinationDir(String destinationDir) {
FileHandler.destinationDir = destinationDir;
}
public static String getDestinationDir() {
return FileHandler.destinationDir;
}
// Save the uploaded file on the server
public static String saveFile(InputStream uploadedInputStream, String fileName) {
StringBuilder feedback = new StringBuilder();
// Here we strip any path components (e.g., "../", absolute paths)
fileName = new File(fileName).getName();
// Here we allow only safe characters (letters, digits, dot, dash, underscore)
fileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
// Here we prevent hidden or empty filenames
if (fileName.isBlank() || fileName.startsWith(".") || fileName.length() > 255) {
fileName = "Temp_" + new SimpleDateFormat("dd.MM.yy_HH_mm_ss").format(new Date());
}
// Here we convert to normalized Path and remove ., .., // and unnecessary
// relative components
Path targetDir = Paths.get(destinationDir).toAbsolutePath().normalize();
// Here we resolve the final file path safely
Path targetFile = targetDir.resolve(fileName).normalize();
// Create the file object
File destinationFile = targetFile.toFile();
try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
byte[] buffer = new byte[1024];
int bytesRead;
while ((bytesRead = uploadedInputStream.read(buffer)) != -1) {
outputStream.write(buffer, 0, bytesRead);
}
feedback.append(destinationFile.getAbsolutePath()).append(" exists? ").append(destinationFile.exists())
.append(", size=").append(destinationFile.length());
} catch (FileNotFoundException e) {
feedback.append("File not found: ").append(e.getLocalizedMessage());
} catch (IOException e) {
feedback.append("IO error: ").append(e.getLocalizedMessage());
}
return feedback.toString();
}
// Return the requested file as a Response object
public static Response download(String fileName) {
StringBuilder feedback = new StringBuilder();
// String fileLocation = destinationDir + fileName;
File file = new File(destinationDir, fileName);
Response response = null;
NumberFormat numberFormat = NumberFormat.getInstance();
numberFormat.setGroupingUsed(true);
if (!file.exists()) {
return Response.status(Response.Status.NOT_FOUND).build();
}
InputStream stream;
if (file.exists()) {
try {
stream = new FileInputStream(file);
response = Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
.header("Content-Disposition", "attachment; filename=" + file.getName())
.header("Content-Length", numberFormat.format(file.length())).build();
/*
* ResponseBuilder builder = Response.ok(file);
* builder.header("Content-Disposition", "attachment; filename=" +
* file.getName()); builder.header("Content-Length",
* numberFormat.format(file.length())); response = builder.build();
*/
} catch (FileNotFoundException e) {
feedback.append(fileName).append(" not found on the server!");
response = Response.status(Response.Status.NOT_FOUND).entity("{'error':'File Not Found: + feedback + }")
.type(MediaType.APPLICATION_JSON).build();
}
}
return response;
}
}