package nl.tudelft.footballmanager.model.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchResult;
import nl.tudelft.footballmanager.model.MatchScheme;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;

/**
 * Logic used to play a gameday, games are played between a home team and an away team, based on the current matchscheme.
 * @author Steven Meijer <stevenmeijer9@gmail.com>
 */
public class GameLogic {

	private static boolean isTesting;
	private static int matchIncome;
	private static long seed = System.currentTimeMillis();

	private static Random random = new Random(seed);
	private static List<Player> playingPlayers = TeamLogic.getPlayingPlayers();
	private static GameState gs;
	
	/**
	 * Creates and initializes a GameLogic instance.
	 * @param gs The current gamestate to use.
	 */
	public GameLogic(GameState gs){
		GameLogic.gs = gs;
	}

	/**
	 * Plays all the games that should be played on the current matchday.
	 */
	public static boolean matchDay() {
		MatchScheme ms = gs.getMatchScheme();
		int matchDay = gs.getGameRound();

		// Returns if there are no more rounds.
		if(matchDay >= gs.getMatchScheme().getMatchdays().size()) return false;

		List<Match> todaysMatches = ms.getMatchdays().get(matchDay).getMatches();

		//Playes the matches for today.
		for(Match m : todaysMatches) {		
			if(m.getHome().equals(gs.getMyTeam())) {
				TeamLogic.createActivePlayers(m.getHome());
				TeamLogic.createAIActivePlayers(m.getAway());
			} else if (m.getAway().equals(gs.getMyTeam())) {
				TeamLogic.createAIActivePlayers(m.getHome());
				TeamLogic.createActivePlayers(m.getAway());
			} else {
				TeamLogic.createAIActivePlayers(m.getHome());
				TeamLogic.createAIActivePlayers(m.getAway());
			}

			m.setMatchResult(game(m.getHome(), m.getAway()));
		}

		//Sets the amount of played games for each playing player to + 1.
		//Resets every 9th playing day to keep scoring possible for AI.
		//Also resets a players' current position, and updates a players' disabled status.
		for(Player p : playingPlayers) {
			if(p.getTeam() == gs.getMyTeam()) {
				p.setPlayedGames(p.getPlayedGames() + 1);
			} else {
				p.setCurPosition(null);
				p.setPlayedGames(p.getPlayedGames() + 1);
			}
			
			if(gs.getGameRound() % 9 == 0) {
				p.setPlayedGames(0);
			}
			
			if(p.getDisabledFor() > 0) {
				p.setDisabledFor(p.getDisabledFor() - 1);
			}
		}
		
		TeamLogic.clearPlayers();
		gs.nextRound();
		return true;
	}

	/**
	 * Logic for the game itself. Determines when a team scores and the outcome of a match.
	 * A goal can only be scored every 5-12 minutes. A maximum of 10 goals per team can be scored.
	 * 
	 * Every match takes 90 + 0-6 minutes.
	 * 
	 * Has a random chance to give a player on the field an injury, that disables that player for some time.
	 * Sets the Matchresult and the stats for the teams played when the game is over.
	 * 
	 * @param home The home team to play.
	 * @param away The away team to play.
	 * @return Returns the result of the match.
	 */
	public static MatchResult game(Team home, Team away){
		MatchResult matchResult = new MatchResult();

		int homeGoals = 0;
		int awayGoals = 0;
		int homeIncome = 0;
		int awayIncome = 0;
		int lastGoal = 0; //Minutes since last goal.
		int randomInterval = generateRandom(5, 12); //Interval where no goals can be scored.
		int extraTime = generateRandom(0, 6); //Extra game time.
		int homeScoreChance = TeamLogic.calculateTeamTotalScore(home);
		int awayScoreChance = TeamLogic.calculateTeamTotalScore(away);
		
		List<Player> playersHome = TeamLogic.getPlayingPlayersPerTeam(home);
		List<Player> playersAway = TeamLogic.getPlayingPlayersPerTeam(away);

		//Match starts here
		for (int i = 1; i <= (90 + extraTime); i++) {
			if ((homeScoreChance - awayScoreChance) + generateRandom(100, 200) > awayScoreChance 
					&& homeGoals < 10 
					&& lastGoal >= randomInterval 
					&& generateRandom(0, 30) == 9) {
				homeGoals++;
				lastGoal = 0;
				
				homeIncome += 100000;
				
				Player arnold = playersHome.get(generateRandom(0, playersHome.size() - 1));
				arnold.setPrice(arnold.getPrice() + 10000);
				matchResult.addHomeGoal(i, arnold);
			}

			if ((awayScoreChance - homeScoreChance) + generateRandom(100, 200) > homeScoreChance 
					&& awayGoals < 10 
					&& lastGoal >= randomInterval 
					&& generateRandom(0, 30) == 9) {
				awayGoals++;
				lastGoal = 0;
				
				awayIncome += 100000;
				
				Player nick = playersAway.get(generateRandom(0, playersAway.size() - 1));
				nick.setPrice(nick.getPrice() + 10000);
				matchResult.addAwayGoal(i, nick);
			}
			
			//Generates a random injury and gives it to a random player.
			//Injuries are disabled for JUnit tests.
			int injuryChance;
			injuryChance = generateRandom(0, 10000);
			
			if (injuryChance == 9 && !isTesting) {
				String injury = generateInjury();
				Player p = playersHome.get(random.nextInt(playersHome.size()));
				System.out.println("Injury: " + p.getFirstName() + " " + p.getLastName() + " - " + injury);
				
				p.setInjury(injury);
				p.setDisabledFor(generateRandom(3,10));
			}
			
			else if (injuryChance == 10 && !isTesting) {
				String injury = generateInjury();
				System.out.println(playersAway.size());
				Player p = playersAway.get(random.nextInt(playersAway.size()));
				System.out.println("Injury: " + p.getFirstName() + " " + p.getLastName() + " - " + injury);
				
				p.setInjury(injury);
				p.setDisabledFor(generateRandom(3,10));
			}

			lastGoal++;

		}
		//Match ends here

		homeIncome += 90000 + ((homeGoals + 1) * generateRandom(0, 100000));
		awayIncome += 90000 + ((awayGoals + 1) * generateRandom(0, 100000));
		home.alterBudget(homeIncome);
		away.alterBudget(awayIncome);

		//Set the matchIncome to the income of the current users' team.
		if (home.equals(gs.getMyTeam())) {
			matchIncome = homeIncome;
		} else if (away.equals(gs.getMyTeam())) {
			matchIncome = awayIncome;
		}
		
		//Sets the statistics for the teams that played the match.
		if (homeGoals > awayGoals) {
			home.setGamesWon(home.getGamesWon() + 1);
			home.setGamesPlayed(home.getGamesPlayed() + 1);
			away.setGamesLost(away.getGamesLost() + 1);
			away.setGamesPlayed(away.getGamesPlayed() + 1);
		} else if (homeGoals == awayGoals) {
			home.setGamesDraw(home.getGamesDraw() + 1);
			home.setGamesPlayed(home.getGamesPlayed() + 1);
			away.setGamesDraw(away.getGamesDraw() + 1);
			away.setGamesPlayed(away.getGamesPlayed() + 1);
		} else if (homeGoals < awayGoals) {
			home.setGamesLost(home.getGamesLost() + 1);
			home.setGamesPlayed(home.getGamesPlayed() + 1);
			away.setGamesWon(away.getGamesWon() + 1);
			away.setGamesPlayed(away.getGamesPlayed() + 1);
		}
		
		matchResult.setHomeScore(homeGoals);
		matchResult.setAwayScore(awayGoals);

		return matchResult;
	}
	
	/**
	 * Generates a random injury.
	 * @return Returns a random injury.
	 */
	public static String generateInjury() {
		List<String> injuries = new ArrayList<String>();
		
		injuries.add("Achilles Tendon Rupture");
		injuries.add("Sprained Ankle");
		injuries.add("Back Muscle Pain");
		injuries.add("Bursitis Knee");
		injuries.add("Dislocated Shoulder");
		injuries.add("Hamstring Strain");
		injuries.add("High Ankle Sprain");
		injuries.add("Knee Arthritis");
		injuries.add("Meniscus Tear");
		injuries.add("Pinched Nerve");
		injuries.add("Shin Splints");
		injuries.add("Shoulder Impingement");
		injuries.add("Thigh Strain");
		injuries.add("Stress Fracture");
		injuries.add("Wry Neck");
		injuries.add("Ruptured left testicle");
		injuries.add("Broken back");
		injuries.add("Broken nose");
		injuries.add("Shot");
		
		return injuries.get(random.nextInt(injuries.size()));
	}

	/**
	 * Generates a pseudo-random number between a min and max value.
	 * 
	 * @param min The minimum value.
	 * @param max The maximum value. Must be larger than min.
	 * @return Returns a semi-random number.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int generateRandom(int min, int max){
		return random.nextInt((max - min) + 1) + min;
	}

	/**
	 * Returns the used seed.
	 * @return The seed used.
	 */
	public static long getSeed() {
		return seed;
	}

	/**
	 * Set the seed used in the random number generator.
	 * 
	 * This method should only be used for testing purposed.
	 * @param seed The seed to be used
	 */
	public static void setSeed(long seed) {
		GameLogic.seed = seed;
	}
	
	/**
	 * Gets the current gamestate.
	 * @return Returns the gameState.
	 */
	public static GameState getGameState() {
		return GameLogic.gs;
	}
	
	/**
	 * Sets the currens gamestate.
	 * @param gs The gamestate to set.
	 */
	public static void setGameState(GameState gs) {
		GameLogic.gs = gs;
	}
	
	/**
	 * Gets the income for a match.
	 * @return Returns the income from a match.
	 */
	public static int getMatchIncome() {
		return GameLogic.matchIncome;
	}
	
	/**
	 * Sets the income for a match.
	 * @param income The income to set.
	 */
	public static void setMatchIncome(int income) {
		GameLogic.matchIncome = income;
	}

	/**
	 * @return the isTesting
	 */
	public static boolean isTesting() {
		return isTesting;
	}

	/**
	 * @param isTesting the isTesting to set
	 */
	public static void setIsTesting(boolean isTesting) {
		GameLogic.isTesting = isTesting;
	}

}
