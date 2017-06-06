package nl.tudelft.footballmanager.development;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nl.tudelft.footballmanager.model.*;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

public class Test {

	public static void main(String[] args) {
		File in = new File("XML/GameState.xml");
		GameState gameState = new GameState(in);
		
		System.out.println(gameState.getCoachName());
		
		File out = new File("XML/out.xml");
		GameState.save(gameState, out);
		
		// XMLPlayer can read and write one or multiple leagues at once.
		// You can therefore input a single String or an ArrayList of Strings.				
		
		in = new File("XML/Players.xml");									// File to read data from.
		XMLPlayer xmlplayer = new XMLPlayer(in);							// Create XMLPlayer with input file.
		
		League league = xmlplayer.readFromFile("Eredivisie");				// Generate one specific League object using a single String with data from input file.
		
		ArrayList<String> leagueNames = new ArrayList<String>();			// Create an ArrayList of Strings
		leagueNames.add("Eredivisie");										// Add Eredivisie as a League name.
		ArrayList<League> leagues = xmlplayer.readFromFile(leagueNames);	// Generate possibly multiple League objects using a ArrayList of Strings.		
		
		out = new File("XML/PlayersOut.xml");								//
		File outArray = new File("XML/PlayersOutArray.xml");					//
		
		xmlplayer.setFile(out);												//
		xmlplayer.writeToFile(league);										//
		
		xmlplayer.setFile(outArray);										//
		xmlplayer.writeToFile(leagues);										//
		
		
		// The following should print to console the 2 players in the nl.tudelft.footballmanager.model.xml file.

		DateFormat df = new SimpleDateFormat("dd-MM-yyy");
		
		System.out.println(league.getName());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getFirstName());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getLastName());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getPosition());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getPace());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getShooting());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getPassing());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getOffensive());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getDefensive());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getStamina());
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).getClub());
		System.out.println(df.format(league.getTeam("Feyenoord").getPlayer(1).getDateOfBirth()));
		
		System.out.println();
		
		System.out.println(leagues.get(0).getName());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getFirstName());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getLastName());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getPosition());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getPace());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getShooting());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getPassing());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getOffensive());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getDefensive());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getStamina());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getClub());
		System.out.println(df.format(leagues.get(0).getTeam("Feyenoord").getPlayer(2).getDateOfBirth()));
		
		System.out.println();
		
		System.out.println(league.getTeam("Feyenoord").getPlayer(1).toString());
		System.out.println(leagues.get(0).getTeam("Feyenoord").getPlayer(2).toString());
		
	}

}