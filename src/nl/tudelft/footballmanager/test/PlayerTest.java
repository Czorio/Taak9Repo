package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;

import org.junit.*;

public class PlayerTest {

	Player CB = new Player(1,"Davy","Klaasen","Ajax","Eredivisie","Dutch", new Date(1/1/1990),"CB",40,50,60,60,80,70);
	Player RW = new Player(2,"Celso","Ortiz","AZ","Eredivisie","Dutch",new Date(1/1/1800),"RW",50,50,60,40,20,70);
	Player CB1 = new Player(1,"Davy","Klaasen","Ajax","Eredivisie","Dutch", new Date(1/1/1990),"CB",40,50,60,60,80,70);
	Player ST = new Player(3,"Mitchell","Shet","Ado Den Haag","Eredivisie","Dutch",new Date(2/2/2000),"ST",50,60,60,70,40,60);
	Player GK = new Player(5,"Jasper","Cillesen","Ajax","Eredivisie","Dutch", new Date(5/5/1950),"GK",0,0,0,0,70,70);
	Player CF = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"CF",40,40,40,40,40,40);
	Player RB = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"RB",40,40,40,40,40,40);
	Player LB = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"LB",40,40,40,40,40,40);
	Player CM = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"CM",40,40,40,40,40,40);
	Player CDM  = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"CDM",40,40,40,40,40,40);
	Player CAM  = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"CAM",40,40,40,40,40,40);
	Player LW = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"LW",40,40,40,40,40,40);
	Player RM = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"RM",40,40,40,40,40,40);
	Player LM = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"LM",40,40,40,40,40,40);
	Player LWB = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"LWB",40,40,40,40,40,40);
	Player RWB = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"RWB",40,40,40,40,40,40);
	Player Attacker = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"Attacker",40,40,40,40,40,40);
	Player Midfielder = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"Midfielder",40,40,40,40,40,40);
	Player Defender = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"Defender",40,40,40,40,40,40);
	Player Goalkeeper = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"Goalkeeper",40,40,40,40,40,40);
	Player NoPos = new Player(1,"Test","Test","Ajax","Eredivisie","Dutch", new Date(1/1/2015),"NO",40,40,40,40,40,40);
	
	Team empTeam = new Team("Ajax", new ArrayList<Player>());

	/**Constructor Test
	 * Tests the constructor using few get Methods and comparing them to actual value.
	 */
	@Test
	public void testPlayerIntegerStringStringStringStringDateStringIntIntIntIntIntInt() {
		//assertEquals(CB.getClub(),"Ajax");
		assertEquals(CB.getFirstName(),"Davy");
		assertEquals(RW.getLastName(),"Ortiz");
		assertEquals(CB.getNationality(),"Dutch");
		assertEquals(CB.getDateOfBirth(), new Date(1/1/1990));
		assertEquals(CB.getPosition(),"CB");
		assertEquals(CB.getPace(),40);
		assertEquals(CB.getShooting(),50);
		assertEquals(CB.getPassing(),60);
		assertEquals(CB.getOffensive(),60);
		assertEquals(CB.getDefensive(),80);
		assertEquals(CB.getStamina(),70);
		assertNotEquals(CB.getFirstName(),"Celso");
	}

	/**Player id nl.tudelft.footballmanager.test
	 * Create players with only an id. Use getId to nl.tudelft.footballmanager.test.
	 */
	@Test
	public void testPlayerInteger() {
		Player pl1 = new Player(1);
		Player pl2 = new Player(3);
		assertEquals(pl1.getId(),1);
		assertNotEquals(pl2.getId(),5);
	}

	/**Equals Test
	 * Compares 2 different player objects and checks if it is the or not(true or false).
	 */
	@Test
	public void testEqualsObject() {
		RW.setNationality(null);
		CF.setCurPosition(null);
		assertFalse(CB.equals(empTeam));
		assertEquals(false,CB.equals(CB1));
		assertEquals(false,CB.equals(ST));
		assertFalse(RW.equals(ST));
		assertFalse(CF.equals(ST));
		assertNotEquals(true,CB.equals(RW));
		CB.setClub("Ajax");
		CB1.setClub("Feyenoord");
		assertEquals(CB.equals(CB1),false);
		CB1.setFirstName("Dave");
		assertFalse(CB.equals(CB1));
		CB1.setFirstName("Davy");
		CB1.setLastName("Zien");
		CB1.setPosition("RW");
		assertFalse(CB.equals(CB1));
	}

	/**toString nl.tudelft.footballmanager.test
	 * Uses the toString method to nl.tudelft.footballmanager.test if 2 players are the same. 
	 */
	@Test
	public void testToString() {
		assertEquals(CB.toString(),CB1.toString());
		assertEquals(CB.toString(),("[1 Davy Klaasen: Ajax Dutch " + new Date(1/1/1990) +  " CB " +null + " 40 50 60 60 80 70 0 0 null 0]"));
		assertNotEquals(CB.toString(),ST.toString());
	}
	
	/**getPosition test
	 * Create new Players with all the positions. And test them using getPosition method.
	 */
	@Test
	public void testGetPosition(){
		assertEquals(CF.getPosition(),"CF");
		assertEquals(ST.getPosition(),"ST");
		assertEquals(RW.getPosition(),"RW");
		assertEquals(CM.getPosition(),"CM");
		assertEquals(CDM.getPosition(),"CDM");
		assertEquals(CAM.getPosition(),"CAM");
		assertEquals(LW.getPosition(),"LW");
		assertEquals(RM.getPosition(),"RM");
		assertEquals(LM.getPosition(),"LM");
		assertEquals(GK.getPosition(),"GK");
		assertEquals(RB.getPosition(),"RB");
		assertEquals(LB.getPosition(),"LB");
		assertEquals(CB.getPosition(),"CB");
	}
	
	/**getReadablePosition Test 
	 * 
	 */
	@Test
	public void testGetReadablePositionCF(){
		assertEquals(CF.getReadablePosition(),"Attacker");
		assertEquals(CAM.getReadablePosition(),"Midfielder");
		assertEquals(LW.getReadablePosition(),"Attacker");
		assertEquals(RM.getReadablePosition(),"Midfielder");
		assertEquals(LM.getReadablePosition(),"Midfielder");
		assertEquals(GK.getReadablePosition(),"Goalkeeper");
		assertEquals(RB.getReadablePosition(),"Defender");
		assertEquals(LB.getReadablePosition(),"Defender");
		assertEquals(CB.getReadablePosition(),"Defender");
		assertEquals(Attacker.getReadablePosition(),"Attacker");
		assertEquals(Midfielder.getReadablePosition(),"Midfielder");
		assertEquals(Defender.getReadablePosition(),"Defender");
		assertEquals(Goalkeeper.getReadablePosition(),"Goalkeeper");
		assertEquals(NoPos.getReadablePosition(),null);
	}
	
	@Test
	public void testGetReadablePositionST(){
		assertEquals("Attacker",ST.getReadablePosition());
	}
	
	@Test
	public void testGetReadablePositionRW(){
		assertEquals("Attacker",RW.getReadablePosition());
	}
	
	@Test
	public void testGetReadablePositionCM(){
		assertEquals("Midfielder",CM.getReadablePosition());
	}
	
	@Test
	public void testGetReadablePositionCDM(){
		assertEquals("Midfielder",CDM.getReadablePosition());
	}
	
	@Test
	public void testGetReadablePositionLWB(){
		assertEquals("Defender",LWB.getReadablePosition());
	}
	
	@Test
	public void testGetReadablePositionRWB(){
		assertEquals("Defender",RWB.getReadablePosition());
	}
	
	/**getCurPosision Test
	 * 
	 */
	@Test
	public void testGetCurPositionCF(){
		CF.setCurPosition("CF");
		assertEquals("CF",CF.getCurPosition());
	}
	
	@Test
	public void testGetCurPositionNULL(){
		assertEquals(null,ST.getCurPosition());
	}

	/**setId Test
	 * set a new id for a player and check it with getID method.
	 */
	@Test
	public void testSetID(){
		CB.setId(3);
		RW.setId(4);
		assertEquals(CB.getId(),3);
		assertNotEquals(RW.getId(),10);
	}

	/**setFirstName Test
	 * Use setFirstName method to rename player. Use getFirstName method to nl.tudelft.footballmanager.test.
	 */
	@Test
	public void testSetFirstName(){
		CB.setFirstName("Jan");
		RW.setFirstName("Klaas");
		assertEquals(CB.getFirstName(),"Jan");
		assertNotEquals(RW.getFirstName(),"Piet");
	}

	/**setLastName Test
	 * Use setLastName to set a new last name for player. Use getLastName and compare.
	 */
	@Test
	public void testSetLastName(){
		CB.setLastName("Jantje");
		RW.setLastName("Klaasje");
		assertEquals(CB.getLastName(),"Jantje");
		assertNotEquals(RW.getLastName(),"Pietje");
	}

	/**setNationality Test
	 * setNationality of players. Use getNationality to compare.
	 */
	@Test
	public void testSetNationality(){
		CB.setNationality("Surinamese");
		RW.setNationality("Surinamese");
		assertEquals(CB.getNationality(),"Surinamese");
		assertNotEquals(RW.getNationality(),"Dutch");
	}

	/**setDateOfBirth Test
	 * use setDateOfBirth to set new date of birth. 
	 */
	@Test
	public void testSetDateOfBirth(){
		CB.setDateOfBirth(new Date(1/1/2010));
		RW.setDateOfBirth(new Date(1/1/2020));
		assertEquals(CB.getDateOfBirth(),(new Date(1/1/2010)));
	}

	/**setPosition Test
	 * setPosition to set new position for player. 
	 */
	@Test
	public void testSetPosition(){
		CB.setPosition("CM");
		RW.setPosition("RM");
		assertEquals(CB.getPosition(),"CM");
		assertNotEquals(RW.getPosition(),"RW");
	}

	/**setPace
	 * Use Setpace to change player pace. 
	 */
	@Test
	public void testSetPace(){
		CB.setPace(80);
		RW.setPace(90);
		assertEquals(CB.getPace(),80);
		assertNotEquals(RW.getPace(),70);	
	}

	/**setShooting Test
	 * Setshooting of player. Compare with getShooting
	 */
	@Test
	public void testSetShooting(){
		CB.setShooting(80);
		RW.setShooting(60);
		assertEquals(CB.getShooting(),80);
		assertNotEquals(RW.getShooting(),100);
	}

	/**setPassing Test
	 * Setpassing to change passing of player. Compare with getPassing
	 */
	@Test
	public void testSetPassing(){
		CB.setPassing(80);
		RW.setPassing(70);
		assertEquals(CB.getPassing(),80);
		assertNotEquals(RW.getPassing(),0);
	}

	/**setOffensive Test
	 * use setoffensive to set offensive of player. Compare with setOffensive
	 */
	@Test
	public void testSetOffensive(){
		CB.setOffensive(80);
		RW.setOffensive(90);
		assertEquals(CB.getOffensive(),80);
		assertNotEquals(RW.getOffensive(),100);
	}
	
	/**setDefensive Test
	 * Use setDefensive to change defensive stat. Compare with getDefensive
	 */
	@Test
	public void testSetDefensive(){
		CB.setDefensive(80);
		RW.setDefensive(90);
		assertEquals(CB.getDefensive(),80);
		assertNotEquals(RW.getDefensive(),100);
	}
	
	/**setStamina Test
	 * setStamina for new player. Compare with getStamina
	 */
	@Test
	public void testSetStaming(){
		CB.setStamina(80);
		RW.setStamina(70);
		assertEquals(CB.getStamina(),80);
		assertNotEquals(RW.getStamina(),100);
	}
	
	/**setLeague Test
	 * setLeague for new player. Compare with getLeague
	 */
	@Test
	public void testSetLeague(){
		CB.setLeague("Premier League");
		RW.setLeague("La Liga");
		assertEquals(CB.getLeague(),"Premier League");
		assertNotEquals(RW.getLeague(),"Eredivisie");
	}
	
	/**
	 * testPrice 
	 */
	@Test
	public void testPrice(){
		CB.setPrice(100);
		assertEquals(CB.getPrice(),100);
	}
	
	/**
	 * testPlayedGames
	 */
	@Test
	public void testPlayedGames(){
		CB.setPlayedGames(10);
		assertEquals(CB.getPlayedGames(),10);
	}

}
