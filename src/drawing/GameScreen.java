package drawing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import keyInput.CharacterInput;
import logic.Bullet;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;

public class GameScreen extends Canvas {

	private Queue<Bullet> pendingBullet;

	private static final int FPS = 60;
	private static final long LOOP_TIME = 1000000000 / FPS;
	private Thread gameAnimation;
	private boolean isAnimationRunning;

	public GameScreen(double width, double height) {
		// TODO Auto-generated constructor stub
		super(width, height);
		this.setVisible(true);
		this.isAnimationRunning = false;
		addKeyEventHandler();

		pendingBullet = new ConcurrentLinkedQueue<>();

	}

	public void startAnimation() {
		gameAnimation = new Thread(this::animationLoop, "Game Animation Thread");
		isAnimationRunning = true;
		gameAnimation.start();
	}

	public void stopAnimation() {
		isAnimationRunning = false;
	}

	private void animationLoop() {
		long lastLoopStartTime = System.nanoTime();
		while (isAnimationRunning) {
			long now = System.nanoTime();
			if (now - lastLoopStartTime >= LOOP_TIME) {
				lastLoopStartTime += LOOP_TIME;

				updateAnimation();
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateAnimation() {

		GraphicsContext gc = this.getGraphicsContext2D();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				while (!pendingBullet.isEmpty()) {
					RenderableHolder.getInstance().add(pendingBullet.poll());
				}
				gc.setFill(Color.BLACK);
				synchronized (RenderableHolder.getInstance().getEntities()) {
					for (IRenderable entity : RenderableHolder.getInstance().getEntities()) {
						if (entity.isVisible() && !entity.isDestroyed()) {
							entity.draw(gc);
						}
					}
				}

				RenderableHolder.getInstance().update();
			}
		});
	}

	private void addKeyEventHandler() {
		// TODO fill code
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub

				if (event.getCode().isArrowKey() || event.getCode() == KeyCode.SPACE
						|| event.getCode() == KeyCode.CONTROL) {
					CharacterInput.setKeyPressed(event.getCode(), true);
				}
			}
		});
		this.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getCode().isArrowKey() || event.getCode() == KeyCode.SPACE
						|| event.getCode() == KeyCode.CONTROL) {
					CharacterInput.setKeyPressed(event.getCode(), false);
				}

			}
		});
	}

	public void addPendingBullet(Bullet a) {

		pendingBullet.add(a);

	}

}
