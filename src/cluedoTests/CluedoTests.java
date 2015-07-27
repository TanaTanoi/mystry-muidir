package cluedoTests;

import java.awt.Point;
import java.util.Set;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.*;

import cluedoPieces.*;

public class CluedoTests {

	/*
	 *---------------------------
	 *------Deck Class tests-----
	 *--------------------------- 
	 */
	Deck deck;
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
		Board board;
		try{
			board = new Board();
			assert true;
		}catch(Exception e){
			assert false;
		}
	}

	@Test
	public void board_find_1(){
		//Create a normal board and find the reachable points
		Board board = new Board();
		Set<Point> reachables = board.reachablePoints(new Point(9,9), 3);
		assert reachables.contains(new Point(11,9));//reachable
		assert reachables.contains(new Point(10,7));//reachable
		assert !reachables.contains(new Point(11,10));//unreachable due to inaccessible point
		assert !reachables.contains(new Point(5,9));//unreachable due to distance
		

	}
	
	@Test
	public void board_find_2(){
		//Cannot find a point from A to B where A is inaccessible 
		Board board= new Board();
		try{
			Set<Point> reachables = board.reachablePoints(new Point(0,0), 3);
			assert false;
		}catch(IllegalArgumentException e){
			assert true;
		}
		

	}
	



}
