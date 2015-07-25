package cluedoPieces;

import java.io.*;
import java.util.*;

public class Board {
	
	private static final String LAYOUT_FILE = "bin/assets/layout.txt";
	private static final String LEGEND_FILE = "bin/assets/layout_legend.txt";
	private static final int boardSize = 26;
	Square[][] board;
	
	
	/**
	 * Represents every different kind of square in the game
	 *
	 */
	enum Square{
		NA,		//Inaccessible 
		OPEN,	//open area
		KITCHEN,
		KITCHEN_DOOR,
		BALLROOM,
		BALLROOM_DOOR,
		CONSERVATORY,
		CONSERVATORY_DOOR,
		DINING_ROOM,
		DINING_ROOM_DOOR,
		BILLIARD_ROOM,
		BILLIARD_ROOM_DOOR,
		CELLAR,
		LIBRARY,
		LIBRARY_DOOR,
		LOUNGE,
		LOUNGE_DOOR,
		HALL,
		HALL_DOOR,
		STUDY,
		STUDY_DOOR
	}
	
	//This enum set represents a door into a room, for ease of use. Use the roomsDoor
	//method to find the room that links to this door
	EnumSet<Square> doors = EnumSet.of(Square.KITCHEN_DOOR,Square.BALLROOM_DOOR,Square.CONSERVATORY_DOOR,
			Square.DINING_ROOM_DOOR,Square.BILLIARD_ROOM_DOOR,Square.LIBRARY_DOOR,Square.LOUNGE_DOOR,
			Square.HALL_DOOR,Square.STUDY_DOOR);
	
	
	
	//This map represents the layout.txt characters to an enum. This must be loaded before use, through loadLegend()
	Map<String,Square> layoutLegend = new HashMap<String,Square>();
	
	public Board(){
		
		board = new Square[boardSize][boardSize];
		this.loadLegend();
		this.loadLayout();
	}
	
	
	/**
	 * Loads the legend of characters to Square enums and places it into the layoutLegend map.
	 * The file it takes it from is the layout_legend.txt, as said above.
	 */
	private void loadLegend(){
		try{
			Scanner sc = new Scanner(new File(LEGEND_FILE));
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] lineSplit = line.split(":");
				assert lineSplit.length == 2;
				layoutLegend.put(lineSplit[1], Square.valueOf(lineSplit[0]));
			}
			sc.close();
		}catch(IOException e){
			throw new RuntimeException("Missing layout_legend.txt file!");//TODO find much better exception
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RuntimeException("layout_legend.txt may be courrupt!");//TODO as above
		}
		
	}
	
	/**
	 * Loads the layout into the board array, using the layout.txt file. Assumes that the loadLegend 
	 * method has been run, such that the legend is ready for use.
	 */
	private void loadLayout(){
		if(layoutLegend.isEmpty())
			throw new RuntimeException("Layout legend has not been loaded!");//TODO as above-above
		try{
			Scanner sc = new Scanner(new File(LAYOUT_FILE));
			sc.useDelimiter("");
			int i, j;//i and j are counters
			i = j = 0;
			while(sc.hasNext()){
				String input = sc.next();
				System.out.print(input);
				
				board[i][j] = layoutLegend.get(input);				
				if(i>=boardSize){
					j++;
					i = 0;
					System.out.println();
				}
				
			}
			sc.close();
		}catch(IOException e){
			throw new RuntimeException("Missing layout.txt file!");//TODO find better exception
			
		}
	}
	
	
	/**
	 * Finds the room associated with a particular door, returning the Square Enum associated with it.
	 * Assumes the parameter is within the 'doors' 'Square' enum subset
	 * @param door
	 * @return
	 */
	private Square roomsDoor(Square door){//FIXME stupid name, can't think of anything short
		if(!doors.contains(door)){throw new IllegalArgumentException("Must enter a door as a parameter!");}
		String squareName = door.toString().replaceFirst("_DOOR", "");
		return Square.valueOf(squareName);//TODO type check this, unless valueOf just errors
	}
}
