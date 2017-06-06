package nl.tudelft.footballmanager.model.logic;

import nl.tudelft.footballmanager.model.Player;

/**
 * Class to calculate a players statistics.
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 */
public final class PlayerLogic {

	private PlayerLogic() { }

	/**
	 * Calculates a player's offensive score by their position.
	 * If the player is an attacker, give him a bonus.
	 * If the player is a defender, give him a debuff.
	 * 
	 * @param player The player to calculate the score for.
	 * @return Returns the offensive score of a player.
	 */
	public static final int calculatePlayerOffScore(Player player){
		int playerOff = player.getOffensive();
		String curPosition = player.getCurPosition();

		if (curPosition == "Attacker") {
			playerOff = (int) Math.round(player.getOffensive() * 1.25);
		} else if (curPosition == "Defender") {
			playerOff = (int) Math.round(player.getOffensive() * 0.75);
		}

		return playerOff;
	}

	/**
	 * Calculates a player's defensive score by their position.
	 * If the player is an attacker, give him a debuff.
	 * If the player is a defender, give him a bonus.
	 * 
	 * @param player The player to calculate the score for.
	 * @return Returns the defensive score of a player.
	 */
	public static final int calculatePlayerDefScore(Player player){
		int playerDef = player.getDefensive();
		String curPosition = player.getCurPosition();

		if (curPosition == "Attacker") {
			playerDef = (int) Math.round(player.getDefensive() * 0.75);
		} else if (curPosition == "Defender") {
			playerDef = (int) Math.round(player.getDefensive() * 1.25);
		}

		return playerDef;
	}

	/**
	 * Calculates a player's stamina.
	 * Stamina is reduced for each game the player had played, but resets every 9th game round (see GameLogic).
	 * Players with less stamina lose more.
	 * 
	 * @param player The player to calculate the stamina for.
	 * @return Returns the stamina of a player.
	 */
	public static final int calculatePlayerStamina(Player player){
		int stamina = player.getStamina(), result = 0;

		if (stamina >= 80) {
			result = (int) Math.round(stamina - (0.2 * player.getPlayedGames()));
		} else if (stamina < 80 && stamina >= 65) {
			result = (int) Math.round(stamina - (0.3 * player.getPlayedGames()));
		} else {
			result = (int) Math.round(stamina - (0.4 * player.getPlayedGames()));
		}

		return result;
	}
	
	/**
	 * Calculates the cost for a player.
	 * Players with higher stats are worth way more.
	 * 
	 * @param player The player to calculate the cost for.
	 * @return Returns the cost of a player.
	 */
	public static final int calculatePrice(Player player) {
		int stats = player.getOffensive() + player.getDefensive() + player.getStamina();
		int price = 0;
		
		if (player.getPosition().equals("GK")) {
			price += 850000;
		}
		
		if (stats > 185) {
			price += 6500 * (player.getOffensive() + player.getDefensive() + player.getStamina());
		} else if (stats <= 185 && stats > 165){
			price += 5500 * (player.getOffensive() + player.getDefensive() + player.getStamina());
		} else {
			price += 4800 * (player.getOffensive() + player.getDefensive() + player.getStamina());
		}
		
		player.setPrice(price);
		return price;
	}
	
}
