package cluedoGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	List<Player> players;
	Board b;
	
	private static final Color SPAWN_COLOR = new Color(0,255,0);
	private static final Color DOOR_MAT_COLOR = new Color(225,178,70);
	private static final Color CELLAR_COLOR = new Color(40,40,40);
	private static final Color HALLWAY_COLOR = new Color(255,208,100);
	/**
	 * Passes in the board to allow the frame to construct a background image
	 * @param board
	 */
	public Control(Board board){
		b = board;
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
		return "dave";
	}
	
	/**
	 * Displays the names of the players along side the portrait 
	 * and then continues once finished.
	 * @param names
	 */
	public void displayPlayers(List<Player> players){
		//TODO
		this.players = players;
		
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
	
	

	private void drawWalls(Graphics2D g, int frameSize) {

		int squareSize = frameSize/Board.boardSize;
		g.setStroke(new BasicStroke(4));
		for (int x = 0; x < Board.boardSize; x++){
			for (int y = 0; y < Board.boardSize; y++){
				Board.Square current = b.getSquare(x, y);
				switch (current) {
				case NA: //Paint inaccessible square
					g.setColor(Color.black);
					g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					break;
				case OPEN://Paint hallway square
					g.setColor(HALLWAY_COLOR);//orangeish
					g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					break;
				case CELLAR:
					g.setColor(CELLAR_COLOR);//grey
					g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					break;
				default:
					String name = current.toString();
					if (name.contains("DOOR")){//draw doormat
						g.setColor(DOOR_MAT_COLOR); //slightly darker than the hallway
						g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					}else if (Character.isDigit(current.toString().charAt(2))){//spawn point
						g.setColor(SPAWN_COLOR);
						g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					}else{
						g.setColor(Color.black);
						if(x == 0 || !b.getSquare(x-1, y).toString().contains(name)) g.drawLine(x*squareSize, y*squareSize, x*squareSize, (y+1)*squareSize);
						if(y == 0 || !b.getSquare(x, y-1).toString().contains(name)) g.drawLine(x*squareSize, y*squareSize, (x+1)*squareSize, y*squareSize);
						if(x == Board.boardSize-1 || !b.getSquare(x+1, y).toString().contains(name)) g.drawLine((x+1)*squareSize, y*squareSize, (x+1)*squareSize, (y+1)*squareSize);
						if(y == Board.boardSize-1  || !b.getSquare(x, y+1).toString().contains(name)) g.drawLine(x*squareSize, (y+1)*squareSize, (x+1)*squareSize, (y+1)*squareSize);
					}
					break;
				}
			}
		}
	}

	public BufferedImage getBoardImage(int frameSize){
		int squareSize = frameSize/Board.boardSize;
		BufferedImage out = new BufferedImage(squareSize*Board.boardSize, squareSize*Board.boardSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g= (Graphics2D)out.getGraphics();
		g.setColor(new Color(180,10,10));
		g.fillRect(0,0, 1000, 1000); //Draws background of board
		
		drawWalls(g,frameSize);
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.BLACK);
		for (int i = 0; i < Board.boardSize; i++){ //Draws grid over top
			g.drawLine(i*squareSize, 0, i*squareSize, frameSize);
			g.drawLine(0,i*squareSize,frameSize, i*squareSize);
		}
		g.setStroke(new BasicStroke(8));//change line thickness for drawing game boarders
		g.drawLine(0, 0, frameSize, 0); //TOP
		g.drawLine(0, frameSize, frameSize, frameSize); //BOTTOM
		g.drawLine(0, 0, 0, frameSize); //LEFT
		g.drawLine(frameSize, 0, frameSize, frameSize); //RIGHT
		return out;
	}
}
