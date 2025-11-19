package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * SecureRESTClient (Jakarta EE 10 version) Demonstrates secure file
 * upload/download using Jersey 3.x and HTTPS.
 */
public class SecureRESTClient {
	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://localhost:8443/secure.jersey.file.upload.download-v1.0.0/rest/").build();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		// --- Authentication setup ---
		String username = "iris";
		String password = "iris";
		String uploadFileName = "rose.jpg";
		String downloadFileName = "rose.jpg";
		// --- SSL trust store configuration ---
		String trustStoreDir = "truststores/".replace('/', File.separatorChar);
		String trustStorePathName = trustStoreDir + "my_truststore";
		String trustStorePassword = "Autumn2025";
		System.setProperty("javax.net.ssl.trustStore", trustStorePathName);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		// Create SSLContext using Jersey SslConfigurator (Jakarta-compatible)
		SslConfigurator sslConfigurator = SslConfigurator.newInstance().trustStoreFile(trustStorePathName)
				.trustStorePassword(trustStorePassword);
		SSLContext sslContext = sslConfigurator.createSSLContext();
		// Create client with Server-Sent Events feature
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).register(SseFeature.class).build();
		// Get list of files from the server (no authentication)
		getServerFileList(client);
		// Here we add credentials to basic authentication feature
		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(username, password);
		// Rebuild client to include authentication
		client = ClientBuilder.newBuilder().sslContext(sslContext).register(SseFeature.class).register(auth).build();
		uploadFile(uploadFileName, client);
		downloadFile(downloadFileName, client);
	}

	private static void getServerFileList(Client client) {
		WebTarget target = client.target(getBaseURI());
		Response response = target.path("file").path("service").path("files").request().accept(MediaType.TEXT_PLAIN)
				.get();
		System.out.println(response.readEntity(String.class));
	}

	public static void uploadFile(String fileName, Client client) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
		WebTarget target = client.target(getBaseURI());
		String filePathName = "uploads" + File.separator + fileName;
		File uploadFile = new File(filePathName);
		try (InputStream fileInStream = new FileInputStream(uploadFile)) {
			String contentDisposition = "attachment; filename=\"" + uploadFile.getName() + "\"";
			Response response = target.path("file").path("service").path("upload").path("{filename}")
					.resolveTemplate("filename", uploadFile.getName()).request(MediaType.APPLICATION_XML)
					.header("Content-Disposition", contentDisposition)
					.header("Content-Length", (int) uploadFile.length())
					.post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM_TYPE));
			System.out.println("Server response : " + response.readEntity(String.class));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void downloadFile(String fileName, Client client) {
		WebTarget target = client.target(getBaseURI());
		Response response = target.path("file").path("service").path("download").queryParam("filename", fileName)
				// .path(fileName)
				.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).get();
		String downloadedFileName = "downloaded_" + fileName;
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			try (InputStream downloadInputStream = response.readEntity(InputStream.class);
					FileOutputStream outputStream = new FileOutputStream(
							"downloads" + File.separator + downloadedFileName)) {
				byte[] bytes = new byte[1024];
				int read;
				while ((read = downloadInputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				File downloadFile = new File("downloads" + File.separator + downloadedFileName);
				System.out.println("Client response : " + downloadFile.getAbsolutePath() + " exists? "
						+ downloadFile.exists() + " size=" + downloadFile.length());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Download failed. Server responded with: " + response.getStatus());
		}
	}
}