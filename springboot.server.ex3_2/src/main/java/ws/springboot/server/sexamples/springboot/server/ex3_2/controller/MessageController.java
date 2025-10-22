package ws.springboot.server.sexamples.springboot.server.ex3_2.controller;

import java.text.DateFormat;
import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class MessageController {

@RequestMapping(value = "/greet")
public String greet() {
return "Greeting from here! Today is " + DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date());
}

@RequestMapping(value = "/bye")
public String bye() {
return "Bye! Today is " + DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT).format(new Date());
}

}