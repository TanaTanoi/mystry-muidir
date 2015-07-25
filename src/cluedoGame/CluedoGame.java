package cluedoGame;

import java.io.IOException;

public class CluedoGame {
	TextInterface ui;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CluedoGame();
		
	}
	
	public CluedoGame(){
		ui = new TextInterface();
		System.out.println(ui.requestInput("Test Input"));
		System.out.println(ui.requestPoint("Enter a point.","Not a valid point!"));
	}

}
