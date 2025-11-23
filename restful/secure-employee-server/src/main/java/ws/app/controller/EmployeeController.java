package ws.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import model.Employee;
import ws.app.repository.EmployeeRepository;

@RestController
@RequestMapping(value = "/api/employees")
public class EmployeeController {

	@Autowired
	private ServletContext context;

	private EmployeeRepository employeeRepository;
	private String dataDir;
	private String imagesDir;

	@PostConstruct
	public void init() {
		dataDir = context.getRealPath(context.getInitParameter("data_dir")) + File.separator;
		imagesDir = dataDir + "images" + File.separator;

		File dataDirFile = new File(dataDir);
		File imagesDirFile = new File(imagesDir);

		if (!dataDirFile.exists()) {
			dataDirFile.mkdirs();
		}
		if (!imagesDirFile.exists()) {
			imagesDirFile.mkdirs();
		}
		employeeRepository = new EmployeeRepository(dataDir);
	}

	// Get all employees
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return ResponseEntity.ok(employees);
		
	}

	// Get employee by ID
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
		Employee employee = employeeRepository.findById(id);
		if (employee == null) {
			return ResponseEntity.status(404).body("Employee with ID " + id + " not found");
		}
		return ResponseEntity.ok(employee);
	}

	// Search employees by name or job
	@GetMapping(value = "/search/{searchTerm}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Employee>> searchEmployees(@PathVariable String searchTerm) {
		List<Employee> results = employeeRepository.search(searchTerm);
		return ResponseEntity.ok(results);
	}

	// Add employee
	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
		try {
			employeeRepository.save(employee);
			
			return ResponseEntity.ok("Employee " + employee.getName() + " added successfully");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
		}
	}

	// Update employee
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) {
		try {
			employeeRepository.update(employee);
			return ResponseEntity.ok("Employee " + employee.getName() + " updated successfully");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
		}
	}

	// Upload image for employee
	@PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadEmployeeImage(@PathVariable int id,
			@RequestParam("image") MultipartFile imageFile) {
		try {
			Employee employee = employeeRepository.findById(id);
			if (employee == null) {
				return ResponseEntity.status(404).body("Employee with ID " + id + " not found");
			}

			if (imageFile.isEmpty()) {
				return ResponseEntity.badRequest().body("Image file is empty");
			}

			String contentType = imageFile.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				return ResponseEntity.badRequest().body("File must be an image");
			}

			if (employee.getImagePath() != null) {
				File oldImage = new File(employee.getImagePath());
				if (oldImage.exists()) {
					oldImage.delete();
				}
			}

			String originalFilename = imageFile.getOriginalFilename();
			String safeName = originalFilename.replace("..", "").replace("/", "").replace("\\", "");
			String filename = "employee_" + id + "_" + safeName;
			String filePath = imagesDir + filename;

			Files.write(Paths.get(filePath), imageFile.getBytes());

			// Update employee with image path
			employee.setImagePath(filePath);
			employeeRepository.update(employee);

			return ResponseEntity.ok("Image uploaded successfully for employee " + id + ". Path: " + filePath);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("ERROR: Failed to upload image - " + e.getMessage());
		}
	}

	// Download image for employee
	@GetMapping(value = "/{id}/image", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<?> downloadEmployeeImage(@PathVariable int id) {
		try {
			Employee employee = employeeRepository.findById(id);
			if (employee == null) {
				return ResponseEntity.status(404).body("Employee not found");
			}

			if (employee.getImagePath() == null || employee.getImagePath().isBlank()) {
				return ResponseEntity.status(404).body("Employee has no image");
			}

			File imageFile = new File(employee.getImagePath());
			if (!imageFile.exists()) {
				return ResponseEntity.status(404).body("Image file not found on server");
			}

			byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
			return ResponseEntity.ok(imageBytes);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("ERROR: Failed to download image");
		}
	}

}