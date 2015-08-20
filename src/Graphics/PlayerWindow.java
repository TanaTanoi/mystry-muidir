package Graphics;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import cluedoPieces.Board;

public class PlayerWindow extends JFrame{
	public PlayerWindow(){
		super("New Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(6,2));
		setRadioButtons();
		this.setSize(500, 500);
		setResizable(false);
		setVisible(true);
	}
	
	private void setTextFields(){
		
	}
	
	private void setRadioButtons(){
		ButtonGroup characters = new ButtonGroup();
		JRadioButton scarlet = new JRadioButton("Miss Scarlet");
		characters.add(scarlet);
		add(scarlet);
		JRadioButton mustard = new JRadioButton("Colonel Mustard");
		characters.add(mustard);
		add(mustard);
		JRadioButton white = new JRadioButton("Ms White");
		characters.add(white);
		add(white);
		JRadioButton reverend = new JRadioButton("Reverend Green");
		characters.add(reverend);
		add(reverend);
		JRadioButton peacock = new JRadioButton("Mrs Peacock");
		characters.add(peacock);
		add(peacock);
		JRadioButton plum = new JRadioButton("Professor Plum");
		characters.add(plum);
		add(plum);
	}
}
