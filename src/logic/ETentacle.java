package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class ETentacle extends Enemy {

	private int bulletDelayTick = 0;
	private GameLogic gameLogic;
	private double startingX;

	public ETentacle(GameLogic gameLogic, double x) {
		super(150, 2.5);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eTentacle.getWidth();
		this.height = RenderableHolder.eTentacle.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.startingX = x;
		this.y = -this.height;
		this.collideDamage = 20;
		this.weight = 1.5;
		this.gameLogic = gameLogic;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eTentacle, x, y);
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x + 5, y, this.width * 0.85, this.height * 0.85);
			collided = false;
		}
	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Rectangle bound = new Rectangle();
		bound.setX(x);
		bound.setY(y);
		bound.setWidth(width);
		bound.setHeight(height);
		return bound;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		long now = System.nanoTime();
		this.x = Math.sin(5 * now * 1e-10 + Math.toRadians(90) + startingX/SceneManager.SCENE_WIDTH * 180) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		y += this.speed;
		
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (bulletDelayTick % 40 == 0) {
			gameLogic.addPendingBullet(new Bullet(x, y - this.width / 2, 0, 15, -1, 5, this));
			RenderableHolder.laser.play();
		}
		bulletDelayTick++;
	}

	public double getWeight() {
		return weight;
	}
}
