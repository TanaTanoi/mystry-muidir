package cluedoGame;

import java.io.IOException;
import java.util.ArrayList;

import cluedoPieces.Board;
import cluedoPieces.Card;
import cluedoPieces.Card.Person;
import cluedoPieces.Player;

public class CluedoGame {
	private TextInterface ui;
	private Board board;
	private ArrayList<Player> players;
	public static void main(String[] args) {
		new CluedoGame();
		
	}
	
	public CluedoGame(){
		startGame();
	}

	/**Method that sets up a new game, reloading all parts of the game
	 * -Rerolls murder cards
	 * -Asks for number of and names of players
	 * -Begins main game loop
	 */
	private void startGame(){
		board = new Board();
		ui = new TextInterface();
		players = new ArrayList<Player>();
		int playerNum = 0;
		String input ="";
		//Get number of players
		while(!input.matches("[2-6]")){//while number is not digit between 2 and 6
			input = ui.requestInput("How many players? Minimum 2, Maximum 6");
		}
		playerNum = Integer.parseInt(input);
		System.out.println(playerNum);
		//Assign names to each player, give initial position, and add them
		for(int i = 1; i <= playerNum;i++){
			input ="";//clear input buffer
			while(input.length()<=1){//needs more than 1 char
				input = ui.requestInput("Enter player " +(i) +"s name");
			}
			Player newP = new Player(input);
			newP.setPos(board.findPlayerStart(i+1));//Sets players initial positions. Plus one because it starts at 1 to 6
			players.add(newP);
		}
		ui.print("Tonight we have..");
		int i = 0;
		for(Person p:Card.Person.values()){//FIXME We may want to use proper names as opposed to the enums, maybe a hard coded array in the Player class?
			ui.print(players.get(i).getName() + " as..." + p.toString());
			i++;
			if(i>=playerNum){break;}//stop once we have met max players
		}
	}
	
}
