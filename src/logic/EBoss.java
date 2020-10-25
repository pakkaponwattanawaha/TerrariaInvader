package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class EBoss extends Enemy {

	private int originalHp;
	private GameLogic gameLogic;
	private int bulletDelayTick = 0;

	public EBoss(GameLogic gameLogic) {
		super(15000, 0.15);
		this.originalHp = 15000;
		this.width = RenderableHolder.eBoss.getWidth();
		this.height = RenderableHolder.eBoss.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = -this.height;
		this.collideDamage = 3000;
		this.weight = 9.5;
		this.gameLogic = gameLogic;

		GameLogic.isBossAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		
		long now = System.nanoTime();
		this.x = Math.sin(4 * now * 1e-9 + Math.toRadians(90)) * ((SceneManager.SCENE_WIDTH - (this.width/3)) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		if (this.y < 40) {
			this.y += this.speed;
		}
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
			GameLogic.isBossAlive = false;
			GameLogic.killedBoss = true;
		}
		if(bulletDelayTick > 100) {
			if (bulletDelayTick % 17 == 16) {
				gameLogic.addPendingBullet(new Bullet(x, y - this.height / 7, 0, 25, -1, 9, this));
				RenderableHolder.laser.play();
			}
			if (bulletDelayTick % 73 == 72) {
				gameLogic.addPendingBullet(new Bullet(x, y - this.height / 7, 0, 8, -1, 1, this));
				RenderableHolder.fireBall.play();
			}
			if (bulletDelayTick % 300 == 257) {
				gameLogic.addPendingBullet(new Bullet(x, y - this.height / 7, 0, 35, -1, 8, this));
				RenderableHolder.powerAttackLaunch.play();
			}
		}
		bulletDelayTick++;
		
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eBoss, x, y);
		drawHpBar(gc);
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x + this.width/7, y + this.height * 0.3, this.width * 0.7, this.height * 0.7);
			collided = false;
		}
	}
	

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.originalHp;
		gc.setFill(Color.RED);
		gc.fillRect(this.x, this.y + this.height + 20, this.width * percentHp, 10);

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

	public double getWeight() {
		return weight;
	}

}
