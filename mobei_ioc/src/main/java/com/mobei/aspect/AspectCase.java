package com.mobei.aspect;

import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//@Component
//@Aspect
public class AspectCase {
	@Pointcut("execution(* com.mobei.dao..*.*(..))")
	public void pointCut(){

	}
	@Before("pointCut()")
	public void beforeAdvice(){
		System.out.println("!!!前置通知!!!");
	}

}
