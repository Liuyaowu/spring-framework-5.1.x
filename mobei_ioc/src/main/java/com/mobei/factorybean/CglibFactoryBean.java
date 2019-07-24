package com.mobei.factorybean;

import com.mobei.dao.BeanDao2;
import org.springframework.beans.factory.FactoryBean;

public class CglibFactoryBean implements FactoryBean {
	@Override
	public Object getObject() throws Exception {
		return new BeanDao2();
	}

	@Override
	public Class<?> getObjectType() {
		return BeanDao2.class;
	}
}
