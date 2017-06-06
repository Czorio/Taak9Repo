package nl.tudelft.footballmanager;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.tudelft.footballmanager.ui.controller.SplashScreen;
import nl.tudelft.footballmanager.ui.controller.TitleScreenController;

/**
 * Main class of the game.
 * @author PassiVision
 * @version 1.0.0
 */
public class FootballManager extends Application {
	private static Stage stage;

	private static boolean bShowSplash = true;

	@Override
	public void start(Stage stage) {
		FootballManager.stage = stage;
		FootballManager.stage.setTitle("Football Manager 2142");
		
		// Get the dimensions of the screen and make the window slightly smaller,
		// maintaining 16:9
		Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		FootballManager.stage.setHeight(screenDimensions.getHeight() - 100);
		FootballManager.stage.setWidth(((screenDimensions.getHeight() - 100)/9)*16);

		FootballManager.stage.centerOnScreen();
		FootballManager.stage.show();

		FootballManager.stage.getIcons().add(new Image(("file:img/ball_icon.png")));

		// Playing an MP4 splash video does not work on Mac OS X, so it is disabled
		if(bShowSplash && !System.getProperty("os.name").equals("Mac OS X")) {		
			SplashScreen ss = null;
			try {
				ss = new SplashScreen(stage, new File("vid/intro.mp4").toURI().toURL().toString());
				ss.show();
			} catch (MalformedURLException e) {
				System.err.println("Showing splash screen failed, skipping...");
				TitleScreenController.show();
			}
		} else {
			TitleScreenController.show();
		}	

	}

	public static void main(String[] args) {
		// Launch the JavaFX application
		launch(args);
	}

	/**
	 * @param chooser
	 * @return
	 */
	public static File getOpenFile(FileChooser chooser) {
		return chooser.showOpenDialog(stage);
	}

	/**
	 * @param chooser
	 * @return
	 */
	public static File getSaveFile(FileChooser chooser) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String now = sdf.format(new Date());
		String fileName = now + ".xml";
		System.out.println("Filename: " + fileName);
		chooser.setInitialFileName(fileName);
		return chooser.showSaveDialog(stage);
	}

	/**
	 * @return the stage
	 */
	public static Stage getStage() {
		return stage;
	}
}
