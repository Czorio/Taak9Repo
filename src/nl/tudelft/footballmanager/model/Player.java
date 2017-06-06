package nl.tudelft.footballmanager.model;

import java.util.Date;
import java.util.Observable;

import javafx.beans.property.SimpleStringProperty;

/**
 * Handles the creation and properties of a player.
 * @author Toine Hartman <tjbhartman@gmail.com>
 */
public class Player extends Observable {
	public static final int SOLD_SELLING_SUCCES = 1;
	public static final int BUDGET_NOT_SUFFICIENT_SELLING_ERROR = 0;
	public static final int TEAM_NULL_SELLING_ERROR = -1;
	public static final int UNKNOWN_SELLING_ERROR = -8;

	// Properties of a player
	private int id;
	private String firstName;
	private String lastName;
	private String club;
	private Team team;
	private String league;
	private String nationality;
	private Date dateOfBirth;
	private String position;
	private SimpleStringProperty curPosition;
	private int pace;
	private int shooting;
	private int passing;
	private int offensive;
	private int defensive;
	private int stamina;
	private int price; // Will be based on his stats.
	private int playedGames;
	private String injury;
	private int disabledFor;

	/**
	 * Creates and initializes a Player object.
	 * 
	 * @param id The id of a player.
	 * @param firstname The first name of a player.
	 * @param lastname The last name of a player.
	 * @param club The club of a player.
	 * @param nationality The nationality of a player.
	 * @param dateOfBirth The date of birth of a player.
	 * @param position The position of a player.
	 * @param pace The pace stats of a player.
	 * @param shooting The shooting stats of a player.
	 * @param passing The passing stats of a player.
	 * @param offensive The offensive stats of a player.
	 * @param defensive The defensive stats of a player.
	 * @param stamina The stamina of a player.
	 */
	public Player(Integer id, String firstname, String lastname, String club, String league, String nationality, Date dateOfBirth, String position,
			int pace, int shooting, int passing, int offensive, int defensive, int stamina) {
		this.id = id;
		this.firstName = firstname;
		this.lastName = lastname;
		this.club = club;
		this.league = league;
		this.nationality = nationality;
		this.dateOfBirth = dateOfBirth;
		this.position = position;
		this.curPosition = new SimpleStringProperty();
		this.pace = pace;
		this.shooting = shooting;
		this.passing = passing;
		this.offensive = offensive;
		this.defensive = defensive;
		this.stamina = stamina;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Create an empty Player object.
	 * 
	 * @param id The id of the player.
	 */
	public Player(Integer id) {
		this.id = id;
		this.firstName = null;
		this.lastName = null;
		this.club = null;
		this.nationality = null;
		this.dateOfBirth = null;
		this.position = null;
		this.curPosition = new SimpleStringProperty();
		this.pace = 0;
		this.shooting = 0;
		this.passing = 0;
		this.offensive = 0;
		this.defensive = 0;
		this.stamina = 0;
		this.price = 0;
		this.playedGames = 0;
		this.injury = null;
		this.disabledFor = 0;

		setChanged();
		notifyObservers(this);
	}

	/**
	 * A completely empty Player object.
	 */
	public Player() {
		this.curPosition = new SimpleStringProperty();
	}

	/**
	 * Compares two players to see if they are equal.
	 * 
	 * @param other The player to check against.
	 * @return Returns if the two players are equal.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Player) {
			Player that = (Player)other;

			boolean res = this.id == that.id &&
					this.firstName.equals(that.firstName) &&
					this.lastName.equals(that.lastName) &&
					this.club.equals(that.club);
			
			if (this.nationality != null) {
				if (that.nationality != null) {
					res = res && this.nationality.equals(that.nationality);
				}
				else res = false;
			}
			
			if (this.curPosition != null) {
				if (that.curPosition != null) {
					res = res && this.curPosition.equals(that.curPosition);
				}
				else res = false;
			}
			
			return res;
		}

		return false;
	}


	/**
	 * Represent a player as a string.
	 * 
	 * @return This Player object as a String.
	 */
	@Override
	public String toString() {
		return "[" + this.id + " " + this.firstName + " " + this.lastName + ": "
				+ this.club + " "
				+ this.nationality + " "
				+ this.dateOfBirth + " "
				+ this.position + " "
				+ this.curPosition.get() + " "
				+ this.pace + " "
				+ this.shooting + " "
				+ this.passing + " "
				+ this.offensive + " "
				+ this.defensive + " "
				+ this.stamina + " "
				+ this.price + " "
				+ this.playedGames + " "
				+ this.injury + " "
				+ this.disabledFor + "]";
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of a player.
	 * 
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the first name.
	 * 
	 * @return The first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstname The first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the last name.
	 * 
	 * @return The last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastname The last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the club.
	 * 
	 * @return The club.
	 */
	public String getClub() {
		return this.getTeam().getName();
	}

	/**
	 * Sets the club.
	 * 
	 * @param club The club to set.
	 */
	public void setClub(String club) {
		this.club = club;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the nationality.
	 * 
	 * @return The nationality.
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Sets the nationality.
	 * 
	 * @param nationality The nationality to set.
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the date of birth.
	 * 
	 * @return The date of birth.
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth The date of birth to set.
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the raw position of a player.
	 * 
	 * @return The position.
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Gets the position in a readable way.
	 * 
	 * @return The position as a readable string.
	 */
	public String getReadablePosition() {
		if (position.equals("ST") || position.equals("CF") || position.equals("LW") || position.equals("RW") || position.equals("Attacker")) {
			return "Attacker";
		} else if (position.equals("CM") || position.equals("CDM") || position.equals("CAM")
				|| position.equals("RM") || position.equals("LM") || position.equals("Midfielder")) {
			return "Midfielder";
		} else if (position.equals("CB") || position.equals("RB") || position.equals("LB") || position.equals("LWB") || position.equals("RWB")
				|| position.equals("Defender")) {
			return "Defender";
		} else if (position.equals("GK") || position.equals("Goalkeeper")) {
			return "Goalkeeper";
		} else {
			return null;
		}
	}

	/**
	 * Sets the position.
	 * 
	 * @param position The position to set.
	 */
	public void setPosition(String position) {
		this.position = position;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the current position.
	 * 
	 * @return The current position.
	 */
	public String getCurPosition(){
		if (curPosition == null) {
			return null;
		} else {
			return curPosition.get();
		}
	}
	
	public SimpleStringProperty curPositionProperty() {
		return curPosition;
	}

	/**
	 * Sets the current position.
	 * 
	 * @param curPosition The current position to set.
	 */
	public void setCurPosition(String curPosition) {
		this.curPosition.set(curPosition);

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the pace stat.
	 * 
	 * @return The pace stat.
	 */
	public int getPace() {
		return pace;
	}

	/**
	 * Sets the pace stat.
	 * 
	 * @param pace The pace stat to set.
	 */
	public void setPace(int pace) {
		this.pace = pace;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the shooting stat.
	 * 
	 * @return The shooting stat.
	 */
	public int getShooting() {
		return shooting;
	}

	/**
	 * Sets the shooting stat.
	 * 
	 * @param shooting The shooting stat to set.
	 */
	public void setShooting(int shooting) {
		this.shooting = shooting;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the passing stat.
	 * 
	 * @return The passing stat.
	 */
	public int getPassing() {
		return passing;
	}

	/**
	 * Sets the passing stat.
	 * 
	 * @param passing The passing stats to set.
	 */
	public void setPassing(int passing) {
		this.passing = passing;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the offensive stat.
	 * 
	 * @return The offensive stat.
	 */
	public int getOffensive() {
		return offensive;
	}

	/**
	 * Sets the offensive stat.
	 * 
	 * @param offensive The offensive stat to set.
	 */
	public void setOffensive(int offensive) {
		this.offensive = offensive;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the defensive stat.
	 * 
	 * @return The defensive stat.
	 */
	public int getDefensive() {
		return defensive;
	}

	/**
	 * Sets the defensive stat.
	 * 
	 * @param defensive The defensive stat to set.
	 */
	public void setDefensive(int defensive) {
		this.defensive = defensive;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the stamina.
	 * 
	 * @return The stamina.
	 */
	public int getStamina() {
		return stamina;
	}

	/**
	 * Sets the stamina.
	 * 
	 * @param stamina The stamina to set.
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the price.
	 * 
	 * @return The price.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price The price to set.
	 */
	public void setPrice(int price) {
		this.price = price;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the amount of played games.
	 * 
	 * @return The number of played games.
	 */
	public int getPlayedGames(){
		return playedGames;
	}

	/**
	 * Sets the amount of played games.
	 * 
	 * @param playedGames The amount of games.
	 */
	public void setPlayedGames(int playedGames){
		this.playedGames = playedGames;

		this.setChanged();
		this.notifyObservers(this);
	}

	/**
	 * Gets the league of this player.
	 * 
	 * @return The league of this player.
	 */
	public String getLeague(){
		return league;
	}

	/**
	 * Sets the league.
	 * 
	 * @param league The league to set.
	 */
	public void setLeague(String league){
		this.league = league;

		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gets the current players' team.
	 * 
	 * @return The team the player is in.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Sets the team.
	 * 
	 * @param team The team to set.
	 */
	public void setTeam(Team team) {
		this.team = team;

		setChanged();
		notifyObservers(this);
	}
	
	/**
	 * Gets the injury of a player.
	 * 
	 * @return Returns the injury
	 */
	public String getInjury() {
		return this.injury;
	}
	
	/**
	 * Sets an injury to a player.
	 * 
	 * @param injury The injury to set.
	 */
	public void setInjury(String injury) {
		this.injury = injury;
		
		setChanged();
		notifyObservers(this);
	}
	
	/**
	 * Gets how long a player is disabled.
	 * 
	 * @return Returns for how long a player is disabled
	 */
	public int getDisabledFor() {
		return this.disabledFor;
	}
	
	/**
	 * Sets how long a player is disabled.
	 * 
	 * @param disabled Sets for how long a player is disabled.
	 */
	public void setDisabledFor(int disabledFor) {
		this.disabledFor = disabledFor;
		
		setChanged();
		notifyObservers(this);
	}

}
