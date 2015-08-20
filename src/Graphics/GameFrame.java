package Graphics;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
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

import com.sun.xml.internal.ws.message.MimeAttachmentSet;

import cluedoGame.Control;
import cluedoPieces.Board;


public class GameFrame extends JFrame implements MouseListener, MouseMotionListener{
	private static final int BOARD_TOP = 61;
	private static final int BOARD_LEFT= 15;
	private static final int MINIMUM_SIZE = 400;

	public Point clickedP = null;

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
		setMinimumSize(new Dimension(MINIMUM_SIZE, MINIMUM_SIZE));
		setVisible(true);
		addButtons();
	}


	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		JMenuItem newGameItem = new JMenuItem(new AbstractAction("N|New Game") {
			public void actionPerformed(ActionEvent e){
				//TODO open players window
				new PlayerWindow();
			}
		});
		gameMenu.add(newGameItem);
		this.setJMenuBar(menuBar);
	}

	private void addButtons(){
		Color panelColor = new Color(0,30,0);
		JPanel buttonPanel = new JPanel(new GridLayout(5, 1));//so the buttons will all align vertically
		JButton roll= new JButton(new AbstractAction("Roll Die") {
			public void actionPerformed(ActionEvent e){
				//TODO roll random number
				System.out.println("dicks");
			}
		});
		JButton endTurn= new JButton(new AbstractAction("End Turn") {
			public void actionPerformed(ActionEvent e){
				//TODO Change to next player
			}
		});
		buttonPanel.add(roll);
		buttonPanel.add(endTurn);
		buttonPanel.setBackground(panelColor);
		this.add(buttonPanel, BorderLayout.EAST);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//TODO write code for detecting what square the mouse is released on
		Point p = findCoords(e.getX(),e.getY());
		System.out.println("X = "+p.x+" Y= "+p.y);
		clickedP = p;
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
