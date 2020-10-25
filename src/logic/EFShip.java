package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class EFShip extends Enemy {
	private int bulletDelayTick = 0;
	private GameLogic gameLogic;
	private double startingX;

	public EFShip(GameLogic gameLogic, double x) {
		super(800, 0.5);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eShip.getWidth();
		this.height = RenderableHolder.eShip.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.startingX = x;
		this.y = -this.height;
		this.collideDamage = 25;
		this.weight = 4;
		this.gameLogic = gameLogic;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eShip, x, y);
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x + this.width/4.5, y + this.height/2.5, this.width * 0.6, this.height * 0.6);
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
		this.x = Math.sin(5 * now * 5e-11 + Math.toRadians(90) + startingX/SceneManager.SCENE_WIDTH * 180) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		y += this.speed;
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (bulletDelayTick % 35 == 0) {
			gameLogic.addPendingBullet(new Bullet(x+20, y-20, 5, 10, -1, 3, this));
			gameLogic.addPendingBullet(new Bullet(x+20, y-20, -5, 10, -1, 3, this));
			gameLogic.addPendingBullet(new Bullet(x+20, y-20, 0, 10, -1, 3, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
	}

	public double getWeight() {
		return weight;
	}

}
