package cluedoPieces;

import java.awt.Point;
import java.util.Set;
import java.util.HashSet;

import cluedoPieces.Room.RoomName;

public class Player {

	Set<Card> hand;
	private Point pos;
	private String name;// Players name
	private boolean active = true;
	private RoomName room;
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
	 * Player receives a suggestion of three cards, and responds if they are holding any of the cards
	 * @param w Suggested weapon for murder
	 * @param r Suggested room of murder
	 * @param c Suggested victim
	 */
	public void answerSuggestion(WeaponCard w, RoomCard r, CharacterCard c){
		if (hand.contains(w)){System.out.println("Player " + name + " cannot refute seeing the " + w + "!");return;}
		if (hand.contains(r)){System.out.println("Player " + name + " cannot refute having checked the " + r);return;}
		if (hand.contains(c)){System.out.println("Player " + name + " cannot refute having been with " + c + " at the time of death...");return;}
	}
	
	public void setRoom(RoomName r){
		room = r;
	}
	
	public RoomName getCurrentRoom(){
		return room;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean active){
		this.active = active;
	}	
	public void setPos(Point p){
		pos = p;
	}
	public Point getPos(){return pos;}
}
