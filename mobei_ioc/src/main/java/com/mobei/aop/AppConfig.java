package com.mobei.aop;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.mobei")
/**
 * 添加该注解以后会往spring容器中注入一个类AnnotationAwareAspectJAutoProxyCreator,这也是一个BeanPostProcessor,
 * 会在bean初始化之后执行这个后置处理器的方法完成代理,生成一个代理对象
 */
//@EnableAspectJAutoProxy
//@ImportResource("classpath:spring.xml")

//@MyScan
//@Import(MyImportBeanDefinitionRegistrar.class)
//@Import(AppConfig2.class)
//@MyEnableProxy
//@Import({ImportDao.class, MySelector.class, MyImportBeanDefinitionRegistrar.class, MyDefferedSelector.class})
//@Order
public class AppConfig {

//	@Bean
//	public static TestBeanStatic testBeanStatic(){//@Bean注解的static方法创建的对象会创建两次
//		return new TestBeanStatic();
//	}
//
//	@Bean
//	public TestBeanNotStatic testBeanNotStatic(){
//		testBeanStatic();
//		return new TestBeanNotStatic();
//	}

//	@Bean
//	public static BeanDao1 beanDao1(){
//		return new BeanDao1();
//	}

//	@Bean
//	public BeanDao1 beanDao1(){
//		return new BeanDao1();
//	}
//
//	@Bean
//	public BeanDao2 beanDao2(){
//		beanDao1();//如果不加@Configuration,BeanDao1的构造函数会执行两遍,加了只执行一遍
//		return new BeanDao2();
//	}

//	@Bean
//	public CglibFactoryBean cglibFactoryBean() {
//		return new CglibFactoryBean();
//	}



}
