package com.mobei.factorymethod;

public class OrderService {
	public Object query(){
		return new NormalBean();
	}
}
