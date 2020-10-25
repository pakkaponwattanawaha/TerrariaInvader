package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import keyInput.CharacterInput;
import manage.SceneManager;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;

//@SuppressWarnings("restriction")
public class Player extends Unit implements IRenderable {
	private Image playerImage = null;
	GameLogic gameLogic;
	private int bulletDelayTick = 0, prevbulletTick = 0;
	private double maxHp;
	private int maxShield;
	private double shield;
	private int shieldLvl;
	private int regenLvl;
	private long regenTimeOut = 0;	
	private boolean isDamaged;
	private boolean collided;
	private long TripleFireTimeOut = 0;
	private int powerAttack = 0;
	private int fireMode = 0;
	private boolean fullShield;
	private final double shieldReduction = 0.35;
    public static int atkLvl;
	
	public Player(GameLogic gameLogic) {
		// TODO Auto-generated constructor stub
		super(2500, 6);
		this.maxHp = this.hp;
		maxShield = 1200;
		shield = maxShield;
		shieldLvl = 1;
		regenLvl = 1;
		atkLvl = 1;
		isDamaged = false;

		this.z = 0;

		playerImage = RenderableHolder.slime;

		this.gameLogic = gameLogic;
		
		
		if (playerImage != null) {
			this.width = playerImage.getWidth();
			this.height = playerImage.getHeight();
			// System.out.println(imageWidth + " " + imageHeight);
			this.x = SceneManager.SCENE_WIDTH / 2 - this.width / 2;
			this.y = (SceneManager.SCENE_HEIGHT - this.height) - 60;
			// this.speed = 3;
			this.side = 1;
			this.collideDamage = 15; // test
		} else {
			width = 0;
			height = 0;
		}
	}

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.maxHp;
		if (percentHp >= 0.65) {
			LinearGradient linearGrad = new LinearGradient(0, // start X
					0, // start Y
					0, // end X
					1, // end Y
					true, // proportional
					CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.RED), new Stop(1.0f, Color.DARKRED));
			gc.setFill(linearGrad);
		} else if (percentHp >= 0.25) {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.ORANGE), new Stop(1.0f, Color.RED));
			gc.setFill(linearGrad);
		} else {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.BLACK), new Stop(1.0f, Color.DARKGRAY));
			gc.setFill(linearGrad);
		}
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200, 740, 400 * percentHp, 10);
		
		gc.setFill(Color.WHITE);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 202, 740, 2, 10);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 + 200, 740, 2, 10);

	}

	private void drawShieldBar(GraphicsContext gc) {
		double percentShield = this.shield / this.maxShield;
		if (percentShield >= 0.65) {
			
			LinearGradient linearGrad = new LinearGradient(0, // start X
					0, // start Y
					0, // end X
					1, // end Y
					true, // proportional
					CycleMethod.NO_CYCLE, // cycle colors
					// stops
					new Stop(0.1f, Color.ROYALBLUE), new Stop(1.0f, Color.BLUE));
			gc.setFill(linearGrad);
		} else if (percentShield >= 0.25) {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.DEEPSKYBLUE), new Stop(1.0f, Color.DODGERBLUE));
			gc.setFill(linearGrad);
		} else {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.LIGHTBLUE), new Stop(1.0f, Color.STEELBLUE));
			gc.setFill(linearGrad);
		}

		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200, 760, 400 * percentShield, 10);
		
		gc.setFill(Color.WHITE);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 202, 760, 2, 10);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 + 200, 760, 2, 10);

	}

	private void drawItemsStatus(GraphicsContext gc) {
		gc.setFont(RenderableHolder.inGameFontSmall);
		//FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		
		gc.setFill(Color.RED);
		String atkLevelDisplay = "Firepower Level : " + Integer.toString(Player.atkLvl);
		double atkLevelDisplay_height = RenderableHolder.inGameFontSmall.getSize();
		gc.fillText(atkLevelDisplay, 10, 10 + atkLevelDisplay_height);
		
		gc.setFill(Color.DODGERBLUE);
		String shieldLevelDisplay = "Shield Level : " + Integer.toString(this.shieldLvl);
		double shieldLevelDisplay_height = RenderableHolder.inGameFontSmall.getSize();
		gc.fillText(shieldLevelDisplay, 10, 32 + shieldLevelDisplay_height);
		
		gc.setFill(Color.MEDIUMSEAGREEN);
		String regenLevelDisplay = "Regen Level : " + Integer.toString(this.regenLvl);
		double regenLevelDisplay_height = RenderableHolder.inGameFontSmall.getSize();
		gc.fillText(regenLevelDisplay, 10, 54 + regenLevelDisplay_height);
		
		if (powerAttack > 0 && fireMode == 1) {
			
			gc.setFill(Color.DARKORANGE);
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = RenderableHolder.inGameFontSmall.getSize();
			gc.fillText(remainPowerAttack, 10, 80 + remainPowerAttack_height);

			gc.setFill(Color.MAGENTA);
			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = RenderableHolder.inGameFontSmall.getSize();
			gc.fillText(TripleFire, 10, 80 + remainPowerAttack_height + TripleFire_height);
		} else if (powerAttack > 0) {
			gc.setFill(Color.DARKORANGE);
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = RenderableHolder.inGameFontSmall.getSize();
			gc.fillText(remainPowerAttack, 10, 80 + remainPowerAttack_height);
		} else if (fireMode == 1) {
			gc.setFill(Color.MAGENTA);
			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = RenderableHolder.inGameFontSmall.getSize();
			gc.fillText(TripleFire, 10, 80 + TripleFire_height);
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(playerImage, x, y);
		drawHpBar(gc);
		drawShieldBar(gc);
		drawItemsStatus(gc);
		
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x, y, 100, 100);
			collided = false;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (CharacterInput.getKeyPressed(KeyCode.UP)) {
			forward(true);
		}
		if (CharacterInput.getKeyPressed(KeyCode.DOWN)) {
			forward(false);
		}
		if (CharacterInput.getKeyPressed(KeyCode.RIGHT)) {
			turn(true);
		}
		if (CharacterInput.getKeyPressed(KeyCode.LEFT)) {
			turn(false);
		}
		if (CharacterInput.getTriggeredCtrl().poll() == KeyCode.CONTROL) {
			

			if (this.powerAttack > 0) {
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 0, 40, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 0, -40, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 12, 0, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -12, 0, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 10, 20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -10, -20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 10, -20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -10, 20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 6, 35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -6, 35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 6, -35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -6, -35, 1, 6, this));
				RenderableHolder.powerAttackLaunch.play();
				
				powerAttack--;

			}
		}
		
		if (CharacterInput.getKeyPressed(KeyCode.SPACE)) {
			// shoot a bullet
			
			if (bulletDelayTick - prevbulletTick > 7) {
				if (fireMode == 0) {
					gameLogic.addPendingBullet(new Bullet(x, y + 18, 0, 20, 1, 0, this));
					RenderableHolder.laser.play();
				} else if (fireMode == 1) {
					gameLogic.addPendingBullet(new Bullet(x, y + 18, 0, 20, 1, -1, this));
					RenderableHolder.laser.play();
					gameLogic.addPendingBullet(new Bullet(x - 15, y + 18, 1, 20, 1, -1, this));
					RenderableHolder.laser.play();
					gameLogic.addPendingBullet(new Bullet(x + 15, y + 18, -1, 20, 1, -1, this));
					RenderableHolder.laser.play();
				}
				prevbulletTick = bulletDelayTick;
			}

		}
		bulletDelayTick++;
		if (this.TripleFireTimeOut <= System.nanoTime()) {
			this.TripleFireTimeOut = 0;
			fireMode = 0;
		}

		if (isDamaged) {
			if (this.regenTimeOut <= System.nanoTime()) {
				this.isDamaged = false;
			}
		} else if (!fullShield) {
			this.shield += (this.regenLvl + 5) * this.maxShield / 2000;
			if (this.shield > this.maxShield) {
				this.shield = this.maxShield;
				this.fullShield = true;
			}
		}

	}

	@Override
	public void onCollision(Unit other) {
		// TODO Auto-generated method stub

		if (other instanceof Enemy || other instanceof Bullet) {
			double damageReduced;
			if (other instanceof Enemy) {
				damageReduced = other.collideDamage * this.shieldReduction;
			} else {
				damageReduced = other.collideDamage;
			}
			if (damageReduced > this.shield) {
				damageReduced = this.shield;
				this.shield = 0;
			} else {
				this.shield -= damageReduced;
			}
			this.hp -= (other.collideDamage - damageReduced);
			this.isDamaged = true;
			this.fullShield = false;
			this.regenTimeOut = System.nanoTime() + (15 - this.regenLvl) * 100000000l;
			this.collided = true;
			RenderableHolder.hits[ThreadLocalRandom.current().nextInt(0,2)].play();
		}

		if (other instanceof IHealth) {
			this.hp += ((IHealth) other).getHPStorage();
			if (this.hp > this.maxHp) {
				this.hp = this.maxHp;
			}
		}
		if (other instanceof ITripleFirePotion) {
			this.fireMode = 1;
			this.TripleFireTimeOut = System.nanoTime() + 15000000000l; // 15secs of triple  fire
		}
		if (other instanceof IPowerAttack) {
			powerAttack+=2;
			if(powerAttack > 6) {
				powerAttack = 6; // maximum power attack == 9
			}
		}
		if (other instanceof IShieldMaxPotion) {
			this.maxShield += ((IShieldMaxPotion) other).getShieldStorage();
			this.shield += ((IShieldMaxPotion) other).getShieldStorage();
			shieldLvl++;
			if(shieldLvl > 5) {
				shieldLvl = 5;
				this.hp += 250;
				if (this.hp > this.maxHp) {
					this.hp = this.maxHp;
				}
			}
		}
		if (other instanceof IShieldRegenPotion) {
			regenLvl++;
			if(regenLvl > 5) {
				regenLvl = 5;
				this.hp += 250;
				if (this.hp > this.maxHp) {
					this.hp = this.maxHp;
				}
			}
		}
		if (other instanceof IAttackPotion) {
			atkLvl++;
			if(atkLvl > 5) {
				atkLvl = 5;
				this.hp += 250;
				if (this.hp > this.maxHp) {
					this.hp = this.maxHp;
				}
			}
		}

		if (this.hp <= 0) {
			this.destroyed = true;
			this.visible = false;
			Explosion e = new Explosion(x, y, width, height, z);
			e.playSfx();
			RenderableHolder.getInstance().add(e);
		}

	}

	private void forward(boolean b) {
		if (b == true) { // move forward
			if (this.y - speed >= 0) {
				this.y -= speed;
			}
		}
		if (b == false) { // move backward
			if (this.y + speed + this.height <= SceneManager.SCENE_HEIGHT) {
				this.y += speed;
			}
		}
	}

	private void turn(boolean b) {
		if (b == true) { // move right
			if (this.x + speed + this.width <= SceneManager.SCENE_WIDTH) {
				this.x += speed;
			}
		}
		if (b == false) { // move left
			if (this.x - speed >= 0) {
				this.x -= speed;
			}
		}
	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + height / 2);
		bound.setRadius(width / 4);
		return bound;
	}
}
