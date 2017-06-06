package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.junit.Test;

public class TeamTest {

	File in = new File("GameData/Leagues.xml");
	XMLPlayer xmlplayer = new XMLPlayer(in);
	League league = xmlplayer.readFromFile("Eredivisie");
	
	Team ajaxEredivisie = league.getTeam("Ajax");
	Team feyenoordEredivisie = league.getTeam("Feyenoord");
	List<Player> ajax = ajaxEredivisie.getPlayers();
	List<Player> feyenoord = feyenoordEredivisie.getPlayers();
	Team testTeam = new Team ("Ajax", ajax);
	Team testTeam2 = new Team ("Feyenoord", feyenoord);
	
	Team empTeam  = new Team("Feyenoord", new ArrayList<Player>());
	Team empTeam2 = new Team("Feyenoord", new ArrayList<Player>());
	
	Player testPlayer = new Player(3,"Mitchell","Shet","Ado Den Haag","Eredivisie","Dutch",new Date(2/2/2000),"ST",50,60,60,70,40,60);
	Player testPlayer2 = new Player(1,"Davy","Klaasen","Ajax","Eredivisie","Dutch", new Date(1/1/1990),"CB",40,50,60,60,80,70);
	
	/**
	 * Constructor test
	 */
	@Test
	public void testTeamStringArrayListOfPlayer() {
		assertEquals(testTeam.getName(),"Ajax");
		assertNotEquals(testTeam.getPlayer("Stefano","Denswil"),testTeam.getPlayer("Mike", "van der Hoorn"));
		assertNotEquals(testTeam.getByPosition("Defender"),testTeam.getByPosition("Attacker"));
	}

	/**
	 * Empty team constructor test
	 */
	@Test
	public void testTeamString() {
		assertEquals(empTeam,empTeam2);
		assertNotEquals(empTeam,testTeam);
	}

	/**
	 * toString method test
	 */
	@Test
	public void testToString() {
		assertEquals(empTeam.toString(),empTeam2.toString());
		assertNotEquals(empTeam.toString(),testTeam.toString());
	}

	/**
	 * addPlayer Test
	 */
	@Test
	public void testAddPlayer() {
		testTeam.addPlayer(testPlayer);
		assertEquals(testTeam.getPlayer(3),testPlayer);
	}
	
	@Test
	public void testGetNumOfPlayingPlayers(){
		testTeam2.addPlayer(testPlayer);
		testPlayer.setCurPosition("ST");
		assertEquals(0,testTeam.getNumOfPlayingPlayers());
		assertEquals(1,testTeam2.getNumOfPlayingPlayers());	
	}
	
	@Test
	public void testGetPlayingPlayers(){
		assertEquals(0,testTeam.getPlayingPlayers());
	}
	
	@Test
	public void testGetPlayingPlayers2(){
		testTeam2.addPlayer(testPlayer);
		testPlayer.setCurPosition("CM");
		assertEquals(1,testTeam2.getPlayingPlayers());
	}

	/**
	 * removePlayer Test
	 */
	@Test
	public void testRemovePlayer() {
		testTeam.addPlayer(testPlayer);
		testTeam.removePlayer(testPlayer);
		assertEquals(testTeam.getPlayers().contains(testPlayer),false);
	}

	/**
	 * Equals Test
	 */
	@Test
	public void testEqualsObject() {
		assertNotEquals(testTeam.equals(testPlayer),true);
		assertTrue(testTeam.equals(testTeam));
		assertFalse(testTeam.equals(empTeam));
		testTeam.addPlayer(testPlayer);
		assertNotEquals(testTeam.equals(testTeam2),true);
		testTeam2.addPlayer(testPlayer);
		assertEquals(testTeam.equals(testTeam2),false);
	}

	/**
	 * setTeam Test
	 */
	@Test
	public void testSetTeam() {
		testTeam.setName("Feyenoord");
		assertEquals(testTeam.getName(),"Feyenoord");
	}

	/**
	 * setPlayers Test
	 */
	@Test
	public void testSetPlayers() {
		empTeam.setPlayers(ajax);
		assertEquals(empTeam.getPlayers(),testTeam.getPlayers());
	}

	/**
	 * getBudget Test
	 */
	@Test
	public void testGetBudget() {
		testTeam.setBudget(1000);
		assertEquals(testTeam.getBudget(),1000);
	}
	
	/**
	 * alterBudget Test
	 */
	@Test
	public void testAlterBudget(){
		testTeam.alterBudget(7000);
		assertEquals(testTeam.getBudget(),3007000);
	}
	
	@Test
	public void testHasFielKeeper(){
		testTeam.addPlayer(testPlayer);
		testPlayer.setCurPosition("GK");
		assertEquals(true,testTeam.hasFieldedKeeper());
		assertEquals(false,testTeam2.hasFieldedKeeper());
	}
}
