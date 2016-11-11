
package wargame.map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class SpriteHandler extends HashMap<String, ArrayList<BufferedImage>> {

	private static final long serialVersionUID = 1L;
	// http://gamedev.stackexchange.com/questions/53705/how-can-i-make-a-sprite-sheet-based-animation-system
	private final static String spriteIndex = "resources/spriteIndex.data";

	private boolean loaded = false;

	public SpriteHandler() {

	}

	public void loadSprites() {
		if (loaded == false) {
			loaded = true;
			loadSprites(spriteIndex);
		}
	}

	private void loadSprites(String indexPath) {

	}
}
