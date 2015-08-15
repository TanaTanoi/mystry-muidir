package cluedoTests;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.*;

import cluedoGame.CluedoGame;
import cluedoGame.TextInterface;
import cluedoPieces.*;
import cluedoPieces.Room.RoomName;

public class CluedoTests {
	/*
	 *---------------------------
	 *------Deck Class tests-----
	 *---------------------------
	 */
	Deck deck;
	Board b= new Board();
	@Test
	public void deck_create_1(){// Should create a new deck without error
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("testplayer1"));
		players.add(new Player("testplayer2"));
		try{
			deck = new Deck(players);
			assert true;
		}catch (Exception e){
			assert false;
		}
	}

	@Test
	public void deck_NullMurderCards(){
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("testplayer1"));
		players.add(new Player("testplayer2"));
		deck = new Deck(players);
		Card[] cards = {null,null,null};
		try{
			deck.compareMurderCards(cards);
			assert true;
		}catch(Exception e){
			assert false;
		}
	}
	@Test
	public void deck_MurderCards_errorcase(){
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("testplayer1"));
		players.add(new Player("testplayer2"));
		deck = new Deck(players);
		Card[] cards = {null,null,null};
		try{
			assert !deck.compareMurderCards(cards);

		}catch(Exception e){
			assert false;
		}
	}

	@Test
	public void deck_error_case_1(){//Tests passing an empty list into the deck
		ArrayList<Player> players = new ArrayList<Player>();
		try{
			deck = new Deck(players);
			assert false;
		}catch (Exception e){
			assert true;
		}
	}

	/*
	 *---------------------------
	 *------Cluedo Game Class tests----
	 *---------------------------
	 */
	@Test
	public void test_dice(){
		//COMPREHENSIVE DICE ROLLING TESTS
		Map<Integer,Integer> distb = new HashMap<Integer,Integer>();
		for(int i = 0; i < 1000;i++){
			int x = CluedoGame.rollDie();
			if(distb.containsKey(x)){
				distb.put(x, distb.get(x)+1);
			}else{
				distb.put(x, 1);
			}
			assertTrue(x >0);
			assertTrue( x < 7);
		}
		assertEquals( distb.size(),6);
	}

	/*
	 *---------------------------
	 *------Board Class tests----
	 *---------------------------
	 */

	@Test
	public void board_create_1(){
		//Board board;
		try{
			b = new Board();
			assert true;
		}catch(Exception e){
			assert false;
		}
	}

	@Test
	public void board_find_1(){
		//Create a normal board and find the reachable points
		//Board board = new Board();
		Set<Point> reachables = b.reachablePoints(new Point(9,9), 3);
		assertTrue(reachables.contains(new Point(11,9)));//reachable
		assertTrue(reachables.contains(new Point(10,8)));//reachable
		assertTrue(!reachables.contains(new Point(11,10)));//unreachable due to inaccessible point
		assertTrue(!reachables.contains(new Point(5,9)));//unreachable due to distance


	}

	@Test
	public void board_find_2(){
		//Cannot find a point from A to B where A is inaccessible

		try{
			b.reachablePoints(new Point(0,0), 3);
			assertTrue( false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}



	@Test
	public void board_room_find_1(){
		b = new Board();
		Set<Room.RoomName> rooms = new HashSet<Room.RoomName>();
		rooms.addAll(b.reachableRooms(new Point(9,9), 25));			//Should find all rooms
		assertTrue(rooms.size() == 10);								//all 9 rooms
		rooms.clear();												//reset

		rooms.addAll(b.reachableRooms(new Point(9,9),3));			//should find ballroom
		assertTrue(rooms.contains(RoomName.BALLROOM));
		assertTrue(rooms.size() == 1);								//and only ballroom
	}

	@Test
	public void board_room_find_2(){
		b = new Board();
		Set<Room.RoomName> rooms = new HashSet<Room.RoomName>();
		rooms.addAll(b.reachableRooms(RoomName.STUDY,4));		//should reach Hall and Kitchen (trap door)
		assertTrue(rooms.contains(RoomName.KITCHEN));
		assertTrue(rooms.contains(RoomName.HALL));
		assertEquals(rooms.size(),2);							//ONLY hall and kitchen
	}
	@Test
	public void board_room_find_3(){
		//should be able to reach all rooms from here
		b = new Board();
		Set<Room.RoomName> rooms = new HashSet<Room.RoomName>();
		rooms.addAll(b.reachableRooms(RoomName.KITCHEN,25));		//should reach all
		assertEquals(rooms.size(),9);
	}

	@Test
	public void board_room_stairs_find_1(){
		b = new Board();
		Set<Room.RoomName> rooms = new HashSet<Room.RoomName>();
		rooms.addAll(b.reachableRooms(RoomName.CONSERVATORY,1));
		assert(rooms.size()==1);
		assert(rooms.contains(RoomName.LOUNGE));

		rooms.clear();
		rooms.addAll(b.reachableRooms(RoomName.LOUNGE,1));
		assert(rooms.size()==1);
		assert(rooms.contains(RoomName.CONSERVATORY));
	}
	@Test
	public void board_print_1(){
		b = new Board();
		try{
			List<Player> players = new ArrayList<Player>();
			Player p = new Player("Dave");
			p.setPos(b.getStartingPoint(1));
			players.add(p);
			b.printBoard(players);


			assert false;
		}catch(IllegalArgumentException e){
			assert true;
		}
	}

	@Test
	public void board_room_find_invalid(){
		b = new Board();
		try{
			b.reachableRooms(new Point(1,1), 1);
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}
	/*
	 *---------------------------
	 *-----Player Class tests----
	 *---------------------------
	 */
	@Test
	public void player_suggestions_test_1(){
		//testing answer suggestion method ROOM
		Player p = new Player("David");
		assertEquals(p.getName(),"David");
		//room
		RoomCard rc = new RoomCard("STUDY");
		p.takeCard(rc);
		assertEquals(p.answerSuggestion(null, rc, null),rc);
		//weapon

	}

	@Test
	public void player_suggestions_test_2(){
		//tests character cards CHARACTER
		Player p = new Player("David");
		CharacterCard cc = new CharacterCard("WHITE");
		p.takeCard(cc);
		assertEquals(p.answerSuggestion(null, null, cc),cc);
	}

	@Test
	public void player_suggestions_test_3(){
		//tests character cards WEAPON
		Player p = new Player("David");
		WeaponCard wc = new WeaponCard("REVOLVER");
		p.takeCard(wc);
		assertEquals(p.answerSuggestion(wc, null, null),wc);
	}

	@Test
	public void play_suggestions_test_4(){
		//doesn't have the card, but does have a card
		Player p = new Player("David");
		p.takeCard(new WeaponCard("REVOLVER"));
		assertEquals(p.getHand().size(),1);
		assertNull(p.answerSuggestion(new WeaponCard("CANDLESTICK"), new RoomCard("STUDY"), new CharacterCard("WHITE")));
	}

	/*
	 *---------------------------
	 *-----Card Class tests------
	 *---------------------------
	 */


	@Test
	public void card_equals_test_1(){
		assertEquals(new WeaponCard("DAGGER").toString(),"DAGGER");
		assertEquals(new CharacterCard("WHITE").toString(),"WHITE");
		assertEquals(new RoomCard("STUDY").toString(),"STUDY");
	}

	/*
	 *---------------------------
	 *-----Card Class tests------
	 *---------------------------
	 */


	@Test
	public void textInterface_test_1(){
		TextInterface ui = new TextInterface();
		ui.print("Test Message");
	}
}
