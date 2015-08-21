package cluedoPieces;

import java.awt.Point;
import java.util.Set;
import java.util.HashSet;

import cluedoPieces.Card.Person;
import cluedoPieces.Room.RoomName;

public class Player {

	Set<Card> hand;
	private Point pos;
	private String name;// Players name
	private boolean active = true;
	private RoomName room;
	private Person person;
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
	 * Returns the cards the player has in a set
	 * @return
	 */
	public Set<Card> getHand(){
		return hand;
	}
	/**
	 * Player receives a suggestion of three cards, and returns the first card that has been suggested,
	 * starting with the Weapon card, Character, Room
	 * @param w Suggested weapon for murder
	 * @param r Suggested room of murder
	 * @param c Suggested victim
	 * @return - w, r, or c cards if they have it, else null
	 */
	public Card answerSuggestion(WeaponCard w, RoomCard r, CharacterCard c){
		if(hand.contains(w)){
			return w;
		}else if(hand.contains(c)){
			return c;
		}else if(hand.contains(r)){
			return r;
		}
		return null;
	}

	public void setRoom(RoomName r){
		room = r;
	}
	
	/**
	 * Returns a Room.RoomName enum signifying what room
	 * the current player is in.
	 * @return
	 */
	public RoomName getCurrentRoom(){
		return room;
	}
	
	/**
	 * Sets the character this player wishes to play as
	 * @param ch The string of the character to be selected (Not case sensitive)
	 */
	public void setCharacter(String ch){
		try {
			person = Person.valueOf(ch.toUpperCase());
		}catch(IllegalArgumentException e){}
	}

	public String getName(){
		return name;
	}
	/**
	 * Returns if this player is still in the game(hasn't made a false accusation)
	 * @return
	 */
	public boolean isActive(){
		return active;
	}
	/**
	 * Sets this player to inactive, when a false accusation is made
	 */
	public void setInactive(){
		this.active = false;
	}
	/**
	 * Set the player's position on the board.
	 * Assumes this point is a valid point.
	 * @param p
	 */
	public void setPos(Point p){
		pos = p;
	}
	public Point getPos(){return pos;}
}
