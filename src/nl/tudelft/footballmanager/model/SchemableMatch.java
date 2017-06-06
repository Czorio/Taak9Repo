package nl.tudelft.footballmanager.model;

/**
 * A match, which specifies whether it is to be put in the match scheme or not.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class SchemableMatch extends Match {
	private boolean schemable;

	/**
	 * Creates and initializes a SchemableMatch.
	 * @param home The home team.
	 * @param away The away team.
	 * @param schemable If the match is schemable.
	 */
	public SchemableMatch(Team home, Team away, boolean schemable) {
		super(home, away);
		this.setSchemable(schemable);
	}

	/**
	 * @return Returns  if the match is schemable.
	 */
	public boolean isSchemable() {
		return schemable;
	}

	/**
	 * Sets if the match is schemable.
	 * @param schemable Whether the match is schemable or not.
	 */
	public void setSchemable(boolean schemable) {
		this.schemable = schemable;
	}
	
}
