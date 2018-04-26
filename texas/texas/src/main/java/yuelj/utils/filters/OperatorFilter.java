package yuelj.utils.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yuelj.action.UserAction;
import yuelj.utils.logs.SystemLog;

public class OperatorFilter implements Filter {
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		// uri
		String uri = req.getRequestURI();

		if (session != null && session.getAttribute(UserAction.PERMISSION) != null) {
			String permission = session.getAttribute(UserAction.PERMISSION).toString();
			if (permission.equals(UserAction.OPERATOR)) {
				chain.doFilter(request, response);
			} else {
				SystemLog.printlog("user no enough power: " + permission);
				FilterUtil.setResponse("{state:2,message:\"user no enough power权限不足\"}", resp);
			}
		} else {
			FilterUtil.setResponse("{state:2,message:\"nopower没有权限\"}", resp);
		}

	}

	public void init(FilterConfig config) throws ServletException {

	}
}
