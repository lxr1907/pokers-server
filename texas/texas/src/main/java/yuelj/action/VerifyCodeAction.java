package yuelj.action;

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Controller;

import yuelj.utils.RandomNumUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 图片验证码
 * @author lxr
 *
 */
@Controller
public class VerifyCodeAction extends ActionSupport {
	private static final long serialVersionUID = -7896389603376939696L;
	private ByteArrayInputStream inputStream;

	public String execute() throws Exception {
		RandomNumUtil rdnu = RandomNumUtil.Instance();
		this.setInputStream(rdnu.getImage());// 取得带有随机字符串的图片
		ActionContext.getContext().getSession().put("random", rdnu.getString());// 取得随机字符串放入HttpSession
		return SUCCESS;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

}
