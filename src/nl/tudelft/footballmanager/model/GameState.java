package nl.tudelft.footballmanager.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Observable;

import javafx.collections.FXCollections;
import nl.tudelft.footballmanager.model.xml.XMLConfig;

/**
 * Handles the current state of the game.
 * @author Boris Schrijver <boris@radialcontext.nl>
 */
public class GameState extends Observable {
	private String coachName = null;
	private int gameRound = -1;

	private League league = null;
	private Team myTeam = null;
	private MatchScheme matchScheme = null;

	/**
	 * Creates and initializes a Gamestate with actual League and Team.
	 * 
	 * @param coachName The name of the coach.
	 * @param gameRound The current gameround.
	 * @param league The current league.
	 * @param myTeam The team the user is playing.
	 */
	public GameState(String coachName, int gameRound, League league, Team myTeam) {
		this.coachName = coachName;
		this.gameRound = gameRound;
		this.league = league;
		this.myTeam = myTeam;

		this.matchScheme = new MatchScheme(this.league, 0);

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Creates and initializes a Gamestate with Lague and Team as String.
	 * 
	 * @param coachName The name of the coach.
	 * @param gameRound The current gameround.
	 * @param league The current league.
	 * @param myTeam The team the user is playing.
	 */
	public GameState(String coachName, int gameRound, String leagueName, String myTeamName) {
		this.coachName = coachName;
		this.gameRound = gameRound;
		this.league = null;

		if (leagueName != null) {
			try {
				this.league = League.readOne(leagueName);
				if (myTeamName != null) {
					this.myTeam = this.league.getTeam(myTeamName);
				}
			} catch (FileNotFoundException e) {
				this.league = null;
			}
		}

		this.matchScheme = new MatchScheme(this.league, 0);

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Creates a gamestate from a file.
	 * 
	 * @param file XML file to read config from.
	 */
	public GameState(File file) {
		XMLConfig xmlConfig = new XMLConfig(file);

		GameState gameState = xmlConfig.readFromFile();

		this.coachName = gameState.getCoachName();
		this.gameRound = gameState.getGameRound();
		this.league = gameState.getLeague();
		this.myTeam = gameState.getMyTeam();
		this.matchScheme = gameState.getMatchScheme();

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Creates and empty GameState.
	 */
	public GameState() {
		this.coachName = "";
		this.gameRound = -1;
		this.league = null;
		this.myTeam = null;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Loads a GameState from a file.
	 * 
	 * @param file The file to read from.
	 * @return The GameState.
	 */
	public static GameState load(File file) {
		return new XMLConfig(file).readFromFile();
	}

	/**
	 * Save a GameState to a file.
	 * 
	 * @param gameState The GameState to save.
	 * @param file The file to save to.
	 * @return Returns if the save was successful.
	 */
	public static boolean save(GameState gameState, File file) {
		return new XMLConfig(file).writeToFile(gameState);
	}

	/**
	 * Checks if a GameState has null elements, and therefor is useless.
	 * 
	 * @param gameState The GameState to check.
	 * @return Returns if the GameState is useless.
	 */
	public static boolean isUseless(GameState gameState) {
		return gameState == null
				|| gameState.getCoachName() == null
				|| gameState.getLeague() == null
				|| gameState.getMyTeam() == null;
	}

	/**
	 * Goes to the next round.
	 */
	public void nextRound() {
		this.setGameRound(getGameRound() + 1);
	}

	/**
	 * Gets the score of all the teams in the current league.
	 * 
	 * @return The score for each team.
	 */
	public Map<Team, Integer> getOverallScores() {
		Map<Team, Integer> points = FXCollections.observableHashMap();

		for (MatchDay md : this.matchScheme.getMatchdays()) {
			for (Match m : md.getMatches()) {
				MatchResult mr = m.getMatchResult();
				if (mr == null) continue;
				if (mr.getHomeScore() == mr.getAwayScore()) {
					Integer currentHomeScore = points.get(m.getHome());
					Integer currentAwayScore = points.get(m.getAway());
					
					if (currentHomeScore == null) currentHomeScore = 0;
					if(currentAwayScore == null) currentAwayScore = 0;
					
					currentHomeScore += 1; // Points for a draw
					currentAwayScore += 1;
					
					points.put(m.getHome(), currentHomeScore);
					points.put(m.getAway(), currentAwayScore);
				} else {
					Team winner = (mr.getHomeScore() > mr.getAwayScore() ? m.getHome() : m.getAway());
					Integer currentScore = points.get(winner);

					if (currentScore == null) currentScore = 0;

					currentScore += 3; // Points for won game
					points.put(winner, currentScore);
				}
			}
		}
		
		return points;
	}

	/**
	 * Gets the users team as a string.
	 * 
	 * @return String of the users team.
	 */
	public String getMyTeamName() {
		return getMyTeam().getName();
	}

	/**
	 * Gets the users league as string.
	 * 
	 * @return String of the users league.
	 */
	public String getLeagueName() {
		if (getLeague() != null) {
			return getLeague().getName();
		}
		return null;
	}

	/**
	 * Represent this gameState as a String.
	 * 
	 * @return Returns the gameState as a string.
	 */
	@Override
	public String toString() {
		return "GameState [coachName=" + coachName + ", round=" + gameRound
				+ ", league=" + league + ", team=" + myTeam + "]";
	}

	/**
	 * Gets the coachName as a string
	 * 
	 * @return A string of the coach name.
	 */
	public String getCoachName() {
		return coachName;
	}

	/**
	 * Sets the coach name.
	 * 
	 * @param coachName The coachName to set.
	 */
	public void setCoachName(String coachName) {
		this.coachName = coachName;

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the current gameround.
	 * 
	 * @return the gameRound.
	 */
	public int getGameRound() {
		return gameRound;
	}

	/**
	 * Sets the gameRound.
	 * 
	 * @param gameRound the gameRound to set.
	 */
	public void setGameRound(int gameRound) {
		this.gameRound = gameRound;

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the current league.
	 * 
	 * @return Returns the league.
	 */
	public League getLeague() {
		return league;
	}

	/**
	 * Sets the current league.
	 * 
	 * @param league the league to set
	 */
	public void setLeague(League league) {
		this.league = league;

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Sets the league, but as string.
	 * 
	 * @param leagueName The name of the league.
	 */
	public void setLeague(String leagueName) {
		try {
			this.league = League.readOne(leagueName);
		} catch (FileNotFoundException e) {
			this.league = null;
		}

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the users team name.
	 * 
	 * @return the myTeam
	 */
	public Team getMyTeam() {
		return myTeam;
	}

	/**
	 * Sets the users team.
	 * 
	 * @param myTeam the myTeam to set
	 */
	public void setMyTeam(Team myTeam) {
		this.myTeam = myTeam;

		setChanged();
		notifyObservers(this);
	}
	/**
	 * Sets team according to name.
	 * Cannot set team if league is not set.
	 * 
	 * @param myTeamName
	 * @throws Exception 
	 */
	public void setMyTeam(String myTeamName) throws Exception {
		if(this.league != null && this.league.getTeam(myTeamName) != null) {
			this.myTeam = this.league.getTeam(myTeamName);

			setChanged();
			notifyObservers(this);
		} else {
			throw new Exception("LEAGUE NOT SET OR TEAM NOT FOUND");
		}
	}

	/**
	 * Gets the matchscheme for the current gamestate.
	 * 
	 * @return the matchScheme
	 */
	public MatchScheme getMatchScheme() {
		return matchScheme;
	}

	/**
	 * Sets the matchscheme for the current gamestate.
	 * 
	 * @param matchScheme the matchScheme to set
	 */
	public void setMatchScheme(MatchScheme matchScheme) {
		this.matchScheme = matchScheme;
	}
}
