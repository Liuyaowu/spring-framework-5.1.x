package com.mobei.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestConstructorBean {
	/****************构造函数测试*********************/
//	Class clazz;//spring是不会自动注入的
//	public TestConstructorBean(){}
//	public TestConstructorBean(String str,Integer integer){}
//	public TestConstructorBean(Integer integer){}
//	@ConstructorProperties("xx")
//	public TestConstructorBean(TestConstructParam testConstructParam){}
//	public TestConstructorBean(Class clazz){this.clazz = clazz;}


	/****************循环依赖测试*********************/
	@Autowired
	private TestConstructParam testConstructParam;


	public void query(){
//		System.out.println(this.clazz);
		System.out.println("TestConstructorBean query()");
	}
}
