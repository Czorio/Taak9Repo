package nl.tudelft.footballmanager.model.xml;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.MatchScheme;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Boris Schrijver <boris@radialcontext.nl>
 */
public class XMLConfig extends XML {

	/**
	 * @param file
	 */
	public XMLConfig(File file) {
		super(file);
	}
	
	/**
	 * @param gameState The GameState to write to XML.
	 * @return true or false based on success.
	 */
	public boolean writeToFile(GameState gameState) {
		Document dom = null;
	    Element el = null;	    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    try {
	    	DocumentBuilder db = dbf.newDocumentBuilder();
	    	dom = db.newDocument();
	    	
	    	// Create the root element
	        Element rootEle = dom.createElement("GameState");
	    	
	        // Create data elements and place them under root
	        // Add coachName
	        el = dom.createElement("coachName");
	        el.appendChild(dom.createTextNode(gameState.getCoachName()));
	        rootEle.appendChild(el);

	        // Add currentRound
	        el = dom.createElement("round");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getGameRound())));
	        rootEle.appendChild(el);

	        // Add teamName
	        el = dom.createElement("myTeam");
	        el.appendChild(dom.createTextNode(gameState.getMyTeam().getName()));
	        rootEle.appendChild(el);
	        
	        // Add gamesWon
	        el = dom.createElement("gamesWon");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getMyTeam().getGamesWon())));
	        rootEle.appendChild(el);
	        
	        // Add gamesLost
	        el = dom.createElement("gamesLost");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getMyTeam().getGamesLost())));
	        rootEle.appendChild(el);
	        
	        // Add gamesDrawn
	        el = dom.createElement("gamesDrawn");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getMyTeam().getGamesDraw())));
	        rootEle.appendChild(el);
	        
	        // Add gamesPlayed
	        el = dom.createElement("gamesPlayed");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getMyTeam().getGamesPlayed())));
	        rootEle.appendChild(el);
	        
	        // Add balance
	        el = dom.createElement("balance");
	        el.appendChild(dom.createTextNode(Integer.toString(gameState.getMyTeam().getBudget())));
	        rootEle.appendChild(el);	        
	        
	        
	        // Add matchScheme
	        addGameScheme(dom, rootEle, gameState.getMatchScheme());
	        
	        // Add league containing all players
	        el = dom.createElement("players");
	        XMLPlayer.addLeagueToDom(dom, el, gameState.getLeague()); 
	        rootEle.appendChild(el);
	        
	        dom.appendChild(rootEle);
	             

        	Transformer tr = TransformerFactory.newInstance().newTransformer();
        	tr.setOutputProperty(OutputKeys.INDENT, "yes");
        	tr.setOutputProperty(OutputKeys.METHOD, "xml");
        	tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            // Write to file
            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));
	    	
	    } catch(ParserConfigurationException | TransformerException | IOException e) {
	    	//e.printStackTrace();
	    	System.err.println("Error parsing GameState file.");
	    }
		
		return true;
	}
	
	/**
	 * @param dom
	 * @param rootEle
	 */
	private void addGameScheme(Document dom, Element rootEle, MatchScheme matchScheme) {
		// Create a league element "League" with given name.
		Element eMatchScheme = dom.createElement("matchScheme");

		Element eMatchDay = null;
		Element eMatch = null;
		Element eMatchElement = null;

		for(int i = 0; i < matchScheme.getMatchdays().size(); i++) {
			eMatchDay = dom.createElement("matchDay");
			eMatchDay.setAttribute("round", Integer.toString(matchScheme.getMatchdays().get(i).getRound()));

			for(int j = 0; j < matchScheme.getMatchdays().get(i).getMatchCount(); j++) {
				eMatch = dom.createElement("match");
				eMatch.setAttribute("played", String.valueOf(matchScheme.getMatchdays().get(i).getMatches().get(j).getPlayed()));

				eMatchElement = dom.createElement("homeTeam");
				eMatchElement.appendChild(dom.createTextNode(matchScheme.getMatchdays().get(i).getMatches().get(j).getHome().getName()));
				eMatch.appendChild(eMatchElement);
				
				eMatchElement = dom.createElement("awayTeam");
				eMatchElement.appendChild(dom.createTextNode(matchScheme.getMatchdays().get(i).getMatches().get(j).getAway().getName()));
				eMatch.appendChild(eMatchElement);
				
				if(matchScheme.getMatchdays().get(i).getMatches().get(j).getPlayed()) {
					eMatchElement = dom.createElement("homeScore");
					eMatchElement.appendChild(dom.createTextNode(String.valueOf(matchScheme.getMatchdays().get(i).getMatches().get(j).getMatchResult().getHomeScore())));
					eMatch.appendChild(eMatchElement);
					
					eMatchElement = dom.createElement("awayScore");
					eMatchElement.appendChild(dom.createTextNode(String.valueOf(matchScheme.getMatchdays().get(i).getMatches().get(j).getMatchResult().getAwayScore())));
					eMatch.appendChild(eMatchElement);
				}
				
				eMatchDay.appendChild(eMatch);
			}
			
			eMatchScheme.appendChild(eMatchDay);
		}

		rootEle.appendChild(eMatchScheme);
	}
	
	/**
	 * @return GameState read from the document.
	 */
	public GameState readFromFile(){
		
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		
		League league = new XMLPlayer(file).readFromFile().get(0);
		
		try {
			// Setup the SAX XML Parser and assign the handler
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLConfigHandler handler = new XMLConfigHandler(league);
			
			// Parse the document
			saxParser.parse(file, handler);
			
			// Return the GameState object extracted from the XML
			return handler.getGameState();
			
			
		} catch(ParserConfigurationException | SAXException | IOException e) {
			//e.printStackTrace();
			System.err.println("Error reading GameState file.");
		}
		
		// Return empty GameState if non found.
		return new GameState(null, 0, (String)null, (String)null);
	}
	
	/**
	 * equals method.
	 */
	public boolean equals(Object other) {
		if(other instanceof XMLConfig) {
			XMLConfig that = (XMLConfig)other;
			
			return this.file.equals(that.file);
		}
		
		return false;
	}
}
