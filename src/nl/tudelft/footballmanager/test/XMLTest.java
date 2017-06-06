package nl.tudelft.footballmanager.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.xml.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

public class XMLTest {
	XMLConfig xmlConfig;
	XMLPlayer xmlPlayer;
	File file1 = new File( "./XML/XMLTest1.xml" );
	File file2 = new File( "./XML/XMLTest2.xml" );
	File singlePlayer = new File( "./XML/XMLTest_SinglePlayer.xml" );
	AttributesImpl attributes = new AttributesImpl();
	AttributesImpl attributesNull = new AttributesImpl();
		
	@Before
	public void setUp() {
		//if( !file1.exists() || !file2.exists() ) {
		//	System.out.println( "File not found, stopping tests." );
		//	System.exit(0);
		//}
		
		// Create XMLConfig
		xmlConfig = new XMLConfig(file1);
		
		
		// Create some Attributes for the getValueIgnoreCase function
		attributes.addAttribute("", "", "Test01", "", "Test01");
		attributes.addAttribute("", "", "Test02", "", "Test02");
	}
	
	@Test
	public void testXML() {
		assertNotNull( xmlConfig );
		assertTrue( xmlConfig.getClass().getName().equals( "nl.tudelft.footballmanager.model.xml.XMLConfig" ) );
	}

	@Test
	public void testGetFile() {
		assertTrue( xmlConfig.getFile().equals( file1 ) );
		assertFalse( xmlConfig.getFile().equals( file2 ) );
	}

	@Test
	public void testSetFile() {
		xmlConfig.setFile( file2 );
		assertFalse( xmlConfig.getFile().equals( file1 ) );
		assertTrue( xmlConfig.getFile().equals( file2 ) );
	}

	@Test
	public void testGetValueIgnoreCase() {
		assertTrue( XML.getValueIgnoreCase(attributes, "Test01").equals( "Test01" ) );
		assertTrue( XML.getValueIgnoreCase(attributes, "TEST01").equals( "Test01" ) );
		assertFalse( XML.getValueIgnoreCase(attributes, "Test01").equals( "Test02" ) );
		assertFalse( XML.getValueIgnoreCase(attributes, "TEST01").equals( "Test02" ) );
		assertTrue( XML.getValueIgnoreCase(attributes, "Test02").equals( "Test02" ) );
		assertTrue( XML.getValueIgnoreCase(attributes, "TEST02").equals( "Test02" ) );
		assertFalse( XML.getValueIgnoreCase(attributes, "Test02").equals( "Test01" ) );
		assertFalse( XML.getValueIgnoreCase(attributes, "TEST02").equals( "Test01" ) );
		assertNull( XML.getValueIgnoreCase(attributes, "Test03" ) );
		assertNull( XML.getValueIgnoreCase(attributesNull, "Test01" ) );
	}
	
	@Test
	public void testXMLPlayer() {
		assertNotNull( xmlConfig );
		assertTrue( xmlConfig.getClass().getName().equals( "nl.tudelft.footballmanager.model.xml.XMLConfig" ) );
	}
	
	@Test
	public void testReadPlayer() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		League test = xmlPlayer.readFromFile("Eredivisie");
		assertTrue( test.getName().equals("Eredivisie"));
	}
	
	@Test
	public void testReadPlayerNotExisitingFile() {
		xmlPlayer = new XMLPlayer(new File("notexisitngfile.xml"));
		League test = xmlPlayer.readFromFile("Eredivisie");
		assertNull( test );
	}
	
	@Test
	public void testReadPlayerEmpty() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		League test = xmlPlayer.readFromFile("NonExisitingLeague");
		assertNull( test );
	}
	
	@Test
	public void testReadPlayerAll() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		ArrayList<League> test = xmlPlayer.readFromFile();
		assertTrue( test.get(0).getName().equals("Eredivisie"));
	}
	
	@Test
	public void testXMLPlayerEquals() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		XMLPlayer xmlPlayer2 = new XMLPlayer(singlePlayer);
		assertTrue( xmlPlayer.equals(xmlPlayer2));
	}
	
	@Test
	public void testXMLPlayerEqualsNot() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		assertFalse( xmlPlayer.equals("Not A XMLPlayer Object"));
	}
	
	@Test
	public void testWriteToFileAll() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		ArrayList<League> league = xmlPlayer.readFromFile();
		
		XMLPlayer write = new XMLPlayer(new File("./XML/XMLTest_Write.xml"));
		assertTrue( write.writeToFile(league) );
	}
	
	@Test
	public void testWriteToFileLeague() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		League league = xmlPlayer.readFromFile("Eredivisie");
		
		XMLPlayer write = new XMLPlayer(new File("./XML/XMLTest_Write.xml"));
		assertTrue( write.writeToFile(league) );		
	}
	
	@Test
	public void testWriteToFileLeagueCurPositionNull() {
		xmlPlayer = new XMLPlayer(singlePlayer);
		League league = xmlPlayer.readFromFile("Eredivisie");
		league.getTeams().get(0).getPlayers().get(0).setCurPosition(null);
		
		XMLPlayer write = new XMLPlayer(new File("./XML/XMLTest_Write.xml"));
		assertTrue( write.writeToFile(league) );
	}
	
	@Test
	public void testReadEmptyFile() {
		xmlPlayer = new XMLPlayer(new File("./XML/XMLTest_Empty.xml"));
		League league = xmlPlayer.readFromFile("Eredivisie");
		
		assertNull( league );
		
	}
	
	@Test
	public void testReadGameState() {
		xmlConfig= new XMLConfig(file1);
		GameState gs = xmlConfig.readFromFile();
		assertTrue( gs.getCoachName().equals("Boris Schrijver") );
	}
	
	@Test
	public void testXMLConfigEquals() {
		xmlConfig= new XMLConfig(file1);
		XMLConfig xmlConfig2= new XMLConfig(file1);
		assertTrue( xmlConfig.equals(xmlConfig2) );
	}
	
	@Test
	public void testXMLConfigEqualsNot() {
		xmlConfig = new XMLConfig(file1);
		assertFalse( xmlConfig.equals("Not A XMLConfig Object"));
	}
	
	@Test
	public void testWriteGsToFile() {
		xmlConfig = new XMLConfig(file1);
		GameState gs = xmlConfig.readFromFile(); 
		
		XMLConfig xmlConfig = new XMLConfig(new File("./XML/XMLTest_GSWrite.xml"));
		assertTrue( xmlConfig.writeToFile(gs) );
	}
	
	@Test
	public void testUnkownPlayerField() {
		xmlPlayer = new XMLPlayer(new File("./XML/XMLTest3_wrongplayerField.xml"));
		League league = xmlPlayer.readFromFile("Eredivisie");
		assertNull(league);
	}
	
	@Test
	public void testTeamDoesntExist() {
		xmlConfig = new XMLConfig( new File("./XML/XMLTest4.xml"));
		assertTrue(xmlConfig.getFile().exists());
		GameState gs = xmlConfig.readFromFile(); 
		assertNotNull(gs);
	}
	
	@Test
	public void testMultipleLeagues() {
		xmlPlayer = new XMLPlayer(new File("./GameDate/Leagues.xml"));
		ArrayList<League> leagues = xmlPlayer.readFromFile();
		assertNotNull(leagues);
	}
	
	@Test
	public void testMultipleLeaguesSharedNames() {
		xmlPlayer = new XMLPlayer(new File("./XML/LeaguesSharedNames.xml"));
		ArrayList<League> leagues = xmlPlayer.readFromFile();
		assertNotNull(leagues);
	}
	

}
