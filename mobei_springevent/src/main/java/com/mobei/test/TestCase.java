package com.mobei.test;

import com.mobei.app.AppConfig;
import com.mobei.springevent.MailBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestCase {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac =
				new AnnotationConfigApplicationContext(AppConfig.class);
		ac.getBean(MailBean.class).sendMail();
//		ac.start();
	}
}
