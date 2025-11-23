package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.sse.SseFeature;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Employee;

public class SecureEmployeeClient {
	private static Client client;
	//private static final String BASE_URL = "https://localhost:8443/secure-employee-server-v1.0/api/employees/";
	private static final String BASE_URL = "http://app.cc.puv.fi/secure-employee-server-v1.0/api/employees/";
	
	//private static String username;
	//private static String password;
	private static Scanner scanner;
	private static final String TRUSTSTORE_PATH = "truststores/my_truststore";
    private static final String TRUSTSTORE_PASSWORD = "Autumn2025";

	public static void main(String[] args) {
		try {
			//promptForCredentials();
			initializeClient();
			runClientMenu();
		} catch (Exception e) {
			System.err.println("Client initialization failed: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
//
//	private static void promptForCredentials() {
//		scanner = new Scanner(System.in);
//		System.out.print("Username: ");
//		username = scanner.nextLine().trim();
//		System.out.print("Password: ");
//		password = scanner.nextLine().trim();
//
//	}
//

	private static void initializeClient() {
//		System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_PATH);
//        System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);

        SslConfigurator sslConfig = SslConfigurator.newInstance()
                .trustStoreFile(TRUSTSTORE_PATH)
                .trustStorePassword(TRUSTSTORE_PASSWORD);
        SSLContext sslContext = sslConfig.createSSLContext();
		//HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(username, password);

		client = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .register(SseFeature.class)
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class)
                .build();

		System.out.println("Server: " + BASE_URL);
	
	}

	private static void runClientMenu() {
		scanner = new Scanner(System.in);
		String choice;

		while (true) {
	        
	        System.out.println("1. List all employees");
	        System.out.println("2. Get employee by ID");
	        System.out.println("3. Search employees");
	        System.out.println("4. Add employee");
	        System.out.println("5. Update employee");
	        System.out.println("6. Delete employee");
	        System.out.println("7. Upload employee image");
	        System.out.println("8. Download employee image");
	        System.out.println("9. Exit");
			System.out.print("Choose option: ");

			choice = scanner.nextLine().trim();

			switch (choice) {
			case "1" -> listAllEmployees();
			case "2" -> getEmployeeById();
			case "3" -> searchEmployees();
			case "4" -> addEmployee();
            case "5" -> updateEmployee();
            case "6" -> deleteEmployee();
            case "7" -> uploadEmployeeImage();
			case "8" -> downloadEmployeeImage();
			case "9" -> {
				System.out.println("Goodbye!");
				scanner.close();
				return;
			}
			default -> System.out.println("Invalid choice");
			}
		}
	}

	private static void listAllEmployees() {
		try {
			WebTarget target = client.target(BASE_URL).path("list");
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				List<Employee> employees = Arrays.asList(response.readEntity(Employee[].class));

				if (employees.isEmpty()) {
					System.out.println("No employees found");
				} else {
					employees.forEach(emp -> System.out.println(emp.toString() + "\n"));
				}
			} else {
				handleErrorResponse(response, "listing employees");
			}
		} catch (Exception e) {
			System.err.println("Error listing employees: " + e.getMessage());
		}
	}

	private static void getEmployeeById() {
		System.out.print("Enter employee ID: ");
		try {
			int id = Integer.parseInt(scanner.nextLine());
			WebTarget target = client.target(BASE_URL).path(String.valueOf(id));
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				Employee employee = response.readEntity(Employee.class);
				System.out.println(employee.toString());
			} else {
				handleErrorResponse(response, "getting employee");
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid ID format");
		}
	}

	private static void searchEmployees() {
		System.out.print("Enter search term (name or job): ");
		String searchTerm = scanner.nextLine().trim();

		try {
			WebTarget target = client.target(BASE_URL).path("search").path(searchTerm);
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				List<Employee> results = Arrays.asList(response.readEntity(Employee[].class));

				if (results.isEmpty()) {
					System.out.println("No results found");
				} else {

					results.forEach(emp -> System.out.println(emp.toString() + "\n"));
				}
			} else {
				handleErrorResponse(response, "searching employees");
			}
		} catch (Exception e) {
			System.err.println("Error searching: " + e.getMessage());
		}
	}

	private static void addEmployee() {
		try {
			System.out.print("Enter ID: ");
			int id = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter name: ");
			String name = scanner.nextLine();
			System.out.print("Enter job: ");
			String job = scanner.nextLine();
			System.out.print("Enter hire date (yyyy-MM-dd): ");
			LocalDate hireDate = LocalDate.parse(scanner.nextLine());
			System.out.print("Enter salary: ");
			double salary = Double.parseDouble(scanner.nextLine());

			Employee employee = new Employee(id, name, null, job, hireDate, salary);

			WebTarget target = client.target(BASE_URL).path("add");
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE));

			System.out.println(response.readEntity(String.class));
		} catch (Exception e) {
			System.err.println("Error adding employee: " + e.getMessage());
		}
	}

	private static void updateEmployee() {
		try {
			System.out.print("Enter employee ID to update: ");
			int id = Integer.parseInt(scanner.nextLine());

			WebTarget getTarget = client.target(BASE_URL).path(String.valueOf(id));
			Response getResponse = getTarget.request(MediaType.APPLICATION_JSON_TYPE).get();

			if (getResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				System.out.println("Employee not found");
				return;
			}

			Employee employee = getResponse.readEntity(Employee.class);

			System.out.print("Enter new name (current: " + employee.getName() + "): ");
			String name = scanner.nextLine();
			if (!name.isEmpty())
				employee.setName(name);

			System.out.print("Enter new job (current: " + employee.getJob() + "): ");
			String job = scanner.nextLine();
			if (!job.isEmpty())
				employee.setJob(job);

			System.out.print("Enter new salary (current: " + employee.getSalary() + "): ");
			String salaryStr = scanner.nextLine();
			if (!salaryStr.isEmpty())
				employee.setSalary(Double.parseDouble(salaryStr));

			WebTarget updateTarget = client.target(BASE_URL).path("update");
			Response updateResponse = updateTarget.request(MediaType.APPLICATION_JSON_TYPE)
					.put(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE));

			System.out.println(updateResponse.readEntity(String.class));
		} catch (Exception e) {
			System.err.println("Error updating employee: " + e.getMessage());
		}
	}

	private static void deleteEmployee() {
		try {
			System.out.print("Enter employee ID to delete: ");
			int id = Integer.parseInt(scanner.nextLine());

			WebTarget target = client.target(BASE_URL).path(String.valueOf(id));
			Response response = target.request().delete();

			System.out.println(response.readEntity(String.class));
		} catch (Exception e) {
			System.err.println("Error deleting employee: " + e.getMessage());
		}
	}

	private static void uploadEmployeeImage() {
		try {
			System.out.print("Enter employee ID: ");
			int id = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter image file path: ");
			String imagePath = scanner.nextLine().trim();

			File imageFile = new File(imagePath);
			if (!imageFile.exists()) {
				System.err.println("Image file not found: " + imagePath);
				return;
			}

			FormDataMultiPart multipart = new FormDataMultiPart();
			FileDataBodyPart filePart = new FileDataBodyPart("image", imageFile);
			multipart.bodyPart(filePart);

			WebTarget target = client.target(BASE_URL).path(String.valueOf(id)).path("image");
			Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

			System.out.println(response.readEntity(String.class));
		} catch (Exception e) {
			System.err.println("Error uploading image: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void downloadEmployeeImage() {
	    try {
	        System.out.print("Enter employee ID: ");
	        int id = Integer.parseInt(scanner.nextLine());
	        System.out.print("Enter destination directory (e.g., /Users/iris/Downloads): ");
	        String destDir = scanner.nextLine().trim();

	        WebTarget target = client.target(BASE_URL).path(String.valueOf(id)).path("image");
	        Response response = target.request().get();

	        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	            byte[] imageBytes = response.readEntity(byte[].class);
	            String filename = "employee_" + id + ".jpg";
	            String destPath = destDir + File.separator + filename;
	            new File(destDir).mkdirs();
	            Files.write(Paths.get(destPath), imageBytes);
	            System.out.println("✓ Image downloaded successfully to: " + destPath);
	        } else {
	            System.err.println("Error: " + response.readEntity(String.class));
	        }
	    } catch (Exception e) {
	        System.err.println("Error downloading image: " + e.getMessage());
	    }
	}

	private static void handleErrorResponse(Response response, String operation) {
	
	        System.err.println("Error " + operation + ": " + response.getStatus());
	        String errorMessage = response.readEntity(String.class);
	        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
	            System.err.println("Message: " + errorMessage);
	        }
	    
	}
	
}