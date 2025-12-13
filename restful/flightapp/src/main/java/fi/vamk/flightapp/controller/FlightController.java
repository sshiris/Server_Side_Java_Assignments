package fi.vamk.flightapp.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import fi.vamk.flightapp.model.Flight;
import fi.vamk.flightapp.model.Flights;
import fi.vamk.flightapp.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
	public final FlightService service;

	public FlightController(FlightService service) {
		this.service = service;
	}
	

	@PostMapping
	public ResponseEntity<Flight> create(@RequestBody Flight f) {
		Flight saved = service.create(f);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<Flight> get(@PathVariable Long id) {
		return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

	}
	@GetMapping(value = "/date/{date}", produces = MediaType.APPLICATION_XML_VALUE)
	public Flights getByDate(@PathVariable String date){
		List<Flight> list = service.getByDate(date);
		Flights fc = new Flights();
		fc.setFlights(list);
		return fc;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public Flights getAll() {
        return service.getAll();
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody Flight f){
		try {
			return ResponseEntity.ok(service.update(id, f));
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
