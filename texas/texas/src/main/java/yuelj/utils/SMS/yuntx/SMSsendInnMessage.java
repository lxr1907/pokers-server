package yuelj.utils.SMS.yuntx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import yuelj.entity.MessageParmsEntity;
import yuelj.entity.UserEntity;

public class SMSsendInnMessage {

	/*
	 * 预定
	 */
	private static final String PRE_ORDER_TEM_ID = "34971";
	/*
	 * 入住
	 */
	private static final String IN_ORDER_TEM_ID = "34126";
	/*
	 * 接机
	 */
	private static final String GET_PLAIN_TEM_ID = "34127";
	/*
	 * 离店
	 */
	private static final String LEAVE_TEM_ID = "34129";

	/**
	 * 发送预定信息到手机
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendPreOrderMessage(UserEntity user, String phone,
			String[] message) {

		boolean success = SMSsendTemplate.sendTemplateToPhone(PRE_ORDER_TEM_ID,
				phone, message);
		if (success) {

		}
		// 记录操作日志
//		OperationLog olog = new OperationLog();
//		olog.setContent(phone + "发送预定短信," + Arrays.toString(message));
//		olog.setTypename("发送预定短信");
//		olog.setType("7");
//		olog.setOpename(user.getName());
		//OperationLogUtil.addOperationLog(olog);
	}

	/**
	 * 发送入住短信
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendCheckInMessage(UserEntity user, String phone,
			String[] message) {

		boolean success = SMSsendTemplate.sendTemplateToPhone(IN_ORDER_TEM_ID,
				phone, message);
		if (success) {

		}
		// 记录操作日志
//		OperationLog olog = new OperationLog();
//		olog.setContent(phone + "发送入住短信," + Arrays.toString(message));
//		olog.setTypename("发送入住短信");
//		olog.setType("7");
//		olog.setOpename(user.getName());
		//OperationLogUtil.addOperationLog(olog);
	}

	/**
	 * 发送离店短信
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public static void sendCheckOutMessage(UserEntity user, String phone,
			String[] message) {

		boolean success = SMSsendTemplate.sendTemplateToPhone(LEAVE_TEM_ID,
				phone, message);
		if (success) {

		}
		// 记录操作日志
		// OperationLog olog = new OperationLog();
		// olog.setContent(phone + "发送离店短信," + Arrays.toString(message));
		// olog.setTypename("发送离店短信");
		// olog.setType("7");
		// olog.setOpename(user.getName());
		//OperationLogUtil.addOperationLog(olog);
	}


	public static String[] getParmsStringArr(MessageParmsEntity parms)
			throws Exception {
		String[] message = null;
		List<String> mList = new ArrayList<String>();
		for (int i = 1; i < 10; i++) {
			Method m = parms.getClass().getMethod("getparm" + i,
					new java.lang.Class[] {});
			Object parm = m.invoke(parms, new Object[] {});
			if (parm != null && parm.toString().length() != 0) {
				mList.add(parm.toString());
			} else {
				break;
			}
		}
		message = mList.toArray(new String[mList.size()]);
		return message;
	}
	
}
