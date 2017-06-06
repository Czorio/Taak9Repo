package nl.tudelft.footballmanager.development;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.xml.XMLPlayer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Boris Schrijver <boris@radialcontext.nl>
 * 
 * Gets all the players and leagues from https://www.easports.com/uk/fifa/ultimate-team/api/fut/item?jsonParamObject={"page":1}
 */
public class XMLWebImport {
	private final static String baseUrlBegin = "https://www.easports.com/uk/fifa/ultimate-team/api/fut/item?jsonParamObject={\"page\":";
	private final static String baseUrlEnd = "}";

	// Contains all the Leagues
	private static ArrayList<League> Leagues = new ArrayList<League>();

	// Start ID
	private static int playerId = 1;

	// Specify if the JSON files need to be downloaded
	private static boolean bDownloadJSON = false;

	public static void main(String[] args) {
		// Number of pages 
		try {
			System.out.println(numberOfPages());
			parseJSONData(numberOfPages());
		} catch (IOException | ParseException | java.text.ParseException e) {
			e.printStackTrace();
		}

		XMLPlayer xmlPlayer = new XMLPlayer(new File("GameData/Leagues.xml"));
		xmlPlayer.writeToFile(Leagues);

		for(int i = 0; i < Leagues.size(); i++) {
			System.out.println("Beginning: " + Leagues.get(i).getName());

			xmlPlayer.setFile(new File("GameData/Leagues/" + Leagues.get(i).getName() + ".xml"));
			xmlPlayer.writeToFile(Leagues.get(i));

			System.out.println("Ended: " + Leagues.get(i).getName());
		}
	}

	/**
	 * @return (INT) The number of pages to download.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static int numberOfPages() throws IOException, ParseException {
		// Read from the first page.
		String url = baseUrlBegin + 1 + baseUrlEnd;

		JSONObject jsonObject = parseToJSONObject(grabJSON(url));

		return ((Long) jsonObject.get("totalPages")).intValue();
	}

	/**
	 * Grab pages and parse. HARD. 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws java.text.ParseException 
	 */
	private static void parseJSONData(int numberOfPages) throws IOException, ParseException, java.text.ParseException {		
		JSONObject jsonPageObject;
		JSONArray jsonPageArray;
		FileWriter fstream;
		BufferedWriter out;
		BufferedReader in;
		String line;

		// Download JSON to files.
		if(bDownloadJSON) {
			// Get all files from the internet
			for (int i = 1; i <= numberOfPages; i++) {
				System.out.println("Start page download: " + i);			

				fstream = new FileWriter("GameData/JSON/" + i + ".json");
				out = new BufferedWriter(fstream);
				in = grabJSON(baseUrlBegin + i + baseUrlEnd);

				while((line = in.readLine()) != null) {
					out.write(line);
				}
				out.close();
			}			
		}

		/**
		 * Files need to be in the GameData/JSON folder.
		 * For each page, retrieve players and parse.
		 */
		File dir = new File("GameData/JSON/");

		for(File jsonFile : dir.listFiles()) {
			System.out.println("Start file: " + jsonFile.getName());

			jsonPageObject = parseToJSONObject(new BufferedReader(new FileReader(jsonFile)));

			// All players on this page.
			jsonPageArray = (JSONArray) jsonPageObject.get("items");

			// Parse each individual player
			for(int j = 0; j < jsonPageArray.size(); j++) {
				addPlayer(parseJSONPlayer((JSONObject) jsonPageArray.get(j)));				
			}

			System.out.println("End file: " + jsonFile.getName());
		}		
	}

	/**
	 * Add a player to the leagues ArrayList.
	 * @param player
	 */
	private static void addPlayer(Player player) {
		// Create league and team accordingly.
		boolean bExistingLeague = false;
		boolean bExistingTeam = false;

		// Search all Leagues for equal names.
		for(int i = 0; i < Leagues.size(); i++) {

			// If an equal name is found, enter that league
			if(Leagues.get(i).getName().equals(player.getLeague())) {
				bExistingLeague = true;

				// Search all teams in this specific league for equal names.
				for(int j = 0; j < Leagues.get(i).getTeams().size(); j++) {

					// If found add player.
					if(Leagues.get(i).getTeams().get(j).getName().equals(player.getClub())) {
						bExistingTeam = true;
						Leagues.get(i).getTeams().get(j).addPlayer(player);
					}
				}
			}
		}

		// No league exists with this name.
		if (!bExistingLeague) {
			Team team = new Team(player.getClub());
			League league = new League(player.getLeague());
			team.addPlayer(player);
			league.addTeam(team);
			Leagues.add(league);
		}
		// League exists but no team.
		else if(bExistingLeague && !bExistingTeam) {
			Team team = new Team(player.getClub());
			team.addPlayer(player);
			for(int i = 0; i < Leagues.size(); i++) {
				if(Leagues.get(i).getName().equals(player.getLeague())) {
					Leagues.get(i).getTeams().add(team);
				}
			}
		}

		if(Leagues.get(0).getTeams().size() > 20) {
			System.out.println("ERRORRR");
		}
	}

	/**
	 * Parse a player from a JSONObject
	 * @param playerJSONObject
	 * @return Player with given data
	 * @throws java.text.ParseException 
	 */
	private static Player parseJSONPlayer(JSONObject playerJSONObject) throws java.text.ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		int id = playerId++;
		String firstName;
		String lastName;
		String club;
		String league;
		String nationality;
		Date dateOfBirth = null;
		String position;
		int pace = 0;
		int shooting = 0;
		int passing = 0;
		int offensive = 0;
		int defensive = 0;
		int stamina = 0;

		// First Name
		firstName = (String) playerJSONObject.get("firstName");

		// Last Name
		lastName = (String) playerJSONObject.get("lastName");

		// Club
		JSONObject tmpClubJsonObject = (JSONObject) playerJSONObject.get("club");
		club = (String) tmpClubJsonObject.get("name");

		// League
		JSONObject tmpLeagueJsonObject = (JSONObject) playerJSONObject.get("league");
		league = (String) tmpLeagueJsonObject.get("name");

		// Nationality
		JSONObject tmpNationJsonObject = (JSONObject) playerJSONObject.get("nation");
		nationality = (String) tmpNationJsonObject.get("name");

		// Date of Birth		
		dateOfBirth = format.parse((String) playerJSONObject.get("birthdate"));

		// Position
		position = (String) playerJSONObject.get("position");

		JSONArray tmpAttributesJsonArray = (JSONArray) playerJSONObject.get("attributes");
		for(int i = 0; i < tmpAttributesJsonArray.size(); i++) {
			switch ((String) ((JSONObject) tmpAttributesJsonArray.get(i)).get("name")) {

			// Pace
			case "fut.attribute.PAC":
				pace = ((Long) ((JSONObject) tmpAttributesJsonArray.get(i)).get("value")).intValue();
				break;

				// Shooting
			case "fut.attribute.SHO":
				shooting = ((Long) ((JSONObject) tmpAttributesJsonArray.get(i)).get("value")).intValue();
				break;

				// Passing
			case "fut.attribute.PAS":				
				passing = ((Long) ((JSONObject) tmpAttributesJsonArray.get(i)).get("value")).intValue();
				break;

				// Defensive
			case "fut.attribute.DEF":				
				defensive = ((Long) ((JSONObject) tmpAttributesJsonArray.get(i)).get("value")).intValue();
				break;

			default:
				break;
			}
		}

		// Stamina
		stamina = ((Long) playerJSONObject.get("stamina")).intValue();

		// Offensive
		offensive = (shooting + passing)/2;

		return new Player(id, firstName, lastName, club, league, nationality, dateOfBirth, position, pace, shooting, passing, offensive, defensive, stamina);		
	}

	/**
	 * Grabs the JSON from the specified URL and returns a BufferedReader with the response.
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	private static BufferedReader grabJSON(String url) throws IOException {
		while(true) {
			// Try connection and return
			// The URL
			URL obj = new URL(url);

			// The connection
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// The HTTP Request method and User-Agent
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			// Response code
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				// Return the BufferedReader
				return new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
		}		
	}

	/**
	 * Parse BufferedReader to JSONObject
	 * @param bufferedReader
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private static JSONObject parseToJSONObject(BufferedReader bufferedReader) throws IOException, ParseException {
		// The parser
		JSONParser parser = new JSONParser();

		// Try to parse
		return (JSONObject) parser.parse(bufferedReader);	
	}
}
