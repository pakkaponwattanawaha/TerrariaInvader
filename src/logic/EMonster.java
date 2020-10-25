package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import sharedObject.RenderableHolder;

public class EMonster extends Enemy {

	Image variation;
	
	public EMonster(double x, Image i) {
		// TODO Auto-generated constructor stub
		super(150 + Distance.distance/500, ThreadLocalRandom.current().nextDouble(2.5,5.5) + Distance.distance/20000);
		this.variation = i;
		this.width = this.variation.getWidth();
		this.height = this.variation.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 18;
		this.weight = 1;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stubs		
		gc.drawImage(variation, x, y);		
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x + this.width/4, y + this.height/4, this.width * 0.45, this.height * 0.45);
			collided = false;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		y += this.speed;
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
	}

	public Shape getBoundary() {
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
