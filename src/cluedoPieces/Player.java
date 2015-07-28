package cluedoPieces;

import java.awt.Point;
import java.util.Set;
import java.util.HashSet;

import cluedoPieces.Card.*;

public class Player {

	Set<Card> hand;
	private Point coords;
	private String name;// Players name

	public Player(String name){
		hand = new HashSet<Card>();
		this.name = name;
	}

	
	/**
	 * Method to recieve a card from the deal
	 * @param c Card to be added to hand
	 */
	public void takeCard(Card c){
		hand.add(c);
	}

	/**
	 * Takes place of dice roll - generates random number from 1-6
	 * @return random number from 1 - 6
	 */
	public int rollDie(){
		return (int)(Math.random()*7);
	}
	
	public void answerSuggestion(WeaponCard w, RoomCard r, CharacterCard c){
		if (hand.contains(w))System.out.println("Player " + name + " cannot refute seeing the " + w + "!");
		if (hand.contains(r))System.out.println("Player " + name + " cannot refute having checked the " + r);
		if (hand.contains(c))System.out.println("Player " + name + " cannot refute having been with " + c + " at the time of death...");
	}
	
	public boolean makeAccusation(){
		return false;
	}

	public RoomType getCurrentRoom(){
		return null;
	}


}
