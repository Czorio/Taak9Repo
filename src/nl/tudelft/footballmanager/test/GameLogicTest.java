package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchDay;
import nl.tudelft.footballmanager.model.MatchScheme;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.logic.GameLogic;
import nl.tudelft.footballmanager.model.logic.TeamLogic;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 */
public class GameLogicTest {

	File in = new File("GameData/Leagues.xml");
	XMLPlayer xmlplayer = new XMLPlayer(in);
	League league = xmlplayer.readFromFile("Eredivisie");
	
	Team ajax = league.getTeam("Ajax");
	List<Player> ajaxPlayers = ajax.getPlayers();
	Team tAjax = new Team ("Ajax", ajaxPlayers);
	
	Team feyenoord = league.getTeam("Feyenoord");
	List<Player> feyenoordPlayers = feyenoord.getPlayers();
	Team tFeyenoord = new Team ("Feyenoord", feyenoordPlayers);
	
	Team fcGroningen = league.getTeam("FC Groningen");
	List<Player> gronPlayers = fcGroningen.getPlayers();
	Team tFCGroningen = new Team ("FC Groningen", gronPlayers);
	
	Team az = league.getTeam("AZ");
	List<Player> azPlayers = az.getPlayers();
	Team tAZ = new Team ("AZ", azPlayers);
	
	GameState gs = new GameState("Steven", 1, league, tAjax);
	MatchScheme ms;
	
	@Before
	public void initialize() {
		new GameLogic(gs);
		TeamLogic.clearPlayers();
		GameLogic.setSeed(1);
		GameLogic.setIsTesting(true);
		
		ms = new MatchScheme();
		GameLogic.setGameState(gs);
		
		ArrayList<Match> matches = new ArrayList<Match>();
		ArrayList<Match> matches2 = new ArrayList<Match>();
		matches.add(new Match(tAjax, tFeyenoord));
		matches2.add(new Match(tAZ, tFCGroningen));
		matches2.add(new Match(tFeyenoord, tAjax));
		matches.add(new Match(tFCGroningen, tAZ));
		ms.addMatchDay(new MatchDay(0, matches));
		ms.addMatchDay(new MatchDay(1, matches2));
	}
	
	@After
	public void end() {
		TeamLogic.clearPlayers();
		ms = new MatchScheme();
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#GameLogic(nl.tudelft.footballmanager.model.GameState)}.
	 */
	@Test
	public void testGameLogic() {
		assertNotNull(new GameLogic(gs));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#matchDay()}.
	 */
	@Test
	public void testMatchDayReturnFalse() {
		gs.setMatchScheme(ms);
		gs.setGameRound(500);
		
		assertFalse(GameLogic.matchDay());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#matchDay()}.
	 */
	@Test
	public void testMatchDayReturnTrue() {
		gs.setMatchScheme(ms);
		gs.setGameRound(0);
		
		assertTrue(GameLogic.matchDay());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#matchDay()}.
	 */
	@Test
	public void testMatchDaySets() {
		Player p = tAjax.getPlayer(1580);
		
		p.setPlayedGames(0);
		p.setDisabledFor(5);
		p.setCurPosition("Attacker");
		
		gs.setMatchScheme(ms);
		gs.setGameRound(1);
		GameLogic.matchDay();
		
		assertEquals(1, p.getPlayedGames());
		assertEquals(4, p.getDisabledFor());
		
		p.setPlayedGames(0);
		p.setDisabledFor(0);
		p.setCurPosition(null);
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#matchDay()}.
	 */
	@Test
	public void testMatchDay() {
		MatchScheme ms = new MatchScheme(league, 0);
		gs.setMatchScheme(ms);
		
		assertTrue(GameLogic.matchDay());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.GameLogic#getSeed()}.
	 */
	@Test
	public void testGetSeed() {
		assertEquals(1, GameLogic.getSeed());
	}

}
