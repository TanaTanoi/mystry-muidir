package cluedoPieces;

import java.util.*;

public class Deck {

	private List<Card> roomPile;		//holds room cards
	private List<Card> weaponPile;		//holds weapon cards
	private List<Card> characterPile;	//holds character cards
	private Card[] murderCards;			//holds the three cards that solve the murder, 
										//1: Room, 2: Weapon, 3: Character
	public Deck(){
		roomPile= new ArrayList<Card>();
		weaponPile= new ArrayList<Card>();
		characterPile= new ArrayList<Card>();
		murderCards  = new Card[3];
	}
	
	
	/**
	 * Selects the three cards that solve the murder, where position one is for the room,
	 * two; the weapon and three; the character.
	 * This also removes the cards from the deck, instead storing them in the array.
	 * Note: Running this method requires the deck to be built
	 */
	public void selectMurderCards(){
		if(!roomPile.isEmpty()&&!weaponPile.isEmpty()&&!characterPile.isEmpty()){
			murderCards[0] = roomPile.remove((int)Math.random()*roomPile.size());
			murderCards[1] = weaponPile.remove((int)Math.random()*weaponPile.size());
			murderCards[2] = characterPile.remove((int)Math.random()*characterPile.size());
		}else{
			throw new IllegalArgumentException("Requires a deck to be built before use!");
		}
	}
	
	
	
	
	/**
	 * Distributes cards to the list of players provided.
	 * 
	 * @param players
	 */
	public void disbCards(List<Player> players){
		List<Card> shuffledDeck = new ArrayList<Card>();
		shuffledDeck.addAll(roomPile);
		shuffledDeck.addAll(weaponPile);
		shuffledDeck.addAll(characterPile);
		int i = 0;
		while(!shuffledDeck.isEmpty()){			//while the deck is not empty, distribute the cards at random
			players.get(i++).giveCard(
					shuffledDeck.remove(
							(int)Math.random()*shuffledDeck.size()));
			i = i % players.size();
		}
	}
	
	
	
}
