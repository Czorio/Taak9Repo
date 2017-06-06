package nl.tudelft.footballmanager.model;

import java.util.Observable;

/**
 * Handles a match between two teams.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class Match extends Observable {
	private Team home;
	private Team away;
	private MatchResult matchResult;
	private int round;
	private boolean played;

	/**
	 * Creates and initializes a match.
	 * 
	 * @param home The home team.
	 * @param away The away team.
	 */
	public Match(Team home, Team away) {
		this.home = home;
		this.away = away;
		this.played = false;
	}
	
	/**
	 * Sets if the game is played.
	 * 
	 * @param played, set if game is played.
	 */
	public void setPlayed(boolean played) {
		this.played = played;
	}
	
	/**
	 * @return Returns true if the game is played.
	 */
	public boolean getPlayed() {
		return this.played;
	}
	
	/**
	 * Sets the matchResult for this game.
	 * 
	 * @param matchResult The matchResult for this game.
	 */
	public void setMatchResult(MatchResult matchResult) {
		this.matchResult = matchResult;
		this.played = true;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * @return Returns the matchResult for this game.
	 */
	public MatchResult getMatchResult() {
		return matchResult;
	}

	/**
	 * @return Returns the home team.
	 */
	public Team getHome() {
		return home;
	}

	/**
	 * Sets the home team.
	 * 
	 * @param home The home team to set.
	 */
	public void setHome(Team home) {
		this.home = home;

		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @return Returns the away team.
	 */
	public Team getAway() {
		return away;
	}

	/**
	 * Sets the away team.
	 * 
	 * @param away The away team to set.
	 */
	public void setAway(Team away) {
		this.away = away;

		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * @return Returns the round the match is played in.
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Sets the round the match is played in.
	 * 
	 * @param round The round to set.
	 */
	public void setRound(int round) {
		this.round = round;
		
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Represets a match as a string.
	 * 
	 * @return Returns the match as string.
	 */
	@Override
	public String toString() {
		return "Match [home=" + home.getName() + ", away=" + away.getName() + ", round=" + round
				+ "]";
	}

}
