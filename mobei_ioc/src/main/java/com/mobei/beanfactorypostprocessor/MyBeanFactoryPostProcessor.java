package com.mobei.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

//@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("MyBeanFactoryPostProcessor   postProcessBeanFactory");
//		AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) beanFactory.getBeanDefinition("indexDao");
//		abd.setScope("prototype");
	}
}
