package client.model;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.*;

@JacksonXmlRootElement(localName = "flights")
public class Flights {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "flight")
    private List<Flight> flights = new ArrayList<>();

    public List<Flight> getFlights() { return flights; }

    public void setFlights(List<Flight> flights) { this.flights = flights; }

    @Override
    public String toString() {
        return "Flights{" +
                "flights=" + flights +
                '}';
    }
}