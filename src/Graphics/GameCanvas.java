package Graphics;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JPanel;

import cluedoGame.Control;

import javax.swing.JMenuBar;

import cluedoPieces.Board;
import cluedoPieces.Player;


public class GameCanvas extends JPanel{
	private Control control;

	public GameCanvas(Control control){
		super();
		this.control = control;

		Timer t = new Timer();
		t.start();
	}

	public class Timer extends Thread {

		public void run() {
			while(true) {
				try {
					Thread.sleep(50);
					repaint();
				} catch(InterruptedException e) {}
			} } }

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		paintBackground(g2d);
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(500,500);
	}

	/**
	 * paints the board onto the canvas
	 * @param g graphics object that the board is to be painted to
	 */
	private void paintBackground(Graphics2D g){
		int width = (int)getSize().getWidth();
		int height = (int)getSize().getHeight();
		int boardSize = Math.min(width, height)-Board.boardSize;
		Color c1 = new Color(0,20,0);
		Color c2 = new Color(0,50,0);
		Point2D p = new Point2D.Float(300, 300);
		float[] dist = {0.0f,0.5f,1f};
		Color[] colors = {c2,c1,c2};
		RadialGradientPaint backColor = new RadialGradientPaint(p, 900, dist, colors);
		g.setPaint(backColor);// Set colour for green board background
		g.fillRect(getX(), getY(), width, height); // draw rect the size of the current window as background
		g.drawImage(control.getBoardImage(boardSize), 10,10,boardSize,boardSize,this); //draw the board itself, leaving space for menu and cards
	}

	/**
	 * Get the size of an indivdual square
	 * @return the size of each square on the board at the current time
	 */
	public double getSquareSize(){
		double width = getSize().getWidth();
		double height = getSize().getHeight();
		return (Math.min(width, height)-Board.boardSize)/(double)Board.boardSize;
	}

}
