package Graphics;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cluedoGame.Control;
import cluedoPieces.Board;


public class GameFrame extends JFrame implements MouseListener, MouseMotionListener{
	private static final int BOARD_TOP = 61;
	private static final int BOARD_LEFT= 15;
	GameCanvas canvas;
	Board board;
	public GameFrame(Control control){
		super("Cluedo");
		addMouseListener(this);
		addMouseMotionListener(this);
		addMenuBar();
		canvas = new GameCanvas(control);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(true);
		setVisible(true);
		addButtons();
	}	
	
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		JMenuItem newGameItem = new JMenuItem("N|New Game");
		gameMenu.add(newGameItem);
		this.setJMenuBar(menuBar);
	}
	
	private void addButtons(){
		JPanel buttonPanel = new JPanel();
		JButton b= new JButton("Button");
		buttonPanel.add(b);
		this.add(buttonPanel, BorderLayout.EAST);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//TODO write code for detecting what square the mouse is released on
		Point p = findCoords(e.getX(),e.getY());
		System.out.println("X = "+p.x+" Y= "+p.y);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		//TODO write code for detecting what is being hovered over
	}
	
	
	/**
	 * takes a mouse position and finds the point on the board that the mouse is over
	 * @param x an x position on the frame
	 * @param y a y position on the frame
	 * @return the point on the board that was clicked
	 */
	private Point findCoords(int x, int y){
		int sqSize = canvas.getSquareSize();
		int xSquare = (x-BOARD_LEFT)/sqSize;
		int ySquare = (y-BOARD_TOP)/sqSize;
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
