package nl.tudelft.footballmanager.model.xml;
import java.util.Arrays;

import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchDay;
import nl.tudelft.footballmanager.model.MatchResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Boris Schrijver <boris@radialcontext.nl>
 */
public class XMLConfigHandler extends DefaultHandler {

	private GameState gameState;
	
	private int currentElement;
	
	private int currentRound;
	
	private Match currentMatch = null;	
	private League league = null;
	private MatchDay currentMatchDay = null;
	
	
	/**
	 * Constructor
	 * Read the league first, it is 
	 * @param league
	 */
	public XMLConfigHandler(League league) {
		this.league = league;
	}
	
	/**
	 * @return The GameState object.
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		String[] knownElements = new String[] {"PLAYERS", "LEAGUE", "TEAM", "PLAYER", "FIRSTNAME",
				"LASTNAME", "POSITION", "PACE", "SHOOTING", "PASSING", "OFFENSIVE", "DEFENSIVE",
				"STAMINA", "CLUB", "DATEOFBIRTH", "INJURED", "CURRENTPOSITION"};
		
		String[] configElements = new String[] {"GAMESTATE", "COACHNAME",
				"ROUND", "MYTEAM", "GAMESWON", "GAMESLOST", "GAMESDRAWN", "GAMESPLAYED", "BALANCE", "MATCHSCHEME",
				"MATCHDAY", "MATCH", "HOMETEAM", "AWAYTEAM", "HOMESCORE", "AWAYSCORE"};
		
		if(!Arrays.asList(knownElements).contains(qName.toUpperCase()) && !Arrays.asList(configElements).contains(qName.toUpperCase())) {
			System.err.println("XML: Unkown element in GameState XML file. --> startElement: " + qName);
			return;
		}
		
		if(Arrays.asList(knownElements).contains(qName.toUpperCase()))
			return;
		
		if(Arrays.asList(configElements).contains(qName.toUpperCase()))
			currentElement = Arrays.asList(configElements).indexOf(qName.toUpperCase()) + 1;
		
		if(currentElement == 1)
			gameState = new GameState(null, 0, (String)null, (String)null);
		
		if(currentElement == 11)
			currentRound = Integer.parseInt(XML.getValueIgnoreCase(attributes, "ROUND"));
		
		/**
		switch(qName.toUpperCase()) {
		case "GAMESTATE":
			// Initialize GameState object if null
			gameState = new GameState(null, 0, (String)null, (String)null);
			currentElement = 1;
			break;
			
		case "COACHNAME":
			currentElement = 2;
			break;
			
		case "ROUND":
			currentElement = 3;
			break;
			
		case "MYTEAM":
			currentElement = 4;
			break;
			
		case "GAMESWON":
			currentElement = 5;
			break;
			
		case "GAMESLOST":
			currentElement = 6;
			break;
			
		case "GAMESDRAWN":
			currentElement = 7;
			break;
			
		case "GAMESPLAYED":
			currentElement = 8;
			break;
			
		case "BALANCE":
			currentElement = 9;
			break;
			
		case "MATCHSCHEME":
			currentElement = 10;
			break;
		
		case "MATCHDAY":
			currentElement = 11;
			currentRound = Integer.parseInt(XML.getValueIgnoreCase(attributes, "ROUND"));
			break;
			
		case "MATCH":
			currentElement = 12;
			break;
			
		case "HOMETEAM":
			currentElement = 13;
			break;
			
		case "AWAYTEAM":
			currentElement = 14;
			break;
			
		case "HOMESCORE":
			currentElement = 15;
			break;
			
		case "AWAYSCORE":
			currentElement = 16;
			break;
		}		**/
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		
		if(currentElement != 0 && currentElement != 10) {
			switch(currentElement) {
			case 1:
				// Add the players at first.
				gameState.setLeague(league);
				break;
				
			case 2:
				gameState.setCoachName(new String(ch, start, length));
				break;
				
			case 3:
				gameState.setGameRound(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 4:
				try {
					gameState.setMyTeam(new String(ch, start, length));
				} catch (Exception e) {
					System.out.println("Unable to set MyTeam, it doesn't exist in the League.");
					throw new SAXException("Couldn't set team.");
				}
				break;
				
			case 5:
				gameState.getMyTeam().setGamesWon(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 6:
				gameState.getMyTeam().setGamesLost(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 7:
				gameState.getMyTeam().setGamesDraw(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 8:
				gameState.getMyTeam().setGamesPlayed(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 9:
				gameState.getMyTeam().setBudget(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 11:
				// Adds the matchday
				currentMatchDay = new MatchDay(currentRound);			
				break;
				
			case 12:
				// Create new match
				currentMatch = new Match(null, null);
				break;
				
			case 13:
				currentMatch.setHome(league.getTeam(new String(ch, start, length)));
				break;
				
			case 14:
				currentMatch.setAway(league.getTeam(new String(ch, start, length)));
				break;
				
			case 15:
				currentMatch.setPlayed(true);
				currentMatch.setMatchResult(new MatchResult());
				currentMatch.getMatchResult().setHomeScore(new Integer(new String(ch, start, length)).intValue());
				break;
				
			case 16:
				currentMatch.getMatchResult().setAwayScore(new Integer(new String(ch, start, length)).intValue());
				break;
				
			//default:
			//	System.err.println("XML: Unkown element in GameState XML file. --> characters:" + new String(ch, start, length));
			//	break;
			}
			
			// currentElement will be set by the next call of startElement().
			currentElement = 0;		
		}		
	}
	
	public void endElement(String uri, String localName, String qName) { 
		switch(qName.toUpperCase()) {
			
		case "MATCHDAY":
			gameState.getMatchScheme().addMatchDay(currentMatchDay);
			currentMatchDay = null;
			break;
			
		case "MATCH":
			currentMatchDay.addMatch(currentMatch);
			currentMatch = null;
			break;
		
		default:
			break;
		}
	}
}
