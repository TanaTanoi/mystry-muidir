package cluedoGame;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import cluedoPieces.Board;
import cluedoPieces.Card;
import cluedoPieces.Room;
import cluedoPieces.Card.Person;
import cluedoPieces.Deck;
import cluedoPieces.Player;

public class CluedoGame {
	private TextInterface ui;
	private Board board;
	private Deck deck;
	private ArrayList<Player> players;

	private static final String POINT_PATT = "\\d{1,2}(\\s*,\\s*|\\s*)\\d{1,2}";

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
		System.out.println(playerNum);
		// Assign names to each player, give initial position, and add them
		for (int i = 1; i <= playerNum; i++) {
			input = "";// clear input buffer
			while (input.length() <= 1) {// needs more than 1 char
				input = ui.requestInput("Enter player " + (i) + "s name");
			}
			Player newP = new Player(input);
			newP.setPos(board.findPlayerStart(i + 1));// Sets players initial
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
		while (!playerTurn(p)) {
			p = players.get(i++);
			if (i >= players.size() - 1) {
				i = 0;
			}
		}
	}

	/**
	 * This method is the main game loop, that returns true when the game is
	 * over. Main sequence: -Ask player to roll die -Tell player options -Accept
	 * input -if movement, do movement --if movement lands in room, ask
	 * suggestion --ask next players to refute, do refute cycle -if in cellar
	 * and accusation chosen, --compare accusation to deck, and if true, return
	 * true, else mark player as out
	 * 
	 * @param p
	 * @return
	 */
	private boolean playerTurn(Player p){
		ui.print(p.getName() + "'s turn!");
		int roll = p.rollDie();					//idk if roll die needs to be on the player class
		ui.print("You rolled a " + roll + "!");
		if(p.getCurrentRoom()==null){			//if not currently in a room, they can only move towards a room
			Set<Point> validPoints = board.reachablePoints(p.getPos(),roll);
			Set<Room.RoomName> reachableRooms = board.reachableRooms(p.getPos(), roll);
			if(!reachableRooms.isEmpty()){
				int i = 1;
				ui.print("Reachable rooms:");
				for(Room.RoomName room: reachableRooms){
					//System.out.println(p2.toString());
					ui.print(i + ": " +room.toString() );
					i++;
				}
			}
			while(true){
				String input = ui.requestInput("Enter a location to go to, a room name, or a command");
				if(input.matches(POINT_PATT)){		//if the user entered a point
					String[] coords = input.split(",");
					assert coords.length == 2;
					System.out.println(input);
					Point newPos = new Point(Integer.parseInt(coords[0].trim()),Integer.parseInt(coords[1].trim()));
					if(validPoints.contains(newPos)){
						p.setPos(newPos);
						break;
					}else{
						ui.print("Can't reach that point! You are at " + p.getPos().toString() + validPoints.size());
					}
					
				}else if(input.equalsIgnoreCase("map")){
					board.printBoard(players);
				}//TODO deal with how the players enter rooms 
			}
		}
		return false;
	}
}
