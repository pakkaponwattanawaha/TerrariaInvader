package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class EDevil extends Enemy {

	private int bulletDelayTick = 0;
	private double yMultiplier;
	private GameLogic gameLogic;
	private boolean inPosition;

	public EDevil(GameLogic gameLogic, double x) {
		super(100, 0.3);
		this.width = RenderableHolder.eDevil.getWidth();
		this.height = RenderableHolder.eDevil.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height;
		this.collideDamage = 80;
		this.weight = 2;
		this.gameLogic = gameLogic;
		this.yMultiplier = ThreadLocalRandom.current().nextDouble(0.6, 0.8);
		this.inPosition = false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		if (this.y <= SceneManager.SCENE_HEIGHT * this.yMultiplier) {
			this.y += (SceneManager.SCENE_HEIGHT * this.yMultiplier - y) / Math.sqrt(SceneManager.SCENE_HEIGHT);
		}
		else {
			this.inPosition = true;
		}
		this.y += this.speed;
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (inPosition) {
			if (bulletDelayTick % 25 == 0) {
				gameLogic.addPendingBullet(new Bullet(x, y - this.height / 2, 15, 0, -1, 10, this));
				gameLogic.addPendingBullet(new Bullet(x, y - this.height / 2, -15, 0, -1, 10, this));
				RenderableHolder.laser.play();
			}
			bulletDelayTick++;
		}

	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub	
		gc.drawImage(RenderableHolder.eDevil, x, y);
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x - 5, y, this.width, this.height);
			collided = false;
		}
	}


	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + width / 2);
		bound.setRadius(width / 2);
		return bound;
	}

	public double getWeight() {
		return weight;
	}
}
