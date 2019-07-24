package com.mobei.factorybean;

import com.mobei.test.TestCase;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyFactoryBean implements FactoryBean {
	private Class clazz;
	public MyFactoryBean(Class clazz) {
		this.clazz = clazz;
	}
	@Override
	public Object getObject() throws Exception {
		Object obj = Proxy.newProxyInstance(TestCase.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("proxy");
				return null;
			}
		});
		return obj;
	}
	@Override
	public Class<?> getObjectType() {
		return clazz;
	}
}
