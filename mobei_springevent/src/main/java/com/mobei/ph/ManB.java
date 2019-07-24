package com.mobei.ph;

public class ManB implements MovieListener {
	public void update(MovieEvent movieEvent){
		if(movieEvent.getType() == 1){
			System.out.println("smile");
		}
	}
}
