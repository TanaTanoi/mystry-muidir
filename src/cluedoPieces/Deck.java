package cluedoPieces;

import java.util.*;
import cluedoPieces.Card.*;

public class Deck {

	private List<Card> roomPile;		//holds room cards
	private List<Card> weaponPile;		//holds weapon cards
	private List<Card> characterPile;	//holds character cards
	private Card[] murderCards;			//holds the three cards that solve the murder, 
										//1: Room, 2: Weapon, 3: Character
	public Deck(List<Player> players){
		roomPile= new ArrayList<Card>();
		weaponPile= new ArrayList<Card>();
		characterPile= new ArrayList<Card>();
		murderCards  = new Card[3];
		loadDeck();
		selectMurderCards();
		distributeCards(players);
	}
	
	
	/**
	 * Selects the three cards that solve the murder, where position one is for the room,
	 * two; the weapon and three; the character.
	 * This also removes the cards from the deck, instead storing them in the array.
	 * Note: Running this method requires the deck to be built
	 */
	private void selectMurderCards(){
		if(!roomPile.isEmpty()&&!weaponPile.isEmpty()&&!characterPile.isEmpty()){
			murderCards[0] = roomPile.remove((int)Math.random()*roomPile.size());
			murderCards[1] = weaponPile.remove((int)Math.random()*weaponPile.size());
			murderCards[2] = characterPile.remove((int)Math.random()*characterPile.size());
		}else{
			throw new IllegalArgumentException("Requires a deck to be built before use!");
		}
	}
	
	/**
	 * Compares an array of three cards to the murder cards. Returns true if the three cards 
	 * are of the same type (As depicted in the Card implementation equals methods) else 
	 * returns false.
	 * If array is not suited, throws IllegalArgumentException.
	 * @param proposal
	 * @return
	 */
	public boolean compareMurderCards(Card[] proposal){
		if(proposal.length!=3||
				!(proposal[0] instanceof RoomCard)||
				!(proposal[1] instanceof WeaponCard)||
				!(proposal[2] instanceof CharacterCard)){
			throw new IllegalArgumentException("Requires array length of 3, index 0 to be RoomCard, 1 to be WeaponCard, and 2 to be CharacterCard!");
		}
		for(int i = 0; i < 3;i++){
			if(!proposal[i].equals(murderCards[i])){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Distributes cards to the list of players provided.
	 * 
	 * @param players
	 */
	private void distributeCards(List<Player> players){
		List<Card> shuffledDeck = new ArrayList<Card>();
		shuffledDeck.addAll(roomPile);
		shuffledDeck.addAll(weaponPile);
		shuffledDeck.addAll(characterPile);
		int i = 0;
		while(!shuffledDeck.isEmpty()){	//while the deck is not empty, distribute the cards at random
			players.get(i++).takeCard(
				shuffledDeck.remove(
					(int)Math.random()*shuffledDeck.size()));
			i = i % players.size();
		}
	}

	/**
	 * Privately used method that loads in the hard coded cards into the game, depending on the enums.
	 */
	private void loadDeck(){
		//Add all weapons+
		for (WeaponType w:WeaponType.values()){
			weaponPile.add(new WeaponCard(w));
		}
		for (Person c:Person.values()){
			characterPile.add(new CharacterCard(c));
		}
		for (RoomType r:RoomType.values()){
			roomPile.add(new RoomCard(r));
		}
	}
	
}
