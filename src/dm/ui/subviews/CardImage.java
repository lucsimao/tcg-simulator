/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui.subviews;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardImage extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5555548043685844676L;
	private final int height = 254;
	private final int width = 177;

	/**
	 * Create the panel.
	 */
	public CardImage(ImageIcon icon) {
		super();
		setLayout(new BorderLayout(0, 0));

		setMaximumSize(new Dimension(width, height));

		JLabel lblImage = new JLabel();
		Image img = icon.getImage();
		img = img.getScaledInstance(width, height,Image.SCALE_DEFAULT);
		
		lblImage.setIcon(new ImageIcon(img));
		add(lblImage, BorderLayout.CENTER);
	}

}
