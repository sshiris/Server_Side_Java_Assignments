package client;

import model.Employee;
import client.service.EmployeeRestClient;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class EmployeeConsoleClient {
    
    private EmployeeRestClient restClient;
    private Scanner scanner;
    private String currentUsername;
    private String currentPassword;
    private String currentRole;
    
    public EmployeeConsoleClient() {
        this.scanner = new Scanner(System.in);
        this.currentUsername = null;
        this.currentPassword = null;
        this.currentRole = null;
    }
    
    public static void main(String[] args) {
        EmployeeConsoleClient client = new EmployeeConsoleClient();
        client.run();
    }
    
    public void run() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  Employee Management System - Secure Console Client");
        System.out.println("=".repeat(60));
        
        // Login first
        if (!login()) {
            System.out.println("\n❌ Authentication failed. Exiting.");
            return;
        }
        
        // Initialize REST client with credentials
        this.restClient = new EmployeeRestClient(currentUsername, currentPassword);
        
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("\nEnter your choice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    listAllEmployees();
                    break;
                case "2":
                    getEmployeeById();
                    break;
                case "3":
                    createEmployee();
                    break;
                case "4":
                    updateEmployee();
                    break;
                case "5":
                    deleteEmployee();
                    break;
                case "6":
                    searchEmployees();
                    break;
                case "7":
                    uploadImage();
                    break;
                case "8":
                    downloadImage();
                    break;
                case "9":
                    System.out.println("\n👋 Logged out. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private boolean login() {
        System.out.println("\n🔐 AUTHENTICATION");
        System.out.println("-".repeat(60));
        System.out.println("Available credentials:");
        System.out.println("  - admin / admin123 (Admin - can create/update/delete)");
        System.out.println("  - Jimmy / McPeters (User - can only view)");
        System.out.println("  - iris / iris (User - can only view)");
        System.out.println("  - Oliver / O1000 (User - can only view)");
        System.out.println("  - Taina / T1000 (User - can only view)");
        System.out.println("  - Alice / wonderful (Friend - can only view)");
        System.out.println("-".repeat(60));
        
        System.out.print("\nUsername: ");
        currentUsername = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        currentPassword = scanner.nextLine().trim();
        
        if (currentUsername.isEmpty() || currentPassword.isEmpty()) {
            System.out.println("❌ Username and password cannot be empty.");
            return false;
        }
        
        // Test connection with credentials
        System.out.println("\n⏳ Verifying credentials...");
        EmployeeRestClient testClient = new EmployeeRestClient(currentUsername, currentPassword);
        List<Employee> employees = testClient.getAllEmployees();
        
        if (employees != null) {
            // Determine role based on username
            if (currentUsername.equals("admin")) {
                currentRole = "ADMIN";
            } else {
                currentRole = "USER";
            }
            
            System.out.println("✅ Authentication successful!");
            System.out.println("🔓 Logged in as: " + currentUsername + " (" + currentRole + ")");
            return true;
        } else {
            System.out.println("❌ Authentication failed. Invalid credentials.");
            return false;
        }
    }
    
    private void printMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("👤 User: " + currentUsername + " (" + currentRole + ") | 🔒 HTTPS");
        System.out.println("=".repeat(60));
        System.out.println("1. List all employees (PUBLIC)");
        System.out.println("2. Get employee by ID (PUBLIC)");
        System.out.println("3. Create new employee (ADMIN ONLY)");
        System.out.println("4. Update employee (ADMIN ONLY)");
        System.out.println("5. Delete employee (ADMIN ONLY)");
        System.out.println("6. Search employees (PUBLIC)");
        System.out.println("7. Upload employee image (ADMIN ONLY)");
        System.out.println("8. Download employee image (PUBLIC)");
        System.out.println("9. Logout & Exit");
        System.out.println("=".repeat(60));
    }
    
    private void listAllEmployees() {
        System.out.println("\n📋 Fetching all employees...");
        List<Employee> employees = restClient.getAllEmployees();
        
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        System.out.println("\n" + "-".repeat(90));
        System.out.printf("%-5s | %-20s | %-20s | %-12s | %-10s%n", 
            "ID", "Name", "Job", "Hire Date", "Salary");
        System.out.println("-".repeat(90));
        
        for (Employee emp : employees) {
            System.out.printf("%-5d | %-20s | %-20s | %-12s | $%-9.2f%n",
                emp.getId(),
                emp.getName(),
                emp.getJob(),
                emp.getHireDate(),
                emp.getSalary());
        }
        System.out.println("-".repeat(90));
        System.out.println("✅ Total: " + employees.size() + " employees");
    }
    
    private void getEmployeeById() {
        System.out.print("\nEnter Employee ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("📋 Fetching employee...");
            Employee emp = restClient.getEmployeeById(id);
            
            if (emp == null) {
                System.out.println("❌ Employee not found.");
                return;
            }
            
            printEmployeeDetails(emp);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }
    
    private void createEmployee() {
        System.out.println("\n🆕 --- Create New Employee (ADMIN ONLY) ---");
        
        // Check if user is admin
        if (!currentRole.equals("ADMIN")) {
            System.out.println("❌ You don't have permission. Only ADMIN can create employees.");
            System.out.println("💡 Tip: Login with admin / admin123");
            return;
        }
        
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Job Title: ");
            String job = scanner.nextLine().trim();
            
            System.out.print("Salary: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Hire Date (yyyy-MM-dd): ");
            String hireDate = scanner.nextLine().trim();
            
            if (name.isEmpty() || job.isEmpty() || hireDate.isEmpty()) {
                System.out.println("❌ All fields are required.");
                return;
            }
            
            System.out.println("⏳ Creating employee...");
            Employee emp = new Employee(0, name, null, job, hireDate, salary);
            Employee created = restClient.createEmployee(emp);
            
            if (created != null) {
                System.out.println("\n✅ Employee created successfully!");
                printEmployeeDetails(created);
                
                System.out.print("\nDo you want to upload an image? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    uploadImageForEmployee(created.getId());
                }
            } else {
                System.out.println("❌ Failed to create employee.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format.");
        }
    }
    
    private void updateEmployee() {
        System.out.println("\n✏️  --- Update Employee (ADMIN ONLY) ---");
        
        if (!currentRole.equals("ADMIN")) {
            System.out.println("❌ You don't have permission. Only ADMIN can update employees.");
            return;
        }
        
        System.out.print("\nEnter Employee ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = restClient.getEmployeeById(id);
            
            if (emp == null) {
                System.out.println("❌ Employee not found.");
                return;
            }
            
            System.out.println("\n--- Current Details ---");
            printEmployeeDetails(emp);
            
            System.out.println("\n--- Update Information (leave blank to keep current) ---");
            
            System.out.print("New Name [" + emp.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) emp.setName(name);
            
            System.out.print("New Job [" + emp.getJob() + "]: ");
            String job = scanner.nextLine().trim();
            if (!job.isEmpty()) emp.setJob(job);
            
            System.out.print("New Salary [" + emp.getSalary() + "]: ");
            String salaryStr = scanner.nextLine().trim();
            if (!salaryStr.isEmpty()) {
                emp.setSalary(Double.parseDouble(salaryStr));
            }
            
            System.out.print("New Hire Date [" + emp.getHireDate() + "]: ");
            String hireDate = scanner.nextLine().trim();
            if (!hireDate.isEmpty()) emp.setHireDate(hireDate);
            
            System.out.println("⏳ Updating employee...");
            Employee updated = restClient.updateEmployee(id, emp);
            if (updated != null) {
                System.out.println("\n✅ Employee updated successfully!");
                printEmployeeDetails(updated);
            } else {
                System.out.println("❌ Failed to update employee.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }
    
    private void deleteEmployee() {
        System.out.println("\n🗑️  --- Delete Employee (ADMIN ONLY) ---");
        
        if (!currentRole.equals("ADMIN")) {
            System.out.println("❌ You don't have permission. Only ADMIN can delete employees.");
            return;
        }
        
        System.out.print("\nEnter Employee ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = restClient.getEmployeeById(id);
            
            if (emp == null) {
                System.out.println("❌ Employee not found.");
                return;
            }
            
            System.out.println("\n--- Employee to Delete ---");
            printEmployeeDetails(emp);
            
            System.out.print("\nAre you sure? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("⏳ Deleting employee...");
                if (restClient.deleteEmployee(id)) {
                    System.out.println("✅ Employee deleted successfully!");
                } else {
                    System.out.println("❌ Failed to delete employee.");
                }
            } else {
                System.out.println("❌ Deletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }
    
    private void searchEmployees() {
        System.out.print("\nEnter search term (name or job): ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("❌ Search term cannot be empty.");
            return;
        }
        
        System.out.println("🔍 Searching for \"" + searchTerm + "\"...");
        List<Employee> results = restClient.searchEmployees(searchTerm);
        
        if (results == null || results.isEmpty()) {
            System.out.println("❌ No employees found matching your search.");
            return;
        }
        
        System.out.println("\n" + "-".repeat(90));
        System.out.printf("%-5s | %-20s | %-20s | %-12s | %-10s%n", 
            "ID", "Name", "Job", "Hire Date", "Salary");
        System.out.println("-".repeat(90));
        
        for (Employee emp : results) {
            System.out.printf("%-5d | %-20s | %-20s | %-12s | $%-9.2f%n",
                emp.getId(),
                emp.getName(),
                emp.getJob(),
                emp.getHireDate(),
                emp.getSalary());
        }
        System.out.println("-".repeat(90));
        System.out.println("✅ Found: " + results.size() + " employees");
    }
    
    private void uploadImage() {
        System.out.println("\n📸 --- Upload Employee Image (ADMIN ONLY) ---");
        
        if (!currentRole.equals("ADMIN")) {
            System.out.println("❌ You don't have permission. Only ADMIN can upload images.");
            return;
        }
        
        System.out.print("\nEnter Employee ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            uploadImageForEmployee(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }
    
    private void uploadImageForEmployee(int employeeId) {
        System.out.print("Enter image file path: ");
        String filePath = scanner.nextLine().trim();
        
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }
        
        System.out.println("⏳ Uploading image...");
        if (restClient.uploadEmployeeImage(employeeId, imageFile)) {
            System.out.println("✅ Image uploaded successfully!");
        } else {
            System.out.println("❌ Failed to upload image.");
        }
    }
    
    private void downloadImage() {
        System.out.print("\nEnter Employee ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            File downloadDir = new File("downloads");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
            
            File destinationFile = new File(downloadDir, "employee_" + id + ".jpg");
            
            System.out.println("⏳ Downloading image...");
            if (restClient.downloadEmployeeImage(id, destinationFile)) {
                System.out.println("✅ Image downloaded successfully!");
                System.out.println("📁 Location: " + destinationFile.getAbsolutePath());
            } else {
                System.out.println("❌ Failed to download image.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }
    
    private void printEmployeeDetails(Employee emp) {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ID:         " + emp.getId());
        System.out.println("Name:       " + emp.getName());
        System.out.println("Job:        " + emp.getJob());
        System.out.println("Hire Date:  " + emp.getHireDate());
        System.out.println("Salary:     $" + String.format("%.2f", emp.getSalary()));
        if (emp.getImage() != null && !emp.getImage().isEmpty()) {
            System.out.println("Image:      " + emp.getImage());
        }
        System.out.println("-".repeat(50));
    }
}