package Graphics;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import sun.awt.DisplayChangedListener;
import cluedoPieces.Board;
import cluedoPieces.Player;

public class PlayerWindow extends JFrame{
	ButtonGroup characters;
	JPanel radioButtons;
	JPanel nameEntry;
	JPanel completionPanel;
	JTextArea nameField;
	ArrayList<Player> players;
	JTextArea playerDisplay;
	Font font = new Font(Font.MONOSPACED,Font.PLAIN,15);
	
	public PlayerWindow(){
		super("New Game");
		players = new ArrayList<Player>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2,2));
		this.setSize(500, 500);
		setRadioButtons();
		setTextFields();
		setCompletionPanel();
		
		displayComponents();
		
		setResizable(false);
		setVisible(true);
	}
	
	private void setCompletionPanel() {
		completionPanel = new JPanel(new GridLayout(5,1));
		
		JButton startGame = new JButton(new AbstractAction("Start Game") {
			public void actionPerformed(ActionEvent e){
				if (players.size() >= 2){//must have a minimum of 2 players in the game
					
				}else{
					
				}
			}
		});
		
	}

	private void displayComponents() {
		playerDisplay = new JTextArea();
		playerDisplay.setEditable(false);
		playerDisplay.setFont(font);
		add(radioButtons);
		add(playerDisplay);
		add(nameEntry);
		add(new JPanel());
		
	}

	private void setTextFields(){
		nameEntry = new JPanel(new GridLayout(5,1));
		nameField = new JTextArea();
		nameField.setFont(font);
		JButton submitPlayer = new JButton(new AbstractAction("Add Player") {
			public void actionPerformed(ActionEvent e){
				
			}
		});
		nameEntry.add(new JPanel());
		nameEntry.add(nameField);	
		nameEntry.add(new JPanel());
		nameEntry.add(submitPlayer);
		nameEntry.add(new JPanel());
		
	}
	
	private void setRadioButtons(){
		characters = new ButtonGroup();
		radioButtons = new JPanel(new GridLayout(6,1));
		JRadioButton scarlet = new JRadioButton("Miss Scarlet");
		characters.add(scarlet);
		radioButtons.add(scarlet);
		JRadioButton mustard = new JRadioButton("Colonel Mustard");
		characters.add(mustard);
		radioButtons.add(mustard);
		JRadioButton white = new JRadioButton("Ms White");
		characters.add(white);
		radioButtons.add(white);
		JRadioButton reverend = new JRadioButton("Reverend Green");
		characters.add(reverend);
		radioButtons.add(reverend);
		JRadioButton peacock = new JRadioButton("Mrs Peacock");
		characters.add(peacock);
		radioButtons.add(peacock);
		JRadioButton plum = new JRadioButton("Professor Plum");
		characters.add(plum);
		radioButtons.add(plum);
	}
}
