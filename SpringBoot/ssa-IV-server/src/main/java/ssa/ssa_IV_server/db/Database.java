package ssa.ssa_IV_server.db;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ssa.ssa_IV_server.model.Employee;

public class Database {
	private static final Map<String, Employee> employees = new ConcurrentHashMap<>();
	
	static {
		employees.put("1", new Employee("1","Iris","Developer", LocalDate.of(2025, 11, 8), 8000,"alice.jpg"));
		employees.put("1", new Employee("1","Iris","Developer", LocalDate.of(2025, 11, 8), 8000,"alice.jpg"));
	}
	
	public static Map<String, Employee> getAll(){return employees;}
	public static Employee get(String id) {return employees.get(id);};
	public static String add(Employee e) {
		employees.put(e.getId(), e);
		return e.getName()+"new employee added successfully";
	}
	public static String update(String id, Employee e) {
		employees.put(id, e);
		return e.getName()+" info updated";
	}
	public static String delete(String id) {
		employees.remove(id);
		return id +" deleted";
	}
}
