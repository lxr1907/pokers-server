package yuelj.utils.messagePush;

import java.util.ArrayList;
import java.util.List;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import com.xiaomi.xmpush.server.TargetedMessage;

public class MiPushUtil {
	private static final String MY_PACKAGE_NAME = "com.srw.licaiandroid";
	private static final String APP_SECRET_KEY = "rULyvP3wWghBYH0KRmNmUw==";
	private static final String MY_PACKAGE_NAME_IOS = "com.aim.fengdengjie.test";
	private static final String APP_SECRET_KEY_IOS = "xERBI2LWj0hyxgMkFXDIOw==";
	private static final String regId = "";

	/**
	 * 构建发送给andorid的消息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Message buildMessageAndroid() throws Exception {
		String messagePayload = "This is a message";
		String title = "notification title";
		String description = "notification description";
		Message message = new Message.Builder().title(title).description(description).payload(messagePayload)
				.restrictedPackageName(MY_PACKAGE_NAME).passThrough(1) // 消息使用透传方式
				.notifyType(1) // 使用默认提示音提示
				.build();
		return message;
	}

	/**
	 * 构建发送给IOS的消息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Message buildMessageIOS() throws Exception {
		String description = "notification description";
		Message message = new Message.IOSBuilder().description(description).soundURL("default") // 消息铃声
				.badge(1) // 数字角标
				.category("action") // 快速回复类别
				.extra("key", "value") // 自定义键值对
				.build();
		return message;
	}

	public List<TargetedMessage> buildMessages() throws Exception {
		List<TargetedMessage> messages = new ArrayList<TargetedMessage>();
		TargetedMessage message1 = new TargetedMessage();
		message1.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias1");
		message1.setMessage(buildMessageAndroid());
		messages.add(message1);
		TargetedMessage message2 = new TargetedMessage();
		message2.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias2");
		message2.setMessage(buildMessageAndroid());
		messages.add(message2);
		return messages;
	}

	public static void main(String[] args) {
		String regId = "LAzOSfb7O0uSSHBg80bnlM5Xv166+vmZEHIvFnVnzrY=";
		String regIdIos = "KguLN+t3SO4sg5We8jR4GFHi6tWI5D5YInRv/22Ao/A=";
		try {
		//	sendMessage(regId, "{type:1,url:'www.baidu.com',pic:58458,text:'test1234232'}", "title", "description");
			sendMessageIOS(regIdIos,
			 "{type:1,url:'www.baidu.com',pic:58458,text:'test1234232'}",
			 "title", "description");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendMessageIOS(String regId, String payload, String title, String description) throws Exception {
		Constants.useSandbox();
		Sender sender = new Sender(APP_SECRET_KEY_IOS);
		Message message = new Message.Builder().title(title).description(description).payload(payload).passThrough(1) // 消息使用透传方式
				.restrictedPackageName(MY_PACKAGE_NAME_IOS).notifyType(1) // 使用默认提示音提示
				.build();
		Result result = sender.send(message, regId, 0); // Result对于sendToAlias()，broadcast()和send()调用方式完全一样
		System.out.printf("Server response: " + "MessageId: " + result.getMessageId() + " ErrorCode: "
				+ result.getErrorCode().toString() + " Reason: " + result.getReason());
	}
	public static void sendMessage(String regId, String payload, String title, String description) throws Exception {
		Constants.useOfficial();
		Sender sender = new Sender(APP_SECRET_KEY);
		Message message = new Message.Builder().title(title).description(description).payload(payload).passThrough(1) // 消息使用透传方式
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		Result result = sender.send(message, regId, 0); // Result对于sendToAlias()，broadcast()和send()调用方式完全一样
		System.out.printf("Server response: " + "MessageId: " + result.getMessageId() + " ErrorCode: "
				+ result.getErrorCode().toString() + " Reason: " + result.getReason());
	}

	public void sendMessages() throws Exception {
		Constants.useOfficial();
		Sender sender = new Sender(APP_SECRET_KEY);
		List<TargetedMessage> messages = new ArrayList<TargetedMessage>();
		TargetedMessage targetedMessage1 = new TargetedMessage();
		targetedMessage1.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias1");
		String messagePayload1 = "This is a message1";
		String title1 = "notification title1";
		String description1 = "notification description1";
		Message message1 = new Message.Builder().title(title1).description(description1).payload(messagePayload1)
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		targetedMessage1.setMessage(message1);
		messages.add(targetedMessage1);
		TargetedMessage targetedMessage2 = new TargetedMessage();
		targetedMessage1.setTarget(TargetedMessage.TARGET_TYPE_ALIAS, "alias2");
		String messagePayload2 = "This is a message2";
		String title2 = "notification title2";
		String description2 = "notification description2";
		Message message2 = new Message.Builder().title(title2).description(description2).payload(messagePayload2)
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		targetedMessage2.setMessage(message2);
		messages.add(targetedMessage2);
		sender.send(messages, 0); // 根据alias，发送消息到指定设备上，不重试。
	}

	public void sendMessageToAlias() throws Exception {
		Constants.useOfficial();
		Sender sender = new Sender(APP_SECRET_KEY);
		String messagePayload = "This is a message";
		String title = "notification title";
		String description = "notification description";
		String alias = "testAlias"; // alias非空白，不能包含逗号，长度小于128。
		Message message = new Message.Builder().title(title).description(description).payload(messagePayload)
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		sender.sendToAlias(message, alias, 0); // 根据alias，发送消息到指定设备上，不重试。
	}

	public void sendMessageToAliases() throws Exception {
		Constants.useOfficial();
		Sender sender = new Sender(APP_SECRET_KEY);
		String messagePayload = "This is a message";
		String title = "notification title";
		String description = "notification description";
		List<String> aliasList = new ArrayList<String>();
		aliasList.add("testAlias1"); // alias非空白，不能包含逗号，长度小于128。
		aliasList.add("testAlias2"); // alias非空白，不能包含逗号，长度小于128。
		aliasList.add("testAlias3"); // alias非空白，不能包含逗号，长度小于128。
		Message message = new Message.Builder().title(title).description(description).payload(messagePayload)
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		sender.sendToAlias(message, aliasList, 0); // 根据aliasList，发送消息到指定设备上，不重试。
	}

	public void sendBroadcast() throws Exception {
		Constants.useOfficial();
		Sender sender = new Sender(APP_SECRET_KEY);
		String messagePayload = "This is a message";
		String title = "notification title";
		String description = "notification description";
		String topic = "testTopic";
		Message message = new Message.Builder().title(title).description(description).payload(messagePayload)
				.restrictedPackageName(MY_PACKAGE_NAME).notifyType(1) // 使用默认提示音提示
				.build();
		sender.broadcast(message, topic, 0); // 根据topic，发送消息到指定一组设备上，不重试。
	}
}
