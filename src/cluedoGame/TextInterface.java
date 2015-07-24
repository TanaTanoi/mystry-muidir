package cluedoGame;

import java.io.*;

public class TextInterface {

	private static final String MSG_PREFIX = ":>";		//goes before any output message
	private static final String INPUT_PREFIX = ">:";	//goes before requesting any input
	
	private static final int 	MAX_INPUT_BUFFER = 10;	//max amount of characters input-able
	/**
	 * Displays the given message and then requests an input from the user
	 * @param message
	 * @return
	 */
	public String requestInput(String message){
		System.out.print(MSG_PREFIX+message+"\n"+INPUT_PREFIX);
		try{
			byte[] input = new byte[MAX_INPUT_BUFFER];
			int read = System.in.read(input, 0, MAX_INPUT_BUFFER);	
			if(read>0){
				return new String(input);
			}else{
				return "\n";
			}
		}catch(IOException e){
			return "\n";
		}
	}
	
	/**
	 * Prints the given message in the appropriate format
	 * @param message
	 */
	public void print(String message){
		System.out.println(MSG_PREFIX+message);
	}
	
	
}
