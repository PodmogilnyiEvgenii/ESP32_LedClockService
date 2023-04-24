package com.home.ledclockservice;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }
}
