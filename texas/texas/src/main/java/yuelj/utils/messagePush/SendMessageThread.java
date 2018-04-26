package yuelj.utils.messagePush;

import java.util.List;

import yuelj.entity.UserMachineEntity;
import yuelj.service.UserMachineService;
import yuelj.utils.SpringUtil;
import yuelj.utils.logs.SystemLog;

/**
 * 推送消息线程
 * 
 * @author lxr
 *
 */
public class SendMessageThread extends Thread {
	private String uid;
	private String payload;
	private String title;
	private String description;

	public SendMessageThread(String uid, String payload, String title, String description) {
		this.uid = uid;
		this.payload = payload;
		this.title = title;
		this.description = description;
	}

	public void run() {
		UserMachineService machineService = (UserMachineService) SpringUtil.getBean("UserMachineService");
		UserMachineEntity um = new UserMachineEntity();
		um.setUid(uid);
		List<UserMachineEntity> list = machineService.queryUserMachine(um);
		for (UserMachineEntity e : list) {
			if (e.getXiaomiid() != null && e.getXiaomiid().length() != 0) {
				try {
					MiPushUtil.sendMessage(e.getXiaomiid(), payload, title, description);
				} catch (Exception e1) {
					SystemLog.printlog("SendMessageThread  uid:" + uid + " error: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
			if (e.getOtherid1() != null && e.getOtherid1().length() != 0) {
				APNSpush.sendMessageIOS(e.getOtherid1(), payload, title, description);
			}
			// if (e.getGetuiid() != null && e.getGetuiid().length() != 0) {
			// GeTuiUtil.sendMessageIOS(e.getGetuiid(), payload, title,
			// description);
			// }
		}
	}

}
