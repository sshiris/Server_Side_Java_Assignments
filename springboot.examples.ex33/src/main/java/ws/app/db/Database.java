package ws.app.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import ws.app.data.model.Message;

public class Database {
	private static Map<String, Message> messages = new ConcurrentHashMap<>();
	static {
		messages.put("1000", new Message("1000", "This is message #1000", "01.02.2020"));
		messages.put("2000", new Message("2000", "This is message #2000", "08.02.2021"));
		messages.put("3000", new Message("3000", "This is message #3000", "0.3.3.2022"));
		messages.put("4000", new Message("4000", "This is message #4000", "04.04.2022"));
	}

	public static Message getMessage(String id) {
		Message result = messages.get(id);
		return (result == null ? new Message(id) : result);
// return Optional<String> optionalID = messages.entrySet().stream().map(e -> e.getKey()==id).findAny();
		/*
		 * Optional<String> o = messages.entrySet() .stream() .filter( e -> e.getKey()
		 * == 1) .map(Map.Entry::getValue) .findFirst();
		 */
	}

	public static Map<String, Message> getAllMap() {
		return messages;
	}

	public static List<Message> getAllList() {
		List<Message> messageList = new ArrayList<Message>(messages.values());
		return messageList;
	}

	public static String addMessage(String id, Message message) {
		messages.put(id, message);
		return "The current number of messages is: " + messages.values().size();
	}

	public static String deleteMessage(String messageId) {
		Optional<Message> result = Optional.ofNullable(messages.remove(messageId));
		return result.toString();
	}

	public static Optional<Message> updateMessage(String messageId, Message message) {
		return Optional.ofNullable(messages.replace(messageId, message));
	}
}