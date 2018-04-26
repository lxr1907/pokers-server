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
public class SendMessagesThread extends Thread {
	private List<String> uids;
	private String payload;
	private String title;
	private String description;

	public SendMessagesThread(List<String> uid, String payload, String title, String description) {
		this.uids = uid;
		this.payload = payload;
		this.title = title;
		this.description = description;
	}

	public void run() {
		for (String uid : uids) {
			try {
				// 每个推送50毫秒
				Thread.sleep(50l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send(uid);
		}

	}

	private void send(String uid) {
		UserMachineService machineService = (UserMachineService) SpringUtil.getBean("UserMachineService");
		UserMachineEntity um = new UserMachineEntity();
		um.setUid(uid);
		List<UserMachineEntity> list = machineService.queryUserMachine(um);
		for (UserMachineEntity e : list) {
			if (e.getXiaomiid() != null && e.getXiaomiid().length() != 0) {
				try {
					MiPushUtil.sendMessage(e.getXiaomiid(), payload, title, description);
				} catch (Exception e1) {
					SystemLog.printlog("SendMessagesThread  uid:" + uid + " error: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
			if (e.getOtherid1() != null && e.getOtherid1().length() != 0) {
				APNSpush.sendMessageIOS(e.getOtherid1(), payload, title, description);
			}
			// if (e.getGetuiid() != null && e.getGetuiid().length() != 0) {
			// GeTuiUtil.SendMessagesIOS(e.getGetuiid(), payload, title,
			// description);
			// }
		}
	}
}
