package com.mobei.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

//@Component
public class TestBeanPostProcessor2 implements BeanPostProcessor, PriorityOrdered {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("indexDao")) {
			System.out.println("TestBeanPostProcessor2 After");
		}
		return bean;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("indexDao")) {
			System.out.println("TestBeanPostProcessor2 Before");
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
