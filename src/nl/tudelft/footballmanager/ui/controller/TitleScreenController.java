/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nl.tudelft.footballmanager.FootballManager;
import nl.tudelft.footballmanager.model.GameState;

/**
 * @author Mathijs de Boer <czorio4@gmail.com>
 *
 */
public class TitleScreenController implements Initializable, Observer {
	
	private static final String titleScreenFileName = "ui/view/TitleScreen.fxml";
	
	@FXML private Button newGameButton;
	@FXML private Button loadGameButton;
	@FXML private Button quitGameButton;

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(String.format("%s:\n\t%s\n\t%s", this.getClass(), o, arg));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		newGameButton.setOnAction((event) -> {
			NewGameViewController.show();
		});
		
		loadGameButton.setOnAction((event) -> {
			RootViewController.loadGame();
		});
		
		// Quit application
		quitGameButton.setOnAction((event) -> {
			Platform.exit();
			System.err.println("Game quit!");
		});

	}
	
	public GameState loadGame(GameState gamestate) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Load game");
		configureFileChooser(chooser);
		
		// file type filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
		chooser.getExtensionFilters().add(extFilter);

		File selectedFile = FootballManager.getOpenFile(chooser);
		if (selectedFile != null) {
			System.out.println("Load file: " + selectedFile.getAbsolutePath());

			gamestate = GameState.load(selectedFile);
			System.out.println("GameState: " + gamestate.toString());
		} else {
			System.err.println("No file selected!");
		}

		return gamestate;
	}
	
	public void configureFileChooser(FileChooser fc) {
		// Standard dir is working dir of application
		fc.setInitialDirectory(new File(System.getProperty("user.dir")));
		fc.setSelectedExtensionFilter(new ExtensionFilter("XML Game files", ".xml"));
	}

	/**
	 * 
	 */
	public static void show() {
		FXMLLoader l = new FXMLLoader();
		l.setLocation(FootballManager.class.getResource(titleScreenFileName));
		try {
			AnchorPane titleScreen = (AnchorPane) l.load();
			FootballManager.getStage().setScene(new Scene(titleScreen));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
