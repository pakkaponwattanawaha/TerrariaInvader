package logic;

import javafx.scene.shape.Shape;
import sharedObject.IRenderable;

public abstract class Unit implements IRenderable {
	protected double x, y, hp, speed, width, height, collideDamage;
	protected int z, side;
	protected boolean visible, destroyed;

	protected Unit() {
		visible = true;
		destroyed = false;
		this.hp = 100;
		this.speed = 20;

	}

	protected Unit(double hp, double speed) {
		visible = true;
		destroyed = false;
		this.hp = hp;
		this.speed = speed;

	}
	
	protected boolean collideWith(Unit other) {
		if (this instanceof Bullet && other instanceof Bullet) {
			return false;
		}
		if ((this instanceof Bullet && other instanceof Items) || (this instanceof Items && other instanceof Bullet)) {
			return false;
		}
		if (this.side != other.side) {
			Shape intersect = Shape.intersect(this.getBoundary(), other.getBoundary());
			return (intersect.getBoundsInLocal().getWidth() != -1);
		}
		return false;
	}

	public abstract void onCollision(Unit other);

	public abstract Shape getBoundary();

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean isVisible() {
		return visible;
	}

	public int getZ() {
		return z;
	}

	public abstract void update();

}
