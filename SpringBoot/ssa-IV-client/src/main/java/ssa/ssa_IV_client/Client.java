package ssa.ssa_IV_client;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ssa.ssa_IV_client.model.Employee;

@SpringBootApplication
public class Client {
    private static final String BASE_URI = "http://localhost:8080/ssa-IV-server/employees";
    
    private static final String CLIENT_UPLOAD_DIR = "client-uploads/";
    private static final String CLIENT_DOWNLOAD_DIR = "client-downloads/";

    public static void main(String[] args) throws Exception {
        createClientDirectories();
        SpringApplication app = new SpringApplication(Client.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
    
    private static void createClientDirectories() {
        File uploadDir = new File(CLIENT_UPLOAD_DIR);
        File downloadDir = new File(CLIENT_DOWNLOAD_DIR);
        
        if (!uploadDir.exists() || !downloadDir.exists()) {
            uploadDir.mkdirs();
            downloadDir.mkdirs();
            System.out.println("Created client upload/download directory: " + uploadDir.getAbsolutePath() +  downloadDir.getAbsolutePath());
        }
        
        listClientUploadFiles();
    }
    
    private static void listClientUploadFiles() {
        File uploadDir = new File(CLIENT_UPLOAD_DIR);
        System.out.println("Client upload directory: " + uploadDir.getAbsolutePath());
        
        File[] files = uploadDir.listFiles();
        if (files != null && files.length > 0) {
            System.out.println("Available files for upload:");
            for (File file : files) {
                System.out.println("  - " + file.getName() + " (" + file.length() + " bytes)");
            }
        } else {
            System.out.println("No files in client upload directory. Please add files to: " + uploadDir.getAbsolutePath());
        }
    }

    @Bean
    public CommandLineRunner clientCommandLineRunner() {
        return args -> {
            WebClient client = WebClient.builder().baseUrl(BASE_URI)
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                            .build())
                    .build();
            
            Scanner scanner = new Scanner(System.in);
            String choice = "";
            while (!choice.equals("8")) {
            	
                System.out.println("1. Show All Employees");
                System.out.println("2. Add Employee");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Upload Image");
                System.out.println("6. Download Image");
                System.out.println("7. List Available Files");
                System.out.println("8. Quit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        showAllEmployees(client);
                        break;

                    case "2": 
                        addEmployee(client, scanner);
                        break;

                    case "3":
                        updateEmployee(client, scanner);
                        break;

                    case "4":
                        deleteEmployee(client, scanner);
                        break;

                    case "5":
                        uploadImage(scanner);
                        break;

                    case "6":
                        downloadImage(scanner);
                        break;
                        
                    case "7":
                        listClientUploadFiles();
                        break;

                    case "8":
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice, please try again!");
                        break;
                }
            }

            scanner.close();
        };
    }
    
    private static void showAllEmployees(WebClient client) {
        try {
            String response = client.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("Employees:\n" + response);
        } catch (Exception e) {
            System.err.println("Error fetching employees: " + e.getMessage());
        }
    }
    
    private static void addEmployee(WebClient client, Scanner scanner) {
        try {
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Job: ");
            String job = scanner.nextLine();
            System.out.print("Enter Salary: ");
            int salary = Integer.parseInt(scanner.nextLine());
            Employee newEmp = new Employee(id, name, job, LocalDate.now(), salary, name + ".jpg");

            String addResult = client.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(newEmp))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(addResult);
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }
    
    private static void updateEmployee(WebClient client, Scanner scanner) {
        try {
            System.out.print("Enter ID to update: ");
            String updId = scanner.nextLine();
            System.out.print("Enter new Name: ");
            String updName = scanner.nextLine();
            System.out.print("Enter new Job: ");
            String updJob = scanner.nextLine();
            System.out.print("Enter new Salary: ");
            int updSalary = Integer.parseInt(scanner.nextLine());
            Employee updatedEmp = new Employee(updId, updName, updJob, LocalDate.now(), updSalary, updName + ".jpg");

            String updResult = client.put()
                    .uri("/" + updId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(updatedEmp))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(updResult);
        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }
    
    private static void deleteEmployee(WebClient client, Scanner scanner) {
        try {
            System.out.print("Enter ID to delete: ");
            String delId = scanner.nextLine();
            String delResult = client.delete()
                    .uri("/" + delId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(delResult);
        } catch (Exception e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
    
    private static void uploadImage(Scanner scanner) {
        System.out.print("Enter image file path: ");
        String fileName = scanner.nextLine();
        uploadFile(fileName);
    }
    
    public static void uploadFile(String fileName) {
        try {
            File fileToUpload = new File(fileName);
            
            if (!fileToUpload.exists()) {
                System.out.println("File not found: " + fileToUpload.getAbsolutePath());
                System.out.println("Available files in " + CLIENT_UPLOAD_DIR + ":");
                listClientUploadFiles();
                return;
            }

            System.out.println("Uploading file: " + fileToUpload.getAbsolutePath());
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(fileToUpload));

            String response = WebClient.create(BASE_URI)
                    .post()
                    .uri("/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Upload result: " + response);
            
        } catch (Exception e) {
            System.err.println("Upload error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void downloadImage(Scanner scanner) {
        System.out.print("Enter image filename to download: ");
        String fileName = scanner.nextLine();
        downloadFile("image", fileName);
    }

    

    public static void downloadFile(String path, String fileName) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(BASE_URI)
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                            .build())
                    .build();

            Flux<DataBuffer> dataBufferFlux = webClient.get()
                    .uri("/" + path + "/" + fileName)
                    .retrieve()
                    .bodyToFlux(DataBuffer.class);

            File targetFile = new File(CLIENT_DOWNLOAD_DIR, fileName);
            targetFile.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                DataBufferUtils.write(dataBufferFlux, Channels.newChannel(fos))
                    .doOnError(err -> System.err.println("Download failed: " + err.getMessage()))
                    .blockLast();
            } catch (Exception e) {
                System.err.println("Download error: " + e.getMessage());
                return;
            }

            System.out.println("File downloaded to: " + targetFile.getAbsolutePath()
                    + " (" + targetFile.length() + " bytes)");
            
        } catch (Exception e) {
            System.err.println("Download error: " + e.getMessage());
        }
    }
}