package Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;

import cluedoPieces.Player;

public class RefuteWindow extends JDialog{
	GameFrame local;
	JTextArea textOut;
	public RefuteWindow(GameFrame local){
		super(local, "Suggestion Refutes");
		setBackground(new Color(0,50,0));
		this.local = local;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		this.setSize(400, 300);
		//Fill display
		drawDisplay();
		
		
		
		setResizable(false);
		setVisible(true);
	}
	
	private void drawDisplay(){
		JPanel p = new JPanel();
		textOut = new JTextArea();
		textOut.setPreferredSize(new Dimension(386,260));
		textOut.setBounds(10, 10, getWidth()-20, getHeight());
		
		textOut.setLineWrap(true);
		textOut.setEditable(false);
		p.add(textOut);
		p.setBackground(Color.BLACK);
		add(p);
	}
	
	public void setText(String text, Font font){
		textOut.setFont(font);
		textOut.setText(text);
	}
}
