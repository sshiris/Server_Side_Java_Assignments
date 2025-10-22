package client;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
@SpringBootApplication
public class Client {

private static String baseURI="http://localhost:8080/springboot.server.ex191-0.0.1-SNAPSHOT/";

public static void main(String[] args) throws Exception {
	SpringApplication app = new SpringApplication(Client.class);
	app.setWebApplicationType(WebApplicationType.NONE);
	app.run(args);
}

@Bean
public CommandLineRunner clientCommandLineRunner() {
	return args -> {
	
	Scanner scanner = new Scanner(System.in);
	String path="";
	do {
	System.out.println("Enter service path: (greet | bye) ");
	path=scanner.nextLine();
	System.out.println("Result: " + callService(baseURI, path));
	
	} while(path.equals("greet") || path.equals("bye"));
	
	scanner.close();

};
}

private String callService(String baseURI, String path) {
	//Here we have to use block() since command line runners do not
	//consume reactive types and simply return after the execution
	
	String result="";
	
	try {
	result=WebClient.create(baseURI)
	//WebClient.builder().baseUrl(baseURI).build()
	.get()
	.uri(path)
	.retrieve()
	//Since we are retrieving a single item, we use bodyToMono, which emits 0-1 item.
	.bodyToMono(String.class)
	.block();
	}catch(Exception e) {
	result="Something went wrong: " + e.getMessage();
	}
	
	return result;

}

}