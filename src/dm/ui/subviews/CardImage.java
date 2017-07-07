/** 
* @author Simão 
* @version 0.1 - 29 de jun de 2017
* 
*/
package dm.ui.subviews;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import dm.constants.FilesConstants;

public class CardImage extends JLabel {

	private static final long serialVersionUID = 1L;

	private Image cardImage;

	private int width;
	private int height;

	public CardImage(File file, int width, int height) {

		this.width = width;
		this.height = height;

		try {
			cardImage = scaleImage(ImageIO.read(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setIcon(new ImageIcon(cardImage));
	}

	public CardImage(int width, int height) {
		this.width = width;
		this.height = height;

		try {
			cardImage = scaleImage(
					ImageIO.read(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setIcon(new ImageIcon(cardImage));
	}

	public void setIcon(Image image) {
		cardImage = scaleImage(image);
		setIcon(new ImageIcon(cardImage));
	}

	public Image scaleImage(Image image) {

		image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		return image;

	}

}
