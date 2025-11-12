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
public class SpringBootClient {

    static String baseURI = "http://localhost:8080/files/";

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(SpringBootClient.class);
        app.setWebApplicationType(WebApplicationType.NONE); // CLI mode
        app.setBannerMode(Banner.Mode.OFF); // Disable banner
        app.run(args);
    }

    @Bean
    public CommandLineRunner clientCommandLineRunner() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            String choice = "";

            while (!choice.equalsIgnoreCase("q")) {
                System.out.println("l: List server files");
                System.out.println("d: Download file");
                System.out.println("u: Upload file");
                System.out.println("q: quit");
                System.out.println("------------------------------");
                System.out.print("Make a selection: ");
                choice = scanner.nextLine();

                switch (choice.toLowerCase()) {
                    case "l":
                        getServerFileList("list");
                        break;
                    case "u":
                        System.out.print("Enter the name of the file to be uploaded: ");
                        String uploadFileName = scanner.nextLine();
                        uploadFile("upload", "uploads", uploadFileName);
                        break;
                    case "d":
                        System.out.print("Enter the name of the file to be downloaded: ");
                        String downloadFileName = scanner.nextLine();
                        downloadFile("download", "downloads", downloadFileName);
                        break;
                    case "q":
                        System.out.println("Bye!");
                        break;
                    default:
                        System.out.println("Make a valid choice!");
                }
            }
            scanner.close();
        };
    }

    private static void getServerFileList(String path) {
        String response = WebClient.builder().baseUrl(baseURI).build().get().uri(path).retrieve()
                .bodyToMono(String.class).block();
        System.out.println(response);
    }

    public static void uploadFile(String path, String uploadDirPath, String fileName) {
        MultiValueMap<String, Object> fileMap = new LinkedMultiValueMap<>();

        FileSystemResource resource = new FileSystemResource(Paths.get(uploadDirPath, fileName).toFile());
        if (!resource.exists()) {
            System.out.println("File " + uploadDirPath + "/" + fileName + " does not exist!");
            return;
        }

        System.out.println("Uploading file: " + resource.getFile().getAbsolutePath());
        fileMap.add("file", resource);

        String response = WebClient.builder().baseUrl(baseURI)
                .build()
                .post()
                .uri(path)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromValue(fileMap))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("WebClient result: " + response);
    }

    public static void downloadFile(String path, String downloadDirPath, String fileName) {
        MultiValueMap<String, String> fileDownloadValues = new LinkedMultiValueMap<>();
        fileDownloadValues.add("filename", fileName);

        WebClient webClient = WebClient.builder().baseUrl(baseURI)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                        .build())
                .build();

        Flux<DataBuffer> dataBufferFlux = webClient.post().uri(path)
                .body(BodyInserters.fromValue(fileDownloadValues))
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        File targetFile = new File(downloadDirPath, fileName);
        targetFile.getParentFile().mkdirs(); // ensure parent dirs exist

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            DataBufferUtils.write(dataBufferFlux, fos)
                    .doOnError(err -> System.err.println("Download failed: " + err.getMessage()))
                    .blockLast();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + targetFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Download error: " + e.getMessage());
        }

        System.out.println("Client response: " + targetFile.getAbsolutePath() + " exists? " + targetFile.exists()
                + " size=" + targetFile.length());
    }
}
