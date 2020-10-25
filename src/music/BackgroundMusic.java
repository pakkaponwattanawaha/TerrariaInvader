package music;

import javafx.concurrent.Task;
import javafx.scene.media.MediaPlayer;
import sharedObject.RenderableHolder;

public class BackgroundMusic {
	private Thread bgmLoop;
	private MediaPlayer bgm;
	final Task<Object> task;
	
	public BackgroundMusic() {
		task = new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				bgm = RenderableHolder.bgm;
				bgm.setVolume(0.6);
				bgm.setCycleCount(MediaPlayer.INDEFINITE);
				bgm.play();
				return null;
			}
		};
	}

	public void startBackgroundMusic() {
		bgmLoop = new Thread(task,"Game BGM Thread");
		bgmLoop.start();
	}
	
	public void stopBackgroundMusic() {
		bgm.stop();
	}

}
