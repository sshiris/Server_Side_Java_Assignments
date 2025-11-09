package ssa.ssa_IV_server.model;

import java.time.LocalDate;
import java.util.Base64;

public class Employee {
	private String id;
	private String name;
	private String job;
	private LocalDate hireDate;
	private int salary;
	private String image;

	public Employee() {
	};

	public Employee(String id, String name,  String job, LocalDate hireDate, int salary, String image) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.job = job;
		this.hireDate = hireDate;
		this.salary = salary;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
	
//	public String getImageBase64() {
//		if(image != null) {
//			return Base64.getEncoder().encodeToString(image);
//		}
//		return null;
//	}
//	public void setImageFromBase64(String base64Image) {
//		if(base64Image != null) {
//			this.image = Base64.getDecoder().decode(base64Image);
//		}
//	}
	
	public boolean hasEmployee(String search) {
		String searchText = search.toLowerCase();
		return (name != null && name.toLowerCase().contains(searchText)) ||
				(job != null && job.toLowerCase().contains(searchText)) ||
	               (id != null && id.toLowerCase().contains(searchText)) ||
	               (hireDate != null && hireDate.toString().contains(searchText)) ||
	               (String.valueOf(salary).contains(searchText));
	}
	
	@Override
	public String toString() {
		return String.format("ID: %s, Name: %s, Job: %s, Hire Date: %s, Salary: $%.2f", id, name, job, hireDate, salary);
	}
}
