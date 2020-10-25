package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import manage.SceneManager;
import sharedObject.IRenderable;
import sharedObject.RenderableHolder;

//@SuppressWarnings("restriction")
public class Distance implements IRenderable {

	public static int distance;
	public static double hiddenDistance;

	public Distance() {
		// TODO Auto-generated constructor stub
		//score = 0;
		distance = 0;
		hiddenDistance = 0;
	}

	@Override
	public int getZ() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.setFont(RenderableHolder.inGameFont);
		gc.setFill(Color.GOLD);
		String distanceDisplay = "Distance: " + Integer.toString(Distance.distance) + " m";
		Text text = new Text("Distance: " + Integer.toString(Distance.distance) + " m");
		text.applyCss();
		//FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		double score_width = text.getLayoutBounds().getWidth();//fontLoader.computeStringWidth(distanceDisplay, gc.getFont());
		double score_height = RenderableHolder.inGameFont.getSize();
		gc.fillText(distanceDisplay, SceneManager.SCENE_WIDTH-120 - score_width, 10 + score_height);
		
	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return true;
	}
}
