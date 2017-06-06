/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import nl.tudelft.footballmanager.FootballManager;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.Match;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.logic.GameLogic;
import nl.tudelft.footballmanager.model.logic.MarketplaceLogic;


/**
 * @author Mathijs de Boer <czorio4@gmail.com>
 *
 */
public class PostMatchViewController implements Initializable {

	public final static String postMatchViewFileName = "ui/view/PostMatchView.fxml";

	@FXML private TableView<Match> playedMatchesTableView;
	@FXML private TableColumn<Match, String> playedMatchesTeam1TableColumn;
	@FXML private TableColumn<Match, String> playedMatchesTeam2TableColumn;
	@FXML private TableColumn<Match, String> playedMatchesScoreTableColumn;

	@FXML private TableView<Integer> eventsTableView;
	@FXML private TableColumn<Integer, Integer> eventsMinuteTableColumn;
	@FXML private TableColumn<Integer, String> eventsPlayerNameTableColumn;
	@FXML private TableColumn<Integer, String> eventsTeamTableColumn;
	@FXML private TableColumn<Integer, String> eventsEventTableColumn;

	@FXML private Label incomeLabel;
	@FXML private Button continueButton;

	private static GameState gameState = null;

	/**
	 * Callback to highlight your team name in a table.
	 */
	private static Callback<TableColumn<Match, String>, TableCell<Match, String>> highlightMyTeam = new Callback<TableColumn<Match, String>, TableCell<Match, String>>() {
		@Override
		public TableCell<Match, String> call(TableColumn<Match, String> param) {
			return new TableCell<Match, String>() {
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
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initializing " + this.getClass());


		int gameRound = gameState.getGameRound();
		try {
			List<Match> matches = gameState.getMatchScheme().getMatchdays().get(gameRound).getMatches();
			for (int i = 0; i < matches.size(); i++) {
				Match m = matches.get(i);
				if (m.getHome().getName().equals(gameState.getMyTeamName()) || m.getAway().getName().equals(gameState.getMyTeamName())) {
					Match tmp = matches.get(0);
					matches.set(0, m);
					matches.set(i, tmp);
				}
			}
			playedMatchesTableView.setItems(FXCollections.observableList(matches));
		} catch (IndexOutOfBoundsException e) {
			PostLeagueViewController.show(gameState);
		}

		continueButton.setOnAction((event) -> {
			if (gameState.getMatchScheme().matchDayExists(gameState.getGameRound() + 1)) {
				System.out.println("Next: " + gameState.getMatchScheme().getMatchDay(gameState.getGameRound() + 1));

				// After the match, players will be trasfered, so they will be visible when the RootViewController is showed.
				MarketplaceLogic.RandomPlayerTransfer(gameState.getGameRound(), gameState.getLeague(), gameState.getMyTeam());
				RootViewController.show(gameState);
			} else {
				PostLeagueViewController.show(gameState);
			}


		});

		playedMatchesTeam1TableColumn.setCellValueFactory(new Callback<CellDataFeatures<Match, String>, ObservableValue<String>>() {
			public ObservableStringValue call(CellDataFeatures<Match, String> p) {
				return new SimpleStringProperty(p.getValue().getHome().getName());
			}
		});

		playedMatchesTeam1TableColumn.setCellFactory(highlightMyTeam);

		playedMatchesTeam2TableColumn.setCellValueFactory(new Callback<CellDataFeatures<Match, String>, ObservableValue<String>>() {
			public ObservableStringValue call(CellDataFeatures<Match, String> p) {
				return new SimpleStringProperty(p.getValue().getAway().getName());
			}
		});

		playedMatchesTeam2TableColumn.setCellFactory(highlightMyTeam);

		playedMatchesScoreTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Match, String>, ObservableValue<String>>() {
			public ObservableStringValue call(CellDataFeatures<Match, String> p) {
				return new SimpleStringProperty(p.getValue().getMatchResult().getReadableScore());
			}
		});

		playedMatchesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Match>() {
			@Override
			public void changed(ObservableValue<? extends Match> observable, Match oldValue, Match newValue) {
				if (newValue != null) {
					Map<Integer, Player> goals = newValue.getMatchResult().getHomeGoals();
					goals.putAll(newValue.getMatchResult().getAwayGoals());
					eventsTableView.setItems(FXCollections.observableArrayList(goals.keySet()));
					eventsTableView.sort();
				}
			}
		});

		eventsMinuteTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Integer, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Integer, Integer> param) {
				return new SimpleIntegerProperty(param.getValue()).asObject();
			}
		});
		
		eventsPlayerNameTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Integer, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Integer, String> param) {
				Map<Integer, Player> goals = playedMatchesTableView.getSelectionModel().getSelectedItem().getMatchResult().getHomeGoals();
				goals.putAll(playedMatchesTableView.getSelectionModel().getSelectedItem().getMatchResult().getAwayGoals());
				return new SimpleStringProperty(goals.get(param.getValue()).getFirstName() + " " + goals.get(param.getValue()).getLastName());
			}
		});
		
		eventsTeamTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Integer, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Integer, String> param) {
				Map<Integer, Player> goals = playedMatchesTableView.getSelectionModel().getSelectedItem().getMatchResult().getHomeGoals();
				goals.putAll(playedMatchesTableView.getSelectionModel().getSelectedItem().getMatchResult().getAwayGoals());
				return new SimpleStringProperty(goals.get(param.getValue()).getClub());
			}
		});
		
		eventsEventTableColumn.setCellValueFactory(new Callback<CellDataFeatures<Integer, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Integer, String> param) {
				if (param.getValue() != null)
					return new SimpleStringProperty("Goal");
				else
					return null;
			}
		});
		
		eventsTableView.sortPolicyProperty().set(new Callback<TableView<Integer>, Boolean>() {
			@Override
			public Boolean call(TableView<Integer> param) {	
				FXCollections.sort(eventsTableView.getItems(), new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return Integer.compare(o1, o2);
					}
				});
				return true;
			}
		});
		
		eventsTableView.sort();

		new GameLogic(gameState);
		GameLogic.matchDay();
		incomeLabel.textProperty().bind(new SimpleIntegerProperty(GameLogic.getMatchIncome()).asString());

		System.out.println("Initializing " + this.getClass() + " finished");
	}

	public static void show(GameState gs) {
		gameState = gs;

		FXMLLoader l = new FXMLLoader();
		l.setLocation(FootballManager.class.getResource(postMatchViewFileName));
		try {
			AnchorPane postMatchView = (AnchorPane) l.load();
			FootballManager.getStage().setScene(new Scene(postMatchView));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
