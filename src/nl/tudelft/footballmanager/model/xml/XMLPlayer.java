package nl.tudelft.footballmanager.model.xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

import nl.tudelft.footballmanager.model.League;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * @author Boris Schrijver
 *
 */
public class XMLPlayer extends XML {

	/**
	 * @param file
	 */
	public XMLPlayer(File file) {
		super(file);
	}

	/**
	 * write specific league to file.
	 * @param league
	 */
	public boolean writeToFile(League league) {
		Document dom;    
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();

			// Create the root element "Players"
			Element ePlayers = dom.createElement("Players");

			addLeagueToDom(dom, ePlayers, league);

			dom.appendChild(ePlayers);

			setTransformer(dom);

		} catch(ParserConfigurationException | TransformerException | IOException e) {
			//e.printStackTrace();
			System.err.println("Error writing players in GameState file.");
			return false;
		}
		
		return true;
	}

	/**
	 * write multiple leagues to file.
	 * @param league
	 */
	public boolean writeToFile(ArrayList<League> leagues) {
		Document dom;    
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();

			// Create the root element "Players"
			Element ePlayers = dom.createElement("Players");

			for(int i = 0; i < leagues.size(); i++) {
				addLeagueToDom(dom, ePlayers, leagues.get(i));
			}

			dom.appendChild(ePlayers);

			setTransformer(dom);

		} catch(ParserConfigurationException | TransformerException | IOException e) {
			//e.printStackTrace();
			System.err.println("Error writing players GameState file.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param dom
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	private void setTransformer(Document dom) throws TransformerException, FileNotFoundException{ 
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		// Write to file
		tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));
	}

	/**
	 * read specific league from file.
	 * @param league
	 * @return
	 */
	public League readFromFile(String league) {
		ArrayList<String> leagueArray = new ArrayList<String>();
		leagueArray.add(league);

		ArrayList<League> result = readFromFile(leagueArray);
		if (result.isEmpty()) return null;
		return result.get(0);
	}

	/**
	 * read multiple leagues from file.
	 * @param league
	 * @return
	 */
	public ArrayList<League> readFromFile(ArrayList<String> leagues) {

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

		try { 
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLPlayerHandler handler = new XMLPlayerHandler(leagues);

			// Parse the document
			saxParser.parse(file, handler);

			// Return the League objects extracted from the XML
			return handler.getLeagueObjects();

		} catch(ParserConfigurationException | SAXException | IOException e) {
			//e.printStackTrace();
			System.err.println("Error reading GameState file.");
		}

		// Return empty ArrayList if non found.
		return new ArrayList<League>();
	}

	public ArrayList<League> readFromFile() {
		return readFromFile((ArrayList<String>)null);
	}

	static protected void addLeagueToDom(Document dom, Element ePlayers, League league) {
		// Create a league element "League" with given name.
		Element eLeague = dom.createElement("League");
		eLeague.setAttribute("Name", league.getName());

		Element eTeam = null;
		Element ePlayer = null;
		Element ePlayerElement = null;

		DateFormat df = new SimpleDateFormat("dd-MM-yyy");

		for(int i = 0; i < league.getTeams().size(); i++) {
			eTeam = dom.createElement("Team");
			eTeam.setAttribute("Name", league.getTeams().get(i).getName());

			for(int j = 0; j < league.getTeams().get(i).getPlayers().size(); j++) {
				ePlayer = dom.createElement("Player");
				ePlayer.setAttribute("id", String.valueOf(league.getTeams().get(i).getPlayers().get(j).getId()));

				ePlayerElement = dom.createElement("FirstName");
				ePlayerElement.appendChild(dom.createTextNode(league.getTeams().get(i).getPlayers().get(j).getFirstName()));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("LastName");
				ePlayerElement.appendChild(dom.createTextNode(league.getTeams().get(i).getPlayers().get(j).getLastName()));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Position");
				ePlayerElement.appendChild(dom.createTextNode(league.getTeams().get(i).getPlayers().get(j).getPosition()));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Pace");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getPace())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Shooting");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getShooting())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Passing");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getPassing())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Offensive");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getOffensive())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Defensive");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getDefensive())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Stamina");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getStamina())));
				ePlayer.appendChild(ePlayerElement);

				ePlayerElement = dom.createElement("Club");
				ePlayerElement.appendChild(dom.createTextNode(league.getTeams().get(i).getPlayers().get(j).getClub()));
				ePlayer.appendChild(ePlayerElement);	        		

				ePlayerElement = dom.createElement("DateOfBirth");
				ePlayerElement.appendChild(dom.createTextNode(df.format(league.getTeams().get(i).getPlayers().get(j).getDateOfBirth())));
				ePlayer.appendChild(ePlayerElement);
				
				ePlayerElement = dom.createElement("Injured");
				ePlayerElement.appendChild(dom.createTextNode(String.valueOf(league.getTeams().get(i).getPlayers().get(j).getDisabledFor())));
				ePlayer.appendChild(ePlayerElement);
				
				if ( league.getTeams().get(i).getPlayers().get(j).getCurPosition() == null ) {
					ePlayerElement = dom.createElement("CurrentPosition");
					ePlayerElement.appendChild(dom.createTextNode( "none" ));
					ePlayer.appendChild(ePlayerElement);
				}
				else {
					ePlayerElement = dom.createElement("CurrentPosition");
					ePlayerElement.appendChild(dom.createTextNode(league.getTeams().get(i).getPlayers().get(j).getCurPosition()));
					ePlayer.appendChild(ePlayerElement);
				}				
				
				
				eTeam.appendChild(ePlayer);
			}

			eLeague.appendChild(eTeam);
		}

		ePlayers.appendChild(eLeague);
	}

	/**
	 * equals method.
	 */
	public boolean equals(Object other) {
		if(other instanceof XMLPlayer) {
			XMLPlayer that = (XMLPlayer)other;

			return this.file.equals(that.file);
		}

		return false;		
	}

}
