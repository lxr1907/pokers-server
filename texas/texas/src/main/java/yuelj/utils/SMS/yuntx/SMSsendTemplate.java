package yuelj.utils.SMS.yuntx;

import java.util.HashMap;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;

import yuelj.utils.logs.SystemLog;

/**
 * 容联，云通讯
 * 
 * @author lixiaoran
 *
 */
public class SMSsendTemplate {
	/**
	 * 发送验证码到手机
	 * 
	 * @param phone
	 * @param message
	 */
	public static void sendCodeToPhoneTest(String phone, String message) {
		phone = "";
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(SMSConstants.testUrl, "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(SMSConstants.accountSid, SMSConstants.accountToken);// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(SMSConstants.testappId);// 初始化应用ID
		result = restAPI.sendTemplateSMS(phone, "1", new String[] { message, "1" });

		SystemLog.printlog("SDKTestSendTemplateSMS result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				SystemLog.printlog(key + " = " + object);
			}
		} else {
			// 异常返回输出错误码和错误信息
			SystemLog.printlog("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
		}
	}

	public static void main(String[] args) {
		sendTemplateToPhone("100010", "18667102107", new String[] { "测试" });
	}

	/**
	 * 发送给云钥匙用户
	 * 
	 * @param templateid
	 * @param phone
	 * @param message
	 */
	public static boolean sendTemplateToPhone(String templateid, String phone, String[] message) {
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(SMSConstants.officialUrl, "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(SMSConstants.accountSid, SMSConstants.accountToken);// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(SMSConstants.appId);// 初始化应用ID
		result = restAPI.sendTemplateSMS(phone, templateid, message);

		SystemLog.printlog("SDKTestSendTemplateSMS result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				SystemLog.printlog(key + " = " + object);
			}
			return true;
		} else {
			// 异常返回输出错误码和错误信息
			SystemLog.printlog("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
			return false;
		}

	}

	/**
	 * 发送给丰登街app
	 * 
	 * @param templateid
	 * @param phone
	 * @param message
	 * @return
	 */
	public static boolean sendAppTempToPhone(String templateid, String phone, String[] message) {
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(SMSConstants.officialUrl, "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(SMSConstants.accountSid, SMSConstants.accountToken);// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(SMSConstants.appId);// 初始化应用ID
		result = restAPI.sendTemplateSMS(phone, templateid, message);

		SystemLog.printlog("SDKTestSendTemplateSMS result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				SystemLog.printlog(key + " = " + object);
			}
			return true;
		} else {
			// 异常返回输出错误码和错误信息
			SystemLog.printlog("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
			return false;
		}

	}
}
