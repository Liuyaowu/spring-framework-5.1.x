package com.mobei.jdkobserver;

import java.util.Observable;
import java.util.Observer;

public class ManA implements Observer {
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("ManA----- ");
	}
}
