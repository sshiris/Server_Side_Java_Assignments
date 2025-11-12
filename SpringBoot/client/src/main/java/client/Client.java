package client;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Client {
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Client.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Bean
	public CommandLineRunner clientCommandLineRunner() {
		return args -> {
			String baseURI = "http://localhost:8080/products/";

			WebClient webClient = WebClient.builder().baseUrl(baseURI).build();

			System.out.println("1. Get all product list in XML:");
			String listResult = webClient.get().uri("/list").accept(MediaType.APPLICATION_XML).retrieve()
					.bodyToMono(String.class).block();
			System.out.println(listResult);

			System.out.println("2. Search by unit_price in xml format :");
			String searchByPrice = webClient.get().uri("/4").accept(MediaType.APPLICATION_XML).retrieve()
					.bodyToMono(String.class).block();
			System.out.println(searchByPrice);

			System.out.println("3. Get cheapest in xml format :");
			String cheapest = webClient.get().uri("/cheapest").accept(MediaType.APPLICATION_XML).retrieve()
					.bodyToMono(String.class).block();
			System.out.println(cheapest);

			System.out.println("4. Get cheapest and expensive in json format :");
			String cheapest_expensive = webClient.get().uri("/expensive&cheapest").accept(MediaType.APPLICATION_JSON)
					.retrieve().bodyToMono(String.class).block();
			System.out.println(cheapest_expensive);
		};
	}

}