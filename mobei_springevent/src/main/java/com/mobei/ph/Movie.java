package com.mobei.ph;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Runnable {
	private List<MovieListener> movieListeners = new ArrayList<>();

	public void addListener(MovieListener listener){
		movieListeners.add(listener);
	}

	public void play(){
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MovieEvent movieEvent = new MovieEvent(this);
		movieEvent.setContext("only");
		movieEvent.setType(1);
		for (MovieListener movieListener : movieListeners) {
			movieListener.update(movieEvent);
		}
	}

	@Override
	public void run() {
		play();
	}
}
