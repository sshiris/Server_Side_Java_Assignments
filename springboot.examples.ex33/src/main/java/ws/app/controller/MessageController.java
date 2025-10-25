package ws.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ws.app.data.model.Message;
import ws.app.data.model.MessageCollection;
import ws.app.db.Database;

@RestController
@RequestMapping("/spring/messages")
public class MessageController {
	@GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public String ping() {
		return "The server is running!";
	}

	// This method uses HTTP.POST request and @RequestParam to add new data
	@PostMapping(value = "/add", produces = MediaType.TEXT_HTML_VALUE)
	// @ResponseBody
	public String addMessagePost(@RequestParam("messageId") String messageId, @RequestParam("message") String message,
			@RequestParam("date") String date) {
		return Database.addMessage(messageId, new Message(messageId, message, date));
	}

	// This method uses HTTP.POST request and @RequestParam to update existing data
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
	// @ResponseBody
	public Optional<Message> updateMessagePost(@RequestParam("messageId") String messageId,
			@RequestParam("message") String message, @RequestParam("date") String date) {
		return Database.updateMessage(messageId, new Message(messageId, message, date));
	}

	// This method uses HTTP.GET request and @PathVariable to add new data
	@GetMapping(value = "/add/{messageId}/{message}/{date}", produces = MediaType.TEXT_HTML_VALUE)
	public String addMessageGet(@PathVariable("messageId") String messageId, @PathVariable("message") String message,
			@PathVariable("date") String date) {
		return Database.addMessage(messageId, new Message(messageId, message, date));
	}

// This method returns the response as an object of ResponseEntity class.
	/*
	 * @RequestMapping(value="/add/{messageId}/{message}/{date}",
	 * method=RequestMethod.GET) public ResponseEntity<Object>
	 * addMessage(@PathVariable("messageId") String
	 * messageId,@PathVariable("message") String message, @PathVariable("date")
	 * String date ) { return new ResponseEntity<>(Database.addMessage(messageId,
	 * new Message(messageId, message, date)), HttpStatus.OK); }
	 */
	// This method uses HTTP.Delete request and @PathVariable to delete a message
	@DeleteMapping(value = "/delete/{messageId}", produces = MediaType.TEXT_HTML_VALUE)
	public String deleteMessage(@PathVariable("messageId") String messageId) {
		return Database.deleteMessage(messageId);
	}

	// This service returns the data of a single Message object as XML data
	@GetMapping(value = "/{messageId}", produces = MediaType.APPLICATION_XML_VALUE)
	public Message getMessage(@PathVariable("messageId") String messageId) {
		return Database.getMessage(messageId);
	}

	// This service returns the data of all Message objects as JSON data
	@GetMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Message> getMessageMap() {
		return Database.getAllMap();
	}

	// This service returns the data of all Message objects as XML data
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_XML_VALUE)
	public MessageCollection getMessageList() {
		List<Message> messageList = Database.getAllList();
		MessageCollection messageCollection = new MessageCollection();
		messageCollection.setMessageList(messageList);
		return messageCollection;
	}
}