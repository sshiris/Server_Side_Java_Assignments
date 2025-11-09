package ssa.ssa_IV_server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ssa.ssa_IV_server.db.Database;
import ssa.ssa_IV_server.model.Employee;
import ssa.ssa_IV_server.utility.EmployeeHandler;



@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	@GetMapping
	public List<Employee> getAll(){
		return new ArrayList<>(Database.getAll().values());
	}
	
	@GetMapping("/{id}")
	public Employee getId(@PathVariable String id) {
		return Database.get(id);
	}

	@PostMapping
	public String add(@RequestBody Employee e) {
		return Database.add(e);
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable String id, @RequestBody Employee e) {
		return Database.update(id, e);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable String id) {
		return Database.delete(id);
	}
	
	@PostMapping("/upload")
	public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws IOException{
		return EmployeeHandler.uploadImage(file);
	}
	
	@GetMapping("/image/{filename}")
	public ResponseEntity<Object> download(@PathVariable String filename) throws IOException{
		return EmployeeHandler.downloadImage(filename);
	}
}