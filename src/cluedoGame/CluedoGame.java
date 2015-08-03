package cluedoGame;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cluedoPieces.*;
import cluedoPieces.Room.RoomName;
import cluedoPieces.Card.Person;
public class CluedoGame {
	private TextInterface ui;
	private Board board;
	private Deck deck;
	private ArrayList<Player> players;
	private int remainingPlayers;
	
	private static final String POINT_PATT = "\\d{1,2}(\\s*\\,\\s*)\\d{1,2}";
	private static final String CHAR_POINT = "[a-xA-X](\\s*\\,\\s*)\\d{1,2}";

	public static void main(String[] args) {
		new CluedoGame();

	}

	public CluedoGame() {
		startGame();
	}

	/**
	 * Method that sets up a new game, reloading all parts of the game -Rerolls
	 * murder cards -Asks for number of and names of players -Begins main game
	 * loop
	 */
	private void startGame() {
		board = new Board();
		ui = new TextInterface();
		players = new ArrayList<Player>();
		int playerNum = 0;
		String input = "";
		// Get number of players
		while (!input.matches("[2-6]")) {// while number is not digit between 2
			// and 6
			input = ui.requestInput("How many players? Minimum 2, Maximum 6");
		}
		playerNum = Integer.parseInt(input);
		remainingPlayers = playerNum;
		System.out.println(playerNum);
		// Assign names to each player, give initial position, and add them
		for (int i = 1; i <= playerNum; i++) {
			input = "";						// clear input buffer
			while (input.length() <= 1) {	// needs more than 1 char
				input = ui.requestInput("Enter player " + (i) + "s name");
			}
			Player newP = new Player(input);
			newP.setPos(board.getStartingPoint(i));
			// positions. Plus one
			// because it starts at
			// 1 to 6
			players.add(newP);
		}
		ui.print("Tonight we have..");
		int i = 0;
		for (Person p : Card.Person.values()) {// FIXME We may want to use
			// proper names as opposed to
			// the enums, maybe a hard coded
			// array in the Player class?
			ui.print(players.get(i).getName() + " as..." + p.toString());
			i++;
			if (i >= playerNum) {
				break;
			}// stop once we have met max players
		}

		deck = new Deck(players);
		i = 0;// redeclare i for use with this loop
		Player p = players.get(i);

		// Main loop
		do{
			board.printBoard(players);
			//System.out.println(makeAccusation());
			p = players.get(i++);
			if (i == players.size()) {
				i = 0;
			}
		}while (!playerTurn(p) && remainingPlayers != 0);
	}

	/**
	 * This method is the main game loop, that returns true when the game is
	 * over. Main sequence: -Ask player to roll die -Tell player options -Accept
	 * input -if movement, do movement --if movement lands in room, ask
	 * suggestion --ask next players to refute, do refute cycle -if in cellar
	 * and accusation chosen, --compare accusation to deck, and if true, return
	 * true, else mark player as out
	 * 
	 * @param p -Players turn
	 * @return - False if game over
	 */
	private boolean playerTurn(Player p){
		if (!p.isActive()) return false;
		ui.print(p.getName() + "'s turn!");
		int roll = rollDie();					
		ui.print("You rolled a " + roll + "!");
		if(p.getCurrentRoom()==null){	//if not currently in a room, they can only move towards a room
			Set<Room.RoomName> reachableRooms = board.reachableRooms(p.getPos(), roll+1);

			if(!reachableRooms.isEmpty()){// if we can reach a room, display them
				int i = 1;
				ui.print("Reachable rooms:");
				for(Room.RoomName room: reachableRooms){
					ui.print(i + ": " +room.toString() );
					i++;
				}
			}
			Set<Point> reachablePoints = board.reachablePoints(p.getPos(), roll+1);
			while(true){
				ui.print("You are at " + (char)((p.getPos().y+1)+64) + ", " + (p.getPos().x+1));
				String input = ui.requestInput("Enter a location to go to, a room name, a command, or help");
				
				if(input.matches(POINT_PATT)){			//if the user entered a traditional coordinate (number,number)
					String[] coords = input.split(",");
					assert coords.length == 2; 			// check that a proper coordinate has been entered
					Point inputPoint = new Point(Integer.parseInt(coords[1].trim())-1, Integer.parseInt(coords[0].trim())-1);//-1 for board coords
					if (reachablePoints.contains(inputPoint)){
						p.setPos(inputPoint);
						return false;}			// if it was a valid coordinate to move to, end turn
				}else if(input.matches(CHAR_POINT)){	// if the user has entered a board coordinate (letter,number)
					String[] coords = input.split(",");
					assert coords.length == 2;
					int x = coords[0].trim().toUpperCase().charAt(0)-65; // convert the character value to integer
					Point inputPoint = new Point(Integer.parseInt(coords[1].trim())-1,x);
					if (reachablePoints.contains(inputPoint)){ 
						p.setPos(inputPoint);
						return false; }			// if it was a valid coordinate to move to, end turn
				}else if(input.equalsIgnoreCase("map")){				
					board.printBoard(players);
				}else if (input.equalsIgnoreCase("help")){
					printHelp();
				}
				else{									//if room name entered
					input = input.toUpperCase();
					try{
					Room.RoomName roomInput = Room.RoomName.valueOf(input);
					p.setRoom(roomInput);
					if (roomInput != RoomName.CELLAR){
						makeSuggestion(p.getCurrentRoom());
						return false;
					}else{
						if (!makeAccusation()){
							p.setActive(false); //Player made a false accusation, they have lost the game and are thus inactive
							remainingPlayers--; //Reduces remaining players
							ui.print(p.getName()+ " answered incorrectly, and have been removed from the game.");
							return false;
						}
						ui.print(p.getName()+ " found the correct answer, they win!");
						return true;
					}
					}catch(IllegalArgumentException e){
						//If the input is not even a room, then it is junk input
						ui.print(input + " is not a valid input.");
					}
					
				}
			}
		} else{	//if they ARE in a room, they can move from this room
			return moveFromRoom(p, roll);
		}
	}
	

	
	/**
	 * 
	 * @return true if the accusation is correct (meaning the player will win the game)
	 * or false if incorrect (player loses game and is eliminated
	 */
	private boolean makeAccusation() {
		Card[] cards = new Card[3];
		
		//loops to load possible answers for user input		
		String rooms = "Choose a room. Options are: \n";
		for(RoomName rn: RoomName.values()){
			if(rn != RoomName.CELLAR)rooms+=rn.toString()+ " ";
		}
		String weapons = "Choose a weapon. Options are: \n";
		for(Card.WeaponType w: Card.WeaponType.values()){
			weapons+=w.toString()+ " ";
		}
		String people = "Choose a person. Options are: \n";
		for(Person c: Person.values()){
			people+=c.toString()+ " ";
		}
		
		do{//User inputs room for room card
			cards[0] = new RoomCard(ui.requestInput(rooms));
		} while (cards[0].toString() == null);
		do{//User inputs weapon for weapon card
			cards[1] = new WeaponCard(ui.requestInput(weapons));
		}while (cards[1].toString() == null);
		do {//User inputs character for character card
			cards[2] = new CharacterCard(ui.requestInput(people));
		} while (cards[2].toString() == null);
		
		return deck.compareMurderCards(cards);
	}

	/**
	 * Used when a player enters a room. allows a player to suggest a weapon and a 
	 * person that may have commited the murder. The method then checks a players 
	 * cards and states if a suggestion is true.
	 * @param r Room the player just entered
	 */
	private void makeSuggestion(RoomName r) {
		RoomCard room = new RoomCard(Card.RoomType.valueOf(r.toString()));
		WeaponCard weapon;
		CharacterCard ch;
		ui.print("Make a suggestion for the "+ r.toString());
		String weapons = "Choose a weapon. Options are: \n";
		for(Card.WeaponType w: Card.WeaponType.values()){
			weapons+=w.toString()+ " ";
		}
		String people = "Choose a person. Options are: \n";
		for(Person w: Person.values()){
			people+=w.toString()+ " ";
		}
		do{
			weapon = new WeaponCard(ui.requestInput(weapons));
		}while (weapon.toString() == null);
		do {
			ch = new CharacterCard(ui.requestInput(people));
		} while (ch.toString() == null);
		for(Player player: players){
			player.answerSuggestion(weapon, room, ch);
		}
	}

	
	/**
	 * Allows a play to move out of their current room. Differs from normal movement as the possible starting points for movement are 
	 * the doors of the current room.
	 * 
	 * @param p The player whose turn it is currently
	 * @param roll the value of the players dice roll
	 * @return true if they enter the cellar and make a correct accusation and win the game, or false if they end their turn
	 * or lose the game
	 */
	private boolean moveFromRoom(Player p, int roll){
		while(true){
			ui.print("You are in the " + p.getCurrentRoom());
			Set<Point> doorPos = board.getDoors(p.getCurrentRoom().toString());//find all of the exit points of the current room
			Set<Point> validPoints = new HashSet<Point>();
			Set<Room.RoomName> reachableRooms = new HashSet<Room.RoomName>();
			ui.print("Exit Points:");//prints to points of the doors(all possible starting points to move from)
			for(Point doorPoint: doorPos){
				validPoints.addAll(board.reachablePoints(doorPoint,roll+1));
				reachableRooms.addAll(board.reachableRooms(doorPoint, roll+1));
				ui.print((doorPoint.x+1) + ", " + (doorPoint.y+1));
			}
			if(!reachableRooms.isEmpty()){
				int i = 1;
				ui.print("Reachable rooms:");
				for(Room.RoomName room: reachableRooms){
					ui.print(i + ": " +room.toString() );
					i++;
				}
			}
			String input = ui.requestInput("Enter a location to go to, a room name, a command, or help");
			
			if(input.matches(POINT_PATT)){		//if the user entered a traditional coordinate (number,number)
				String[] coords = input.split(",");
				assert coords.length == 2; // check that a proper coordinate has been entered
				Point newPos = new Point(Integer.parseInt(coords[1].trim())-1, Integer.parseInt(coords[0].trim())-1);
				if(validPoints.contains(newPos)){
					p.setPos(newPos);
					return false;
				}else{ui.print("Can't reach that point!");}
			}else if(input.matches(CHAR_POINT)){	// if the user has entered a board coordinate (letter,number)
				String[] coords = input.split(",");
				assert coords.length == 2;
				int x = coords[0].trim().toUpperCase().charAt(0)-65; // convert the character value to integer
				Point newPos = new Point(Integer.parseInt(coords[1].trim())-1, x);
				if(validPoints.contains(newPos)){
					p.setPos(newPos);
					return false;
				}else{ui.print("Can't reach that point!");}
			}
			else if(input.equalsIgnoreCase("map")){
				board.printBoard(players);
			}else if (input.equalsIgnoreCase("help")){
				printHelp();
			}
			else{
				input = input.toUpperCase();
				for(Room.RoomName r: Room.RoomName.values()){
					if (r.toString().equals(input)){
						p.setRoom(r);
						if (r != RoomName.CELLAR){
							makeSuggestion(p.getCurrentRoom());
							return false;
						}else{
							if (!makeAccusation()){
								p.setActive(false); //Player made a false accusation, they have lost the game and are thus inactive
								remainingPlayers--; //Reduces remaining players
								ui.print(p.getName()+ " answered incorrectly, and have been removed from the game.");
								return false;
							}
							ui.print(p.getName()+ " found the correct answer, they win!");
							return true;
						}
					}
				}
			}
		}
	}

	/**
	 * prints a simple help method
	 */
	private void printHelp(){
		ui.print("Enter a coordinate you wish to move to in the form of x, y");
		ui.print("To enter a reachable room, enter the name of that room");
		ui.print("To make an accusation, type accusation(in cellar only)");
	}

	/**
	 * Takes place of dice roll - generates random number from 1-6
	 * @return random number from 1 - 6
	 */
	public int rollDie(){
		return (int)(Math.random()*6+1);
	}

}
