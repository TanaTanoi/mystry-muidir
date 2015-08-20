package cluedoGame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Graphics.*;
import cluedoPieces.*;
import cluedoPieces.Room.RoomName;
import cluedoPieces.Card.Person;
public class CluedoGame {
	private Control control;
	//private GameFrame frame;
	
	private Board board;
	private Deck deck;
	private ArrayList<Player> players;
	private int remainingPlayers;

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
		control = new Control(board);
		//frame = new GameFrame(control);
		players = new ArrayList<Player>();
		int playerNum = 0;
		// Get number of players
		playerNum = control.getTotalPlayers();
		remainingPlayers = playerNum;
		System.out.println(playerNum);
		//Create every player and assign their positions
		for (int i = 1; i <= playerNum; i++) {
			String name = control.requestPlayerName(i);
			Player newP = new Player(name);
			newP.setPos(board.getStartingPoint(i));
			players.add(newP);
		}
		//Display players on GUI
		control.displayPlayers(players);
		//Initialize deck with players
		deck = new Deck(players);
		
		int i = 0;// redeclare i for use with this loop
		Player p = players.get(i);

		// Main loop
		do{
			//board.printBoard(players);
			//System.out.println(makeAccusation());
			p = players.get(i++);
			if (i == players.size()) {
				i = 0;
			}
		}while (!playerTurn(p) && remainingPlayers > 1 );
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
		if(p.getCurrentRoom()==null){		//if player is not in a room, use position
			reachablePoints	.addAll(board.reachablePoints(p.getPos(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getPos(), playerRoll));
		}else{								//if a player is in a room, use room
			reachablePoints	.addAll(board.reachablePoints(p.getCurrentRoom(), playerRoll));
			reachableRooms	.addAll(board.reachableRooms(p.getCurrentRoom(), playerRoll));
		}
		//Pass movements into the view and await a selection from the user
		Point playerSelection = control.displayPlayerMove(reachablePoints, reachableRooms);
		if(playerSelection!=null){//If the player made a point selection, move there
			RoomName room = board.getRoom(playerSelection);	
			p.setPos(playerSelection);
			p.setRoom(room);								//Room is null if they didn't select a room
		}
		//then make suggestion/accusation
		if(p.getCurrentRoom()==RoomName.CELLAR){
			return makeAccusation(p);
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
		WeaponCard weapon = control.requestWeaponCard();
		CharacterCard ch = control.requestCharacterCard();
		for(Player player: players){
			if(p==player){continue;}
			Card refutedCard = player.answerSuggestion(weapon,room,ch);
			if(refutedCard!=null){
				control.displayRefutedCard(player, refutedCard);
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
		//Form proposal
		RoomCard room= control.requestRoomCard();
		WeaponCard weapon = control.requestWeaponCard();
		CharacterCard ch = control.requestCharacterCard();
		Card[] accusation = {room,weapon,ch};
		if(deck.compareMurderCards(accusation)){	//if proposal is correct
			return true;
		}		
		//if they are wrong
		remainingPlayers--;
		p.setInactive();
		return false;
	}
	
	
	
	/**
	 * Takes place of dice roll - generates random number from 1-6
	 * @return random number from 1 - 6
	 */
	public static int rollDie(){
		return (int)(Math.random()*6+1);
	}

}
