package cluedoGame;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import cluedoPieces.Board;
import cluedoPieces.Card;
import cluedoPieces.CharacterCard;
import cluedoPieces.Player;
import cluedoPieces.Room;
import cluedoPieces.RoomCard;
import cluedoPieces.WeaponCard;

/**
 * This class is the bridge between the CluedoGame (model) and the GUI (view).
 * It decodes information from the GUI into information usable by the model, 
 * and makes requests that can be then displayed to the use via the GUI.
 * @author Tana
 *
 */
public class Control {

	Player currentPlayer;
	
	/**
	 * Passes in the board to allow the frame to construct a background image
	 * @param board
	 */
	public Control(Board board){
		
	}
	
	/**
	 * Asks the user for the number of players and 
	 * returns it to the cluedogame
	 * @return
	 */
	public int getTotalPlayers(){
		//TODO actual content
		return 0;
	}
	/**
	 * Asks the current player for a weapon card, for use with accusations and 
	 * suggestions.
	 * @return
	 */
	public WeaponCard requestWeaponCard(){
		
		return null;
	}
	
	/**
	 * Asks the current player for a character card, for use with accusations
	 * and suggestions.
	 * @return
	 */
	public CharacterCard requestCharacterCard(){
		
		return null;
	}
	
	/**
	 * Asks the current player for a room card, for use with accusations.
	 * @return
	 */
	public RoomCard requestRoomCard(){
		
		return null;
	}
	/**
	 * Asks the user n (which corresponds to a character portrait) 
	 * for their player name and returns the string.
	 * @param n
	 * @return
	 */
	public String requestPlayerName(int n){
		//TODO
		return "name";
	}
	
	/**
	 * Displays the names of the players along side the portrait 
	 * and then continues once finished.
	 * @param names
	 */
	public void displayPlayers(List<String> names){
		//TODO
		
	}
	/**
	 * Displays the given player's information on screen,
	 * including their cards, name, dice roll, etc.
	 * This method is used preemptive of a player's actual turn
	 * @param p - The player who's information will be displayed
	 * @param diceRoll - The player's dice roll
	 */
	public void displayPlayerInformation(Player p){
		//TOOD
	}
	
	/**
	 * Displays the new information which happens after displayPlayerInformation 
	 * has been run. This method shows the player what points and rooms they can
	 * reach, then awaits an input coordinate or if another option is selected, null,
	 *  which would then tell the game to run suggestion/accusation option.
	 *  The input coordinate includes room squares, so they can click anywhere in the room
	 *  in order to move to that room.
	 * @param reachablePoints
	 * @param reachableRooms
	 * @return - Point the player has selected to move to (including room points) or null for other options.
	 */
	public Point displayPlayerMove(Set<Point> reachablePoints, Set<Room.RoomName> reachableRooms){
		
		
		return null;
	}
	
	/**
	 * Displays the refuted card to the current viewer from the given player.
	 * TODO? maybe also wait for confirmation before returning?
	 * @param p
	 * @param c
	 */
	public void displayRefutedCard(Player p, Card c){
		
		//TODO
	}
	/**
	 * Displays the winning player
	 * @param p
	 */
	public void displayWinner(Player p){
		//TODO
	}
}
