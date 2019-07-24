package com.mobei.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

//@Component
public class TestBeanPostProcessor implements BeanPostProcessor/*, PriorityOrdered*/ {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("indexDao")) {
			System.out.println("TestBeanPostProcessor After");
		}
		return bean;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("indexDao")) {
			System.out.println("TestBeanPostProcessor Before");
		}
		return bean;
	}

//	@Override
//	public int getOrder() {
//		return 2;
//	}
}
