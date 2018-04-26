package yuelj.utils.messagePush;

import java.io.IOException;

public class GeTuiUtil {

	// 定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
	private static String appId = "QdAIFWzQ7d9jVsmvKPOty9";
	private static String appKey = "v3ZSNetWaO8wFpLUqqcLS4";
	private static String masterSecret = "UaWoRy7ltPAnd7djNXUO23";
	private static String url = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void main(String[] args) throws IOException {

		// IGtPush push = new IGtPush(url, appKey, masterSecret);
		//
		// // 定义"点击链接打开通知模板"，并设置标题、内容、链接
		// LinkTemplate template = new LinkTemplate();
		// template.setAppId(appId);
		// template.setAppkey(appKey);
		// template.setTitle("server 欢迎使用个推!");
		// template.setText("server 这是一条推送消息~");
		// template.setUrl("http://getui.com");
		// List<String> appIds = new ArrayList<String>();
		// appIds.add(appId);
		//
		// // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
		// AppMessage message = new AppMessage();
		// message.setData(template);
		// message.setAppIdList(appIds);
		// message.setOffline(true);
		// message.setOfflineExpireTime(1000 * 600);
		// IPushResult ret = push.pushMessageToApp(message);
		// SystemLog.printlog(ret.getResponse().toString());
	}

	// public static TransmissionTemplate getTransmissionTemplate(String
	// payload) {
	// TransmissionTemplate template = new TransmissionTemplate();
	// template.setAppId(appId);
	// template.setAppkey(appKey);
	// // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	// template.setTransmissionType(2);
	// template.setTransmissionContent(payload);
	// // 设置定时展示时间
	// // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	// return template;
	// }

	public static void sendMessageIOS(String regId, String payload, String title, String description) throws Exception {
		// IGtPush push = new IGtPush(url, appKey, masterSecret);
		// // 定义"点击链接打开通知模板"，并设置标题、内容、链接
		// TransmissionTemplate template = getTransmissionTemplate(payload);
		// List<String> appIds = new ArrayList<String>();
		// appIds.add(appId);
		//
		// // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
		// AppMessage message = new AppMessage();
		// message.setData(template);
		// message.setAppIdList(appIds);
		// message.setOffline(true);
		// message.setOfflineExpireTime(1000 * 600);
		// IPushResult ret = push.pushMessageToApp(message);
		// SystemLog.printlog(ret.getResponse().toString());
	}
	// public static TransmissionTemplate getTemplate() {
	// TransmissionTemplate template = new TransmissionTemplate();
	// template.setAppId(appId);
	// template.setAppkey(appkey);
	// template.setTransmissionContent("透传内容");
	// template.setTransmissionType(2);
	// APNPayload payload = new APNPayload();
	// payload.setBadge(1);
	// payload.setContentAvailable(1);
	// payload.setSound("default");
	// payload.setCategory("$由客户端定义");
	// //简单模式APNPayload.SimpleMsg
	// payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
	// //字典模式使用下者
	// //payload.setAlertMsg(getDictionaryAlertMsg());
	// template.setAPNInfo(payload);
	// return template;
	// }
	// private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(){
	// APNPayload.DictionaryAlertMsg alertMsg = new
	// APNPayload.DictionaryAlertMsg();
	// alertMsg.setBody("body");
	// alertMsg.setActionLocKey("ActionLockey");
	// alertMsg.setLocKey("LocKey");
	// alertMsg.addLocArg("loc-args");
	// alertMsg.setLaunchImage("launch-image");
	// // IOS8.2以上版本支持
	// alertMsg.setTitle("Title");
	// alertMsg.setTitleLocKey("TitleLocKey");
	// alertMsg.addTitleLocArg("TitleLocArg");
	// return alertMsg;
	// }
}
