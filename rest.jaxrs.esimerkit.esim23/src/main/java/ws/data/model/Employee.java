package ws.data.model;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {
  @XmlElement
    private int id;
  @XmlElement
    private String job;
  @XmlElement
    private double salary;
    public Employee() {}
    public Employee(int id, String job, double salary) {
        this.id = id;
        this.job = job;
        this.salary = salary;
    }
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public double getSalary() {
        return this.salary;
    }
    @Override
    public String toString() {
        return id + " " + job + " " + salary;
    }
}