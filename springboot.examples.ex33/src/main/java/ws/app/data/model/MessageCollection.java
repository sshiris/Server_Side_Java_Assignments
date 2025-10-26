package ws.app.data.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="messages")
public class MessageCollection {
	public MessageCollection() {
	}

	@JacksonXmlProperty(localName = "message")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Message> messageList = new ArrayList<Message>();

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messages) {
		this.messageList = messages;
	}
}