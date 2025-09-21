package hw1.webforum;

import java.util.Date;

public class Entry {
	private String username;
	private String message;
	private Date date;
	
	public Entry(String username, String message) {
		this.username = username;
		this.message = message;
		this.date = new Date();
	}
	
	public String getUsername() {
		return username;
	}
	public String getMessage() {
		return message;
	}
	public Date getDate() {
		return date;
	}
}
