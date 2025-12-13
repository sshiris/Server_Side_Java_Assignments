package fi.vamk.flightapp.service;

import org.springframework.stereotype.Service;

import fi.vamk.flightapp.model.Flight;
import fi.vamk.flightapp.model.Flights;
import fi.vamk.flightapp.repository.FlightRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    public Flight create(Flight f) {
        return repo.save(f);
    }
    
    public Flights getAll(){
    	List<Flight> fl = repo.findAll();
    	Flights flights = new Flights();
    	flights.setFlights(fl);
    	return flights;
    }

    public Optional<Flight> get(Long id) {
        return repo.findById(id);
    }

    public List<Flight> getByDate(String date) {
        return repo.findByDate(date);
    }

    public Flight update(Long id, Flight data) {
        return repo.findById(id).map(existing -> {

            existing.setDate(data.getDate());
            existing.setTime(data.getTime());
            existing.setOrigin(data.getOrigin());
            existing.setDestination(data.getDestination());
            existing.setPrice(data.getPrice());

            return repo.save(existing);

        }).orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
