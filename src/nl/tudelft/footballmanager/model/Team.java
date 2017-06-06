package nl.tudelft.footballmanager.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handles the creation and properties of a team.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class Team extends Observable {
	private String name;
	private ObservableList<Player> players;
	private SimpleIntegerProperty budget, gamesWon, gamesDraw, gamesLost, gamesPlayed;

	/**
	 * Construct a team with given name and playerlist.
	 * 
	 * @param name
	 * @param players
	 */
	public Team(String name, List<Player> players) {
		this.name = name;
		this.players = FXCollections.observableList(players);
		this.budget = new SimpleIntegerProperty(3000000);
		this.gamesWon = new SimpleIntegerProperty(0);
		this.gamesDraw = new SimpleIntegerProperty(0);
		this.gamesLost = new SimpleIntegerProperty(0);
		this.gamesPlayed = new SimpleIntegerProperty(0);

		for (Player player : this.players) {
			player.setTeam(this);
		}

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Construct an empty Team.
	 * 
	 * @param name Name of the Team.
	 */
	public Team(String name) {
		this(name, new ArrayList<Player>());
	}

	/**
	 * Alters the budget by a given amount.
	 * 
	 * @param mutation Amount to add (positive number) or subtract (negative number).
	 */
	public void alterBudget(int mutation) {
		setBudget(getBudget() + mutation);
	}
	
	/**
	 * Represent the team as a string.
	 * 
	 * @return Returns the team as a string.
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Compares two teams, and checks if they are equal.
	 * 
	 * @return Returns if two teams are equal.
	 */
	public final static Comparator<Team> NAME_SORTER = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			return t1.getName().compareToIgnoreCase(t2.getName());
		}
	};

	/**
	 * Add player to this name, only if their doesn't exist a player with the same id.
	 * 
	 * @param player The player to add.
	 */
	public boolean addPlayer(Player player) {
		boolean bExists = false;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == player.getId())
				bExists = true;
			else if (players.get(i).getFirstName()
					.equals(player.getFirstName())
					&& players.get(i).getLastName()
							.equals(player.getLastName()))
				bExists = true;
		}

		if (!bExists) {
			players.add(player);
			player.setTeam(this);

			setChanged();
			notifyObservers(this);
		}

		return !bExists;
	}
	
	/**
	 * Get the number of players with a set playing position.
	 * 
	 * @return The number of players.
	 */
	public int getNumOfPlayingPlayers() {
		int count = 0;
		for (Player p : this.getPlayers()) {
			if (p.getCurPosition() != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Remove player, if the player exists, based on Player ID.
	 * 
	 * @param player The player to remove.
	 */
	public boolean removePlayer(Player player) {
		boolean bExists = false;
		int index = 0;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == player.getId()) {
				bExists = true;
				index = i;
			}
		}

		if (bExists) {
			players.remove(index);
			player.setTeam(null);

			setChanged();
			notifyObservers(this);
		}

		return bExists;
	}

	/**
	 * Checks if two players are equal.
	 * 
	 * @param other The player to check against.
	 * @return Returns if two players are equal.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Team) {
			Team that = (Team) other;

			return this.name.equals(that.name)
					&& this.players.equals(that.players);
		}

		return false;
	}
	
	/**
	 * Gets the budget of the team.
	 * 
	 * @return The budget.
	 */
	public SimpleIntegerProperty budgetProperty() {
		return this.budget;
	}
	
	/**
	 * Gets the games played.
	 * 
	 * @return Returns the amount of games this team has played.
	 */
	public SimpleIntegerProperty gamesPlayedProperty() {
		return this.gamesPlayed;
	}
	
	/**
	 * Gets the games won.
	 * 
	 * @return Returns the amount of games this team has won.
	 */
	public SimpleIntegerProperty gamesWonProperty() {
		return this.gamesWon;
	}
	
	/**
	 * Gets the games draw.
	 * 
	 * @return Returns the amount of games this team has played a draw.
	 */
	public SimpleIntegerProperty gamesDrawProperty() {
		return this.gamesDraw;
	}
	
	/**
	 * Gets the games lost.
	 * 
	 * @return Returns the amount of games this team has lost.
	 */
	public SimpleIntegerProperty gamesLostProperty() {
		return this.gamesLost;
	}

	/**
	 * Gets the name of the team.
	 * 
	 * @return Returns the name of the team.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the team.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String team) {
		this.name = team;
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Get Player based on first and last name
	 * 
	 * @param firstName The first name of a player.
	 * @param lastName The last name of a player.
	 * @return Return the player with the given sur- and last name.
	 */
	public Player getPlayer(String firstName, String lastName) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getFirstName().equals(firstName)
					&& players.get(i).getLastName().equals(lastName)) {
				return players.get(i);
			}
		}

		return null;
	}

	/**
	 * Get Player based on Player ID.
	 * 
	 * @param id The id to check against.
	 * @return Returns the player ith the given id.
	 */
	public Player getPlayer(int id) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == id) {
				return players.get(i);
			}
		}

		return null;
	}

	/**
	 * Gets all players from the current team.
	 * 
	 * @return Returns the players.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Sets the players of this team.
	 * 
	 * @param players The players to set.
	 */
	public void setPlayers(List<Player> players) {
		this.players = FXCollections.observableList(players);
		setChanged();
		notifyObservers(this.players);
	}

	/**
	 * Gets the budget of this team.
	 * 
	 * @return Returns the budget.
	 */
	public int getBudget() {
		return budget.get();
	}

	/**
	 * Sets the budget.
	 * 
	 * @param budget The budget to set
	 */
	public void setBudget(int budget) {
		this.budget.set(budget);
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Get players with a certain position.
	 * 
	 * @param position The position that the player must have.
	 * @return Returns a list with the players with the requested position.
	 */
	public List<Player> getByPosition(String position) {
		List<Player> certainPosition = new ArrayList<Player>();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getReadablePosition() == position && players.get(i).getDisabledFor() == 0) {
				certainPosition.add(players.get(i));
			}
		}

		return certainPosition;
	}
	
	/**
	 * Gets the amount of players in the team.
	 * 
	 * @return The amount of players.
	 */
	public int getPlayerCount() {
		return this.getPlayers().size();
	}
	
	/**
	 * Gets the amount of playingPlayers.
	 * A playing player is a plyer with a current position.
	 * 
	 * @return Returns the amount of playing players.
	 */
	public int getPlayingPlayers() {
		int playing = 0;
		
		for (Player player : players) {
			if(player.getCurPosition() != null) {
				playing++;
			}
		}
		return playing;
	}

	/**
	 * Gets the amount of games won.
	 * 
	 * @return Returns the amount of games won.
	 */
	public int getGamesWon() {
		return gamesWon.get();
	}

	/**
	 * Sets the amount of games won.
	 * 
	 * @param gamesWon The amount to set to.
	 */
	public void setGamesWon(int gamesWon) {
		this.gamesWon.set(gamesWon);
	}

	/**
	 * Gets the amount of games draw.
	 * 
	 * @return Returns the amount of games draw.
	 */
	public int getGamesDraw() {
		return gamesDraw.get();
	}

	/**
	 * Sets the amount of games draw.
	 * 
	 * @param gamesDraw The amount to set to.
	 */
	public void setGamesDraw(int gamesDraw) {
		this.gamesDraw.set(gamesDraw);
	}

	/**
	 * Gets the amount of games lost.
	 * 
	 * @return Returns the amount of games lost.
	 */
	public int getGamesLost() {
		return gamesLost.get();
	}

	/**
	 * Sets the amount of games lost.
	 * 
	 * @param gamesLost The amount to set.
	 */
	public void setGamesLost(int gamesLost) {
		this.gamesLost.set(gamesLost);
	}

	/**
	 * Gets the amount of games played.
	 * 
	 * @return Returns the amount of games played.
	 */
	public int getGamesPlayed() {
		return gamesPlayed.get();
	}

	/**
	 * Sets the amount of games played.
	 * 
	 * @param gamesPlayed The amount to set to.
	 */
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed.set(gamesPlayed);
	}
	
	public boolean hasFieldedKeeper() {
		for (Player player : players) {
			if(player.getCurPosition() == "GK") {
				return true;
			}
		}
		return false;
	}
	
}
