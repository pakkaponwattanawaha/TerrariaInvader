package sharedObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RenderableHolder {
	private static final RenderableHolder instance = new RenderableHolder();

	private List<IRenderable> entities;
	private Comparator<IRenderable> comparator;

	public static Image slime, eSemiBoss, eBoss, eUfo, eDevil, eShip, eTentacle, bullet, background, backgroundMM,
			backgroundW, healthpack, bossBullet, bossPower, bossLow, roundBulletB, spikeBullet, eyeBullet,
			roundBulletP, beamSmallG, eyeBulletG, sparkArr[], powerAttack, exploArr[], monsterArr[], shieldmax,
			shieldregen, attackPotion, triplefire, powerattack;

	public static Font inGameFont, inGameFontSmall, titleFont, menuFont, tutorialFont;

	public static AudioClip fireBall, explosion, explosion2, powerAttackLaunch, laser, hit, hit2;

	public static AudioClip[] hits, explosions;

	public static MediaPlayer bgm, gameOverMusic, mainMenuMusic, gameWinnerMusic;

	public RenderableHolder() {
		entities = Collections.synchronizedList(new ArrayList<>());
		comparator = (IRenderable o1, IRenderable o2) -> {
			if (o1.getZ() > o2.getZ()) {
				return 1;
			}
			return -1;
		};
	}

	public static RenderableHolder getInstance() {
		return instance;
	}

	public static void loadResource() throws LoadUnableException {
		
		//player
		slime = imageLoader("player/SuperSlime.gif");
		
		//mops
		monsterArr = new Image[4];
		
		for (int i = 0; i < 4; i++) {
			monsterArr[i] = imageLoader("enemy/idleEnemy/idleenemy" + i + ".gif");
		}
		
		eSemiBoss = imageLoader("enemy/semiboss.gif");
		eBoss = imageLoader("enemy/BigBoss.png");
		eUfo = imageLoader("enemy/eUfo.png");
		eTentacle = imageLoader("enemy/eTentacle.png");
		eDevil = imageLoader("enemy/eDevil.gif");
		eShip = imageLoader("enemy/eShip.gif");

		exploArr = new Image[12];
		for (int i = 0; i < 12; i++) {
			exploArr[i] = imageLoader("explosion/" + i + ".gif");
		}

		sparkArr = new Image[4];
		for (int i = 0; i < 4; i++) {
			sparkArr[i] = imageLoader("spark/" + i + ".png");
		}
		
		//bullet
		bullet = imageLoader("bullet/Arrow.png");
		powerAttack = imageLoader("bullet/bomb.png");
		bossBullet = imageLoader("bullet/bossBullet.gif");
		bossPower = imageLoader("bullet/bossPower.png");
		bossLow = imageLoader("bullet/bossLow.png");

		roundBulletB = imageLoader("bullet/roundBulletB.png");
		spikeBullet = imageLoader("bullet/spikeBullet.png");
		eyeBullet = imageLoader("bullet/eyeBullet.png");
		roundBulletP = imageLoader("bullet/roundBulletP.png");
		beamSmallG = imageLoader("bullet/beamSmallG.png");
		eyeBulletG = imageLoader("bullet/eyeBulletG.png");
		
		//background
		
		background = imageLoader("background/BackGame.jpg");
		backgroundMM = imageLoader("background/BackMenu.jpg");
		backgroundW = imageLoader("background/Backwin.jpg");
		
		//items
		attackPotion = imageLoader("items/attackPotion.png");
		triplefire = imageLoader("items/triple.png");
		powerattack = imageLoader("items/SUPERPOWER.png");
		healthpack = imageLoader("items/health.png");
		shieldmax = imageLoader("items/shieldmax.png");
		shieldregen = imageLoader("items/shieldregen.png");
		
		//soundtrack
		bgm = mediaPlayerLoader("song/GameScreen.mp3");
		fireBall = audioClipLoader("song/Fire_Ball.mp3");
		laser = audioClipLoader("song/laser.wav");
		hit =  audioClipLoader("song/hit.wav");
		hit2 =  audioClipLoader("song/hit2.wav");
		gameWinnerMusic = mediaPlayerLoader("song/GameWinner.mp3");
		gameOverMusic = mediaPlayerLoader("song/GameLoser.mp3");
		mainMenuMusic = mediaPlayerLoader("song/MenuSound.mp3");
		explosion = audioClipLoader("song/Explosion.wav");
		explosion2 = audioClipLoader("song/Explosion2.wav");
		powerAttackLaunch = audioClipLoader("song/PowerAttack.mp3");

		fireBall.setVolume(0.20);
		laser.setVolume(0.1);
		hit.setVolume(0.10);
		hit2.setVolume(0.20);
		explosion.setVolume(0.07);
		explosion2.setVolume(0.15);

		hits = new AudioClip[] { hit,hit2 };
		explosions = new AudioClip[] { explosion, explosion2 };

		inGameFont = Font.font("Tahoma",FontWeight.NORMAL, 24);
		inGameFontSmall = Font.font("Tahoma",FontWeight.NORMAL, 24);
		titleFont = Font.font("Tahoma",FontWeight.NORMAL, 24);
		menuFont = Font.font("Tahoma",FontWeight.NORMAL, 24);
		tutorialFont = Font.font("Tahoma",FontWeight.NORMAL, 24);
		
	}

	public void add(IRenderable entity) {
		entities.add(entity);
		Collections.sort(entities, comparator);
	}

	public void update() {
		for (int i = entities.size() - 1; i >= 0; i--) {
			if (entities.get(i).isDestroyed())
				entities.remove(i);
		}
	}

	public List<IRenderable> getEntities() {
		return entities;
	}

	public void clear() {
		entities.clear();
	}

	private static Image imageLoader(String url) throws LoadUnableException {
		try {
			return new Image(ClassLoader.getSystemResource(url).toString());
		} catch (Exception e) {
			throw new LoadUnableException(url);
		}
	}

	private static AudioClip audioClipLoader(String url) throws LoadUnableException {
		try {
			return new AudioClip(ClassLoader.getSystemResource(url).toString());
		} catch (Exception e) {
			throw new LoadUnableException(url);
		}
	}

	private static MediaPlayer mediaPlayerLoader(String url) throws LoadUnableException {
		try {
			return new MediaPlayer(new Media(ClassLoader.getSystemResource(url).toString()));
		} catch (Exception e) {
			throw new LoadUnableException(url);
		}
	}

}
