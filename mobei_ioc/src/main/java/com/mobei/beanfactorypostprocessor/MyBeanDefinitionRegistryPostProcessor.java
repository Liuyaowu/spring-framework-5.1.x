package com.mobei.beanfactorypostprocessor;

import com.mobei.dao.BDRegistryPostProcessorDao;
import com.mobei.factorybean.MyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

//@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		System.out.println("MyBeanFactoryPostProcessor   postProcessBeanFactory");
//		AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) beanFactory.getBeanDefinition("indexDao");
//		abd.setScope("prototype");
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(BDRegistryPostProcessorDao.class);
		GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) bdb.getBeanDefinition();
		genericBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(genericBeanDefinition.getBeanClassName());
		genericBeanDefinition.setBeanClass(MyFactoryBean.class);
		registry.registerBeanDefinition("bDRegistryPostProcessorDao", genericBeanDefinition);
	}
}
