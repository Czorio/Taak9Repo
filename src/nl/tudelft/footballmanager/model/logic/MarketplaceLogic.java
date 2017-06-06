package nl.tudelft.footballmanager.model.logic;

import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;

/**
 * Logic for the marketplace. 
 * Includes methods for determining what players are offered a contract and for which price. 
 * Has functions to change players from teams. 
 * Gives the user a random bid on a random player.
 * @author Boris Schrijver <boris@radialcontext.nl>
 */
public final class MarketplaceLogic {
	private MarketplaceLogic(){};

	/**
	 * Handles transfering of players.
	 * 
	 * @param fromTeam	Team from which the player needs to be removed.
	 * @param toTeam	Team to which the players needs to be moved.
	 * @param player	The player
	 * @return 			If the transfer was succesful.
	 */
	public static final boolean transferPlayer(Team fromTeam, Team toTeam, Player player, int currentRound) {
		boolean ret = false;

		// Transfermarket not open.
		if(!isTransferWindow(currentRound))
			return false;

		// Budget not sufficient
		if(toTeam.getBudget() < player.getPrice())
			return false;

		// A team cannot have less then 11 players.
		if(fromTeam.getPlayers().size() <= 11)
			return false;

		// Change players and set new budgets.
		if(fromTeam.removePlayer(player)) {
			if(toTeam.addPlayer(player)) {
				fromTeam.setBudget(fromTeam.getBudget() + player.getPrice());				
				toTeam.setBudget(toTeam.getBudget() - player.getPrice());
				ret = true;
			}
			else {
				fromTeam.addPlayer(player);
				ret = false;
			}
		}		
		return ret;
	}

	/**
	 * Transfers a random player to a random team.
	 * 
	 * @param currentRound	The round we currently play in.
	 * @param league		The league object.
	 * @param myTeam		The team you're coaching.
	 * @return				If the transfer has succeded. //Used for JUnit tests.
	 */
	public static final boolean RandomPlayerTransfer(int currentRound, League league, Team myTeam) {
		if(!isTransferWindow(currentRound)) return false; 

		int fromTeam;
		int toTeam;
		int player;

		// On a single day, anywhere between 0 and 60 players can be transfered.
		for (int i = 0; i < GameLogic.generateRandom(0, 60); i++) {

			// Select the from team.
			do {
				int x = GameLogic.generateRandom(0, league.getTeams().size()-1);
				if(!league.getTeams().get(x).getName().equals(myTeam.getName())) {
					fromTeam = x;
					break;
				}
			} while (true);

			// Select the to team.
			do {
				int y = GameLogic.generateRandom(0, league.getTeams().size()-1);
				if(!league.getTeams().get(y).getName().equals(myTeam.getName()) && 
						!league.getTeams().get(y).getName().equals(league.getTeams().get(fromTeam).getName())) {
					toTeam = y;
					break;
				}
			} while (true);

			// Select the player.
			player = GameLogic.generateRandom(0, league.getTeams().get(fromTeam).getPlayers().size()-1);

			System.out.print("[Transfering " + league.getTeams().get(fromTeam).getPlayers().get(player).getFirstName() + " "
					+ league.getTeams().get(fromTeam).getPlayers().get(player).getLastName()
					+ " from " + league.getTeams().get(fromTeam).getName() + " to "
					+ league.getTeams().get(toTeam).getName());

			boolean bSuccess = transferPlayer(league.getTeams().get(fromTeam), league.getTeams().get(toTeam), league.getTeams().get(fromTeam).getPlayers().get(player), currentRound);								

			System.out.println(" -> SUCCESS: " + bSuccess);
		}

		return true;		
	}

	/**
	 * Handles opening and closing of the transfer window.
	 * Transfer window is opened the first 4 rounds and between round 18 and 20.
	 * 
	 * @return Whether or not the transfer window is open or closed.
	 */
	public static final boolean isTransferWindow(int currentRound) {
		if(currentRound >= 0 && currentRound < 4)
			return true;
		else if(currentRound >= 18 && currentRound <= 20)
			return true;
		else
			return false;
	}
	
}
