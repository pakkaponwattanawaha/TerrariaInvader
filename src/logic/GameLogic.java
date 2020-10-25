package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

import drawing.GameScreen;
import gamemain.GameMain;
import javafx.scene.image.Image;
import manage.SceneManager;
import sharedObject.RenderableHolder;

public class GameLogic {

	private Queue<Bullet> pendingBullet;

	private List<Unit> gameObjectContainer;
	private static final int FPS = 60;
	public static final long LOOP_TIME = 1000000000 / FPS;

	private int gameOverCountdown = 60;
	private int gameWinCountdown = 60; //1 sec delay after the game ends

	private double maxEnemyCap;
	public static double currentEnemyWeight;
	public static boolean isBossAlive;
	public static boolean isSemiAlive;
	public static boolean killedBoss;
	public static boolean killedSemi;
	private int stageLevel;

	private long nextItemsSpawnTime;
	public static long relaxTime;

	private GameScreen canvas;
	private boolean isGameRunning;

	private Player player;
	private ESemiBoss esemi;
	private EBoss eboss;

	public GameLogic(GameScreen canvas) {
		this.gameObjectContainer = new ArrayList<Unit>();
		this.maxEnemyCap = 6; // default enemy capacity
		GameLogic.currentEnemyWeight = 0;
		stageLevel = 1;
		GameLogic.isBossAlive = false;
		GameLogic.isSemiAlive = false;
		killedBoss = false;
		killedSemi = false;

		RenderableHolder.getInstance().add(new Background());
		RenderableHolder.getInstance().add(new Distance());
		player = new Player(this);
		addNewObject(player);

		spawnEnemy();

		this.canvas = canvas;
		nextItemsSpawnTime = System.nanoTime() + ThreadLocalRandom.current().nextLong(8000000000l, 10000000000l);
		pendingBullet = new ConcurrentLinkedQueue<>();

	}

	protected void addNewObject(Unit unit) {
		if(unit instanceof Enemy) {
			GameLogic.currentEnemyWeight += ((Enemy) unit).getWeight();
		}
		gameObjectContainer.add(unit);
		RenderableHolder.getInstance().add(unit);
	}

	protected void winGame() {
		this.isGameRunning = false;
		this.gameObjectContainer.clear();
		this.pendingBullet.clear();
	}

	public void startGame() {
		this.isGameRunning = true;
		new Thread(this::gameLoop, "Game Loop Thread").start();
	}

	public void stopGame() {
		this.isGameRunning = false;
		this.gameObjectContainer.clear();
		this.pendingBullet.clear();

	}

	private void gameLoop() {
		long lastLoopStartTime = System.nanoTime();
		GameLogic.relaxTime = System.nanoTime() + 6000000000l;
		GameLogic.currentEnemyWeight += 10.8;
		while (isGameRunning) {
			long elapsedTime = System.nanoTime() - lastLoopStartTime;
			if (elapsedTime >= LOOP_TIME) {
				lastLoopStartTime += LOOP_TIME;
				updateGame();
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateGame() {
		// TODO fill code

		if (killedSemi) {
			GameLogic.relaxTime = System.nanoTime() + 12000000000l; //wait 12 secs til the next state
			GameLogic.currentEnemyWeight += 21.6;

			nextItemsSpawnTime = System.nanoTime() + 11000000000l;
			
			addNewObject(new IShieldMaxPotion((SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth()) / 2 - 100));
			addNewObject(new IAttackPotion((SceneManager.SCENE_WIDTH - RenderableHolder.attackPotion.getWidth())/2));
			addNewObject(new IShieldRegenPotion((SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth()) / 2 + 100));
			addNewObject(new IPowerAttack((SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth()) / 2 + 200));
			killedSemi = false;
		}

		if (relaxTime >= System.nanoTime()) {
			GameLogic.currentEnemyWeight -= 0.03;
		}
		spawnEnemy();
		spawnItems();

		while (!pendingBullet.isEmpty()) {
			gameObjectContainer.add(pendingBullet.poll());

		}

		for (Unit i : gameObjectContainer) {
			i.update();
		}
		for (Unit i : gameObjectContainer) {
			for (Unit j : gameObjectContainer) {
				if (i != j && ((Unit) i).collideWith((Unit) j)) {
					((Unit) i).onCollision((Unit) j);
				}
			}
		}
		int i = 0;
		while (i < gameObjectContainer.size()) {
			if (gameObjectContainer.get(i).isDestroyed()) {
				gameObjectContainer.remove(i);
			} else {
				i++;
			}
		}
		if (player.isDestroyed()) {
			gameOverCountdown--;
		}
		if (killedBoss) {
			gameWinCountdown--;
		}
		if (gameWinCountdown == 0) {
			GameMain.winGame();
		}
		if (gameOverCountdown == 0) {
			GameMain.loseGame();
		}

		double mod = Distance.distance / 500;
		Distance.hiddenDistance += 0.5 + mod / 4;
		Distance.distance = (int) Distance.hiddenDistance;

	}

	public void addPendingBullet(Bullet a) {
		pendingBullet.add(a);
		canvas.addPendingBullet(a);
	}

	private void spawnEnemy() {
		Random r = new Random();
		this.maxEnemyCap = 6 + stageLevel * 0.85;

		if (Distance.distance >= 5000 && !isSemiAlive) {
			esemi = new ESemiBoss(this);
			addNewObject(esemi);
			GameLogic.currentEnemyWeight += esemi.getWeight(); // spawn semiBoss
		}
		if (Distance.distance >= 50000 && !isBossAlive) {
			eboss = new EBoss(this);
			addNewObject(eboss);
			GameLogic.currentEnemyWeight += eboss.getWeight(); //spawn Boss
		}

		if (Distance.distance >= 800 * stageLevel * stageLevel) {
			stageLevel++;
		}

		if (GameLogic.currentEnemyWeight < this.maxEnemyCap) {
			int chance = r.nextInt(100) - 10000 / (Distance.distance + 1); 
			//random enemy spawn
			if (chance < 30) {
				Image variation = RenderableHolder.monsterArr[ThreadLocalRandom.current().nextInt(0, 4)];
				EMonster emonster = new EMonster(
						ThreadLocalRandom.current().nextDouble(SceneManager.SCENE_WIDTH - variation.getWidth()),
						variation);
				addNewObject(emonster);
			} else if (chance < 50) {
				ETentacle eTentacle = new ETentacle(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eTentacle.getWidth()));
				addNewObject(eTentacle);
			} 
			else if (chance < 75) {
				EDevil eDevil = new EDevil(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eDevil.getWidth()));
				addNewObject(eDevil);
			}else if (chance < 90) {
				EUfo eUfo = new EUfo(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eUfo.getWidth()));
				addNewObject(eUfo);
			} else {
				EFShip eShip = new EFShip(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eShip.getWidth())); 
				addNewObject(eShip);
			}

		}

	}

	private void spawnItems() {
		long now = System.nanoTime();
		if (this.nextItemsSpawnTime <= now) {
			this.nextItemsSpawnTime = now + ThreadLocalRandom.current().nextLong(8000000000l, 11000000000l);

			double rand = ThreadLocalRandom.current().nextDouble(100);
			if (rand <= 10) {
				addNewObject(new IAttackPotion(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.attackPotion.getWidth())));
			} else if (rand <= 30) {
				addNewObject(new ITripleFirePotion(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.triplefire.getWidth())));
			} else if (rand <= 50) {
				addNewObject(new IPowerAttack(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.powerattack.getWidth())));
			} else if (rand <= 60) {
				addNewObject(new IShieldMaxPotion(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth())));
			} else if (rand <= 70) {
				addNewObject(new IShieldRegenPotion(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth())));
			} else {
				addNewObject(new IHealth(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.healthpack.getWidth())));
			}
		}

	}
}
