package com.mobei.test;

import com.mobei.aop.AppConfig;
import com.mobei.bean.TestConstructorBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestCase {

public static void main(String[] args) {
	//spring前提环境准备
	//1.准备工厂=DefaultListableBeanFactory
	//2.实例化一个bdReader和一个scanner
		AnnotationConfigApplicationContext ac =
				new AnnotationConfigApplicationContext();
		//把一个class转成beandefinition,最后put到map(DefaultListableBeanFactory的一个变量:beanDefinitionMap)中
	ac.register(AppConfig.class);
//	ac.register(TestDao.class);
//	ac.register(NormalBeanWithBeanAnno.class);
//	ac.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
//	ac.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
//		ac.registerBean(IndexDao.class);
		//初始化spring的环境
		ac.refresh();
		ac.getBean(TestConstructorBean.class).query();
//	System.out.println(ac.getBean("beanDao1"));
//	System.out.println(ac.getBean("beanDao2"));

//	MyRegistrarDao myRegistrarDao = (MyRegistrarDao) ac.getBean("myRegistrarDao");
//	MyRegistrarDao myRegistrarDao = ac.getBean(MyRegistrarDao.class);
//	System.out.println("---" + myRegistrarDao);
//	myRegistrarDao.find();
//	System.out.println("+++" + myRegistrarDao);
//		ac.close();

//	AnnotationConfigApplicationContext ac =
//			new AnnotationConfigApplicationContext(AppConfig.class);
//	IndexDao dao = ac.getBean(IndexDao.class);
//dao.query();
//
//	ImportSelectorDao importDao = 	ac.getBean(ImportSelectorDao.class);
//	System.out.println(importDao);
	//	IndexDao dao1 = ac.getBean(IndexDao.class);

//	dao.query();

//	System.out.println(dao.hashCode() + "   " + dao1.hashCode());


	/***************************************************/
//	Enhancer enhancer = new Enhancer();
//	enhancer.setSuperclass(EnhancerDao.class);
//	enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
//	enhancer.setCallback(new MyMethodInterceptor());
//	EnhancerDao enhancerDao = (EnhancerDao) enhancer.create();
//	enhancerDao.test();

	/**************test getMergedLocalBeanDefinition method***********************/
//	System.out.println(((ParentBean)ac.getBean("parent")).getName());
//	System.out.println(((ParentBean)ac.getBean("child")).getName());


}



}
