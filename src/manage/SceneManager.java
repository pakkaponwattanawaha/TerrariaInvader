package manage;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import drawing.MainMenu;

public final class SceneManager {
	private static Stage primaryStage;
	private static Canvas mainMenuCanvas = new MainMenu();
	private static Scene mainMenuScene = new Scene(new Pane(mainMenuCanvas));
	public static final int SCENE_WIDTH = 589;
	public static final int SCENE_HEIGHT = 800;
	
	public static void initialize(Stage stage) {
		primaryStage = stage;
		primaryStage.show();
	}
	
	public static void gotoMainMenu() {
		primaryStage.setScene(mainMenuScene);
		mainMenuCanvas.requestFocus();
	}
	
	public static void gotoSceneOf(Canvas canvas) {
		primaryStage.setScene(new Scene(new Pane(canvas), 589, 800));
		canvas.requestFocus();
	}
}
