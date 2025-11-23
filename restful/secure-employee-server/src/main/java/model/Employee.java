package model;

import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String imagePath;  // Path to image file on server
    private String job;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    private double salary;

    public Employee() {
    }

    public Employee(int id, String name, String imagePath, String job, LocalDate hireDate, double salary) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.job = job;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", hireDate=" + hireDate +
                ", salary=" + salary +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

}