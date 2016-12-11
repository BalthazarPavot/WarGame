package wargame.basic_types;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Make the BufferedImage class serializable.
 * Unluckily, it does not make it deserializable.
 * So we cannot load games.
 * @author Balthazar Pavot
 *
 */
public class SerializableBufferedImage extends BufferedImage implements Serializable {

	private static final long serialVersionUID = 473492050159430456L;

	public SerializableBufferedImage() {
		this(1, 1, BufferedImage.TYPE_INT_ARGB);
	}

	public SerializableBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}

	public SerializableBufferedImage getSubimage(int x, int y, int w, int h) {
		SerializableBufferedImage subImage;

		subImage = new SerializableBufferedImage(w, h, getType());
		for (int dx = x; dx < x + w; dx += 1)
			for (int dy = y; dy < y + h; dy += 1)
				subImage.setRGB(dx - x, dy - y, getRGB(dx, dy));
		return subImage;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		ImageIO.write(this, "png", out);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		ImageIO.read(in);
	}

}
