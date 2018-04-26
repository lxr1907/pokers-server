package yuelj.utils.messagePush;

import java.util.List;

public class MessagePushUtil {
	public static void sendMessage(String uid, String payload, String title, String description) {
		SendMessageThread setMIN = new SendMessageThread(uid, payload, title, description);
		setMIN.start();
	}

	public static void sendMessageToUsers(List<String> uids, String payload, String title, String description) {
		SendMessagesThread setMIN = new SendMessagesThread(uids, payload, title, description);
		setMIN.start();

	}
}
