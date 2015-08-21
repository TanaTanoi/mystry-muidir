package Graphics;

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

import sun.awt.DisplayChangedListener;
import cluedoPieces.Board;
import cluedoPieces.Player;

public class PlayerWindow extends JDialog{
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
	Font font = new Font(Font.MONOSPACED,Font.PLAIN,15);

	public PlayerWindow(GameFrame local){
		super(local, "Player Selection");
		this.local = local;
		players = new ArrayList<Player>();
		picked = new HashSet<String>();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(new GridLayout(2,2));
		this.setSize(500, 500);
		setRadioButtons();
		setTextFields();
		setCompletionPanel();

		displayComponents();

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
					//TODO send players to control class
					local.control.setPlayers(players);
				} else{// if the minimum number of players required is not met
					JOptionPane.showMessageDialog(local,
						    "Game requires a minimum of 2 players");
				}
			}
		});
		completionPanel.add(new JPanel());//filler panels
		completionPanel.add(new JPanel());
		completionPanel.add(new JPanel());
		completionPanel.add(startGame);
		completionPanel.add(new JPanel());
	}

	/**
	 * Adds completed JPanels to the window
	 */
	private void displayComponents() {
		playerDisplay = new JTextArea();
		playerDisplay.setEditable(false);
		playerDisplay.setFont(font);
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
		nameField = new JTextArea();
		nameField.setFont(font);
		nameField.setLineWrap(true);
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
					Player p = new Player(name);
					p.setCharacter(buttonSelected);
					players.add(p);
					playerDisplay.setText(playerDisplay.getText() + buttonSelected + ": " + name + "\n");
					picked.add(buttonSelected);//add character to list of chosen character (no duplicates)
				}
			}
		});
		JLabel label = new JLabel();
		label.setFont(font);
		label.setText("Player Name: ");
		nameEntry.add(label);
		nameEntry.add(nameField);
		nameEntry.add(new JPanel());
		nameEntry.add(submitPlayer);
		nameEntry.add(new JPanel());
	}

	/**
	 * Creates radio buttons and sets them into the buttongroup characters
	 */
	private void setRadioButtons(){
		characters = new ButtonGroup();
		radioButtons = new JPanel(new GridLayout(6,1));
		JRadioButton scarlet = new JRadioButton(new AbstractAction("Miss Scarlett") {
			public void actionPerformed(ActionEvent e){setLastPressed("Scarlett");}
		});
		characters.add(scarlet);
		radioButtons.add(scarlet);
		JRadioButton mustard = new JRadioButton(new AbstractAction("Colonel Mustard") {
			public void actionPerformed(ActionEvent e){setLastPressed("Mustard");}
		});
		characters.add(mustard);
		radioButtons.add(mustard);
		JRadioButton white = new JRadioButton(new AbstractAction("Mrs White") {
			public void actionPerformed(ActionEvent e){setLastPressed("White");}
		});
		characters.add(white);
		radioButtons.add(white);
		JRadioButton reverend = new JRadioButton(new AbstractAction("Reverend Green") {
			public void actionPerformed(ActionEvent e){setLastPressed("Reverend");}
		});
		characters.add(reverend);
		radioButtons.add(reverend);
		JRadioButton peacock = new JRadioButton(new AbstractAction("Mrs Peacock") {
			public void actionPerformed(ActionEvent e){setLastPressed("Peacock");}
		});
		characters.add(peacock);
		radioButtons.add(peacock);
		JRadioButton plum = new JRadioButton(new AbstractAction("Professor Plum") {
			public void actionPerformed(ActionEvent e){setLastPressed("Plum");}
		});
		characters.add(plum);
		radioButtons.add(plum);
		scarlet.setSelected(true);
		buttonSelected = "Scarlett";
	}

	/**
	 * used by radio buttons to save the last selected character
	 * @param p String of the character represented by the radio button
	 */
	private void setLastPressed(String p){
		buttonSelected = p;
		System.out.println(p + " Selected");
	}
}
