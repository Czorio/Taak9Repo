package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.footballmanager.model.MatchResult;
import nl.tudelft.footballmanager.model.Player;

import org.junit.Test;

public class MatchResultTest {

	MatchResult testResult = new MatchResult();
	MatchResult testResult2 = new MatchResult();
	Player nick = new Player(1,"Davy","Klaasen","Ajax","Eredivisie","Dutch", new Date(1/1/1990),"CB",40,50,60,60,80,70);
	Map<Integer, Player> homeGoals = new HashMap<Integer, Player>();
	Map<Integer, Player> awayGoals = new HashMap<Integer, Player>();
	
	@Test
	public void testAddHomeGoal() {
		testResult.addHomeGoal(20, nick);
		testResult2.addHomeGoal(20, nick);
		assertEquals(testResult.getHomeGoals(),testResult2.getHomeGoals());
	}

	@Test
	public void testAddAwayGoal() {
		testResult.addAwayGoal(20, nick);
		testResult2.addAwayGoal(20, nick);
		assertEquals(testResult.getAwayGoals(),testResult2.getAwayGoals());
	}

	@Test
	public void testSetHomeScore() {
		testResult.setHomeScore(3);
		assertEquals(testResult.getHomeScore(),3);
	}

	@Test
	public void testGetAwayScore() {
		testResult.setAwayScore(4);
		assertEquals(testResult.getAwayScore(),4);
	}

	@Test
	public void testSetHomeGoals() {
		testResult.setHomeGoals(homeGoals);
		testResult2.setHomeGoals(awayGoals);
		assertEquals(testResult2.getHomeGoals(),testResult.getHomeGoals());
	}

	@Test
	public void testSetAwayGoals() {
		testResult.setAwayGoals(awayGoals);
		testResult2.setAwayGoals(awayGoals);
		assertEquals(testResult.getAwayGoals(),testResult2.getAwayGoals());
	}
	
	@Test
	public void testGetReadableScore() {
		testResult.setHomeScore(1);
		testResult.setAwayScore(3);
		assertEquals("1 - 3", testResult.getReadableScore());
	}

}
