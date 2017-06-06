/**
 * Test for GameState class
 */
package nl.tudelft.footballmanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javafx.collections.FXCollections;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchDay;
import nl.tudelft.footballmanager.model.MatchResult;
import nl.tudelft.footballmanager.model.MatchScheme;
import nl.tudelft.footballmanager.model.Team;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 * @version final
 */
public class GameStateTest {

	File loadFile, loadFile2, saveFile;
	GameState testGameState1, testGameState2, testGameState3, testGameState4, testGameState5, testGameState6, testGameState7;
	Team testTeam1;
	League testLeague1;
	MatchScheme ms;
	MatchDay md;
	ArrayList<Match> matches;
	Match match;
	MatchResult mr;
	Map<Team, Integer> points;
	
	Team ajax = new Team("Ajax");
	Team feyenoord = new Team("Feyenoord");
	
	@Before 
	public void initialize() {
		loadFile = new File("XML/GameStateTest.xml");
		loadFile2 = new File("XML/GameStateTest2.xml");
		saveFile = new File("XML/SaveGameStateTest.xml");
		
		testTeam1 = new Team("Teamname");
		testLeague1 = new League("LeagueName");
		
		testGameState1 = new GameState("Karel Janssen", 7, "Eredivisie", "ADO Den Haag");
		testGameState2 = new GameState("Pjotter Karelson", 1, "Eredivisie", "Feyenoord");
		testGameState3 = new GameState(loadFile);
		testGameState4 = new GameState(loadFile2);
//		testGameState5 = new GameState(saveFile);
		testGameState6 = new GameState("Jans Janssen", 2, testLeague1, testTeam1);
		testGameState7 = new GameState();
		
		ajax = new Team("Ajax");
		feyenoord = new Team("Feyenoord");
		
		ms = new MatchScheme();
		matches = new ArrayList<Match>();
		match = new Match(ajax, feyenoord);
		mr = new MatchResult();
		
		points = FXCollections.observableHashMap();
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.lang.String, int, java.lang.String, java.lang.String)}.
	 * Tests Constructor.
	 */
	@Test
	public void testGameStateStringIntStringStringConstructor() {
		assertNotNull(testGameState1);
		assertNotNull(testGameState2);
	}
	
	@Test
	public void testSetLeagueString() {
		testGameState7.setLeague("Eredivisie");
		assertEquals("Eredivisie", testGameState7.getLeagueName());
	}
	
	@Test
	public void testSetMyTeamMyTeam() {
		testGameState7.setMyTeam(testTeam1);
	}
	
	@Test
	public void testIsUselessGamestate() {
		assertFalse(GameState.isUseless(testGameState6));
	}
	
	@Test
	public void testGameState() {
		assertNotNull(testGameState7);
	}
	
	@Test
	public void testGameStateStringIntLeagueTeamConstructor() {
		assertNotNull(testGameState6);
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.lang.String, int, java.lang.String, java.lang.String)}.
	 * Tests Coach name.
	 */
	@Test
	public void testGameStateStringIntStringStringCoachName() {
		assertEquals("Karel Janssen", testGameState1.getCoachName());
		assertEquals("Pjotter Karelson", testGameState2.getCoachName());
	}

	@Test
	public void testGameStateStringIntStringStringTeamName() {
		assertEquals("ADO Den Haag", testGameState1.getMyTeamName());
		assertEquals("Feyenoord", testGameState2.getMyTeamName());
	}

	@Test
	public void testGameStateStringIntStringStringLeagueName() {
		assertEquals("Eredivisie", testGameState1.getLeagueName());
		assertEquals("Eredivisie", testGameState2.getLeagueName());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.lang.String, int, java.lang.String, java.lang.String)}.
	 * Tests Round number.
	 */
	@Test
	public void testGameStateStringIntStringStringRound() {
		assertEquals(7, testGameState1.getGameRound());
		assertEquals(1, testGameState2.getGameRound());
	}

	@Test
	public void testNextRound() {
		assertEquals(1, testGameState2.getGameRound());
		testGameState2.nextRound();
		assertEquals(2, testGameState2.getGameRound());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.lang.String, int, java.lang.String, java.lang.String)}.
	 * Tests League.
	 */
	@Test
	public void testGameStateStringIntStringStringLeague() {
		assertEquals("Eredivisie", testGameState1.getLeague().getName());
		assertEquals("Eredivisie", testGameState2.getLeague().getName());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.lang.String, int, java.lang.String, java.lang.String)}.
	 * Tests Team.
	 */
	@Test
	public void testGameStateStringIntStringStringTeam() {
		assertEquals("ADO Den Haag", testGameState1.getMyTeam().getName());
		assertEquals("Feyenoord", testGameState2.getMyTeam().getName());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.io.File)}.
	 * Tests Constructor.
	 */
	@Test
	public void testGameStateFileContructor() {
		System.out.println(testGameState3.toString());
		assertNotNull(testGameState3);	
		assertNotNull(testGameState4);
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.io.File)}.
	 * Tests Coach Name.
	 */
	@Test
	public void testGameStateFileCoachName() {
		assertEquals("gjfhjgf", testGameState3.getCoachName());
		assertEquals("Jan Janssen", testGameState4.getCoachName());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.io.File)}.
	 * Tests Round number.
	 */
	@Test
	public void testGameStateFileRound() {
		assertEquals(1, testGameState3.getGameRound());
		assertEquals(3, testGameState4.getGameRound());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.io.File)}.
	 * Tests League.
	 */
	@Test
	public void testGameStateFileLeague() {
		assertEquals("Bundesliga 2", testGameState3.getLeague().getName());
		assertEquals("Belgium Pro League", testGameState4.getLeague().getName());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#GameState(java.io.File)}.
	 * Tests Team.
	 */
	@Test
	public void testGameStateFileTeam() {
		assertEquals("FC Erzgebirge Aue", testGameState3.getMyTeam().getName());
		assertEquals("KV Kortrijk", testGameState4.getMyTeam().getName());
	}

	@Test
	public void testLoadFile() {
		assertNotNull(GameState.load(loadFile));
	}
	
	@Test
	public void testSaveFile() {
		assertNotNull(GameState.save(testGameState3, saveFile));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#getOverallScores()}.
	 * Tests Team.
	 */
	@Test
	public void testGetOverallScoresDraw() {
		mr.setHomeScore(1);
		mr.setAwayScore(1);
		
		match.setMatchResult(mr);
		matches.add(0, match);

		md = new MatchDay(0, matches);
		ms.addMatchDay(md);
		testGameState1.setMatchScheme(ms);
		
		points.put(ajax, 1);
		points.put(feyenoord, 1);

		assertEquals(points, testGameState1.getOverallScores());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#getOverallScores()}.
	 * Tests Team.
	 */
	@Test
	public void testGetOverallScoresHomeWin() {
		mr.setHomeScore(1);
		mr.setAwayScore(0);
		
		match.setMatchResult(mr);
		matches.add(0, match);

		md = new MatchDay(0, matches);
		ms.addMatchDay(md);
		testGameState1.setMatchScheme(ms);
		
		points.put(ajax, 3);

		assertEquals(points, testGameState1.getOverallScores());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.GameState#getOverallScores()}.
	 * Tests Team.
	 */
	@Test
	public void testGetOverallScoresAwayWin() {
		mr.setHomeScore(0);
		mr.setAwayScore(1);
		
		match.setMatchResult(mr);
		matches.add(0, match);

		md = new MatchDay(0, matches);
		ms.addMatchDay(md);
		testGameState1.setMatchScheme(ms);
		
		points.put(feyenoord, 3);

		assertEquals(points, testGameState1.getOverallScores());
	}
	
}
