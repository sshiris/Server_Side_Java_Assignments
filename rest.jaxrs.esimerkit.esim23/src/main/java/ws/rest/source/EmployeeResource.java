package ws.rest.source;
import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ws.data.EmployeeDB;
import ws.data.model.Employee;
@Path("/employee")
public class EmployeeResource {
    //This method will be called if the requested media type is APPLICATION_JSON.
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Employee> getListEmployee() {
        return EmployeeDB.getEmployeeList();
    }
    //This method will be called if the requested media type is APPLICATION_XML.
    @GET
    @Produces({ MediaType.APPLICATION_XML })
    public List<Employee> getEmployeeList() {
        return EmployeeDB.getEmployeeList();
    }
    //This method will be called if the requested media type is TEXT_XML.
    @GET
    @Produces({ MediaType.TEXT_XML})
    public Employee getSingleEmployee() {
        return EmployeeDB.getRandomEmployee();
    }
}