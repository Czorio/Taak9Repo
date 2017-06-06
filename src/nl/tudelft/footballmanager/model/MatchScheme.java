package nl.tudelft.footballmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Random;

/**
 * Handles the matchscheme.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class MatchScheme extends Observable {
	private ArrayList<MatchDay> matchdays;
	private static String inFile = "XML/GameState.xml";

	/**
	 * Creates and initializes a matchscheme.
	 * 
	 * @param state The current gamestate.
	 * @param maxMatchesPerDay The maximum amount of matches per day.
	 */
	public MatchScheme(GameState state, int maxMatchesPerDay) {
		this(state.getLeague(), maxMatchesPerDay);
	}

	/**
	 * Creates and initializes a matchscheme.
	 * 
	 * @param league The current league.
	 * @param maxMatchesPerDay The maximum amount of matches per day.
	 */
	public MatchScheme(League league, int maxMatchesPerDay) {
		matchdays = new ArrayList<MatchDay>();
		if (league != null)
			this.populate(league, maxMatchesPerDay);
	}

	/**
	 * Creates an matchscheme with no matches.
	 * 
	 * @param league The current league.
	 */
	public MatchScheme() {
		matchdays = new ArrayList<MatchDay>();
	}

	/**
	 * Populates a MatchScheme with Matches, with constraints:
	 * - a single team can play only one Match on a MatchDay
	 * - if a number higher than 0 is passed to maxMatchesPerDay, this is the maximum number of Matches a day can have.
	 * - the actual number of Matches per MatchDay is random
	 * 
	 * @param league The league for which the GameScheme needs to be created.
	 * @param maxMatchesPerDay The maximum number of Matches a day.
	 */
	public void populate(League league, int maxMatchesPerDay) {
		ArrayList<SchemableMatch> possibleMatches = new ArrayList<SchemableMatch>();
		Random random = new Random(System.nanoTime());
		
		possibleMatches = league.getPossibleMatches(league);
		Collections.shuffle(possibleMatches, random);
		
		int round = 1;
		while (possibleMatches
				.stream()
				.filter(match -> match.isSchemable() == true)
				.count() > 0) {
			MatchDay matchDay = new MatchDay(round);
			
			int maxToday = (maxMatchesPerDay == 0 ? 0 : random.nextInt(maxMatchesPerDay - 1) + 2);
			for (SchemableMatch m : possibleMatches) {
				if ((matchDay.getMatchCount() < maxToday || maxToday <= 0) && matchDay.matchCanBePlayedToday(m) && m.isSchemable()) {
					m.setRound(round);
					matchDay.addMatch((Match)m);
					m.setSchemable(false);
				}
			}

			this.matchdays.add(matchDay);
			round++;
		}
	}

	/**
	 * Gets the matchdays.
	 * 
	 * @return Returns the matchdays.
	 */
	public ArrayList<MatchDay> getMatchdays() {
		return matchdays;
	}

	/**
	 * Sets the matchdays.
	 * 
	 * @param matchdays The matchdays to set.
	 */
	public void setMatchdays(ArrayList<MatchDay> matchdays) {
		this.matchdays = matchdays;

		setChanged();
		notifyObservers(this);
	}
	
	/**
	 * Add a single matchday to the list.
	 * 
	 * @param matchDay The matchday to add.
	 * @return Returns if the match is not yet in the matchscheme.
	 */
	public boolean addMatchDay(MatchDay matchDay) {
		for(MatchDay m : matchdays) {
			if(m.getRound() == matchDay.getRound())
				return false;				
		}
		
		this.matchdays.add(matchDay);
		return true;
	}
	
	/**
	 * Return a single matchday if it exists.
	 * 
	 * @param round The gameround.
	 * @return Returs the matches for today.
	 */
	public MatchDay getMatchDay(int round) {
		for(MatchDay m : matchdays) {
			if(m.getRound() == round)
				return m;				
		}
		return null;
	}
	
	/**
	 * Check if a matchday exists.
	 * 
	 * @param matchDay The matchday to check.
	 * @return Returns if the match already exists.
	 */
	public boolean matchDayExists(int round) {
		for(MatchDay m : matchdays) {
			if(m.getRound() == round)
				return true;				
		}
		return false;
	}

	/**
	 * String representation of the current matchscheme.
	 * 
	 * @return Returns the matchscheme as a string.
	 */
	@Override
	public String toString() {
		return "MatchScheme [matchdays=" + matchdays + "]";
	}
	
	/**
	 * Gets the file used.
	 * 
	 * @return Returns the inFile.
	 */
	public static String getInFile() {
		return inFile;
	}

	/**
	 * Sets the file to use.
	 * 
	 * @param inFile The inFile to set.
	 */
	public static void setInFile(String inFile) {
		MatchScheme.inFile = inFile;
	}
}
