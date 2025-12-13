package fi.vamk.flightapp.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "flights")
@JacksonXmlRootElement(localName = "flight")
public class Flight {
    
    @Id
    private long id;  // Client provides the ID, no auto-generation
    
    @NotBlank
    private String date;
    
    @NotBlank
    private String time;
    
    @NotBlank
    private String origin;
    
    @NotBlank
    private String destination;
    
    @Min(0)
    @Column(nullable = false)
    private double price;
    
    public Flight() {}
    
    public Flight(long id, String date, String time, String origin, String destination, double price) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                '}';
    }
}