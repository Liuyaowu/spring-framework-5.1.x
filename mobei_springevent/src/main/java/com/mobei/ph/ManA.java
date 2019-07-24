package com.mobei.ph;

public class ManA implements MovieListener {
	public void update(MovieEvent movieEvent){
		if(movieEvent.getType() == 1){
			System.out.println("cry");
		}
	}
}
