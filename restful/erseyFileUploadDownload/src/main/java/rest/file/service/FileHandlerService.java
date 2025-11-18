package rest.file.service;

import java.io.File;
import java.io.InputStream;
import helper.FileHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@jakarta.ws.rs.Path("/file/service")
public class FileHandlerService {

	@GET
	@Path("/files")
	public String getFiles() {
		String[] fileList = new File(FileHandler.getDestinationDir()).list();
		StringBuilder stringBuilder = new StringBuilder();
		for (String file : fileList)
			stringBuilder.append(file + System.lineSeparator());
		return "The file list:" + System.lineSeparator() + stringBuilder.toString();
	}

	@POST
	@Path("/upload/{filename}")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_XML)
	public String uploadFile(@PathParam("filename") String fileName, InputStream uploadInputStream) {

		return FileHandler.saveFile(uploadInputStream, fileName);

	}

	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyQuery(@QueryParam("filename") String fileName) {
		return FileHandler.download(fileName);
	}

	@GET
	@Path("/download/{filename}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyPath(@PathParam("filename") String fileName) {
		return FileHandler.download(fileName);
	}
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
	    return "Jersey is working!";
	}
}