package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Scanner;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SecureSpringBootClient {
	public static void main(String[] args) throws Exception {
//Here we specify the location of the truststore and its password 
		/*
		 * System.setProperty("javax.net.ssl.trustStore", "truststores/my_truststore");
		 * System.setProperty("javax.net.ssl.trustStorePassword", "Autumn2025");
		 */
		SpringApplication app = new SpringApplication(SecureSpringBootClient.class);
//Here we turn off the web application type
		app.setWebApplicationType(WebApplicationType.NONE);
//Here we turn off Spring Boot banner mode
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	static String baseURI = "https://localhost:8443/secure-file-server-v1.0/files/";

	@Bean
	public CommandLineRunner clientCommandLineRunner() {
		return args -> {
			String choice = "";
			String path = "";
			Scanner scanner = new Scanner(System.in);
			while (!choice.equalsIgnoreCase("q")) {
				System.out.println("l: List server files");
				System.out.println("d: Download file");
				System.out.println("u: Upload file");
				System.out.println("q: quit");
				System.out.println("------------------------------");
				System.out.println("Make a selection: ");
				choice = scanner.nextLine();
				switch (choice.toLowerCase()) {
				case "l":
					path = "list";
// Here we get the list of files on the server
					geterverFileList(path);
					break;
				case "u":
					path = "upload";
					String uploadDirPath = "uploads" + File.separator;
// Here we test the methods for downloading and uploading files
					System.out.println("Enter the name of the file to be uploaded:");
					String upnloadFileName = scanner.nextLine();
					File uploadFile = new File(uploadDirPath, upnloadFileName);
					if (!uploadFile.exists()) {
						System.out.println(uploadFile.getPath() + " does not exist!");
						break;
					}
					uploadFile(path, uploadDirPath, upnloadFileName);
					break;
				case "d":
					path = "download";
					String downloadDirPath = "downloads" + File.separator;
					System.out.println("Enter the name of the file to be downloaded:");
					String downloadFileName = scanner.nextLine();
					if (downloadFileName == null || downloadFileName.isBlank())
						break;
					downloadFile(path, downloadDirPath, downloadFileName);
					break;
				case "q":
					System.out.println("Bye!");
					break;
				default:
					System.out.println("Make a valid choice!");
					break;
				}
			}
			scanner.close();
		};
	}

	private static void geterverFileList(String path) {
// Here we get the list of data as XML data
		String response = WebClient.builder().baseUrl(baseURI).build().get().uri(path).retrieve()
				.bodyToMono(String.class).block();
		System.out.println(response);
	}

	public static void uploadFile(String path, String uploadDirPath, String fileName) {
		MultiValueMap<String, Object> fileMap = new LinkedMultiValueMap<String, Object>();
		try {
			fileMap.add("file", new FileSystemResource(Paths.get(uploadDirPath, fileName)));
		} catch (Exception e) {
			System.out.println(uploadDirPath + fileName + " was not found!");
			return;
		}
		String response = WebClient.builder().baseUrl(baseURI).build().post().uri(path)
				.contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromValue(fileMap)).retrieve()
				.bodyToMono(String.class).block();
		System.out.println("WebClient result: " + response);
	}

	public static void downloadFile(String path, String downloadPath, String fileName) {
// Here we prepare request parameters
		MultiValueMap<String, String> fileDownloadValues = new LinkedMultiValueMap<>();
		fileDownloadValues.add("filename", fileName);
// Here we configure WebClient with increased buffer size (5 MB)
		WebClient webClient = WebClient.builder().baseUrl(baseURI)
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024)).build())
				.build();
// Here we Perform POST request to download file
		Flux<DataBuffer> dataBufferFlux = webClient.post().uri(path).body(BodyInserters.fromValue(fileDownloadValues))
				.retrieve().bodyToFlux(DataBuffer.class);
		File targetFile = new File(downloadPath, fileName);
// Here we ensure parent directories exist
		targetFile.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(targetFile)) {
// Here we write to file and wait until done
			DataBufferUtils.write(dataBufferFlux, fos)
					.doOnError(err -> System.err.println("Download failed: " + err.getMessage())).blockLast(); // THis
// command
// makes
// it
// wait
// for
// completion
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + targetFile.getAbsolutePath());
		} catch (Exception e) {
			System.err.println("Download error: " + e.getMessage());
		}
		System.out.println("Client response: " + targetFile.getAbsolutePath() + " exists? " + targetFile.exists()
				+ " size=" + targetFile.length());
	}
}