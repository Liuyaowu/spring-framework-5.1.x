package com.mobei.springevent;

import org.springframework.context.ApplicationEvent;

public class SpringMailEvent extends ApplicationEvent {

	private String content;

	public SpringMailEvent(Object source) {
		super(source);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
