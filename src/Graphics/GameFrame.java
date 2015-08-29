package Graphics;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

//import com.sun.xml.internal.ws.message.MimeAttachmentSet;


import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import cluedoGame.Control;
import cluedoPieces.Board;
import cluedoPieces.Player;


public class GameFrame extends JFrame implements MouseListener, MouseMotionListener{
	private static final int BOARD_TOP = 60;//FIXME 
	private static final int BOARD_LEFT= 15;
	private static final int MINIMUM_SIZE = 400;
	

	public Control control;
	public Point clickedP = null;
	public Point movedP = null;
	private JTextArea cardDisplay;
	GameCanvas canvas;
	Board board;
	PlayerWindow startingWindow;
	public GameFrame(Control control){
		super("Cluedo");
		this.control = control;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMenuBar();
		canvas = new GameCanvas(control);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addCardPanel();
		setBindings();
		pack();
		setResizable(true);
		setMinimumSize(new Dimension(MINIMUM_SIZE, MINIMUM_SIZE));
		setVisible(true);
	}
	
	
	/**
	 * Sets hotkeys for starting new game
	 */
	private void setBindings(){
		AbstractAction a = new AbstractAction("StartGame") {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}		
		};
		canvas.getInputMap().put(KeyStroke.getKeyStroke('N'), "Start");
		canvas.getInputMap().put(KeyStroke.getKeyStroke('n'), "Start");
		canvas.getActionMap().put("Start",a);
		
		cardDisplay.getInputMap().put(KeyStroke.getKeyStroke('N'), "Start");
		cardDisplay.getInputMap().put(KeyStroke.getKeyStroke('n'), "Start");
		cardDisplay.getActionMap().put("Start",a);
	}


	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		JMenuItem newGameItem = new JMenuItem(new AbstractAction("N|New Game") {
			public void actionPerformed(ActionEvent e){
				startNewGame();
			}
		});
		gameMenu.add(newGameItem);
		this.setJMenuBar(menuBar);
	}

	private void startNewGame(){
		startingWindow = new PlayerWindow(this);
	}

	
	/**
	 * adds text feild for card display
	 */
	private void addCardPanel(){	
		cardDisplay = new JTextArea();
		cardDisplay.setPreferredSize(new Dimension(200,getHeight()));
		cardDisplay.setEditable(false);
		cardDisplay.setLineWrap(true);
		this.add(cardDisplay, BorderLayout.EAST);
	}
	
	
	/**
	 * controls text in right hand text pane. Used to displayplayers card
	 * @param text Text to be displays
	 * @param font font the text will be in
	 */
	public void setCardDisplay(String text, Font font){
		cardDisplay.setFont(font);
		cardDisplay.setText(text);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//TODO write code for detecting what square the mouse is released on\
		Point p = findCoords(e.getX(),e.getY());
		clickedP = p;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		movedP = findCoords(e.getX(),e.getY());
	}


	/**
	 * takes a mouse position and finds the point on the board that the mouse is over
	 * @param x an x position on the frame
	 * @param y a y position on the frame
	 * @return the point on the board that was clicked
	 */
	private Point findCoords(int x, int y){
		double sqSize = canvas.getSquareSize();
		int xSquare = (int)((x-BOARD_LEFT)/sqSize);
		int ySquare = (int)((y-BOARD_TOP)/sqSize);
		return new Point(xSquare,ySquare);
	}



	/*
	 * UNUSED LISTENER METHODS
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}
	/*
	 * UNUSED LISTENER METHODS
	 */
}
