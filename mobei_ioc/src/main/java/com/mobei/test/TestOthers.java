package com.mobei.test;

import com.mobei.dao.MyRegistrarDao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestOthers {
	public static void main(String[] args) {
		MyRegistrarDao dao = (MyRegistrarDao) Proxy.newProxyInstance(TestOthers.class.getClassLoader(), new Class[]{MyRegistrarDao.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
		});
//		System.out.println(dao instanceof );
//		TestInstance ti = new TestInstance();
//		System.out.println(ti instanceof MyRegistrarDao);
	}
}

class TestInstance implements MyRegistrarDao {

	@Override
	public void find() {
		System.out.println("implements");
	}
}