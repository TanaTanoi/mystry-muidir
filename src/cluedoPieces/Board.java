package cluedoPieces;

import java.awt.Point;
import java.io.*;
import java.util.*;

import cluedoPieces.Card.RoomType;
import cluedoPieces.Room.RoomName;
import javafx.geometry.Point3D;

public class Board {

	private static final String LAYOUT_FILE = "bin/assets/layout.txt";
	private static final String LEGEND_FILE = "bin/assets/layout_legend.txt";
	public static final int boardSize = 26;
	Square[][] board;
	private Point[] startingPoints;
	private HashMap<String,Set<Point>> doorMap;

	/**
	 * Represents every different kind of square in the game
	 *
	 */
	public static  enum Square{
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
		STUDY_DOOR,
		S_1,//Starting positions for each character
		S_2,
		S_3,
		S_4,
		S_5,
		S_6
	}

	//This enum set represents a door into a room, for ease of use. Use the roomsDoor
	//method to find the room that links to this door
	public static EnumSet<Square> doors = EnumSet.of(Square.KITCHEN_DOOR,Square.BALLROOM_DOOR,Square.CONSERVATORY_DOOR,
			Square.DINING_ROOM_DOOR,Square.BILLIARD_ROOM_DOOR,Square.LIBRARY_DOOR,Square.LOUNGE_DOOR,
			Square.HALL_DOOR,Square.STUDY_DOOR,Square.S_1,Square.S_2,Square.S_3,Square.S_4,Square.S_5,Square.S_6);


	//This map represents the layout.txt characters to an enum. This must be loaded before use, through loadLegend()
	Map<String,Square> layoutLegend = new HashMap<String,Square>();

	public Board(){

		board = new Square[boardSize][boardSize];
		this.loadLegend();
		this.loadLayout();
		this.mapDoors();
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
				//first part is enum second part is character
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
		startingPoints = new Point[6];
		if(layoutLegend.isEmpty())
			throw new RuntimeException("Layout legend has not been loaded!");//TODO as above-above
		try{
			Scanner sc = new Scanner(new File(LAYOUT_FILE));

			int x = 0;
			int y = 0;

			while(sc.hasNext()){
				String line = sc.nextLine();		//read line
				Scanner sc2 = new Scanner(line);	//analyse
				sc2.useDelimiter("");
				while(sc2.hasNext()){
					String input = sc2.next();
					//					System.out.print(input + "|");
					board[x][y] = layoutLegend.get(input);
					if (Character.isDigit(input.charAt(0))){
						int num = Integer.parseInt(input)-1;
						startingPoints[num] = new Point(x,y);
					}
					//	System.out.print("|"+i+" " +j+  " " + input+"|");
					x++;

				}
				x=0;
				y++;
				sc2.close();
			}
			sc.close();
		}catch(IOException e){
			throw new RuntimeException("Missing layout.txt file!");//TODO find better exception

		}
	}

	private void mapDoors(){
		doorMap = new HashMap<String,Set<Point>>();
		for(RoomType r: RoomType.values()){
			doorMap.put(r.toString(), new HashSet<Point>());
		}
		for (int x = 0; x < board.length; x++){
			for (int y = 0; y < board.length-1; y++){
				if(board[x][y].toString().contains("DOOR")){
					Point p = new Point(y, x);
					String title = board[x][y].toString();
					doorMap.get(title.substring(0, title.length()-5)).add(p);
				}
			}
		}
	}
	
	/**
	 * Gets the points associated with a given room string
	 * @param room
	 * @return
	 */
	public Set<Point> getDoors(String room){
		return doorMap.get(room);
	}
	/**
	 * This method finds all points that can be reached from a given position
	 * and returns the set. Assumes the source point is accessible, else throws error.
	 * @param s - The source point (e.g. where the player is)
	 * @param radius - Amount of steps away from the source that can be moved too
	 * @return - Set of accessible Point objects
	 */
	public Set<Point> reachablePoints(Point s,int radius){
		if(board[s.x][s.y] != Square.OPEN&&!doors.contains(board[s.x][s.y])){//if the source is not a valid square
			throw new IllegalArgumentException("Cannot find points from inaccessible source!: " + s.x + " " + s.y + " " + board[s.x][s.y]);
		}
		Map<Point,Integer> toReturn = new HashMap<Point,Integer>();
		reachableRec(toReturn,s.x,s.y,radius);
		return toReturn.keySet();
	}
	
	/**
	 * This method finds all reachable points from a given room ( as opposed to a given
	 * point) and returns the set of points.
	 * @param room  -Room to find reachable points to
	 * @param radius - Point search radius
	 * @return
	 */
	public Set<Point> reachablePoints(RoomName room, int radius){
		Map<Point, Integer> toReturn = new HashMap<Point,Integer>();
		for(Point p:getDoors(room.toString())){
			reachableRec(toReturn,p.x,p.y,radius);
		}
		return toReturn.keySet();
	}
	
	/**
	 * Recursive method used by reachablePoints method that adds all nearby, accessible points
	 * to the set until it runs out of steps or combinations.
	 * @param visited
	 * @param x
	 * @param y
	 * @param stepsLeft
	 */
	private void reachableRec(Map<Point,Integer> visited, int x, int y, int stepsLeft){
		if(stepsLeft<=0)return;												//base case #1, if no steps left
		stepsLeft--;
		if(x>=boardSize||x<0||y>=boardSize||y<0)return;						//case case #2, if out of bounds
		if(board[x][y] != Square.OPEN&&!doors.contains(board[x][y]))return;	//base case #3, if not valid square
		Point current = new Point(x,y);
		if(visited.containsKey(current)&&
				visited.get(current)>stepsLeft)return;						//base case #4, if current square has gone further than here

		visited.put(current,stepsLeft);										//even if its been visited, overwrite with longer path
		reachableRec(visited,x+1,y,stepsLeft);
		reachableRec(visited,x-1,y,stepsLeft);
		reachableRec(visited,x,y+1,stepsLeft);
		reachableRec(visited,x,y-1,stepsLeft);

	}

	/**
	 * Recursive method that finds the rooms accessible from a certain point. Throws exception
	 * if point is not a valid source point.
	 * @param s - Source point (e.g. where the player is)
	 * @param radius - Amount of steps the player can take
	 * @return - A set of RoomName enums that represent available rooms.
	 */
	public Set<Room.RoomName> reachableRooms(Point s, int radius){//Possibly may need to change the enum used here
		if(board[s.x][s.y] != Square.OPEN&&!doors.contains(board[s.x][s.y])){//if the source is not a valid square
			throw new IllegalArgumentException("Cannot find points from inaccessible source!");
		}
		Set<Room.RoomName> toReturn = new HashSet<Room.RoomName>();
		reachableRoomsRec(toReturn,new HashMap<Point,Integer>(),s.x,s.y,radius);//(The map is irrelevant past the execution of this method, so not saved)
		return toReturn;
	}
	/**
	 * Recursive method used for reachableRooms that adds to the set, the rooms reachable from the
	 * given point
	 * @param reachable - Reachable rooms set
	 * @param visited - Map of visited points to the amount of steps left at that point
	 * @param x
	 * @param y
	 * @param stepsLeft
	 */
	private void reachableRoomsRec(Set<Room.RoomName> reachable, Map<Point,Integer> visited, int x, int y, int stepsLeft){
		if(stepsLeft<=1)return;												//base case #1, if no steps left
		stepsLeft--;
		if(x>=boardSize||x<0||y>=boardSize||y<0)return;						//case case #2, if out of bounds
		if(board[x][y]==Square.CELLAR) reachable.add(Room.RoomName.CELLAR);
		if(board[x][y] != Square.OPEN&&!doors.contains(board[x][y]))return;	//base case #3, if not valid square
		Point current = new Point(x,y);
		if(visited.containsKey(current)&&
				visited.get(current)>stepsLeft)return;						//base case #4, if current square has gone further than here
		visited.put(current, stepsLeft);

		if(doors.contains(board[x][y]) && stepsLeft>=1){
			try{
				reachable.add(Room.RoomName.valueOf(this.roomsDoor(board[x][y]).toString()));//may need cleaning, might be better if use of other enum
			}catch(Exception e){}
		}

		reachableRoomsRec(reachable, visited,x+1,y,stepsLeft);
		reachableRoomsRec(reachable, visited,x-1,y,stepsLeft);
		reachableRoomsRec(reachable, visited,x,y+1,stepsLeft);
		reachableRoomsRec(reachable, visited,x,y-1,stepsLeft);
	}

	/**
	 * Method that finds the reachable rooms from a given room (as opposed to a given point)
	 * @param room - Source Room
	 * @param radius - Steps allowed (e.g. dice roll)
	 * @return - Set containing visit-able rooms
	 */
	public Set<Room.RoomName> reachableRooms(RoomName room, int radius){//Possibly may need to change the enum used here

		Square door = doorsRoom(Square.valueOf(room.toString()));	//Finds the associated door   TODO null/invalid check
		Set<Point> roomsDoors = findEnum(door);						//Find the points to do with that door
		Set<Room.RoomName> toReturn = new HashSet<Room.RoomName>();
		for(Point p:roomsDoors){
			System.out.println("Start: " + p.x + " " + p.y);
			toReturn.addAll(reachableRooms(p,radius));				//At all the door points, call the regular method
		}
		//SPECIAL CASES: Trap doors between corner rooms
		switch(room){
		case KITCHEN:
			toReturn.add(RoomName.STUDY);break;
		case STUDY:
			toReturn.add(RoomName.KITCHEN);break;
		case LOUNGE:
			toReturn.add(RoomName.CONSERVATORY);break;
		case CONSERVATORY:
			toReturn.add(RoomName.LOUNGE);break;
		default:
			break;
		}
		toReturn.remove(room);//Remove self to prevent confusion

		return toReturn;
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

	/**
	 * Finds the door associated with a particular room, returning the Square Enum associated with it.
	 * Assumes the parameter is within the 'doors' 'Square' enum subset
	 * @param door
	 * @return
	 */
	private Square doorsRoom(Square room){
		String squareName = room.toString()+"_DOOR";
		Square result = Square.valueOf(squareName);
		if(!doors.contains(result)){throw new IllegalArgumentException("Must enter a room as a parameter!");}
		return result; //TODO type check this, unless valueOf just errors
	}

	/**
	 * Finds all the points in the map that contain the specified Square enum
	 * @param target
	 * @return
	 */
	private Set<Point> findEnum(Square target){
		Set<Point> toReturn = new HashSet<Point>();
		for(int x = 0; x < boardSize;x++){
			for(int y = 0; y <boardSize;y++){
				if(board[x][y].equals(target)){
					toReturn.add(new Point(x,y));
				}
			}
		}
		return toReturn;
	}
	/**
	 * Finds the starting point of players based on their number (1 - 6)
	 *
	 * @param player - Index of player
	 * @return - Starting point for that player
	 */
	//	public Point findPlayerStart(int player){
	//
	//		if(player>6||player<1)throw new IllegalArgumentException("Only 1 to 6 players!");
	//
	//		Square toFind = Square.valueOf("S_"+player);
	//		for(int x = 0; x < boardSize;x++){
	//			for(int y = 0; y <boardSize;y++){
	//				if(board[x][y].equals(toFind)){
	//					return new Point(x,y);
	//				}
	//			}
	//		}
	//		throw new RuntimeException("Invalid layout, player " + player + " start point not found!");
	//	}

	/**
	 * TODO
	 * A method that prints the board and the players at given positions on the board.
	 * @param players
	 */
	public void printBoard(List<Player> players){
		char[][] out = new char[board.length][board.length-1];
		for (int x = 0; x < board.length; x++){
			for (int y = 0; y < board.length-1; y++){
				if (board[x][y].toString().equals("OPEN")){							//if open
					out[x][y] = '_';
				}
				else if (doors.contains(board[x][y])){								//if door
					out[x][y] = board[x][y].toString().toLowerCase().charAt(0);
				}else{																//if room or NA
					out[x][y] = board[x][y].toString().charAt(0);
				}
			}
		}
		int i = 1;
		for(Player p: players){
			if (p.getCurrentRoom() == null){
				out[p.getPos().x][p.getPos().y] = Character.forDigit(i, 10);
			}
			i++;
		}
		System.out.print("  |");
		for(int a = 65; a < 65 + board.length-1; a++){//loop that prints letters along the top of the board
			System.out.print((char)a+"|");
		}
		System.out.println("");
		for (int x = 0; x < board.length; x++){//board printing loop
			System.out.printf("%2d|",x+1);//prints numbers alond the bottom of the board
			for (int y = 0; y < board.length-1; y++){
				System.out.print(out[x][y]+"|");
			}
			System.out.println("");
		}
	}
	/**
	 * Returns the starting point of a given player, indicated by their number.
	 * Starts at 1, s.t. player 1 is 1.
	 * @param point
	 * @return
	 */
	public Point getStartingPoint(int point){
		return startingPoints[point-1];
	}
	
	public Square getSquare(int x, int y){
		return board[x][y];
	}
	/**
	 * Method to tell if a point is inside a room or not. Returns null if not
	 * on a room
	 * @param p
	 * @return - Room at this point, null if not a room
	 */
	public Room.RoomName getRoom(Point p){
		if(p.x>boardSize||p.y>boardSize||p.x<0||p.y<0)throw new IllegalArgumentException("Point must be within board area");
		Square tile = board[p.x][p.y];
		if(!(doors.contains(tile)||tile==Square.OPEN)){	//if room
			return Room.RoomName.valueOf(board[p.x][p.y].toString());
		}else{											//if not room	
			return null;
		}
	}
	/**
	 * This method gets points at the center of the room for use
	 * with dynamically drawing the names of the rooms on the board.
	 * @return
	 */
	public Map<String, Point> getRoomCenters(){
		/*Collect the data*/
		Map<String,Point3D> points= new HashMap<String, Point3D>();
		for(int i = 0;i<boardSize;i++){
			for(int j = 0;j<boardSize;j++){
				String name = board[i][j].toString();
				try{
					Room.RoomName.valueOf(name);//if it is not a room, it will throw exception
					if(!points.containsKey(name)){				//if not in the map, add with value of one
						points.put(name, new Point3D(i,j,1));
					}else{										//else, add to it and increment count
						Point3D temp = points.get(name);
						points.put(name, new Point3D(temp.getX()+i,temp.getY()+j,temp.getZ()+1));
					}
				
				}catch(IllegalArgumentException e){
					//skip this name if its bad
				}
			}
		}
		/*Average the data*/
		Map<String, Point> toReturn = new HashMap<String,Point>();
		for(String s:points.keySet()){
			Point3D temp = points.get(s);
			//Average the point and add it to the set
			
			double count = temp.getZ();
			int x = (int)(temp.getX()/count);
			int y = (int)(temp.getY()/count);
			System.out.println("Count " + count  + "  TEMP X|Y " + temp.getX() + "|"+temp.getY());
			System.out.println(x + " " +y);
			toReturn.put(s, new Point(x,y));
		}
	return toReturn;	
	}

}
