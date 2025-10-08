package ws.data;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ws.data.model.Employee;
public class EmployeeDB {
//Here we define a Map object for keeping the employee data
private static List<Employee> employees = new ArrayList<Employee>();
//Here we populate the emps list
static {
employees.add(new Employee(100, "Account Executive", 1000));
employees.add(new Employee(200, "Nursing Assistant", 2000));
employees.add(new Employee(300, "Web Developer", 3000));
employees.add(new Employee(400, "DevOps Engineer", 4000));
employees.add(new Employee(500, "Medical Assistant", 5000));
}
//This method selects randomly an ID and returns
//the equivalent employee
public static Employee getRandomEmployee() {
Random r = new Random();
return employees.get(r.nextInt(employees.size()));
}
//This method returns all employees as a list
public static List<Employee> getEmployeeList() {
return employees;
}
}