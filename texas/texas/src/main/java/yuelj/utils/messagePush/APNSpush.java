package yuelj.utils.messagePush;

import java.io.File;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import yuelj.utils.logs.SystemLog;

public class APNSpush {
	private void push(final String alias, final String action, final int badge, String title, String content) {
		// WEB-INF/classes/
		// final String certPath =
		// "./src/main/java/yuelj/utils/messagePush/push-cert.p12";
		final String certPath = "/home/apache-tomcat-8.0.12/webapps/fdj/WEB-INF/classes/push-cert.p12";

		final String certPassword = "sairewo";

		ApnsService apnsService = APNS.newService().withCert(certPath, certPassword).withSandboxDestination().build();

		String payload = APNS.newPayload().alertBody(content).alertAction(action).alertTitle(title).badge(badge)
				.build();
		SystemLog.printlog(payload);
		apnsService.push(alias, payload);
	}

	public static void main(String[] args) {
		File directory = new File("");// 设定为当前文件夹
		try {
			SystemLog.printlog(directory.getCanonicalPath());// 获取标准的路径
			SystemLog.printlog(directory.getAbsolutePath());// 获取绝对路径
		} catch (Exception e) {
		}
		APNSpush push = new APNSpush();
		String deviceToken = "d7722cf70a84ed51d23ff6411e5b07a0684637be554d5e5925159464a7d3f0d9";
		push.push(deviceToken, "asdfasdf", 1, "title", "content");
	}

	public static void sendMessageIOS(String deviceToken, String payload, String title, String content) {
		APNSpush push = new APNSpush();
		push.push(deviceToken, payload, 1, title, content);
	}
}
