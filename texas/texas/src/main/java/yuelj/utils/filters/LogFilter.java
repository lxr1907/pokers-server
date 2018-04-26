package yuelj.utils.filters;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import yuelj.action.UserAction;
import yuelj.entity.SystemLogEntity;
import yuelj.entity.UserMachineEntity;
import yuelj.service.SystemLogService;
import yuelj.service.UserMachineService;
import yuelj.utils.dateTime.DateUtil;
import yuelj.utils.logs.LogThread;

/**
 * 日志记录过滤器
 * 
 * @author lxr
 *
 */
@Controller
public class LogFilter implements Filter {
	private static final long serialVersionUID = -4220585371840995250L;
	@Autowired
	SystemLogService logservice;
	@Autowired
	UserMachineService userMachineS;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(request, response);
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		// uri
		String uri = req.getRequestURI();

		String message = "";
		SystemLogEntity log = new SystemLogEntity();
		String parm = "";
		Map<String, String[]> properties = request.getParameterMap();
		// 返回值Map
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		String uid = null;
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			parm = parm + name + ":" + value + ",";
		}
		uid = req.getParameter("uid");
		if (session != null && session.getAttribute(UserAction.UID) != null) {
			log.setUserid(session.getAttribute(UserAction.UID).toString());
			message = "用户：" + session.getAttribute(UserAction.UID) + ",访问：" + req.getRequestURI() + ",参数：" + parm;
		} else {
			if (uid != null && uid.length() != 0) {
				log.setUserid(uid);
			}
			String token = req.getParameter("token");
			if (token != null && token.length() != 0) {
				log.setToken(token);
			}
			message = "匿名访问：" + req.getRequestURI() + ",参数：" + parm;
		}
		String machine = req.getParameter("machineid");
		if (machine != null && machine.length() != 0) {
			log.setMachine(machine);
		}
		String devicetype = req.getParameter("devicetype");
		if (devicetype != null && devicetype.length() != 0) {
			log.setClienttype(devicetype);
		}
		String appversion = req.getParameter("appversion");
		if (appversion != null && appversion.length() != 0) {
			log.setAppversion(appversion);
		}

		log.setContent(message);
		log.setIp(req.getRemoteAddr());
		// nginx转发获取ip
		if (req.getHeader("X-real-ip") != null) {
			log.setIp(req.getHeader("X-real-ip").toString());
		}
		log.setOperation(req.getRequestURI());
		log.setDatetime(DateUtil.getDateTimeString(new Date()));

		if (!uri.contains("uploadImg") && !uri.contains("selectStock")) {// 加入系统访问日志
			LogThread logt = new LogThread(log, logservice);
			Thread logtt = new Thread(logt);
			logtt.start();
		}
		// 记录用户的machine,用于推送等
		if (machine != null && machine.length() != 0) {
			UserMachineEntity machineinfo = new UserMachineEntity();
			machineinfo.setMachine(machine);
			List<UserMachineEntity> mlist = userMachineS.queryUserMachine(machineinfo);
			if (uid != null && uid.length() != 0) {
				machineinfo.setUid(uid);
			} else {
				machineinfo.setUid("0");
			}
			String xiaomiid = req.getParameter("xiaomiid");
			if (xiaomiid != null && xiaomiid.length() != 0) {
				machineinfo.setXiaomiid(xiaomiid.replace(" ", "+"));
			}
			String otherid1 = req.getParameter("otherid1");
			if (otherid1 != null && otherid1.length() != 0) {
				machineinfo.setOtherid1(otherid1);
			}
			if (devicetype != null && devicetype.length() != 0) {
				machineinfo.setDevicetype(devicetype);
			}
			if (mlist.size() == 0) {
				userMachineS.addUserMachine(machineinfo);
			} else {
				userMachineS.updateUserMachine(machineinfo);
			}
		}
	}

	public void init(FilterConfig config) throws ServletException {

	}
}
