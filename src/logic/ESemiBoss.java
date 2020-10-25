package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class ESemiBoss extends Enemy {
	private int originalHp;
	private int bulletDelayTick = 0;
	private double yOffset;
	private double xOffset;
	private long moveTime;
	private double yMultiplier;
	private boolean returning;
	private boolean charging;
	private GameLogic gameLogic;
	private long chargeDelay;

	public ESemiBoss(GameLogic gameLogic) {
		super(5000, 0.15);
		this.originalHp = 5000;
		this.width = RenderableHolder.eSemiBoss.getWidth();
		this.height = RenderableHolder.eSemiBoss.getHeight();
		this.yOffset = 0;
		this.xOffset = 0;
		this.yMultiplier = 0.7;
		this.visible = true;
		this.destroyed = false;
		this.x = (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = -this.height;
		this.collideDamage = 3000;
		this.weight = 7;
		this.gameLogic = gameLogic;
		this.chargeDelay = System.nanoTime() + ThreadLocalRandom.current().nextLong(8000000000l, 10000000000l);
		this.returning = false;
		this.charging = false;
		this.moveTime = System.nanoTime();

		GameLogic.isSemiAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		long now = System.nanoTime();

		if (now >= this.chargeDelay) {
			this.charging = false;
			if (this.returning) {
				if (this.y >= yOffset) {
					this.y -= 6.5;
				} else {
					this.returning = false;
					this.chargeDelay = now + ThreadLocalRandom.current().nextLong(6000000000l, 8000000000l);
				}

			} else if (this.y < SceneManager.SCENE_HEIGHT * this.yMultiplier) {
				this.y += (SceneManager.SCENE_HEIGHT * this.yMultiplier) / Math.cbrt(SceneManager.SCENE_HEIGHT);
			} else {
				this.returning = true;
			}
		} else if (now >= this.chargeDelay - 1000000000l) {
			this.y -= 11 * SceneManager.SCENE_HEIGHT / (SceneManager.SCENE_HEIGHT - this.y);
			this.charging = true;

		} else {
			this.moveTime += GameLogic.LOOP_TIME;
			this.x = Math.sin(3.5 * (this.moveTime) * 1e-9 + Math.toRadians(90))
					* ((SceneManager.SCENE_WIDTH - this.width) / 2) + (SceneManager.SCENE_WIDTH - this.width) / 2.0;
			this.y += this.speed;
			if (y >= SceneManager.SCENE_HEIGHT * 0.5) {
				this.yOffset = - this.height / 2;
			} else {
				this.yOffset = this.y;
			}
			this.xOffset = this.x;
		}

		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (now < this.chargeDelay - 1000000000l) {
			if (bulletDelayTick % 35 == 0) {
				gameLogic.addPendingBullet(new Bullet(x, y, 0, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x - 50, y - 20, 9, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x + 50, y - 20, -9, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x - 15, y, 5, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x + 15, y, -5, 15, -1, 4, this));
				RenderableHolder.fireBall.play();
			}
			bulletDelayTick++;
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eSemiBoss, x, y);
		drawHpBar(gc);
		if (collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0, 4)];
			gc.drawImage(spark, x + this.width / 10, y + this.height * 0.32, this.width * 0.8, this.height * 0.8);
			collided = false;
		}
		if (charging) {
			drawDangerZone(gc);
		}
	}

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.originalHp;
		gc.setFill(Color.RED);
		gc.fillRect(this.x + this.width / 5, this.y + this.height + 20, this.width * percentHp * 0.6, 10);
	}

	private void drawDangerZone(GraphicsContext gc) {

		LinearGradient linearGrad = new LinearGradient(0, // start X
				0, // start Y
				0.5, // end X
				0, // end Y
				true, // proportional
				CycleMethod.REFLECT, // cycle colors
				// stops
				new Stop(0.1f, Color.rgb(255, 30, 30, 0.3)), new Stop(1.0f, Color.rgb(255, 150, 150, 0.3)));
		gc.setFill(linearGrad);

		gc.fillRect(this.xOffset, 0, this.width, SceneManager.SCENE_HEIGHT * 2);
	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + width / 2);
		bound.setRadius(height / 2);
		return bound;
	}

	public double getWeight() {
		return weight;
	}

}
