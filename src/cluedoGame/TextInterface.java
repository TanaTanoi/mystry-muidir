package cluedoGame;

import java.awt.Point;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This interface completely deals with the user interaction, both input and output
 * It allows proper encapsulation when the GUI comes in.
 * Note: When testing, try to avoid using this class, and directly call methods in the CluedoGame if possible
 * @author Tana
 *
 */
public class TextInterface {

	private static final String MSG_PREFIX = ":>";		//goes before any output message
	private static final String INPUT_PREFIX = ">:";	//goes before requesting any input
	
	private static final String POINT_PATT ="\\d{1,2}(\\s*,\\s*|\\s*)\\d{1,2}";
	
	private static final int 	MAX_INPUT_BUFFER = 10;	//max amount of characters input-able through system.in
	/**
	 * Displays the given message and then requests an input (returned
	 * in the form of a string) from the user.
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
	 * Displays the given message then requests a point from the user.
	 * Similar to requestInput but sorts the formatting itself.
	 * @param message - The message to display when asking for the input
	 * @param errorMessage - The message to display if it is not a valid input
	 * @return
	 */
	public Point requestPoint(String message,String errorMessage){
		Scanner sc = null;
		while(true){//FIXME probably bad practice, but it works for this case
			System.out.print(MSG_PREFIX+message+"\n"+INPUT_PREFIX);
			try{
				byte[] input = new byte[MAX_INPUT_BUFFER];					//read an input from user
				int read = System.in.read(input, 0, MAX_INPUT_BUFFER);	
				if(read>0){													//if we have a valid input
					String s = new String(input).trim();	//scan it for two ints
					
					System.out.println(s + "|" + s.matches(POINT_PATT));
					sc = new Scanner(s);
					
					if(s.matches(POINT_PATT)){
						sc.useDelimiter("(\\s|,)");
						System.out.println("match");
						
						return new Point(sc.nextInt(),sc.nextInt());
					}
				}
			}
			
			catch(IOException e){
				print(errorMessage);
			}finally{
				sc.close();
			}
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
