package ws.app.data.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "message")
public class Message {
// @JacksonXmlProperty(isAttribute = true)
	private String id;
	private String message;
// @JacksonXmlProperty(isAttribute = true)
	private String date;

	public Message() {
		this.id = "no_id";
		this.message = "no_message";
		this.date = "no_date";
	}

	public Message(String id) {
		this.id = id;
		this.message = "no_message";
		this.date = "no_date";
	}

	public Message(String id, String message, String date) {
		super();
		this.id = id;
		this.message = message;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message: " + id + "-> " + date + " " + message;
	}
}