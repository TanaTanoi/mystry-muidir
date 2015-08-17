package cluedoGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cluedoPieces.*;
import cluedoPieces.Room.RoomName;
import cluedoPieces.Card.Person;
public class CluedoGame {
	private TextInterface ui;
	
	private Control control;
	
	
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
		//ui = new TextInterface();
		control = new Control();
		players = new ArrayList<Player>();
		int playerNum = 0;
		String input = "";
		// Get number of players
		playerNum = control.getTotalPlayers();
		remainingPlayers = playerNum;
		System.out.println(playerNum);
		//Create every player and assign their positions
		List<String> names = new ArrayList<String>();
		for (int i = 1; i <= playerNum; i++) {
			String name = control.requestPlayerName(i);
			names.add(name);
			Player newP = new Player(name);
			newP.setPos(board.getStartingPoint(i));
			// 1 to 6
			players.add(newP);
		}
		//Display players on GUI
		control.displayPlayers(names);
		//Initialize deck with players
		deck = new Deck(players);
		int i = 0;// redeclare i for use with this loop
		Player p = players.get(i);

		// Main loop
		do{
			//board.printBoard(players);
			//System.out.println(makeAccusation());
			p = players.get(++i);
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
	private boolean playerTurn2(Player p){
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
				}else if(input.equalsIgnoreCase("cards")){//prints player's cards
					StringBuilder sb = new StringBuilder();
					sb.append("Your cards:\n");
					for(Card c:p.getHand()){
						sb.append("|"+c.toString() +"\n");
					}
					ui.print(sb.toString());
				}
				else{									//if room name entered
					input = input.toUpperCase();
					try{
						Room.RoomName roomInput = Room.RoomName.valueOf(input);
						p.setRoom(roomInput);
						if (roomInput != RoomName.CELLAR){
							makeSuggestion(p.getCurrentRoom(),p);
							return false;
						}else{
							if (!makeAccusation()){
								p.makeInactive(); //Player made a false accusation, they have lost the game and are thus inactive
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

	private boolean playerTurn(Player p){
		//Display information
		
		control.displayPlayerInformation(p);
		
		//Roll die and calculate possible locations to move
		int playerRoll = rollDie();
		Set<Point> reachablePoints = new HashSet<Point>();
		Set<Room.RoomName> reachableRooms = new HashSet<Room.RoomName>();
		if(p.getCurrentRoom()==null){		//if player is not in a room, use pos
			reachablePoints	.addAll(board.reachablePoints(p.getPos(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getPos(), playerRoll));
		}else{								//if a player is in a room, use room
			reachablePoints	.addAll(board.reachablePoints(p.getCurrentRoom(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getCurrentRoom(), playerRoll));
		}
		//TODO not done yet
		return false;
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
	private void makeSuggestion(RoomName r, Player p) {
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
			if(p==player){continue;}
			Card refutedCard = player.answerSuggestion(weapon, room, ch);
			if(refutedCard!=null){
				ui.print(player.getName() + " has the " + refutedCard.toString() + " card!");
				break;
			}
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
				ui.print((char)(doorPoint.x+65) + ", " + (doorPoint.y+1));
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
					p.setRoom(null);
					return false;
				}else{ui.print("Can't reach that point!");}
			}else if(input.matches(CHAR_POINT)){	// if the user has entered a board coordinate (letter,number)
				String[] coords = input.split(",");
				assert coords.length == 2;
				int x = coords[0].trim().toUpperCase().charAt(0)-65; // convert the character value to integer
				Point newPos = new Point(Integer.parseInt(coords[1].trim())-1, x);
				if(validPoints.contains(newPos)){
					p.setPos(newPos);
					p.setRoom(null);
					return false;
				}else{ui.print("Can't reach that point!");}
			}
			else if(input.equalsIgnoreCase("map")){
				board.printBoard(players);
			}else if (input.equalsIgnoreCase("help")){
				printHelp();
			}else if(input.equalsIgnoreCase("cards")){//prints player's cards
				StringBuilder sb = new StringBuilder();
				sb.append("Your cards:\n");

				for(Card c:p.getHand()){
					sb.append("|"+c.toString() +"\n");
				}
				ui.print(sb.toString());
			}else{
				input = input.toUpperCase();
				for(Room.RoomName r: Room.RoomName.values()){
					if (r.toString().equals(input)){
						p.setRoom(r);
						if (r != RoomName.CELLAR){
							makeSuggestion(p.getCurrentRoom(),p);
							return false;
						}else{
							if (!makeAccusation()){
								p.makeInactive(); //Player made a false accusation, they have lost the game and are thus inactive
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
		ui.print("To see your cards, type 'CARDS'.");
		ui.print("To make an accusation, type accusation(in cellar only)");

	}

	/**
	 * Takes place of dice roll - generates random number from 1-6
	 * @return random number from 1 - 6
	 */
	public static int rollDie(){
		return (int)(Math.random()*6+1);
	}

}
