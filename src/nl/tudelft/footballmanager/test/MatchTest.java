package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchResult;
import nl.tudelft.footballmanager.model.Team;

import org.junit.Test;

public class MatchTest {

	Team t1 = new Team("Ajax");
	Team t2 = new Team("Feyenoord");
	Team t3 = new Team("Ado Den Haag");
	Team t4 = new Team("AZ Alkmaar");
	Match m1 = new Match(t1,t2);
	Match m2 = new Match(t1,t4);
	
	MatchResult testResult = new MatchResult();
	
	/**Constructor Match nl.tudelft.footballmanager.test
	 * Test the constructor with getters.
	 */
	@Test
	public void testMatch() {
		assertEquals(m1.getHome(),t1);
		assertEquals(m1.getAway(),t2);
		assertNotEquals(m1.getHome(),t2);
		assertNotEquals(m1.getAway(),t3);
	}

	/**setHome Test
	 * Use sethome method to set new home team. Use getHome to compare and nl.tudelft.footballmanager.test.
	 */
	@Test
	public void testSetHome() {
		m1.setHome(t4);
		m2.setHome(t2);
		assertEquals(m2.getHome(),t2);
		assertEquals(m1.getHome(),t4);
		assertNotEquals(m2.getHome(),t3);
	}

	/**setAway Test
	 * Use setAway method to set new away team. Use getAway to compare and nl.tudelft.footballmanager.test.
	 */
	@Test
	public void testSetAway() {
		m1.setAway(t2);
		m2.setAway(t3);
		assertEquals(m1.getAway(),t2);
		assertEquals(m2.getAway(),t3);
		assertNotEquals(m1.getAway(),t1);
		assertNotEquals(m1.getAway(),t4);		
	}
	
	/**setRound Test nl.tudelft.footballmanager.test.
	 * 
	 */
	@Test
	public void testSetRound(){
		m1.setRound(4);
		m2.setRound(7);
		assertEquals(m1.getRound(),4);
		assertNotEquals(m2.getRound(),10);
	}
	
	/**toString Test nl.tudelft.footballmanager.test.
	 * 
	 */
	@Test
	public void testToString(){
		assertEquals(m1.toString(),m1.toString());
		assertEquals(m1.toString(),"Match [home=Ajax, away=Feyenoord, round=0]");
	}
	
	@Test
	public void testSetMatchResult(){
		testResult.setHomeScore(2);
		testResult.setAwayScore(3);
		m1.setMatchResult(testResult);
		assertEquals(m1.getMatchResult().getAwayScore(),3);
	}

}
