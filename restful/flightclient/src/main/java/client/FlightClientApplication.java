package client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FlightClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(FlightClientApplication.class).web(WebApplicationType.NONE).run(args);
	}

	@Override
	public void run(String... args) {
		WebClient client = WebClient.builder().baseUrl("http://app.cc.puv.fi/flightapp-0.0.1-SNAPSHOT/flights")
				.defaultHeader("Accept", "application/xml").defaultHeader("Content-Type", "application/xml").build();

		// 1. GET all flights
		System.out.println("1. GET all flights:");
		String allFlights = client.get().retrieve().bodyToMono(String.class).block();
		System.out.println(allFlights);
		System.out.println();

		// 2. GET flight by ID
		System.out.println("2. GET flight ID 1:");
		String flight1 = client.get().uri("/1").retrieve().bodyToMono(String.class).block();
		System.out.println(flight1);
		System.out.println();

		// 3. GET flights by date
		System.out.println("3. GET flights for 2024-12-25:");
		String flightsByDate = client.get().uri("/date/2024-12-25").retrieve().bodyToMono(String.class).block();
		System.out.println(flightsByDate);
		System.out.println();

		// 4. POST create flight
		System.out.println("4. POST create flight:");
		String newFlightXml = "<flight><id>100</id><date>2025-01-20</date><time>14:30:00</time><origin>Test</origin><destination>Test</destination><price>99.99</price></flight>";
		String created = client.post().bodyValue(newFlightXml).retrieve().bodyToMono(String.class).block();
		System.out.println("Created: " + created);
		System.out.println();

		// 5. PUT update flight
		System.out.println("5. PUT update flight ID 100:");
		String updateXml = "<flight><id>100</id><date>2025-01-20</date><time>15:00:00</time><origin>Updated</origin><destination>Updated</destination><price>199.99</price></flight>";
		String updated = client.put().uri("/100").bodyValue(updateXml).retrieve().bodyToMono(String.class).block();
		System.out.println("Updated: " + updated);
		System.out.println();

		// 6. DELETE flight
		System.out.println("6. DELETE flight ID 100:");
		client.delete().uri("/100").retrieve().toBodilessEntity().block();
		System.out.println("Deleted");
		System.out.println();
	}
}