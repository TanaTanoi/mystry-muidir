package cluedoGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graphics.AccusationWindow;
import Graphics.GameFrame;
import Graphics.RefuteWindow;
import Graphics.SuggestionWindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import cluedoPieces.*;

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
	Board b;
	GameFrame frame;

	/*Various colors used for the map*/
	private static final Color SPAWN_COLOR = new Color(130,90,20);
	private static final Color CELLAR_COLOR = new Color(40,40,40);
	private static final Color DOOR_MAT_COLOR = new Color(215,175,105);
	private static final Color HALLWAY_COLOR = new Color(230,190,120);
	private static final Color ROOM_COLOR = new Color(212,197,200);
	private static final Color WALL_COLOR = new Color(130,100,100);
	/*Controls the color and color change of highlighted squares*/
	private static final  Color HIGHLIGHT_COLOR = new Color(230,230,80);
	private static int HIGHLIGHT_PHASE = -2;
	private static int HIGHLIGHT_OFFSET = 0;
	/*The colors of the grid, relative to the square it is on*/
	private static final int GRID_COLOR_OFFSET = -10;
	/*The scale of the names of the rooms */
	private static final double NAME_FONT_SCALE = 0.6;
	
	private static final Font ITALIC_SERIF= new Font("Serif",Font.ITALIC,12);
	private static final Font BOLD_SERIF= new Font(Font.SERIF,Font.BOLD,12);
	Map<String,Point> roomNames;

	private Set<Point> reachablePoints = new HashSet<Point>();
	private Set<String> reachableRoomNames = new HashSet<String>();
	private List<Player> players;// = new ArrayList<Player>();

	private int squareSize =10;
	private static final double ANIMATION_STEPS = 10;//amount of ticks to take when moving a player
	public void setPlayers(List<Player> players){
		this.players = players;
	}

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
	 * Asks the user for a suggestion. 
	 * This opens a new window that can only be closed once the player has clicked submit.
	 * @param p - Player who is suggesting
	 * @return - A Length 2 array where 0-Weapon, 1-Character cards
	 */
	public Card[] requestSuggestion(Player p){
		//new AccusationWindow(frame, p);
		SuggestionWindow window = new SuggestionWindow(frame,p);
		String weapon = "",character = "";
		while(window.isShowing()){//wait until its done to actually do something with the values
			weapon = window.getWeapon();
			character = window.getCharacter();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Card[] cards =  {new WeaponCard(weapon),new CharacterCard(character)};
		return cards;
	}
	
	/**
	 * Asks the player for an accusation.
	 * This opens a new window that can only be closed once the player has submitted 
	 * the accusation.
	 * @param p
	 * @return - A Length 3 array where 0-Room,1-Weapon,2-Character cards
	 */
	public Card[] requestAccusation(Player p){
		AccusationWindow window = new AccusationWindow(frame,p);
		String weapon = "",character = "",room="";
		while(window.isShowing()){//wait until its done to actually do something with the values
			weapon = window.getWeapon();
			character = window.getCharacter();
			room = window.getRoom();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Card[] cards =  {new RoomCard(room),new WeaponCard(weapon),new CharacterCard(character)};
		return cards;
	}
	
	
	/**
	 * Asks the user n (which corresponds to a character portrait)
	 * for their player name and returns the string.
	 * @param n
	 * @return
	 */
	public List<Player> requestPlayers(){
		while(players==null){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return players;
	}

	/**
	 * Displays the given player's information on screen,
	 * including their cards, name, dice roll, etc.
	 * This method is used preemptive of a player's actual turn
	 * @param p - The player who's information will be displayed
	 * @param diceRoll - The player's dice roll
	 */
	public void displayPlayerInformation(Player p){
		StringBuilder sb = new StringBuilder();
		sb.append("Your cards are; \n");
		for(Card c:p.getHand()){
			sb.append(c);
			sb.append("\n");
		}
		frame.setCardDisplay(sb.toString());
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
	public Point displayPlayerMove(Set<Point> reachablePoints, Set<Room.RoomName> reachableRooms,Player p){
		currentPlayer = p;
		this.reachablePoints = reachablePoints;
		for(Room.RoomName rn:reachableRooms){
			reachableRoomNames.add(rn.toString());
		}
		do{
			frame.clickedP = null;
			while(frame.clickedP==null){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while(!reachablePoints.contains(frame.clickedP)&&
				(b.getRoom(frame.clickedP)==null||
				!reachableRoomNames.contains(b.getRoom(frame.clickedP).toString()))
				);
		movePlayerAnimation(p, frame.clickedP);
		this.reachablePoints.clear();
		this.reachableRoomNames.clear();
		return frame.clickedP;
	}

	/**
	 * Displays the refuted card to the current viewer from the given player.
	 * TODO? maybe also wait for confirmation before returning?
	 * @param p
	 * @param c
	 */
	public void displayRefutedCard(Player p, Card c){
		StringBuilder sb = new StringBuilder();
		sb.append(p.getName()); 
		sb.append(" has refuted your suggestion with the card ");
		sb.append(c);
		RefuteWindow rw = new RefuteWindow(frame);
		
		rw.setText(sb.toString(), ITALIC_SERIF);
		//System.out.println(sb.toString());
		frame.setCardDisplay("CARD ");
	}	
	/**
	 * Displays the winning player
	 * @param p
	 */
	public void displayWinner(Player p){
		System.out.println("WINNER IS " + p.getName());
		RefuteWindow rw = new RefuteWindow(frame);
		rw.setText("The winner is " +p.getName() ,BOLD_SERIF );
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
		this.squareSize = frameSize/Board.boardSize;
		BufferedImage out = new BufferedImage(squareSize*Board.boardSize, squareSize*Board.boardSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g= (Graphics2D)out.getGraphics();
		g.setColor(new Color(180,10,10));
		g.fillRect(0,0, 1000, 1000); //Draws background of board

		drawWalls(g,frameSize);
		highlightSquares(g);
		g.setStroke(new BasicStroke(8));//change line thickness for drawing game boarders
		g.setColor(Color.black);
		g.drawLine(0, 0, frameSize, 0); //TOP
		g.drawLine(0, frameSize, frameSize, frameSize); //BOTTOM
		g.drawLine(0, 0, 0, frameSize); //LEFT
		g.drawLine(frameSize, 0, frameSize, frameSize); //RIGHT

		drawPlayers(g);
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,(int)(squareSize*NAME_FONT_SCALE)));
		for(String s:roomNames.keySet()){
			if(reachableRoomNames.contains(s)){
				g.setColor(HIGHLIGHT_COLOR.darker());
			}else{
				g.setColor(Color.black);
			}
			Point p = roomNames.get(s);
			g.drawString(formatString(s), (int) (p.x*squareSize-s.length()*3*NAME_FONT_SCALE), p.y*squareSize);
		}
		return out;
	}

	/**
	 * Draws the players stored in the players global list.
	 * @param g
	 * @param squareSize
	 */
	private void drawPlayers(Graphics2D g){
		if(players==null||players.isEmpty())return;
		g.setStroke(new BasicStroke(2));
			for(Player p: players){
				Color p_color = new Color(p.getPerson().toString().toLowerCase().hashCode());
				
				if(currentPlayer!=null&&currentPlayer.equals(p)){
					Color h_p_color = new Color(
							Math.min(255,Math.max(0, p_color.getRed()-HIGHLIGHT_OFFSET)),
							Math.min(255,Math.max(0,p_color.getGreen()-HIGHLIGHT_OFFSET)),
							Math.min(255,Math.max(0,p_color.getBlue()-HIGHLIGHT_OFFSET)));
					//Main circle
					g.setColor(h_p_color.brighter());
					g.fillOval((int)(p.fakeX*squareSize), (int)(p.fakeY*squareSize), squareSize , squareSize);
					g.setColor(Color.black);
					//Outline
					g.drawOval((int)(p.fakeX*squareSize), (int)(p.fakeY*squareSize), squareSize , squareSize);
				}else{
					g.setColor(p_color);
					g.drawOval((int)(p.fakeX*squareSize)+1, (int)(p.fakeY*squareSize)+1, squareSize-1 , squareSize-1);
				}
		}
	}
	/**
	 * Sets the players animation position (fakeX/Y) to a variable amount across
	 * the old position (p.getPos()) and newPos.
	 * @param p - Player who is being moved
	 * @param newPos - The position this player is traveling to
	 */
	private void movePlayerAnimation(Player p,Point newPos){
		p.fakeX = newPos.x;
		p.fakeY = newPos.y;
		double incX = (newPos.x-p.getPos().x)/ANIMATION_STEPS;
		double incY = (newPos.y-p.getPos().y)/ANIMATION_STEPS;
		for(int i = 0; i < 10;i++){
			p.fakeX=p.getPos().x+incX*i;
			p.fakeY=p.getPos().y+incY*i;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the squares in the reachableRooms to highlighted on the board
	 * Also changes the colour of the highlight every time it is called,
	 * (per tick in this case).
	 * @param g
	 * @param squareSize
	 */
	private void highlightSquares(Graphics2D g){
		Color H_FLASH =new Color(HIGHLIGHT_COLOR.getRed()+HIGHLIGHT_OFFSET,
				HIGHLIGHT_COLOR.getGreen()+HIGHLIGHT_OFFSET,
				HIGHLIGHT_COLOR.getBlue()+HIGHLIGHT_OFFSET);
		HIGHLIGHT_OFFSET+=HIGHLIGHT_PHASE;
		if(HIGHLIGHT_OFFSET>0){
			HIGHLIGHT_PHASE*=-1;
		}else if(HIGHLIGHT_OFFSET<-40){
			HIGHLIGHT_PHASE*=-1;
		}
		Set<Point> localPoints = new HashSet<Point>();//avoid concurrent modification of reachablePoints
		localPoints.addAll(reachablePoints);
		for(Point p:localPoints){
			if(p.equals(frame.movedP)){
				drawSquareGrid(p.x, p.y, H_FLASH.brighter().brighter(), g, squareSize);
			}else{
				drawSquareGrid(p.x, p.y, H_FLASH, g, squareSize);
			}
		}
	}



	/**
	 * Formats the given string to follow basic English
	 * convention.
	 * @param s
	 * @return
	 */
	private String formatString(String s){
		StringBuilder sb = new StringBuilder();
		sb.append(s.charAt(0));
		sb.append(s.substring(1, s.length()).toLowerCase());
		return sb.toString().replace("_", " ");
	}
}
