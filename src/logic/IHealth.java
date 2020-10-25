package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import sharedObject.RenderableHolder;

public class IHealth extends Items {
	private double HPStorage;

	public IHealth(double x) {
		super(ThreadLocalRandom.current().nextDouble(1, 5));
		this.HPStorage = ThreadLocalRandom.current().nextDouble(500, 700);
		this.width = RenderableHolder.healthpack.getWidth();
		this.height = RenderableHolder.healthpack.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 0;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.healthpack, x, y);
	}

	@Override
	public void onCollision(Unit others) {
		// TODO Auto-generated method stub
		this.hp = 0;
		this.destroyed = true;
		this.visible = false;
	}

	protected double getHPStorage() {
		return HPStorage;
	}

}
