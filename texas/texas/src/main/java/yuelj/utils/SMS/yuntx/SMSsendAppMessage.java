package yuelj.utils.SMS.yuntx;

public class SMSsendAppMessage {

	/*
	 * 注册验证码
	 */
	private static final String APP_REGIST_CODE = "100010";
	/*
	 * 重置密码验证码
	 */
	private static final String APP_FORGET_PWD_CODE = "100013";
	/*
	 * 预定订单支付成功短信
	 */
	private static final String APP_PREORDER_PAYED_SUCC = "";

	/**
	 * 发送注册验证码到手机
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendRegistMessage(String phone, String[] message) {
		/**
		 * 【】您的注册验证码为{1},请妥善保管,如非本人操作,建议您留意个人信息安全。
		 */
		boolean success = SMSsendTemplate.sendAppTempToPhone(APP_REGIST_CODE, phone, message);
		String txt = "发送注册验证码";
		if (success) {
			txt = txt + "成功";
		} else {
			txt = txt + "失败";
		}
	}

	/**
	 * 发送忘记密码短信验证码
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendForgetPwdMessage(String phone, String[] message) {
		/**
		 * 【】您正在重置密码，验证码{1}，请在3分钟内按提示提交验证码，切勿将验证码泄露于他人。
		 */
		boolean success = SMSsendTemplate.sendAppTempToPhone(APP_FORGET_PWD_CODE, phone, message);
		String txt = "发送忘记密码验证码";
		if (success) {
			txt = txt + "成功";
		} else {
			txt = txt + "失败";
		}
	}

	/**
	 * 发送订单支付成功短信
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendOrderPayedMsg(String order) {
		/**
		 * 【】您已成功预定{1}客栈，{2}晚，总价¥{3}，请在{4}前到店办理入住，
		 * 逾期默认取消；本客栈提供接送机、景点门票代订、包车等服务， 若需请致电联系或到店预约。 客栈地址：{5}，联系电话：{6}。
		 * 感谢您的选择，静候光临！
		 */
		String phone = "";
		String[] message = { "" };
		boolean success = SMSsendTemplate.sendAppTempToPhone(APP_PREORDER_PAYED_SUCC, phone, message);
		String txt = "发送订单支付成功短信";
		if (success) {
			txt = txt + "成功";
		} else {
			txt = txt + "失败";
		}
	}
}
