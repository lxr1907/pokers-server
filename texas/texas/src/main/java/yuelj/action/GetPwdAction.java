package yuelj.action;

import org.springframework.stereotype.Controller;

import yuelj.utils.md5encrypt.Md5;

@Controller
public class GetPwdAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void getPwd() {
		Md5.getPassword();
		setResponse("success");
	}
}
