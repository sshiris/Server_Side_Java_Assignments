package ws.rest.springboot.servers.springboot.server.ex191;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping(value = "/greet")
	public String greet() {
		return "Greeting from here! Todau is "+DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(new Date());
	}
	
	@RequestMapping(value = "/bye")
	public String bye() {
		return "Bye! Today is " + DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG).format(new Date());
	}

}
