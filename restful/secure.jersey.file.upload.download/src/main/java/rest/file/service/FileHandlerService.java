package rest.file.service;
import java.io.File;
import java.io.InputStream;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import helper.FileHandler;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
@Path("/file/service")
public class FileHandlerService {
// Allow all users to use this service
@PermitAll
@GET
@Path("/files")
@Produces(MediaType.TEXT_PLAIN)
public String getFiles() {
String[] fileList = new File(FileHandler.getDestinationDir()).list();
StringBuilder stringBuilder = new StringBuilder();
if (fileList != null && fileList.length > 0) {
for (String file : fileList) {
stringBuilder.append(file).append(System.lineSeparator());
}
} else {
stringBuilder.append("No files found in directory.");
}
return "The file list:" + System.lineSeparator() + stringBuilder;
}
// Only "admin" users can upload files
@RolesAllowed("admin")
@POST
@Path("/upload/{filename}")
@Consumes(MediaType.APPLICATION_OCTET_STREAM)
@Produces(MediaType.APPLICATION_XML)
public String uploadFile(@PathParam("filename") String fileName, InputStream uploadInputStream) {
return FileHandler.saveFile(uploadInputStream, fileName);
}
// Only "user" role can download using query parameter
@RolesAllowed("user")
@GET
@Path("/download")
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public Response downloadFileByQuery(@QueryParam("filename") String fileName) {
fileName = new File(fileName).getName();
return FileHandler.download(fileName);
}
// Only "user" role can download using path parameter
@RolesAllowed("user")
@GET
@Path("/download/{filename}")
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public Response downloadFileByPath(@PathParam("filename") String fileName) {
fileName = new File(fileName).getName();
return FileHandler.download(fileName);
}
}

//This is util/ConfigListener.java file:
