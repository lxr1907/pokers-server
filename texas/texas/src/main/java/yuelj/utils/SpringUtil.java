package yuelj.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author xll
 * 2015年8月4日 上午11:27:33
 */
public class SpringUtil implements ApplicationContextAware {
    static ApplicationContext context = null;

    public void setApplicationContext(ApplicationContext appcontext) throws BeansException {
        context = appcontext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext appcontext) {
        context = appcontext;
    }
    
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }
}