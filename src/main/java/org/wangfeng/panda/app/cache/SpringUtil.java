package org.wangfeng.panda.app.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public static String env;

    public static String getEnv(){
        return env;
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException {
        SpringUtil.applicationContext = arg0;
    }


//    public static Object getBean(String name){
//        return applicationContext.getBean(name);
//    }

//    public static boolean containsBean(String name){
//        return applicationContext.containsBean(name);
//    }

//    public static Object getConstainsBean(String name){
//        if(containsBean(name)){
//            return applicationContext.getBean(name);
//        }
//        return null;
//    }


    /**
     *
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
