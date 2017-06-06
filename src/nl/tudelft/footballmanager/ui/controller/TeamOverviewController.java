/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import nl.tudelft.footballmanager.FootballManager;
import nl.tudelft.footballmanager.model.GameState;
import nl.tudelft.footballmanager.model.Player;
import nl.tudelft.footballmanager.model.Team;
import nl.tudelft.footballmanager.model.logic.MarketplaceLogic;

import org.controlsfx.dialog.Dialogs;


/**
 * @author Toine Hartman <tjbhartman@gmail.com>
 *
 */
@SuppressWarnings("deprecation")
public class TeamOverviewController implements Initializable, Observer {
	
	@FXML private Label gamesWonLabel;
	@FXML private Label gamesLostLabel;
	@FXML private Label gamesDrawLabel;

	public final static String teamOverviewFileName = "ui/view/TeamOverview.fxml";

	@FXML private TableView<Player> yourPlayerTableView;
	@FXML private TableColumn<Player, String> yourPlayerFirstNameCol;
	@FXML private TableColumn<Player, String> yourPlayerLastNameCol;
	@FXML private TableColumn<Player, String> yourPlayerPositionCol;
	@FXML private TableColumn<Player, String> yourPlayerCurPositionCol;
	@FXML private TableColumn<Player, Integer> yourPlayerOffCol;
	@FXML private TableColumn<Player, Integer> yourPlayerDefCol;
	@FXML private TableColumn<Player, Integer> yourPlayerStaminaCol;
	@FXML private TableColumn<Player, Integer> yourPlayerPriceCol;

	@FXML private Label yourPlayerNameLabel;
	@FXML private Label yourPlayerPositionLabel;
	@FXML private Label yourPlayerOffensiveLabel;
	@FXML private Label yourPlayerDefensiveLabel;
	@FXML private Label yourPlayerStaminaLabel;
	@FXML private Label yourPlayerPriceLabel;

	SimpleStringProperty name;
	SimpleStringProperty position;
	SimpleIntegerProperty off;
	SimpleIntegerProperty def;
	SimpleIntegerProperty stamina;
	SimpleIntegerProperty price;

	@FXML private ChoiceBox<String> curPosChoiceBox;
	@FXML private Label placedPlayersLabel;

	@FXML private TableView<Player> otherPlayersTableView;
	@FXML private TableColumn<Player, String> otherPlayersFirstNameCol;
	@FXML private TableColumn<Player, String> otherPlayersLastNameCol;
	@FXML private TableColumn<Player, String> otherPlayersPositionCol;
	@FXML private TableColumn<Player, Integer> otherPlayersOffCol;
	@FXML private TableColumn<Player, Integer> otherPlayersDefCol;
	@FXML private TableColumn<Player, Integer> otherPlayersStaminaCol;
	@FXML private TableColumn<Player, Integer> otherPlayersPriceCol;
	@FXML private TableColumn<Player, String> otherPlayersTeamCol;

	@FXML private Label otherPlayerNameLabel;
	@FXML private Label otherPlayerPositionLabel;
	@FXML private Label otherPlayerOffensiveLabel;
	@FXML private Label otherPlayerDefensiveLabel;
	@FXML private Label otherPlayerStaminaLabel;
	@FXML private Label otherPlayerPriceLabel;
	@FXML private Label otherPlayerTeamLabel;
	@FXML private Label transferWindowLabel1;
	@FXML private Button buyOtherPlayerButton;

	SimpleStringProperty otherName;
	SimpleStringProperty otherPosition;
	SimpleIntegerProperty otherOff;
	SimpleIntegerProperty otherDef;
	SimpleIntegerProperty otherStamina;
	SimpleIntegerProperty otherPrice;
	SimpleStringProperty otherTeam;	

	private Player yourSelectedPlayer = new Player();
	private Player otherSelectedPlayer = new Player();
	private static GameState gameState = new GameState();
	private static Pane rootLayout = null;

	static ListProperty<Player> yourPlayers = new SimpleListProperty<Player>();
	static ListProperty<Player> otherPlayers = new SimpleListProperty<Player>();
	
	public static SimpleIntegerProperty iFielded = new SimpleIntegerProperty();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gameState.addObserver(this);
		
		gamesWonLabel.setText("" + gameState.getMyTeam().getGamesWon());
		gamesLostLabel.setText("" + gameState.getMyTeam().getGamesLost());
		gamesDrawLabel.setText("" + gameState.getMyTeam().getGamesDraw());
		
		placedPlayersLabel.textProperty().bind(iFielded.asString());

		yourPlayerTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Player>() {
			@Override
			public void changed(ObservableValue<? extends Player> observable, Player oldValue, Player newValue) {
				yourSelectedPlayer = newValue;
				update(yourSelectedPlayer, null);
				
				if (gameState.getMyTeam().getNumOfPlayingPlayers() >= 11 && yourSelectedPlayer.getCurPosition() == null) {
					curPosChoiceBox.setDisable(true);
				} else {
					curPosChoiceBox.setDisable(false);
				}
			}
		});

		otherPlayersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Player>() {
			@Override
			public void changed(ObservableValue<? extends Player> observable, Player oldValue, Player newValue) {
				otherSelectedPlayer = newValue;
				update(otherSelectedPlayer, null);
			}
		});

		SimpleStringProperty isTransfer = null;
		if(MarketplaceLogic.isTransferWindow(gameState.getGameRound())) {
			isTransfer = new SimpleStringProperty("Open");
		} else {
			isTransfer = new SimpleStringProperty("Closed");
		}

		transferWindowLabel1.textProperty().bind(isTransfer);

		buyOtherPlayerButton.setDisable(!MarketplaceLogic.isTransferWindow(gameState.getGameRound()));

		curPosChoiceBox.setItems(FXCollections.observableArrayList(
				null,
				"Goalkeeper",
				"Attacker",
				"Midfielder",
				"Defender"));

		curPosChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				// First, check whether the player has chosen to field a GK
				if(newValue == "Goalkeeper") {
					// GK? get whether the team already has a Player with CurPos == "Goalkeeper"
					if(!gameState.getMyTeam().hasFieldedKeeper()) {
						// set curPos
						yourSelectedPlayer.setCurPosition(newValue);	
					} else {
						// team already has GK, notify user
						Dialogs.create()
				        .owner(FootballManager.getStage())
				        .title("Already have a keeper in the field!")
				        .masthead(null)
				        .message("You already have selected a keeper to play with!")
				        .showError();
						// set choicebox to the first child, null
						curPosChoiceBox.getSelectionModel().selectFirst();
						
						yourSelectedPlayer.setCurPosition(null);
					}
					// if not GK, just continue
				}
				
				if (gameState.getMyTeam().getNumOfPlayingPlayers() < 11 || (gameState.getMyTeam().getNumOfPlayingPlayers() == 11 && yourSelectedPlayer.getCurPosition() != null)) {
					yourSelectedPlayer.setCurPosition(newValue);
				}
				
				iFielded.set(gameState.getMyTeam().getNumOfPlayingPlayers());
				
			}
		});
		
		buyOtherPlayerButton.setOnAction((event) -> {
			if (!MarketplaceLogic.transferPlayer(otherSelectedPlayer.getTeam(), gameState.getMyTeam(), otherSelectedPlayer, gameState.getGameRound())) {
				Dialogs.create()
		        .owner(FootballManager.getStage())
		        .title("Player not bought!")
		        .masthead(null)
		        .message("Check if your not buying the 11th player of this team, and make sure your budget is sufficient.")
		        .showError();
			} else {
				TeamOverviewController.show(rootLayout, gameState);
			}
		});

		otherPlayersTeamCol.setCellValueFactory((param) -> {
			return new SimpleStringProperty(param.getValue().getClub());
		});
		
		this.new PlayersLoader().start();
	}

	class PlayersLoader extends Thread {
		public void run() {
			bindYourPlayerStats();
			bindOtherPlayerStats();

			ObservableList<Player> observablePlayers = FXCollections.observableList(gameState.getMyTeam().getPlayers());
			yourPlayers = new SimpleListProperty<Player>(observablePlayers);
			
			ObservableList<Player> otherObservablePlayers = FXCollections.observableArrayList();
			for (Team t : gameState.getLeague().getTeams()) {
				if (t == gameState.getMyTeam()) continue;
				otherObservablePlayers.addAll(t.getPlayers());
			}
			otherPlayers = new SimpleListProperty<Player>(otherObservablePlayers);
			
			yourPlayerTableView.itemsProperty().bind(yourPlayers);
			otherPlayersTableView.itemsProperty().bind(otherPlayers);
			
			yourPlayerTableView.getSelectionModel().selectFirst();
			otherPlayersTableView.getSelectionModel().selectFirst();
		}
	}

	/**
	 * 
	 */
	private void bindOtherPlayerStats() {
		otherName = new SimpleStringProperty();
		otherPlayerNameLabel.textProperty().bind(otherName);
		otherPosition = new SimpleStringProperty();
		otherPlayerPositionLabel.textProperty().bind(otherPosition);
		otherOff = new SimpleIntegerProperty();
		otherPlayerOffensiveLabel.textProperty().bind(otherOff.asString());
		otherDef = new SimpleIntegerProperty();
		otherPlayerDefensiveLabel.textProperty().bind(otherDef.asString());
		otherStamina = new SimpleIntegerProperty();
		otherPlayerStaminaLabel.textProperty().bind(otherStamina.asString());
		otherPrice = new SimpleIntegerProperty();
		otherPlayerPriceLabel.textProperty().bind(otherPrice.asString());
		otherTeam = new SimpleStringProperty();
		otherPlayerTeamLabel.textProperty().bind(otherTeam);
	}

	/**
	 * 
	 */
	private void bindYourPlayerStats() {
		name = new SimpleStringProperty();
		yourPlayerNameLabel.textProperty().bind(name);
		position = new SimpleStringProperty();
		yourPlayerPositionLabel.textProperty().bind(position);
		off = new SimpleIntegerProperty();
		yourPlayerOffensiveLabel.textProperty().bind(off.asString());
		def = new SimpleIntegerProperty();
		yourPlayerDefensiveLabel.textProperty().bind(def.asString());
		stamina = new SimpleIntegerProperty();
		yourPlayerStaminaLabel.textProperty().bind(stamina.asString());
		price = new SimpleIntegerProperty();
		yourPlayerPriceLabel.textProperty().bind(price.asString());
	}

	public static void show(Pane root, GameState gs) {
		gameState = gs;
		rootLayout = root;

		FXMLLoader l = new FXMLLoader();
		l.setLocation(FootballManager.class.getResource(teamOverviewFileName));
		try {
			AnchorPane teamOverview = (AnchorPane) l.load();
			((BorderPane) rootLayout).setCenter(teamOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		if (yourSelectedPlayer == o) {
			try {
				name.set(yourSelectedPlayer.getFirstName().concat(" ").concat(yourSelectedPlayer.getLastName()));
				position.set(yourSelectedPlayer.getReadablePosition());
				off.set(yourSelectedPlayer.getOffensive());
				def.set(yourSelectedPlayer.getDefensive());
				stamina.set(yourSelectedPlayer.getStamina());
				price.set(yourSelectedPlayer.getPrice());
//				curPosChoiceBox.getSelectionModel().select(curPosChoiceBox.getItems().indexOf(yourSelectedPlayer.getCurPosition()));
				curPosChoiceBox.getSelectionModel().select(yourSelectedPlayer.getCurPosition());
			} catch (NullPointerException e) {
				name.set(null);
				position.set(null);
				off.set(0);
				def.set(0);
				stamina.set(0);
				price.set(0);
			}
		} else if (otherSelectedPlayer == o) {
			try {
				otherName.set(otherSelectedPlayer.getFirstName().concat(" ").concat(otherSelectedPlayer.getLastName()));
				otherPosition.set(otherSelectedPlayer.getReadablePosition());
				otherOff.set(otherSelectedPlayer.getOffensive());
				otherDef.set(otherSelectedPlayer.getDefensive());
				otherStamina.set(otherSelectedPlayer.getStamina());
				otherPrice.set(otherSelectedPlayer.getPrice());
				otherTeam.set(otherSelectedPlayer.getClub());
			} catch (NullPointerException e) {
				otherName.set(null);
				otherPosition.set(null);
				otherOff.set(0);
				otherDef.set(0);
				otherStamina.set(0);
				otherPrice.set(0);
				otherTeam.set(null);
			}
		}
	}

	/**
	 * @return the yourSelectedPlayer
	 */
	public Player getSelectedPlayer() {
		return yourSelectedPlayer;
	}

	/**
	 * @return the gameState
	 */
	public static GameState getGameState() {
		return gameState;
	}
}