/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import nl.tudelft.footballmanager.FootballManager;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.MatchDay;
import nl.tudelft.footballmanager.model.Team;

/**
 * @author Mathijs de Boer <czorio4@gmail.com>
 *
 */
public class PostLeagueViewController implements Initializable {
	
	public final static String postLeagueViewFileName = "ui/view/PostLeagueView.fxml";
	
	@FXML private Button doneButton;
	@FXML private Label teamPosLabel;
	@FXML private TableView<Team> leagueScoreboardTableView;
	@FXML private TableColumn<Team, String> leagueTeamTableColumn;
	@FXML private TableColumn<Team, Integer> leagueScoreTableColumn;
	@FXML private TableColumn<Team, Integer> leaguePosTableColumn;
	
	private static GameState gameState = null;
	
	/**
	 * Callback to highlight your team name in a table.
	 */
	private static Callback<TableColumn<Team, String>, TableCell<Team, String>> highlightMyTeam = new Callback<TableColumn<Team, String>, TableCell<Team, String>>() {
		@Override
		public TableCell<Team, String> call(TableColumn<Team, String> param) {
			return new TableCell<Team, String>() {
				@Override
				protected void updateItem(String teamName, boolean empty) {
					if (teamName == null || empty) {
						this.setText(null);
						this.setTextFill(Color.WHITE);
					} else if (teamName.equals(gameState.getMyTeamName())) {
						this.setText(teamName);
						this.setTextFill(Color.BLUE);
						this.setStyle("-fx-font-weight: bold");
					} else {
						this.setText(teamName);
						this.setTextFill(Color.BLACK);
					}
				}
			};
		}
	};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		Map<Team, Integer> scores = gameState.getOverallScores();
		System.out.println("Scores: " + scores);
		
		Comparator<Team> TeamComparator = new Comparator<Team>() {
			@Override
			public int compare(Team t1, Team t2) {
				int score1 = (scores.get(t1) != null ? scores.get(t1) : 0);
				int score2 = (scores.get(t2) != null ? scores.get(t2) : 0);
				int goals1 = 0; //t1 goals
				int goals11 = 0; //t1 counter goals
				int goals2 = 0; //t2 goals
				int goals21 = 0; //t2 counter goals

				for (MatchDay md : gameState.getMatchScheme().getMatchdays()) {
					for (Match m : md.getMatches()) {
						if (m.getHome().equals(t1) && !m.getAway().equals(t2) && m.getMatchResult() != null) {
							goals1 += m.getMatchResult().getHomeScore();
							goals11 += m.getMatchResult().getAwayScore();
						} else if (m.getAway().equals(t1) && !m.getHome().equals(t2) && m.getMatchResult() != null) {
							goals1 += m.getMatchResult().getAwayScore();
							goals11 += m.getMatchResult().getHomeScore();
						} else if (m.getHome().equals(t2) && !m.getAway().equals(t1) && m.getMatchResult() != null) {
							goals2 += m.getMatchResult().getHomeScore();
							goals21 += m.getMatchResult().getAwayScore();
						} else if (m.getAway().equals(t2) && !m.getHome().equals(t1) && m.getMatchResult() != null) {
							goals2 += m.getMatchResult().getAwayScore();
							goals21 += m.getMatchResult().getHomeScore();
						}
					}
				}
				
				if (score1 > score2) {
					return -1;
				} else if (score1 < score2) {
					return 1;
				} else if (goals1 > goals2) {
					return -1;
				} else if ((goals11 - goals1) > (goals21 - goals2)) {
					return 1;
				} else if ((goals11 - goals1) < (goals21 - goals2)) {
					return -1;
				} else if ((goals1 - goals11) > (goals2 - goals21)) {
					return 1;
				} else if ((goals1 - goals11) < (goals2 - goals21)) {
					return -1;
				} else if (goals1 > goals2) {
					return 1;
				} else if (goals1 < goals2) {
					return -1;
				} else if (t1.getName().equals(gameState.getMyTeamName())) {
					return -1;
				} else if (t2.getName().equals(gameState.getMyTeamName())) {
					return 1;
				} else {
					return 0;
				}
			}
		};
		
		leagueScoreboardTableView.setItems(FXCollections.observableList(gameState.getLeague().getTeams()));
		leagueTeamTableColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
		leagueTeamTableColumn.setCellFactory(highlightMyTeam);
		
		leagueScoreTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Team, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Team, Integer> param) {
				Integer score = scores.get(param.getValue());
				if (score == null)
					return new SimpleIntegerProperty(0).asObject();
				return new SimpleIntegerProperty(score).asObject();
			}
		});

		leagueScoreboardTableView.sortPolicyProperty().set(new Callback<TableView<Team>, Boolean>() {
			@Override
			public Boolean call(TableView<Team> param) {				
				FXCollections.sort(leagueScoreboardTableView.getItems(), TeamComparator);
				return true;
			}
		});
		
		// Quit to Menu
		doneButton.setOnAction((event) -> {
			System.out.println(event.getSource());
			TitleScreenController.show();
			System.out.println("Returned to Menu!");
		});
		
		List<Team> positions = new ArrayList<Team>(gameState.getLeague().getTeams());
		Collections.sort(positions, TeamComparator);
		teamPosLabel.textProperty().bind(new SimpleIntegerProperty(positions.indexOf(gameState.getMyTeam()) + 1).asString());
	}

	/**
	 * @param gameState
	 */
	public static void show(GameState gs) {
		gameState = gs;
		
		FXMLLoader l = new FXMLLoader();
		l.setLocation(FootballManager.class.getResource(postLeagueViewFileName));
		try {
			AnchorPane postMatchView = (AnchorPane) l.load();
			FootballManager.getStage().setScene(new Scene(postMatchView));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
