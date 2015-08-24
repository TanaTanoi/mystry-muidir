package Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cluedoPieces.Player;

public class SuggestionWindow extends JDialog{
	private static final Font HEADER_FONT = new Font(Font.MONOSPACED,Font.PLAIN,15);
	private static final Color BACKGROUND_COLOR = new Color(230,190,120);
	private static final Color BUTTON_COLOR = new Color(212,197,200);
	private GameFrame local;
	private Player player;
	private String lastWeapon = "candlestick";
	private String lastPerson = "scarlett";
	public SuggestionWindow(GameFrame local, Player p){
		super(local, p.getName() + "'s Suggestion");
		this.local = local;
		this.player = p;
		this.setLayout(new GridLayout(1,3));	
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(550, 220);
		//add panels to the screen
		setCharacterPanel();
		setWeaponPanel();
		setButtonPanel();

		setResizable(false);
		setVisible(true);
	}
	
	public String getWeapon(){
		return lastWeapon;
	}
	public String getCharacter(){
		return lastPerson;
	}
	
	private void setButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(5,1));
		JButton submitSuggestion = new JButton(new AbstractAction("Submit Suggestion"){
			public void actionPerformed(ActionEvent e){
				//TODO link with control class
				dispose();
			}
		});
		//fill button  panel
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		buttonPanel.add(submitSuggestion);
		//set colours
		buttonPanel.setBackground(BACKGROUND_COLOR);
		submitSuggestion.setBackground(BUTTON_COLOR);
		add(buttonPanel);
	}

	private void setWeaponPanel(){
		JPanel weaponPanel = new JPanel(new GridLayout(7,1));
		ButtonGroup buttons = new ButtonGroup();
		JRadioButton candlestick = new JRadioButton(new AbstractAction("Candlestick") {
			public void actionPerformed(ActionEvent e){lastWeapon = "candlestick";}
		});
		JRadioButton dagger = new JRadioButton(new AbstractAction("Dagger") {
			public void actionPerformed(ActionEvent e){lastWeapon = "dagger";}
		});
		JRadioButton leadPipe = new JRadioButton(new AbstractAction("Lead Pipe") {
			public void actionPerformed(ActionEvent e){lastWeapon = "lead_pipe";}
		});
		JRadioButton revolver = new JRadioButton(new AbstractAction("Revolver") {
			public void actionPerformed(ActionEvent e){lastWeapon = "revolver";}
		});
		JRadioButton rope = new JRadioButton(new AbstractAction("Rope") {
			public void actionPerformed(ActionEvent e){lastWeapon = "rope";}
		});
		JRadioButton spanner = new JRadioButton(new AbstractAction("Spanner") {
			public void actionPerformed(ActionEvent e){lastWeapon = "spanner";}
		});
		//set the first button to be selected
		candlestick.setSelected(true);
		//add header to JPanel
		JLabel head = new JLabel("Suggest a weapon:");
		head.setFont(HEADER_FONT);
		weaponPanel.add(head);
		//add all buttons to the button group
		buttons.add(candlestick);
		buttons.add(dagger);
		buttons.add(leadPipe);
		buttons.add(revolver);
		buttons.add(rope);
		buttons.add(spanner);
		//add all buttons to the panel
		weaponPanel.add(candlestick);
		weaponPanel.add(dagger);
		weaponPanel.add(leadPipe);
		weaponPanel.add(revolver);
		weaponPanel.add(rope);
		weaponPanel.add(spanner);
		//set colour
		weaponPanel.setBackground(BACKGROUND_COLOR);
		for (int i = 0; i < weaponPanel.getComponentCount();i++){
			weaponPanel.getComponent(i).setBackground(BACKGROUND_COLOR);
		}
		//add panel to window
		add(weaponPanel);
	}

	private void setCharacterPanel(){
		JPanel characterPanel = new JPanel(new GridLayout(7,1));
		ButtonGroup characters = new ButtonGroup();
		JRadioButton scarlett = new JRadioButton(new AbstractAction("Scarlett") {
			public void actionPerformed(ActionEvent e){lastPerson = "scarlett";}
		});
		JRadioButton mustard = new JRadioButton(new AbstractAction("Colonel Mustard") {
			public void actionPerformed(ActionEvent e){lastPerson = "mustard";}
		});
		JRadioButton white = new JRadioButton(new AbstractAction("Ms White") {
			public void actionPerformed(ActionEvent e){lastPerson = "white";}
		});
		JRadioButton reverend = new JRadioButton(new AbstractAction("Reverend Green") {
			public void actionPerformed(ActionEvent e){lastPerson = "reverend";}
		});
		JRadioButton peacock = new JRadioButton(new AbstractAction("Mrs Peacock") {
			public void actionPerformed(ActionEvent e){lastPerson = "peacock";}
		});
		JRadioButton plum = new JRadioButton(new AbstractAction("Professor Plum") {
			public void actionPerformed(ActionEvent e){lastPerson = "plum";}
		});
		//set the first button to be selected
		scarlett.setSelected(true);
		//add all buttons to the button group
		characters.add(scarlett);
		characters.add(mustard);
		characters.add(white);
		characters.add(reverend);
		characters.add(peacock);
		characters.add(plum);
		//add header to JPanel
		JLabel head = new JLabel("Suggest a murderer:");
		head.setFont(HEADER_FONT);
		characterPanel.add(head);
		//add all buttons to the JPanel
		characterPanel.add(scarlett);
		characterPanel.add(mustard);
		characterPanel.add(white);
		characterPanel.add(reverend);
		characterPanel.add(peacock);
		characterPanel.add(plum);
		//set colour
		characterPanel.setBackground(BACKGROUND_COLOR);
		for (int i = 0; i < characterPanel.getComponentCount();i++){
			characterPanel.getComponent(i).setBackground(BACKGROUND_COLOR);
		}
		//add JPanel to the window
		add(characterPanel);
	}
}
