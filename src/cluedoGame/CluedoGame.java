package cluedoGame;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cluedoPieces.*;
import cluedoPieces.Room.RoomName;
public class CluedoGame {
	private Control control;
	//private GameFrame frame;

	private Board board;
	private Deck deck;
	private List<Player> players;
	private int remainingPlayers;
	private static String default_layout = "bin/assets/layout.txt";
	public static void main(String[] args) {
		if(args.length==1){
			default_layout = args[0];
		}
		new CluedoGame();

	}

	public CluedoGame() {
		board = new Board(default_layout);
		control = new Control(board);
		startGame();

	}

	/**
	 * Method that sets up a new game, reloading all parts of the game -Rerolls
	 * murder cards -Asks for number of and names of players -Begins main game
	 * loop
	 */
	private void startGame() {
		players = control.requestPlayers();
		remainingPlayers= players.size();
		for (int i = 0; i < remainingPlayers; i++) {
			players.get(i).setPos(board.getStartingPoint(i+1));

		}
		//Initialize deck with players
		deck = new Deck(players);

		int i = 0;// redeclare i for use with this loop
		Player p = players.get(i);

		// Main loop
		do{
			p = players.get(i++);
			if (i == players.size()) {
				i = 0;
			}
		}while (playerTurn(p) && remainingPlayers > 1 );
		control.displayWinner(p);
	}

	/**
	 * The main game loop is this method run repeatedly. Once this returns true, the game is over.
	 * @param p
	 * @return - True if the player is correct and the game is over. False if player is wrong or no accusation made
	 */
	private boolean playerTurn(Player p){
		//Display information
		control.displayPlayerInformation(p);
		//Roll die and calculate possible locations to move
		int playerRoll = rollDie();
		Set<Point> reachablePoints = new HashSet<Point>();
		Set<Room.RoomName> reachableRooms = new HashSet<Room.RoomName>();
		if(p.getCurrentRoom()==null){		//if player is not in a room, use position method
			reachablePoints	.addAll(board.reachablePoints(p.getPos(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getPos(), playerRoll));
		}else{								//if a player is in a room, use room method
			reachablePoints	.addAll(board.reachablePoints(p.getCurrentRoom(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getCurrentRoom(), playerRoll));
		}
		//Pass movements into the view and await a selection from the user
		Point playerSelection = control.displayPlayerMove(reachablePoints, reachableRooms,p);
		RoomName room = board.getRoom(playerSelection);
		p.setPos(playerSelection);
		p.setRoom(room);								//Room is null if they didn't select a room
		
		//then make suggestion/accusation
		if(p.getCurrentRoom()==RoomName.CELLAR){
			return !makeAccusation(p);
		}else if(p.getCurrentRoom()!=null){					//make suggestion
			makeSuggestion(p);
			return true;
		}
		return true;
	}

	/**
	 * Makes a suggestion using the current player.
	 * @param p
	 */
	private void makeSuggestion(Player p){
		RoomCard room= new RoomCard(p.getCurrentRoom());
		Card[] WandC = control.requestSuggestion(p);//TODO get return
		if(WandC.length!=2){throw new IllegalArgumentException("Must have two cards, weapon and character!");}
		for(Player otherPlayer: players){
			if(p==otherPlayer){continue;}//skip primary player
			Card refutedCard = otherPlayer.answerSuggestion(room,(WeaponCard)WandC[0],(CharacterCard)WandC[1]);
			if(refutedCard!=null){
				control.displayRefutedCard(otherPlayer, refutedCard);
				break;
			}
		}

	}

	/**
	 * Makes an accusation using the current player.
	 * @param p
	 * @return - If accusation is correct or not
	 */
	private boolean makeAccusation(Player p){
		Card[] cards = control.requestAccusation(p);
		if(cards.length!=3){throw new IllegalArgumentException("Must have three cards,room, weapon and character!");}
		return deck.compareMurderCards(cards);
	}



	/**
	 * Takes place of dice roll - generates random number from 1-6
	 * @return random number from 1 - 6
	 */
	public static int rollDie(){
		return (int)(Math.random()*6+1);
	}

}
