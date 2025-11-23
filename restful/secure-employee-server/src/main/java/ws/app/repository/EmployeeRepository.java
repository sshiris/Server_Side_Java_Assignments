package ws.app.repository;

import java.io.*;
import java.util.*;
import model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EmployeeRepository {
	private String dataDir;
	private static final String EMPLOYEES_FILE = "employees.json";
	private final ObjectMapper mapper = new ObjectMapper();

	public EmployeeRepository(String dataDir) {
		this.dataDir = dataDir;
		mapper.registerModule(new JavaTimeModule());
	}

	// Get all employees
	public List<Employee> findAll() {
		try {
			File file = new File(dataDir, EMPLOYEES_FILE);
			if (!file.exists()) {
				return new ArrayList<>();
			}
			Employee[] employees = mapper.readValue(file, Employee[].class);
			
			return Arrays.asList(employees);
			
		} catch (Exception e) {
			System.err.println("Error reading employees: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	// Get employee by ID
	public Employee findById(int id) {
		return findAll().stream().filter(emp -> emp.getId() == id).findFirst().orElse(null);
	}

	// Search employees by name or job
	public List<Employee> search(String searchTerm) {
		if (searchTerm == null || searchTerm.isBlank()) {
			return findAll();
		}

		String term = searchTerm.toLowerCase();
		return findAll().stream()
				.filter(emp -> emp.getName().toLowerCase().contains(term) || emp.getJob().toLowerCase().contains(term))
				.toList();
	}

	// Save (add new employee)
	public void save(Employee employee) throws IOException {
		List<Employee> employees = new ArrayList<>(findAll());

		// Check if already exists 
		if (employees.stream().anyMatch(e -> e.getId() == employee.getId())) {
			throw new IOException("Employee with ID " + employee.getId() + " already exists");
		}

		employees.add(employee);
		saveToFile(employees);
	}

	// Update existing employee
	public void update(Employee employee) throws IOException {
		List<Employee> employees = findAll();
		int index = -1;

		for (int i = 0; i < employees.size(); i++) {
			if (employees.get(i).getId() == employee.getId()) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			throw new IOException("Employee with ID " + employee.getId() + " not found");
		}

		employees.set(index, employee);
		saveToFile(employees);
	}

	private void saveToFile(List<Employee> employees) throws IOException {
		try {
			File file = new File(dataDir, EMPLOYEES_FILE);
			File parent = file.getParentFile();

			if (!parent.exists()) {
				System.out.println("=== DEBUG saveToFile: Creating directory: " + parent.getAbsolutePath() + " ===");
				boolean created = parent.mkdirs();
				System.out.println("=== DEBUG saveToFile: Directory created: " + created + " ===");
			}

			mapper.writerWithDefaultPrettyPrinter().writeValue(file, employees);

		} catch (Exception e) {
			throw new IOException("Failed to save: " + e.getMessage(), e);
		}
	}
}
