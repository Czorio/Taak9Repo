package nl.tudelft.footballmanager.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Handles a matchday, in where multiple matches can be played.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class MatchDay extends Observable {
	private ArrayList<Match> matches;
	private int round;

	/**
	 * Creates and initializes a matchday.
	 * 
	 * @param round The gameround of this matchday.
	 * @param matches The matches to be played this day.
	 */
	public MatchDay(int round, ArrayList<Match> matches) {
		this.matches = matches;
		this.round = round;
	}
	
	/**
	 * Creates and initializes a gameday with no matches.
	 * 
	 * @param round The gameround of this matchday.
	 */
	public MatchDay(int round) {
		this(round, new ArrayList<Match>());
	}
	
	/**
	 * Gets the teams that are playing this matchday.
	 * 
	 * @return Returns the teams that are playng today.
	 */
	public ArrayList<Team> getTodaysTeams() {
		ArrayList<Team> t = new ArrayList<Team>();
		for (Match m : matches) {
			t.add(m.getAway());
			t.add(m.getHome());
		}
		
		return t;
	}
	
	/**
	 * Checks if a team can play today, i.e. they are not already playing today.
	 * 
	 * @param t The team to check.
	 * @return Returns if team t can play today.
	 */
	public boolean teamCanPlayToday(Team t) {
		if (getTodaysTeams().contains(t)) return false;
		return true;
	}
	
	/**
	 * Checks if a match can be played today, i.e. if both teams can play today.
	 * 
	 * @param m The match to check.
	 * @return Returns if a match can be played today.
	 */
	public boolean matchCanBePlayedToday(Match m) {
		if (teamCanPlayToday(m.getHome()) && teamCanPlayToday(m.getAway())) return true;
		return false;
	}

	/**
	 * Gets the matches for today.
	 * 
	 * @return Returns the matches of today.
	 */
	public ArrayList<Match> getMatches() {
		return matches;
	}

	/**
	 * Sets the matches for today.
	 * 
	 * @param matches The matches to set.
	 */
	public void setMatches(ArrayList<Match> matches) {
		this.matches = matches;
		
		setChanged();
		notifyObservers(this);
	}
	
	/**
	 * Adds a match for today.
	 * 
	 * @param m The match to add.
	 */
	public void addMatch(Match m) {
		this.matches.add(m);
		
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the round of this matchday.
	 * 
	 * @return Returns the roundnumber.
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Sets the roundnumber of this matchday.
	 * 
	 * @param round The roundnumber to set.
	 */
	public void setRound(int round) {
		this.round = round;
		
		setChanged();
		notifyObservers(this);
	}

	/**
	 * String representation of the matchday.
	 * 
	 * @return Returns this matchday as a string.
	 */
	@Override
	public String toString() {
		return "MatchDay [matches=" + matches + ", round=" + round + "]";
	}

	/**
	 * Gets the amount of matches played today.
	 * 
	 * @return The number of matches played this matchday.
	 */
	public int getMatchCount() {
		return this.matches.size();
	}
	
}
