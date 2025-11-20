package model;

public class Employee {
    private int id;
    private String name;
    private String image;
    private String job;
    private String hireDate;  // Changed from LocalDate to String
    private double salary;
    
    public Employee() {}
    
    public Employee(int id, String name, String image, String job, String hireDate, double salary) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.job = job;
        this.hireDate = hireDate;
        this.salary = salary;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    
    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    @Override
    public String toString() {
        return String.format("Employee[ID=%d, Name=%s, Job=%s, Salary=%.2f]", id, name, job, salary);
    }
}