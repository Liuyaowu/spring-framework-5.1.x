package com.mobei.ph;

public class MovieEvent {
	private String context;
	private int type;
	private Object source;//事件源:比如这里的movie
	public MovieEvent(Object source) {
		this.source = source;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
