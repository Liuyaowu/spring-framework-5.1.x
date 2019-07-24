package com.mobei.dao;

import javax.annotation.PostConstruct;

//@Repository
public class IndexDao implements Dao {
	public IndexDao() {
		System.out.println("构造函数");
	}
	@PostConstruct
	public void init(){
		System.out.println("init method");
	}
	public void query(){
		System.out.println("dao query");
	}
}
