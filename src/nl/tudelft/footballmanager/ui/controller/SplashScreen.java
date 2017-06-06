/**
 * 
 */
package nl.tudelft.footballmanager.ui.controller;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * @author Mathijs de Boer <czorio4@gmail.com>
 *
 */
public class SplashScreen {

	private MediaView mv;
	private Stage stage;

	public SplashScreen(Stage stage, String url) {
		this.stage = stage;
		this.mv = new MediaView(new MediaPlayer(new Media(url)));
	}

	public void show() {
		this.stage.setScene(new Scene(new Group(mv)));
		
		// When movie ends, goto main menu.
		this.mv.getMediaPlayer().setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				TitleScreenController.show();
			}

		});
		
		// In order to use the MovieView as listener for a KeyEvent, you need to make it request focus first
		mv.requestFocus();
		
		// Listen for any key to be pressed
		mv.setOnKeyTyped(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				System.out.println("Intro Movie Skipped!");
				mv.getMediaPlayer().stop();
				TitleScreenController.show();
			}
			
		});
		
		//mv.setLayoutY(stage.heightProperty().divide(4.5).get());

		mv.fitHeightProperty().bind(stage.heightProperty());
		mv.fitWidthProperty().bind(stage.widthProperty());
		mv.getMediaPlayer().play();
	}

}
