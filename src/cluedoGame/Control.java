package cluedoGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graphics.GameFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import cluedoPieces.Board;
import cluedoPieces.Card;
import cluedoPieces.CharacterCard;
import cluedoPieces.Player;
import cluedoPieces.Room;
import cluedoPieces.RoomCard;
import cluedoPieces.WeaponCard;
import java.awt.Font;

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
	GameFrame frame;

	/*Various colors used for the map*/
	private static final Color SPAWN_COLOR = new Color(130,90,20);
	private static final Color CELLAR_COLOR = new Color(40,40,40);
	private static final Color DOOR_MAT_COLOR = new Color(215,175,105);
	private static final Color HALLWAY_COLOR = new Color(230,190,120);
	private static final Color ROOM_COLOR = new Color(212,197,200);
	private static final Color WALL_COLOR = new Color(130,100,100);
	private static final Color GRID_COLOR = new Color(100,10,10);
	/*The colors of the grid, relative to the square it is on*/
	private static final int GRID_COLOR_OFFSET = -10;
	private static final double NAME_FONT_SCALE = 0.6;
	Map<String,Point> roomNames;// = b.getRoomCenters();
	/**
	 * Passes in the board to allow the frame to construct a background image
	 * @param board
	 */
	public Control(Board board){
		b = board;
		roomNames = b.getRoomCenters();
		frame = new GameFrame(this);
	}

	/**
	 * Asks the user for the number of players and
	 * returns it to the cluedogame
	 * @return
	 */
	public int getTotalPlayers(){
		//TODO actual content
		return 1;
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
		System.out.println("Asking for move");
		int x,y;
		do{
			frame.clickedP = null;

			while(frame.clickedP==null){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			x = (int)frame.clickedP.getX();
			y = (int)frame.clickedP.getY();
			//if(x>0&&y>0&&x<Board.boardSize&&y<Board.boardSize&&)
		//}while((!(x<0)&&!(y<0)&&!(x>Board.boardSize)&&!(y>Board.boardSize))
			//	&&(b.getSquare(x, y)==Board.Square.OPEN)&&reachablePoints.contains(frame.clickedP));
				//(x <0||y<0||x>Board.boardSize||y>Board.boardSize)
				//||b.getSquare(x, y)!=Board.Square.OPEN||!reachablePoints.contains(frame.clickedP));
		}while(!reachablePoints.contains(frame.clickedP));
		System.out.println("Returning " +frame.clickedP.toString());
		return frame.clickedP;
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


	/**
	 * Draws the walls of the board, dynamically.
	 * @param g
	 * @param frameSize
	 */
	private void drawWalls(Graphics2D g, int frameSize) {

		int squareSize = frameSize/Board.boardSize;

		for (int x = 0; x < Board.boardSize; x++){
			for (int y = 0; y < Board.boardSize; y++){
				Board.Square current = b.getSquare(x, y);

				switch (current) {
				case NA: //Paint inaccessible square
					g.setColor(Color.black);
					g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					break;
				case OPEN://Paint hallway square
					drawSquareGrid(x,y,HALLWAY_COLOR,g,squareSize);
					break;
				case CELLAR:
					g.setColor(CELLAR_COLOR);//grey
					g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					break;
				default:
					String name = current.toString();
					if (name.contains("DOOR")){//draw door mat
						drawSquareGrid(x,y,DOOR_MAT_COLOR,g,squareSize);
					}else if (Character.isDigit(current.toString().charAt(2))){//spawn point
						g.setColor(SPAWN_COLOR);
						g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
					}else{

						g.setColor(ROOM_COLOR); //slightly darker than the hallway
						g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
						g.setStroke(new BasicStroke(4));
						g.setColor(WALL_COLOR);
						if(x == 0 || !b.getSquare(x-1, y).toString().contains(name)) g.drawLine(x*squareSize+1, y*squareSize, x*squareSize+1, (y+1)*squareSize);
						if(y == 0 || !b.getSquare(x, y-1).toString().contains(name)) g.drawLine(x*squareSize, y*squareSize+1, (x+1)*squareSize, y*squareSize+1);
						if(x == Board.boardSize-1 || !b.getSquare(x+1, y).toString().contains(name)) g.drawLine((x+1)*squareSize-1, y*squareSize, (x+1)*squareSize-1, (y+1)*squareSize);
						if(y == Board.boardSize-1  || !b.getSquare(x, y+1).toString().contains(name)) g.drawLine(x*squareSize, (y+1)*squareSize-1, (x+1)*squareSize, (y+1)*squareSize-1);
					}
					break;
				}
			}
		}
	}

	/**
	 * Draws a square that has an out line that is slightly darker than the given colour,
	 * for a grid effect.
	 * @param x
	 * @param y
	 * @param c
	 * @param g
	 * @param squareSize
	 */
	private void drawSquareGrid(int x, int y, Color c,Graphics2D g,int squareSize){
		g.setStroke(new BasicStroke(0.5f));
		g.setColor(c);
		g.fillRect(x*squareSize,y*squareSize,squareSize,squareSize);
		g.setColor(new Color(c.getRed()+GRID_COLOR_OFFSET,c.getGreen()+GRID_COLOR_OFFSET,c.getBlue()+GRID_COLOR_OFFSET));
		g.drawRect(x*squareSize,y*squareSize,squareSize,squareSize);
	}
	/**
	 * Creates the image of the board, and adds a border and saves it as an image
	 * @param frameSize
	 * @return
	 */
	public BufferedImage getBoardImage(int frameSize){
		int squareSize = frameSize/Board.boardSize;
		BufferedImage out = new BufferedImage(squareSize*Board.boardSize, squareSize*Board.boardSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g= (Graphics2D)out.getGraphics();
		g.setColor(new Color(180,10,10));
		g.fillRect(0,0, 1000, 1000); //Draws background of board

		drawWalls(g,frameSize);
		g.setStroke(new BasicStroke(1));
		g.setColor(GRID_COLOR);
		/*for (int i = 1; i < Board.boardSize-1; i++){ //Draws grid over top
			g.drawLine(i*squareSize, squareSize, i*squareSize, frameSize-squareSize);
			g.drawLine(squareSize,i*squareSize,frameSize-squareSize, i*squareSize);
		}*/
		g.setStroke(new BasicStroke(8));//change line thickness for drawing game boarders
		g.setColor(Color.black);
		g.drawLine(0, 0, frameSize, 0); //TOP
		g.drawLine(0, frameSize, frameSize, frameSize); //BOTTOM
		g.drawLine(0, 0, 0, frameSize); //LEFT
		g.drawLine(frameSize, 0, frameSize, frameSize); //RIGHT
		g.setColor(Color.black);
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,(int)(squareSize*NAME_FONT_SCALE)));
		for(String s:roomNames.keySet()){
			Point p = roomNames.get(s);
			g.drawString(formatString(s), (p.x*squareSize-s.length()*2), p.y*squareSize);
		}


		return out;
	}

	private String formatString(String s){
		StringBuilder sb = new StringBuilder();
		sb.append(s.charAt(0));
		sb.append(s.substring(1, s.length()).toLowerCase());
		return sb.toString().replace("_", " ");
	}
}
