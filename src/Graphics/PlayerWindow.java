package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

//import sun.awt.DisplayChangedListener;
import cluedoPieces.Board;
import cluedoPieces.Player;

public class PlayerWindow extends JDialog{
	private static final Color BACKGROUND_COLOR = new Color(215,175,105);
	private static final Color TEXTBOX_COLOR = new Color(230,190,120);
	private static final Color BUTTON_COLOR = new Color(212,197,200);
	GameFrame local;
	ButtonGroup characters;
	JPanel radioButtons;
	JPanel nameEntry;
	JPanel completionPanel;
	JTextArea nameField;
	ArrayList<Player> players;
	Set<String> picked;//set of all players that have already been picked
	JTextArea playerDisplay;
	String buttonSelected;
	JPanel filler;
	Font font = new Font(Font.MONOSPACED,Font.PLAIN,15);
	JRadioButton lastPlayerButton;

	public PlayerWindow(GameFrame local){
		super(local, "Player Selection");
		setBackground(new Color(0,50,0));
		this.local = local;
		players = new ArrayList<Player>();
		picked = new HashSet<String>();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.setLayout(new GridLayout(2,2));
		this.setSize(450, 500);
		setRadioButtons();
		setTextFields();
		setCompletionPanel();
		displayComponents();
		setBackground(TEXTBOX_COLOR);
		
		setResizable(false);
		setVisible(true);
	}


	/**
	 * Code for bottom left panel, just submitting players arraylist
	 */
	private void setCompletionPanel() {
		completionPanel = new JPanel(new GridLayout(5,1));

		JButton startGame = new JButton(new AbstractAction("Start Game") {
			public void actionPerformed(ActionEvent e){
				if (players.size() >= 2){
					local.control.setPlayers(players);
					dispose();
				} else{// if the minimum number of players required is not met
					JOptionPane.showMessageDialog(local,
						    "Game requires a minimum of 2 players");
				}
			}
		});
		startGame.setBackground(BUTTON_COLOR);
		for (int i = 0; i < 3; i++){
			completionPanel.add(new JLabel());
		}
		completionPanel.add(startGame);
		completionPanel.setBackground(BACKGROUND_COLOR);
	}

	/**
	 * Adds completed JPanels to the window
	 */
	private void displayComponents() {
		playerDisplay = new JTextArea();
		playerDisplay.setEditable(false);
		playerDisplay.setFont(font);
		playerDisplay.setBackground(TEXTBOX_COLOR);
		add(radioButtons);
		add(playerDisplay);
		add(nameEntry);
		add(completionPanel);
	}

	/**
	 * Code for the player name text field and add player button, does checking of other
	 * fields to ensure that there are not too many players and that no character is picked twice
	 */
	private void setTextFields(){
		nameEntry = new JPanel(new GridLayout(5,1));
		nameEntry.setBackground(BACKGROUND_COLOR);
		nameField = new JTextArea();
		nameField.setFont(font);
		nameField.setLineWrap(true);
		nameField.setBackground(TEXTBOX_COLOR);
		JButton submitPlayer = new JButton(new AbstractAction("Add Player") {
			public void actionPerformed(ActionEvent e){
				String name;
				name = nameField.getText();
				if (players.size() == 6){
					JOptionPane.showMessageDialog(local,
						    "Game has a maximum of 6 players");
					return;
				}
				else if (nameField.getText().length() < 3){
					JOptionPane.showMessageDialog(local,
						    "Please enter a player name");
				}
				else if (picked.contains(buttonSelected)){
					JOptionPane.showMessageDialog(local,
						    "Cannot have a character picked more than once");
				}
				else {
					lastPlayerButton.setEnabled(false);
					Player p = new Player(name);
					p.setCharacter(buttonSelected);
					players.add(p);
					playerDisplay.setText(playerDisplay.getText() + buttonSelected + ": " + name + "\n");
					picked.add(buttonSelected);//add character to list of chosen character (no duplicates)
					nameField.setText("");//clear name from text field
				}
			}
		});
		submitPlayer.setBackground(BUTTON_COLOR);
		JLabel label = new JLabel();
		label.setFont(font);
		label.setText("Player Name: ");
		nameEntry.add(label);
		nameEntry.add(nameField);
		nameEntry.add(new JLabel());
		nameEntry.add(submitPlayer);
		nameEntry.add(new JLabel());
	}

	/**
	 * Creates radio buttons and sets them into the buttongroup characters
	 */
	private void setRadioButtons(){
		characters = new ButtonGroup();
		radioButtons = new JPanel(new GridLayout(6,1));
		JRadioButton scarlett = new JRadioButton(new AbstractAction("Miss Scarlett") {
			public void actionPerformed(ActionEvent e){
				setLastPressed("Scarlett");
				lastPlayerButton = (JRadioButton)radioButtons.getComponent(0);
			}
		});
		characters.add(scarlett);
		radioButtons.add(scarlett);
		scarlett.setBackground(BACKGROUND_COLOR);
		JRadioButton mustard = new JRadioButton(new AbstractAction("Colonel Mustard") {
			public void actionPerformed(ActionEvent e){
				setLastPressed("Mustard");
				lastPlayerButton = (JRadioButton)radioButtons.getComponent(1);
			}
		});
		characters.add(mustard);
		radioButtons.add(mustard);
		mustard.setBackground(BACKGROUND_COLOR);
		JRadioButton white = new JRadioButton(new AbstractAction("Mrs White") {
			public void actionPerformed(ActionEvent e){setLastPressed("White");
			lastPlayerButton = (JRadioButton)radioButtons.getComponent(2);}
		});
		characters.add(white);
		radioButtons.add(white);
		white.setBackground(BACKGROUND_COLOR);
		JRadioButton reverend = new JRadioButton(new AbstractAction("Reverend Green") {
			public void actionPerformed(ActionEvent e){setLastPressed("Reverend");
			lastPlayerButton = (JRadioButton)radioButtons.getComponent(3);
			}
		});
		characters.add(reverend);
		radioButtons.add(reverend);
		reverend.setBackground(BACKGROUND_COLOR);
		JRadioButton peacock = new JRadioButton(new AbstractAction("Mrs Peacock") {
			public void actionPerformed(ActionEvent e){setLastPressed("Peacock");
			lastPlayerButton = (JRadioButton)radioButtons.getComponent(4);
			}
		});
		characters.add(peacock);
		radioButtons.add(peacock);
		peacock.setBackground(BACKGROUND_COLOR);
		JRadioButton plum = new JRadioButton(new AbstractAction("Professor Plum") {
			public void actionPerformed(ActionEvent e){setLastPressed("Plum");
			lastPlayerButton = (JRadioButton)radioButtons.getComponent(5);
			}
		});
		characters.add(plum);
		radioButtons.add(plum);
		radioButtons.setBackground(BACKGROUND_COLOR);
		plum.setBackground(BACKGROUND_COLOR);
		scarlett.setSelected(true);
		buttonSelected = "Scarlett";
		lastPlayerButton = scarlett;
	}

	/**
	 * used by radio buttons to save the last selected character
	 * @param p String of the character represented by the radio button
	 */
	private void setLastPressed(String p){
		System.out.println(getWidth() + " " + getHeight());
		buttonSelected = p;
		System.out.println(p + " Selected");
	}
}
