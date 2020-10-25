package keyInput;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.scene.input.KeyCode;

public class CharacterInput {

	private static ArrayList<KeyCode> keyPressed = new ArrayList<>();
	private static boolean isHoldingCtrlKey = false;
	private static Queue<KeyCode> triggeredCtrl = new ConcurrentLinkedQueue<>();


	public static boolean getKeyPressed(KeyCode keycode) {
		return keyPressed.contains(keycode);
	}

	public static void setKeyPressed(KeyCode keycode, boolean pressed) {
		if (keycode == KeyCode.CONTROL) {
			if (!isHoldingCtrlKey && pressed) {
				triggeredCtrl.add(keycode);
				isHoldingCtrlKey = true;
			} else if (!pressed) {
				isHoldingCtrlKey = false;
			}
		} else {
			if (pressed) {
				if (!keyPressed.contains(keycode)) {
					keyPressed.add(keycode);
				}
			} else {
				keyPressed.remove(keycode);
			}
		}
	}

	public static Queue<KeyCode> getTriggeredCtrl() {
		return triggeredCtrl;
	}

	// starting/reseting a new game
	public static void clear() {
		keyPressed.clear();
		triggeredCtrl.clear();
		isHoldingCtrlKey = false;
	}

}
