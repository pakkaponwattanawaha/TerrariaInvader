package drawing;

import gamemain.GameMain;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Distance;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class GameWinnerScreen extends Canvas {
	private static final Font TITLE_FONT = RenderableHolder.titleFont;
	private static final Font Distance_FONT = RenderableHolder.inGameFont;
	private MediaPlayer music = RenderableHolder.gameWinnerMusic;

	public GameWinnerScreen() {
		super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		GraphicsContext gc = this.getGraphicsContext2D();
		music.play();
		gc.drawImage(RenderableHolder.backgroundW, 0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
		gc.setFont(TITLE_FONT);
		gc.fillText("YOU WIN", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT / 4);
		gc.setFont(Distance_FONT);
		gc.setFill(Color.WHITE);
		String distance = "You travelled " + Distance.distance + " m";
		gc.fillText(distance, SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 2 / 4);
		gc.setFill(Color.WHITE);
		gc.fillText("Press Enter to Play Again", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 4 / 5);
		this.addKeyEventHandler();
	}

	private void addKeyEventHandler() {
		// TODO Fill Code

		this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getCode() == KeyCode.ENTER) {
					music.stop();
					GameMain.newGame();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					Platform.exit();
				}
			}
		});
	}

	
	
}
