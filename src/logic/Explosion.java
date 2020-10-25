package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;

public class Explosion implements IRenderable {
	private Image explosion[];
	private AudioClip sound;
	private int z;
	private double x, y, width, height;
	private int explosionTick;
	private boolean destroyed, visible;

	public Explosion(double posx, double posy, double width, double height, int originalZ) {
		// TODO Auto-generated constructor stub
		explosion = RenderableHolder.exploArr;
		this.x = posx;//position
		this.y = posy;
		this.width = width;
		this.height = height;
		sound = RenderableHolder.explosions[ThreadLocalRandom.current().nextInt(0,2)];
		this.z = Math.abs(originalZ) + 1; // + 1 to prevent bug
		this.visible = true;
		this.destroyed = false;
	}

	@Override
	public int getZ() {
		// TODO Auto-generated method stub
		return z;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(explosion[explosionTick / 2], x, y, width, height);
		updateExplosion();
	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return destroyed;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return visible;
	}

	public void updateExplosion() {
		// TODO Auto-generated method stub
		explosionTick++;
		if (explosionTick >= 24) {
			this.visible = false;
			this.destroyed = true;
		}

	}

	public void playSfx() {
		sound.play();
	}

}
