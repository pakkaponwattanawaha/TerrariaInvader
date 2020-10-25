package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class Bullet extends Unit {

	private Image bulletSprite;
	private int type;
	private static int zCounter = -700; // Bullet z is between -1100 and -300 inclusive.
	private int speedX, speedY;
	private boolean exploding = false;

	protected Bullet(double x, double y, int speedX, int speedY, int side, int type, Unit e) {
		super(0.001, speedY);
		// TODO Auto-generated constructor stub
		this.speedX = speedX;
		this.speedY = speedY;
		this.side = side;
		if (side == -1) {
			this.z = zCounter - 400;
		} else {
			this.z = zCounter;
		}
		zCounter++;
		if (zCounter > -300) {
			zCounter = -700;
		}
		this.type = type;
		if (type == -1) {
			this.height = RenderableHolder.bullet.getHeight();
			this.width = RenderableHolder.bullet.getWidth();
			bulletSprite = RenderableHolder.bullet;
			this.collideDamage = 20 + 3 * Player.atkLvl;
		} else if (type == 0) {
			this.height = RenderableHolder.bullet.getHeight();
			this.width = RenderableHolder.bullet.getWidth();
			bulletSprite = RenderableHolder.bullet;
			this.collideDamage = 25 + 5 * Player.atkLvl;
		} else if (type == 1) {
			this.height = RenderableHolder.bossBullet.getHeight();
			this.width = RenderableHolder.bossBullet.getWidth();
			bulletSprite = RenderableHolder.bossBullet;
			this.collideDamage = 70 + Distance.distance/20000;
		} else if (type == 2) {
			this.height = RenderableHolder.roundBulletB.getHeight();
			this.width = RenderableHolder.roundBulletB.getWidth();
			bulletSprite = RenderableHolder.roundBulletB;
			this.collideDamage = 25;
		} else if (type == 3) {
			this.height = RenderableHolder.spikeBullet.getHeight();
			this.width = RenderableHolder.spikeBullet.getWidth();
			bulletSprite = RenderableHolder.spikeBullet;
			this.collideDamage = 20;
		} else if (type == 4) {
			this.height = RenderableHolder.eyeBullet.getHeight();
			this.width = RenderableHolder.eyeBullet.getWidth();
			bulletSprite = RenderableHolder.eyeBullet;
			this.collideDamage = 30 + Distance.distance/1200;
		} else if (type == 5) {
			this.height = RenderableHolder.eyeBulletG.getHeight();
			this.width = RenderableHolder.eyeBulletG.getWidth();
			bulletSprite = RenderableHolder.eyeBulletG;
			this.collideDamage = 25;
		} else if (type == 6) {
			this.height = RenderableHolder.powerAttack.getHeight();
			this.width = RenderableHolder.powerAttack.getWidth();
			bulletSprite = RenderableHolder.powerAttack;
			this.collideDamage = 20;
		} else if (type == 8) {
			this.height = RenderableHolder.bossPower.getHeight();
			this.width = RenderableHolder.bossPower.getWidth();
			bulletSprite = RenderableHolder.bossPower;
			this.collideDamage = 500 + Distance.distance/800;
		} else if (type == 9) {
			this.height = RenderableHolder.bossLow.getHeight();
			this.width = RenderableHolder.bossLow.getWidth();
			bulletSprite = RenderableHolder.bossLow;
			this.collideDamage = 10 + Distance.distance/30000;
		} else if (type == 10) {
			this.height = RenderableHolder.beamSmallG.getHeight();
			this.width = RenderableHolder.beamSmallG.getWidth();
			bulletSprite = RenderableHolder.beamSmallG;
			this.collideDamage = 20;
		}
		if (side == 1) {
			this.x = x + (e.width - this.width) / 2.0;
			this.y = y - this.height;
		} else if (side == -1) {
			this.x = x + (e.width - this.width) / 2.0;
			this.y = y + e.height;
		}
		this.visible = true;
		this.destroyed = false;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(bulletSprite, x, y);
	}

	@Override
	public void onCollision(Unit others) {
		// TODO Auto-generated method stub
		if (type == 6) {
			exploding = true;
			this.visible = false;

		}
		else {
			this.hp -= others.collideDamage;
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (type == 7) {
			this.collideDamage = 0;
			this.destroyed = true;
			this.visible = false;
		}
		if (exploding) {
			type = 7;
			this.width = 200; // explosion area of effect
			this.height = 200; // explosion area of effect
			this.x = x - this.width/2;
			this.y = y - this.height/2 + 30;
			this.collideDamage = 200 + 40 * Player.atkLvl; // explode damage
			Explosion e = new Explosion(x, y, width + 50, height + 50, z);
			e.playSfx();
			RenderableHolder.getInstance().add(e);
			exploding = false;
		}
		if (type == 7) {
				y += speedY;
		}
		if (type != 7) {
			x -= speedX;
			if (side == 1) {
				if(type == 6) {
					y-=speedY/3;
				}else
				y -= speedY;

			} else {
				y += speedY;
			}
		}
		if (this.hp <= 0 || isOutOfScreen()) {
			this.destroyed = true;
			this.visible = false;
		}

	}

	private boolean isOutOfScreen() {
		return (int) this.y > SceneManager.SCENE_HEIGHT || (int) this.y < 0;
	}

	@Override
	public Shape getBoundary() {
		if (type == -1 || type == 0 || type == 1 || type == 8 || type == 9) {
			Rectangle bound = new Rectangle();
			bound.setX(x);
			bound.setY(y);
			bound.setWidth(width);
			bound.setHeight(height);
			return bound;
		} else {
			Circle bound = new Circle();
			bound.setCenterX(x + width / 2);
			bound.setCenterY(y + height / 2);
			bound.setRadius(width / 2);
			return bound;
		}
	}
	
	public int getSide() {
		return this.side;
	}

}
