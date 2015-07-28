package cluedoTests;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.*;

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
	public void deck_create_1(){
		deck = new Deck();
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
			Set<Point> reachables = b.reachablePoints(new Point(0,0), 3);
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
		assertTrue(rooms.size() == 9);								//all 9 rooms
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
		System.out.println(rooms.size());
		assertEquals(rooms.size(),2);							//ONLY hall and kitchen
			
		
	}



}
