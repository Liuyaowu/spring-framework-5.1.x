package com.mobei.jdkobserver;

import java.util.Observable;

public class Movie extends Observable {
	public void move() {
		setChanged();
		notifyObservers();
	}
}
