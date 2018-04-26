package yuelj.utils.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import yuelj.utils.logs.SystemLog;

public class ExceptionInterceptor implements Interceptor {
	private static final long serialVersionUID = -4654850207254592726L;

	public void destroy() {

	}

	public void init() {

	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String methodName = actionInvocation.getProxy().getMethod();
		String className = actionInvocation.getAction().getClass().getName();
		String result = "";
		try {
			if (actionInvocation != null) {
				result = actionInvocation.invoke();
			}
		} catch (Exception e) {
			SystemLog.printlog("method:" + methodName + "，in class:" + className + ",exception :" + e);
		}
		return result;

	}
}
