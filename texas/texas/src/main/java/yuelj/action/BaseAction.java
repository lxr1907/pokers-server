package yuelj.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import yuelj.entity.ListPo;
import yuelj.entity.PageEntity;
import yuelj.entity.Result;
import yuelj.entity.UserEntity;
import yuelj.service.UserService;
import yuelj.utils.SessionUtil;
import yuelj.utils.StringUtil;
import yuelj.utils.cache.MemCacheOcsUtils;
import yuelj.utils.serialize.JsonUtils;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1137290399578358652L;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	protected String id;
	protected String ids;
	protected String uid;
	protected String token;
	// 实体json
	protected String entityJson = "{}";
	// 分页参数json
	protected String pageJson;

	@Autowired
	UserService userService;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPageJson() {
		return pageJson;
	}

	public void setPageJson(String pageJson) {
		this.pageJson = pageJson;
	}

	public String getEntityJson() {
		return entityJson;
	}

	public void setEntityJson(String entityJson) {
		this.entityJson = entityJson;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	/**
	 * 将字符串json转换为c指定的类 json-lib包
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T jsonToObject(String json, Class<T> c) {
		if (json == null || json.length() == 0) {
			return null;
		}
		return (T) JsonUtils.fromJson(json, c);
	}

	/**
	 * 将字符串json转换为c指定的类 gson包
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T gsonToObject(String json, Class<T> c) {
		if (json == null || json.length() == 0) {
			return null;
		}
		return (T) JsonUtils.fromJson(json, c);
	}

	/**
	 * c指定的类转换为json gson包
	 * 
	 * @param obj
	 * @param c
	 * @return
	 */
	public static <T> String toGsonString(Object obj, Class<T> c) {
		return JsonUtils.toJson(obj, c);
	}

	protected void setResponse(String json) {
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
				.get(StrutsStatics.HTTP_RESPONSE);

		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;

		try {
			pw = response.getWriter();
			pw.write(json);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected void setResponseObject(Object obj) {
		this.setResponse(toGsonString(obj, obj.getClass()));
	}

	protected void setResponseList(List<? extends Object> list) {
		ListPo listPo = new ListPo();
		listPo.setList(list);
		this.setResponse(toGsonString(listPo, listPo.getClass()));
	}

	protected void setResponseListPage(List<? extends Object> list, PageEntity page) {
		ListPo listPo = new ListPo();
		listPo.setList(list);
		listPo.setPage(page);
		this.setResponse(toGsonString(listPo, listPo.getClass()));
	}

	protected void setResponseListPage(List<? extends Object> list, PageEntity page, String data) {
		ListPo listPo = new ListPo();
		listPo.setList(list);
		listPo.setPage(page);
		listPo.setData(data);
		this.setResponse(toGsonString(listPo, listPo.getClass()));
	}

	public static void setPageFrom(PageEntity page) {
		if (StringUtil.isEmpty(page.getPageSize()) || StringUtil.isEmpty(page.getPageNum())) {
			return;
		}
		int ipageSize = Integer.parseInt(page.getPageSize());

		int ipageNum = Integer.parseInt(page.getPageNum());
		int ifrom = (ipageNum - 1) * ipageSize;
		page.setIfrom(ifrom);

		page.setIpageSize(ipageSize);

	}

	public PageEntity getPage() {
		PageEntity page = new PageEntity();
		if (pageJson != null) {
			page = jsonToObject(pageJson, PageEntity.class);
			setPageFrom(page);
		} else {
			page = PageEntity.getDefaultPage();
		}
		return page;
	}

	/**
	 * 返回结果
	 * 
	 * @param state
	 *            1：表示成功； 0：表示失败； 500：系统错误
	 * @param message
	 */
	protected void setResponseResult(int state, String message) {
		Result result = new Result(state, message);
		this.setResponse(toGsonString(result, result.getClass()));
	}

	/**
	 * 返回结果
	 * 
	 * @param state
	 *            1：表示成功； 0：表示失败；
	 * @param message
	 */
	protected void setResponseResult(int state, String message, String id) {
		Result result = new Result(state, message);
		result.setId(id);
		this.setResponse(toGsonString(result, result.getClass()));
	}

	/**
	 * 返回结果和数据
	 * 
	 * @param state
	 *            1：表示成功； 0：表示失败；
	 * @param message
	 *            消息
	 * @param data
	 *            数据
	 */
	protected void setResponseResultData(int state, String message, String data) {
		Result result = new Result(state, message);
		result.setData(data);
		this.setResponse(toGsonString(result, result.getClass()));
	}

	/**
	 * 返回结果和数据
	 * 
	 * @param state
	 *            1：表示成功； 0：表示失败；
	 * @param message
	 *            消息
	 * @param datastr
	 *            数据
	 */
	protected void setResponseResultDataStr(int state, String message, String datastr) {
		Result result = new Result(state, message);
		result.setDatastr(datastr);
		this.setResponse(toGsonString(result, result.getClass()));
	}

	@Override
	public void setServletResponse(HttpServletResponse paramHttpServletResponse) {
		this.response = paramHttpServletResponse;
	}

	@Override
	public void setServletRequest(HttpServletRequest paramHttpServletRequest) {
		this.request = paramHttpServletRequest;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getPvalue(String paramName) {
		return this.getRequest().getParameter(paramName);
	}

	public UserEntity getLoginUser() {
		UserEntity e = SessionUtil.getUser(getRequest());
		if (uid != null && uid.length() != 0) {
			if (e != null && !e.getId().equals(uid)) {
				String json = MemCacheOcsUtils.getData("UserMapper" + uid);
				if (json == null || json.length() == 0) {
					UserEntity quser = new UserEntity();
					quser.setId(uid);
					e = userService.selectLoginUser(quser);
				} else {
					e = JsonUtils.fromJson(json, UserEntity.class);
				}
			}
		}
		if (e == null && uid != null && uid.length() != 0) {
			String json = MemCacheOcsUtils.getData("UserMapper" + uid);
			if (json == null || json.length() == 0) {
				UserEntity quser = new UserEntity();
				quser.setId(uid);
				e = userService.selectLoginUser(quser);
			} else {
				e = JsonUtils.fromJson(json, UserEntity.class);
			}
		}
		return e;
	}

}
