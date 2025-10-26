package client;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
public class Client {
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Client.class);
// Here we turn off the web application type
		app.setWebApplicationType(WebApplicationType.NONE);
// Here we turn off Spring Boot banner mode
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	/*
	 * private ClientHttpConnector clientHttpConnector() { return new
	 * ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection
	 * ())); }
	 */
	@Bean
	public CommandLineRunner clientCommandLineRunner() {
		return args -> {
			String baseURI = "http://localhost:9090/spring/messages/";
			System.out.println("Posting data:");
// Here we add data using HTTP GET method
			String path = "add";
			String messageId = "10005";
			String messageText = "Finish the report by 10.5.2022.";
			String messageDate = "10.2.2022";
			MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
			bodyValues.add("messageId", messageId);
			bodyValues.add("message", messageText);
			bodyValues.add("date", messageDate);
			String result = WebClient.builder().baseUrl(baseURI).build().post().uri(path)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromValue(bodyValues))
					.retrieve().bodyToMono(String.class).block();
			System.out.println("Add (POST) result: " + result);
			System.out.println("------------------------------");
// Here we add data using HTTP GET method
			path = "add";
			messageId = "10006";
			messageText = "Let's meet on Saturday.";
			messageDate = "22.11.2025";
//A Mono is a specific but very common type of Flux:
//a Flux that will asynchronously emit either 0 or 1
//results before it completes.
			result = WebClient.builder().baseUrl(baseURI).build().get()
					.uri(path + "/" + messageId + "/" + messageText + "/" + messageDate).retrieve()
					.bodyToMono(String.class).block();
			System.out.println("Add (GET) result: " + result);
			System.out.println("------------------------------");
			System.out.println("Deleting data:");
// Here we add data using HTTP GET method
			path = "delete";
			messageId = "4000";
			MultiValueMap<String, String> deleteValues = new LinkedMultiValueMap<>();
			deleteValues.add("messageId", messageId);
			String deleteResult = WebClient.builder().baseUrl(baseURI).build().delete().uri(path + "/" + messageId)
					.retrieve().bodyToMono(String.class).block();
			System.out.println("Deletein result: " + deleteResult);
			System.out.println("------------------------------");
// Here we get the list of data as XML data
			path = "list";
			result = WebClient.builder().baseUrl(baseURI).build().get().uri(path)
// .accept(MediaType.APPLICATION_XML)
					.retrieve().bodyToMono(String.class).block();
			System.out.println("List result: " + result);
			System.out.println("------------------------------");
			path = "map";
// Here we get the list of data as JSON data
			result = WebClient.builder().baseUrl(baseURI).build().get().uri("map")
// .accept(MediaType.APPLICATION_JSON)
					.retrieve().bodyToMono(String.class).block();
			System.out.println("Map result: " + result);
			System.out.println("------------------------------");
			path = "2000";
// Here we have to use block() since command line runners don't
// consume reactive types and simply return after the execution
			result = WebClient.create(baseURI)
// WebClient.builder().baseUrl(baseURI).build()
					.get().uri(path)
// .accept(MediaType.APPLICATION_XML)
					.retrieve().bodyToMono(String.class).block();
			System.out.println("Single item result: " + result);
			System.out.println("------------------------------");
			System.out.println("Updating data:");
// Here we add data using HTTP GET method
			path = "update";
			messageId = "1000";
			messageText = "Update 1: Finish the report by 10.5.2022.";
			messageDate = "12.4.2022";
			MultiValueMap<String, String> updateValues = new LinkedMultiValueMap<>();
			updateValues.add("messageId", messageId);
			updateValues.add("message", messageText);
			updateValues.add("date", messageDate);
			result = WebClient.builder().baseUrl(baseURI).build().post().uri(path)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromValue(updateValues))
					.retrieve().bodyToMono(String.class).block();
			System.out.println("Update result: " + result);
			System.out.println("------------------------------");
//Here we create a WebClient object
			WebClient webClient = WebClient.builder().baseUrl(baseURI).build();
			// The following will get the response in a blockin manner.
			System.out.println("Printing results synchronously using Flux:");
			webClient.get().uri("/list").retrieve().bodyToFlux(String.class).doOnNext(System.out::println).blockLast(); // This
																														// will
																														// wait
																														// until
																														// all
																														// elements
																														// are
																														// printed
			// A Flux represents a reactive stream of multiple items (0..N)
			Flux<String> messageFlux = webClient.get().uri("/list").retrieve().bodyToFlux(String.class);
			// Here we subscribe asynchronously (non-blocking)
			messageFlux.subscribe(response -> System.out.println("Asynchronous response: " + response),
					error -> System.err.println("Error: " + error.getMessage()), () -> System.out.println("Completed"));
//A Flux represents a stream of elements. It’s a sequence that will
//asynchronously emit any number of items (0 or more) in the future,
//before completing (either successfully or with an error).
			/*
			 * String response = webClient.get().uri("/list").retrieve()
			 * .bodyToFlux(String.class) .blockLast(); //Makes flux wait for the last
			 * response. System.out.println(response);
			 */
		};
	}
}