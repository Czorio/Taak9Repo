/**
 * 
 */
package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.logic.MarketplaceLogic;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.junit.Test;

/**
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 */
public class MarketplaceLogicTest {

	File in = new File("GameData/Leagues.xml");
	XMLPlayer xmlplayer = new XMLPlayer(in);
	League league = xmlplayer.readFromFile("Eredivisie");
	
	Team ajax = league.getTeam("Ajax");
	List<Player> ajaxPlayers = ajax.getPlayers();
	Team tAjax = new Team ("Ajax", ajaxPlayers);
	
	Team feyenoord = league.getTeam("Feyenoord");
	List<Player> feyenoordPlayers = feyenoord.getPlayers();
	Team tFeyenoord = new Team ("Feyenoord", feyenoordPlayers);
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#transferPlayer(nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Player, int)}.
	 */
	@Test
	public void testTransferPlayerNotOpen() {
		assertFalse(MarketplaceLogic.transferPlayer(tAjax, tFeyenoord, tAjax.getPlayer(1580), 6));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#transferPlayer(nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Player, int)}.
	 */
	@Test
	public void testTransferPlayerNoCash() {
		tFeyenoord.setBudget(1337);
		assertFalse(MarketplaceLogic.transferPlayer(tAjax, tFeyenoord, tAjax.getPlayer(1580), 2));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#transferPlayer(nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Player, int)}.
	 */
	@Test
	public void testTransferPlayerNoPlayers() {
		List<Player> players = new ArrayList<Player>();
		tAjax.setPlayers(players);
		tAjax.addPlayer(ajax.getPlayer(213));
		tAjax.addPlayer(ajax.getPlayer(215));
		
		assertFalse(MarketplaceLogic.transferPlayer(tAjax, tFeyenoord, tAjax.getPlayer(215), 2));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#transferPlayer(nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Player, int)}.
	 */
	@Test
	public void testTransferPlayerNonEx() {
		tFeyenoord.setBudget(13371337);
		assertFalse(MarketplaceLogic.transferPlayer(tAjax, tFeyenoord, tFeyenoord.getPlayer(466), 2));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#transferPlayer(nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Team, nl.tudelft.footballmanager.model.Player, int)}.
	 */
	@Test
	public void testTransferPlayer() {
		tFeyenoord.setBudget(13371337);
		assertTrue(MarketplaceLogic.transferPlayer(tAjax, tFeyenoord, tAjax.getPlayer(215), 2));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#RandomPlayerTransfer(int, nl.tudelft.footballmanager.model.League, nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testRandomPlayerTransferClosed() {
		assertFalse(MarketplaceLogic.RandomPlayerTransfer(6, league, ajax));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#RandomPlayerTransfer(int, nl.tudelft.footballmanager.model.League, nl.tudelft.footballmanager.model.Team)}.
	 */
	@Test
	public void testRandomPlayerTransferIf1() {
		assertTrue(MarketplaceLogic.RandomPlayerTransfer(2, league, ajax));
	}

	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#isTransferWindow(int)}.
	 */
	@Test
	public void testIsTransferWindowOpen1() {
		assertTrue(MarketplaceLogic.isTransferWindow(1));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#isTransferWindow(int)}.
	 */
	@Test
	public void testIsTransferWindowOpen2() {
		assertTrue(MarketplaceLogic.isTransferWindow(19));
	}
	
	/**
	 * Test method for {@link nl.tudelft.footballmanager.model.logic.MarketplaceLogic#isTransferWindow(int)}.
	 */
	@Test
	public void testIsTransferWindowClosed() {
		assertFalse(MarketplaceLogic.isTransferWindow(5));
	}

}
