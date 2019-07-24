package com.mobei.ph;

public class TestMan {
	public static void main(String[] args) {
		Movie movie = new Movie();
		movie.addListener(new ManA());
		movie.addListener(new ManB());

		Thread threadMovie = new Thread(movie);
		threadMovie.start();
	}
}
