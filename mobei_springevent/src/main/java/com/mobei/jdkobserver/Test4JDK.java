package com.mobei.jdkobserver;



public class Test4JDK {
	public static void main(String[] args) {
		Movie movie = new Movie();
		movie.addObserver(new ManA());
		movie.addObserver(new ManB());

		movie.move();
	}
}
