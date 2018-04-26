package yuelj.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import yuelj.constants.CacheKeys;
import yuelj.constants.Types;
import yuelj.entity.PageEntity;
import yuelj.entity.QQInfoEntity;
import yuelj.entity.UserEntity;
import yuelj.entity.UserMachineEntity;
import yuelj.entity.UserTokenEntity;
import yuelj.utils.HttpTool;
import yuelj.utils.NextIdDao;
import yuelj.utils.OAuth2Verify;
import yuelj.utils.OSSUpload;
import yuelj.utils.SessionUtil;
import yuelj.utils.StringUtil;
import yuelj.utils.SMS.yuntx.SMSsendAppMessage;
import yuelj.utils.cache.MemCacheOcsUtils;
import yuelj.utils.dateTime.DateUtil;
import yuelj.utils.md5encrypt.Md5;

/**
 * 用户登录注册信息维护
 * 
 * @author lixiaoran
 *
 */
@Controller
public class UserAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private NextIdDao nextId;
	private String phone;

	private String password;
	private String passwordConfirm;
	private String pic;
	private String name;
	private String validateCode; // 表单中的验证码
	private String qq;
	private String sessionKey; // session中的参数名
	public static final String UID = "uid";
	public static final String PERMISSION = "permission";
	// permission的权限值，操作员，用户
	public static final String OPERATOR = "3";
	// 1普通，3后台管理员
	public static final String USER = "1";
	public static final int userPicWidth = 300;
	public static final int userPicHeight = 300;

	/**
	 * 查询用户列表
	 */
	public void selectUsers() {
		UserEntity user = new UserEntity();
		PageEntity page = new PageEntity();
		if (phone != null) {
			// 支持模糊查询
			user.setPhone("%" + phone + "%");
		}
		page = gsonToObject(pageJson, PageEntity.class);
		user = gsonToObject(entityJson, UserEntity.class);
		if (page != null) {
			// 根据pageSize和pageNum计算从第几个开始
			setPageFrom(page);
			List<UserEntity> userlist = userService.selectUsersPage(user, page);
			setResponseListPage(userlist, page);
		} else {
			List<UserEntity> userlist = userService.selectUsers(user);
			setResponseList(userlist);
		}
	}

	/**
	 * 修改用户个人信息
	 */
	public void updateMyInfo() {
		UserEntity user = new UserEntity();
		user = jsonToObject(entityJson, UserEntity.class);
		String uid = getPvalue("uid");
		if (StringUtil.isEmpty(user.getId()) && !StringUtil.isEmpty(uid)) {
			user.setId(uid);
		}

		userService.updateUser(user);
		setResponseResult(Types.SUCCESS, "用户资料修改成功！");
	}

	/**
	 * 更新用户状态，审核，冻结
	 */
	public void updateUserState() {
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		userService.updateUserState(userEntity);
		setResponseResult(Types.SUCCESS, "用户状态修改成功！");
	}

	/**
	 * 更新用户状态，审核，冻结
	 */
	public void updateUser() {
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		userService.updateUser(userEntity);
		setResponseResult(Types.SUCCESS, "用户状态修改成功！");
	}

	/**
	 * 登录
	 */
	public void login() {
		// if(!validateCode()){
		// return;
		// }
		UserEntity user = new UserEntity();
		user = jsonToObject(entityJson, UserEntity.class);
		if (user == null || user.getLogintype() == null) {
			setResponseResult(Types.FAIL, "登录类型不能为空！");
			return;
		}
		// 登录类型，1phone，2qq，3weixin，4sina等
		if (user.getLogintype().equals("1")) {
			if (password != null) {
				user.setPassword(Md5.GetMD5Code(password));
			} else {
				user.setPassword(Md5.GetMD5Code(user.getPassword()));
			}
			if (user == null || user.getPassword() == null || user.getPhone() == null
					|| user.getPassword().length() == 0 || user.getPhone().length() == 0) {
				setResponseResult(Types.FAIL, "用户名和密码不能为空！");
				return;
			}
			// 1正常，2冻结
			user.setStatus("1");
			UserEntity datauser = userService.selectLoginUser(user);
			if (datauser == null) {
				setResponseResult(Types.FAIL, "登录失败,用户名密码错误！");
			} else {
				datauser.setPassword("");
				getSession().setAttribute(UID, datauser.getId());
				user.setUsertype("1");
				SessionUtil.setUser(this.getRequest(), datauser);
				// 获取token
				String userRespJson = getTokenResp(datauser);
				// 登录成功
				getSession().setAttribute(PERMISSION, USER);
				setResponseResultData(Types.SUCCESS, "登录成功", userRespJson);
			}
		} else {
			// 第三方登录
			if (user == null || user.getOauthid() == null || user.getAccesstoken() == null
					|| user.getOauthid().length() == 0 || user.getAccesstoken().length() == 0) {
				setResponseResult(Types.FAIL, "openid或accesstoken不能为空！");
				return;
			}
			if (user.getLogintype().equals("2")) {
				String openid = OAuth2Verify.getQQopenId(user.getAccesstoken());
				// 验证与qq服务器传来的是否一致
				if (openid.contains(user.getOauthid())) {
					UserEntity getuser = userService.selectLoginUser(user);
					if (getuser == null) {
						// 1正常，2冻结
						user.setStatus("1");
						// 不存在该用户的记录则新增
						user.setPhone(user.getOauthid());
						user.setPassword(user.getAccesstoken());
						QQInfoEntity info = OAuth2Verify.getQQinfo(user.getAccesstoken(), user.getOauthid());
						if (info != null) {
							user.setName(info.getNickname());
							// 下载头像，上传到云存储
							OSSUpload ossUp = new OSSUpload();
							String id = nextId.getNextId();
							File f = HttpTool.downloadNet(info.getFigureurl_qq_2(), id);
							try {
								ossUp.putObject(OSSUpload.IMG_BUCKET, id, f);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							user.setPic(id);
							user.setPicurl(info.getFigureurl_qq_2());
						} else {
							setDefaultUserName(user);
						}
						user.setUsertype(USER);
						userService.insertUser(user);
						user.setRegdate(DateUtil.getDateTimePastStr(new Date(), 0));
					} else {
						if (getuser.getPic() == null || getuser.getPic().length() == 0) {
							QQInfoEntity info = OAuth2Verify.getQQinfo(user.getAccesstoken(), user.getOauthid());
							// 下载头像，上传到云存储
							OSSUpload ossUp = new OSSUpload();
							String id = nextId.getNextId();
							File f = HttpTool.downloadNet(info.getFigureurl_qq_2(), id);

							try {
								ossUp.putObject(OSSUpload.IMG_BUCKET, id, f);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							getuser.setPic(id);
							userService.updateUser(getuser);
						}
						user = getuser;
					}
					user.setPhone("");
					user.setPassword("");

					// 获取token
					String userRespJson = getTokenResp(user);
					// 登录成功，返回token等用户信息
					getSession().setAttribute(PERMISSION, USER);
					setResponseResultData(Types.SUCCESS, "登录成功", userRespJson);
				} else {
					setResponseResult(Types.FAIL, "验证失败,tokenaccess第三方验证失败！");
				}
			}
		}

	}

	/**
	 * 管理员登录
	 */
	public void operatorLogin() {
		if (!validateCode()) {
			return;
		}
		UserEntity user = new UserEntity();
		user = jsonToObject(entityJson, UserEntity.class);
		if (user == null || user.getLogintype() == null) {
			setResponseResult(Types.FAIL, "登录类型不能为空！");
			return;
		}
		// 登录类型，1phone
		if (user.getLogintype().equals("1")) {
			if (password != null) {
				user.setPassword(Md5.GetMD5Code(password));
			} else {
				user.setPassword(Md5.GetMD5Code(user.getPassword()));
			}
			if (user == null || user.getPassword() == null || user.getPhone() == null
					|| user.getPassword().length() == 0 || user.getPhone().length() == 0) {
				setResponseResult(Types.FAIL, "用户名和密码不能为空！");
				return;
			}
			// 1正常，2冻结
			user.setStatus("1");
			user.setUsertype("3");
			user = userService.selectLoginUser(user);
			if (user == null) {
				setResponseResult(Types.FAIL, "登录失败,用户名密码错误！");

				ActionContext.getContext().getSession().remove("random");
			} else {
				user.setPassword("");
				getSession().setAttribute(UID, user.getId());
				SessionUtil.setUser(this.getRequest(), user);
				// 获取token
				String userRespJson = getTokenResp(user);
				// 登录成功
				getSession().setAttribute(PERMISSION, OPERATOR);
				setResponseResultData(Types.SUCCESS, "登录成功", userRespJson);
			}
		}

	}

	/**
	 * 客户端请求更新token值，在过滤器中验证过token
	 */
	public void updateToken() {
		if (uid == null) {
			setResponseResult(Types.FAIL, "uid不能为空！");
			return;
		}
		// 缓存写入token
		setResponseResultData(Types.SUCCESS, "更新成功", getTokenResp(uid, ""));
	}

	/**
	 * 退出登录
	 */
	public void logOut() {
		MemCacheOcsUtils.setData(CacheKeys.APP_TOKEN + uid, "");
		getSession().removeAttribute(UID);
		getSession().removeAttribute(PERMISSION);
		// 清除登录后绑定的设备
		UserMachineEntity info = new UserMachineEntity();
		info.setMachine(getPvalue("machine"));
		info.setUid("0");
		setResponseResult(Types.SUCCESS, "退出成功！");
	}

	private void setDefaultUserName(UserEntity userEntity) {
		// 默认用户昵称
		if (userEntity.getName() == null || userEntity.getName().length() == 0) {

			Random rand = new Random();
			int randNum = rand.nextInt(899999);
			randNum = randNum + 100000;
			userEntity.setName("用户" + randNum);
		}
	}

	/**
	 * app注册
	 */
	public void regist() {
		UserEntity userEntity = new UserEntity();
		if (entityJson == null || entityJson.length() == 0) {
			setResponseResult(Types.FAIL, "entityJson cannot be null");
			return;
		}
		userEntity = jsonToObject(entityJson, UserEntity.class);

		phone = userEntity.getPhone();
		validateCode = userEntity.getValidateCode();

		// 登录类型，1phone
		userEntity.setLogintype("1");
		setDefaultUserName(userEntity);
		String validateCodeServer = MemCacheOcsUtils.getData(phone + "validateCode");
		if (validateCodeServer == null) {
			setResponseResult(Types.FAIL, "验证码无效或过期（3分钟内有效）！");
			return;
		}
		if (!validateCodeServer.equals(validateCode)) {
			setResponseResult(Types.FAIL, "验证码错误！");
			return;
		}
		userEntity.setPassword(Md5.GetMD5Code(userEntity.getPassword()));
		userEntity.setUsertype(USER);
		userEntity.setStatus("1");

		if (userService.checkAccountAndMobile(userEntity) != null) {
			setResponseResult(Types.FAIL, "手机号已经存在！");
			return;
		}
		userService.insertUser(userEntity);
		userEntity.setRegdate(DateUtil.getDateTimePastStr(new Date(), 0));
		// 获取token
		String userRespJson = getTokenResp(userEntity.getId(), userEntity.getName());
		getSession().setAttribute(PERMISSION, USER);
		setResponseResultData(Types.SUCCESS, "注册成功！", userRespJson);
	}

	/**
	 * 获取注册短信验证码
	 */
	public void getRegistCode() {
		if (entityJson == null) {
			setResponseResult(Types.FAIL, "实体不能为空！");
			return;
		}
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		phone = userEntity.getPhone();
		if (phone == null) {
			setResponseResult(Types.FAIL, "电话号码不能为空！");
			return;
		}
		if (userService.checkAccountAndMobile(userEntity) != null) {
			setResponseResult(Types.FAIL, "手机号已经存在！");
			return;
		}

		Random rand = new Random();
		int randNum = rand.nextInt(8999);
		randNum = randNum + 1000;
		String arr[] = { randNum + "" };
		String validateCodeServer = MemCacheOcsUtils.getData(phone + "validateCode");
		if (validateCodeServer != null && validateCodeServer.length() != 0) {
			setResponseResult(Types.SUCCESS, "上次发送未失效！");
			return;
		}
		/**
		 * 3分钟有效
		 */
		MemCacheOcsUtils.setDataTime(phone + "validateCode", randNum + "", 60 * 3);
		SMSsendAppMessage.sendRegistMessage(phone, arr);
		setResponseResult(Types.SUCCESS, "成功");
	}

	/**
	 * 判断session中的图片验证码是否一致
	 * 
	 * @return
	 */
	private boolean validateCode() {
		if (validateCode == null || validateCode.length() == 0) {
			setResponseResult(Types.FAIL, "验证码为空！");
			return false;
		}
		// 从session中取出RandomAction.java 中生成的验证码random
		Object ob = ActionContext.getContext().getSession().get("random");
		if (ob == null) {
			setResponseResult(Types.FAIL, "验证码失效！");
			return false;
		}
		String arandom = (String) (ob);
		// 下面就是将session中保存验证码字符串与客户输入的验证码字符串对比了
		if (arandom.equals(validateCode)) {
			return true;
		} else {
			setResponseResult(Types.FAIL, "验证码错误！");
			return false;
		}
	}

	/**
	 * 获取当前登录用户信息
	 */
	public void getCurrentLoginUser() {
		setResponseObject(SessionUtil.getUser(getRequest()));
	}

	/**
	 * 修改密码
	 */
	public void updatePassword() {
		String oldPwd = getPvalue("oldPwd");
		String newPwd = getPvalue("newPwd");
		UserEntity user = SessionUtil.getUser(getRequest());
		UserEntity query = new UserEntity();
		query.setId(user.getId());
		query.setPassword(Md5.GetMD5Code(oldPwd));

	}

	/**
	 * 获取忘记密码验证码
	 */
	public void getForgetPasswordCode() {
		if (entityJson == null) {
			setResponseResult(Types.FAIL, "实体不能为空！");
			return;
		}
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		phone = userEntity.getPhone();
		if (phone == null) {
			setResponseResult(Types.FAIL, "电话号码不能为空！");
			return;
		}
		Random rand = new Random();
		int randNum = rand.nextInt(8999);
		randNum = randNum + 1000;
		String arr[] = { randNum + "" };
		String validateCodeServer = MemCacheOcsUtils.getData(phone + "forgetCode");
		if (validateCodeServer != null && validateCodeServer.length() != 0) {
			setResponseResult(Types.SUCCESS, "上次发送未失效！");
			return;
		}
		/**
		 * 三分钟内有效
		 */
		MemCacheOcsUtils.setDataTime(phone + "forgetCode", randNum + "", 60 * 3);
		SMSsendAppMessage.sendForgetPwdMessage(phone, arr);
		setResponseResult(Types.SUCCESS, "成功");
	}

	/**
	 * 忘记密码
	 */
	public void forgetUpdatePassword() {
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		phone = userEntity.getPhone();
		validateCode = userEntity.getValidateCode();

		if (phone == null) {
			setResponseResult(Types.FAIL, "账号或手机号不能为空！");
			return;
		}
		String validateCodeServer = MemCacheOcsUtils.getData(phone + "forgetCode");
		if (validateCodeServer == null) {
			setResponseResult(Types.FAIL, "验证码过期！");
			return;
		}
		if (!validateCodeServer.equals(validateCode)) {
			setResponseResult(Types.FAIL, "验证码错误！");
			return;
		}
		UserEntity qEntity = userService.selectUser(userEntity);
		if (qEntity == null) {
			setResponseResult(Types.FAIL, "手机号未注册！");
			return;
		}
		String newPwd = userEntity.getPassword();
		if (newPwd == null || newPwd.length() == 0) {
			setResponseResult(Types.FAIL, "密码不能为空！");
			return;
		}
		userEntity.setPassword(Md5.GetMD5Code(newPwd));
		userService.updateUserPassword(userEntity);
		setResponseResult(Types.SUCCESS, "修改成功");
	}

	/**
	 * 冻结用户及其发送的所有帖子和评论
	 */
	public void freezeUser() {
		UserEntity userEntity = new UserEntity();
		userEntity = jsonToObject(entityJson, UserEntity.class);
		if (userEntity.getId() == null || userEntity.getId().length() == 0) {
			setResponseResult(Types.FAIL, "id不能为空！");
			return;
		}
		userService.freezeUser(userEntity);
		setResponseResult(Types.SUCCESS, "冻结成功");
	}

	/**
	 * 根据uid获取要回传客户端的token对象
	 * 
	 * @param uid
	 * @return
	 */
	private String getTokenResp(String uid, String name) {
		// 缓存写入token
		String tokenMd5 = getNewToken(uid);
		UserTokenEntity userResponse = new UserTokenEntity();
		userResponse.setId(id);
		userResponse.setToken(tokenMd5);
		userResponse.setName(name);
		String userRespJson = toGsonString(userResponse, userResponse.getClass());
		return userRespJson;
	}

	/**
	 * 根据uid获取要回传客户端的token对象
	 * 
	 * @param uid
	 * @return
	 */
	private String getTokenResp(UserEntity user) {
		// 缓存写入token
		String tokenMd5 = getNewToken(user.getId());
		user.setToken(tokenMd5);
		String userRespJson = toGsonString(user, user.getClass());
		return userRespJson;
	}

	/**
	 * 根据uid生成新token，并写入缓存
	 * 
	 * @param uid
	 * @return
	 */
	private String getNewToken(String uid) {
		// 缓存写入token
		String token = uid + new Date().getTime();
		String tokenMd5 = Md5.GetMD5Code(token);
		MemCacheOcsUtils.setData(CacheKeys.APP_TOKEN + uid, tokenMd5);
		return tokenMd5;
	}

	private void getSessionParm() {
		Object value = getSession().getAttribute(sessionKey);
		if (value == null || value.toString().length() == Types.FAIL) {
			setResponseResult(Types.FAIL, "不存在该值");
		} else {
			setResponseResult(Types.SUCCESS, value.toString());
		}
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}
