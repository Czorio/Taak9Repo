package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.logic.TeamLogic;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 */
public class TeamLogicTest {

	File in = new File("GameData/Leagues.xml");
	XMLPlayer xmlplayer = new XMLPlayer(in);
	League league = xmlplayer.readFromFile("Eredivisie");
	
	Team ajax = league.getTeam("Ajax");
	List<Player> ajaxPlayers = ajax.getPlayers();
	
	Team testTeam = new Team ("Ajax", ajaxPlayers);
	
	@Before
	public void initialize() {
		TeamLogic.setSeed(1);
		TeamLogic.setTeamSetup("4 3 3");
		TeamLogic.setIsTesting(true);
	}
	
	@After
	public void end() {
		TeamLogic.clearPlayers();
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#TeamLogic()}.
	 */
	@Test
	public void testTeamLogic() {
		assertNotNull(new TeamLogic());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#calculateTeamOffScore(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCalculateTeamOffScore() {
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals(51, TeamLogic.calculateTeamOffScore(ajax));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#calculateTeamDefScore(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCalculateTeamDefScore() {
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals(58, TeamLogic.calculateTeamDefScore(ajax));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#calculateTeamStaminaScore(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCalculateTeamStaminaScore() {
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals(63, TeamLogic.calculateTeamStaminaScore(ajax));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#calculateTeamTotalScore(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCalculateTeamTotalScore() {
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals(172, TeamLogic.calculateTeamTotalScore(ajax));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#createActivePlayers(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCreateActivePlayers() {
		ajax.getPlayer(1053).setCurPosition("Goalkeeper");
		ajax.getPlayer(213).setCurPosition("Defender");
		ajax.getPlayer(9341).setCurPosition("Defender");
		ajax.getPlayer(11487).setCurPosition("Defender");
		ajax.getPlayer(12587).setCurPosition("Defender");
		ajax.getPlayer(339).setCurPosition("Midfielder");
		ajax.getPlayer(5121).setCurPosition("Midfielder");
		ajax.getPlayer(6145).setCurPosition("Midfielder");
		ajax.getPlayer(12483).setCurPosition("Midfielder");
		ajax.getPlayer(1580).setCurPosition("Attacker");
		ajax.getPlayer(12467).setCurPosition("Attacker");
		TeamLogic.createActivePlayers(ajax);
		
		assertEquals(TeamLogic.getPlayingPlayersPerTeam(ajax), TeamLogic.getPlayingPlayers());
	}
	
	/**
	 * Tests invalid amount of goalkeepers.
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#createAIActivePlayers(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCreateAIInvalidGK() {
		ajax.removePlayer(ajax.getPlayer(1053));
		ajax.removePlayer(ajax.getPlayer(2958));
		ajax.removePlayer(ajax.getPlayer(5646));
		
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals("Goalkeeper", TeamLogic.getTestPlayer().getCurPosition());
	}
	
	/**
	 * Tests invalid amount of defenders.
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#createAIActivePlayers(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCreateAIInvalidDef() {
		ajax.removePlayer(ajax.getPlayer(213));
		ajax.removePlayer(ajax.getPlayer(215));
		ajax.removePlayer(ajax.getPlayer(9341));
		ajax.removePlayer(ajax.getPlayer(11487));
		ajax.removePlayer(ajax.getPlayer(12047));
		ajax.removePlayer(ajax.getPlayer(12587));
		
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals("Defender", TeamLogic.getTestPlayer().getCurPosition());
	}
	
	/**
	 * Tests invalid amount of midfielders.
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#createAIActivePlayers(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCreateAIInvalidMid() {
		ajax.removePlayer(ajax.getPlayer(339));
		ajax.removePlayer(ajax.getPlayer(5121));
		ajax.removePlayer(ajax.getPlayer(6145));
		ajax.removePlayer(ajax.getPlayer(12048));
		ajax.removePlayer(ajax.getPlayer(12483));
		
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals("Midfielder", TeamLogic.getTestPlayer().getCurPosition());
	}

	/**
	 * Tests invalid amount of attackers.
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#createAIActivePlayers(nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testCreateAIInvalidAtt() {
		ajax.removePlayer(ajax.getPlayer(1398));
		ajax.removePlayer(ajax.getPlayer(1580));
		ajax.removePlayer(ajax.getPlayer(4639));
		ajax.removePlayer(ajax.getPlayer(7741));
		ajax.removePlayer(ajax.getPlayer(11481));
		ajax.removePlayer(ajax.getPlayer(12467));
		ajax.removePlayer(ajax.getPlayer(12607));
		ajax.removePlayer(ajax.getPlayer(12783));
		
		TeamLogic.createAIActivePlayers(ajax);
		assertEquals("Attacker", TeamLogic.getTestPlayer().getCurPosition());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#clearPlayers()}.
	 */
	@Test
	public void testClearPlayers() {
		TeamLogic.createAIActivePlayers(ajax);
		TeamLogic.clearPlayers();
		assertEquals(new ArrayList<Player>(), TeamLogic.getPlayingPlayers());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#getPlayingPlayers()}.
	 */
	@Test
	public void testGetPlayingPlayers() {
		TeamLogic.setPlayingPlayers(ajaxPlayers);
		assertEquals(ajaxPlayers, TeamLogic.getPlayingPlayers());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#getTeamSetup()}.
	 */
	@Test
	public void testGetTeamSetup() {
		assertEquals("4 3 3", TeamLogic.getTeamSetup());
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#getIsTesting()}.
	 */
	@Test
	public void testGetIsTesting() {
		assertEquals(true, TeamLogic.getIsTesting());
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.TeamLogic#getSeed()}.
	 */
	@Test
	public void testGetSeed() {
		assertEquals(1, TeamLogic.getSeed());
	}

}
