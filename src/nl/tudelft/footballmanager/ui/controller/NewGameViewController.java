/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import nl.tudelft.footballmanager.FootballManager;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.League;
import nl.tudelft.footballmanager.model.Team;

/**
 * @author Toine Hartman <tjbhartman@gmail.com>
 *
 */
public class NewGameViewController implements Initializable {
	
	public final static String newGameViewFileName = "ui/view/NewGameView.fxml";
	
	@FXML private Button cancelButton;
	@FXML private Button doneButton;
	@FXML private ListView<League> leagueListView;
	@FXML private ListView<Team> teamListView;
	@FXML private TextField coachNameTextField;

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.new LeaguesLoader().start();
		doneButton.setDisable(true);
		
		// default coach name is username
		coachNameTextField.setText(System.getProperty("user.name"));
		
		cancelButton.setOnAction((event) -> {
			TitleScreenController.show();
		});
		
		doneButton.setOnAction((event) -> {
			League selectedLeague = leagueListView.getSelectionModel().getSelectedItem();
			String coachName = coachNameTextField.getText();
			Team selectedTeam = null;

			if (coachName.equals("Andy Zaidman")) {
				try {
					selectedTeam = League.readOne("Legends").getTeam("Legends");
					selectedTeam.setBudget(90019001);
					selectedLeague.addTeam(selectedTeam);
				} catch (Exception e) {
					System.err.println("Andy Hacky Fail");
				}
			} else {
				selectedTeam = teamListView.getSelectionModel().getSelectedItem();
			}

			GameState gs = new GameState(coachName, 0, selectedLeague, selectedTeam);
			RootViewController.show(gs);
		});
		
		leagueListView.setCellFactory(new Callback<ListView<League>, ListCell<League>>() {
			@Override
			public ListCell<League> call(ListView<League> param) {
				ListCell<League> cell = new ListCell<League>() {
					protected void updateItem(League l, boolean empty) {
						super.updateItem(l, empty);
						if (l != null) setText(l.getName());
						else setText(null);
					}
				};
				return cell;
			}
		});
		
		leagueListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<League>() {
			@Override
			public void changed(ObservableValue<? extends League> observable, League oldValue, League newValue) {	
				ObservableList<Team> teams = FXCollections.observableArrayList(newValue.getTeams());
				FXCollections.sort(teams, Team.NAME_SORTER);
				
				if (teams != null) teamListView.setItems(teams);
				teamListView.getSelectionModel().clearSelection();
				toggleDone();
			}
		});
		
		teamListView.setCellFactory(new Callback<ListView<Team>, ListCell<Team>>() {
			@Override
			public ListCell<Team> call(ListView<Team> param) {
				ListCell<Team> cell = new ListCell<Team>() {
					protected void updateItem(Team t, boolean empty) {
						super.updateItem(t, empty);
						if (t != null) setText(t.getName());
						else setText(null);
					}
				};
				return cell;
			}
		});
		
		teamListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
			@Override
			public void changed(ObservableValue<? extends Team> observable,	Team oldValue, Team newValue) {
				toggleDone();
			}
		});
		
		coachNameTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				toggleDone();
			}
		});
	}
	
	class LeaguesLoader extends Thread {
		public void run() {
			List<League> leagues = League.readAll();
			
			ObservableList<League> checkedLeagues = FXCollections.observableArrayList(League.checkNumbersAndAddPrice(leagues, 3, 11));
			FXCollections.sort(checkedLeagues, League.NAME_COMPARATOR);

			leagueListView.setItems(checkedLeagues);
			leagueListView.getSelectionModel().clearSelection();
		}
	}
	
	/**
	 * Decides whether the "Done" buttons is enabled. It wil be enabled if:
	 * 1. You are Andy Zaidman and you chose a league to play in, with your Legends team.
	 * 2. You are anyone else and you chose a league and team (and you do have a name).
	 */
	protected void toggleDone() {
		if ((leagueListView.getSelectionModel().getSelectedItem() != null && coachNameTextField.getText().equals("Andy Zaidman")) || 
				(coachNameTextField.getText().length() > 0 && teamListView.getSelectionModel().getSelectedItem() != null)) {
			doneButton.setDisable(false);
		} else {
			doneButton.setDisable(true);
		}
	}

	public static void show() {
		FXMLLoader l = new FXMLLoader();
		l.setLocation(FootballManager.class.getResource(newGameViewFileName));
		try {
			AnchorPane newGameLayout = (AnchorPane) l.load();
			FootballManager.getStage().setScene(new Scene(newGameLayout));
			FootballManager.getStage().show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
