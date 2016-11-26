package wargame.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import wargame.basic_types.Position;

public class AnimationWidget extends Component {
	private static final long serialVersionUID = 5692785884158017868L;

	protected ArrayList<BufferedImage> imageList;
	protected ArrayList<Position> positionList;
	protected double frameDuration;
	private int currentImage = 0;
	private long lastFrame = 0;

	public AnimationWidget(double frameDuration) {
		imageList = new ArrayList<BufferedImage>();
		positionList = new ArrayList<Position>();
		this.frameDuration = frameDuration;
	}

	public void addImage(BufferedImage image) {
		addImage(image, new Position(0, 0));
	}

	public void addImage(BufferedImage image, Position p) {
		imageList.add(image);
		positionList.add(p);
	}

	public void replay () {
		currentImage = 0 ;
	}

	public void paintComponent(Graphics g) {
		if (System.currentTimeMillis() - lastFrame > frameDuration) {
			g.drawImage(imageList.get(currentImage), positionList.get(currentImage).getX(),
					positionList.get(currentImage++).getY(), this);
			lastFrame = System.currentTimeMillis();
		}
	}

}
