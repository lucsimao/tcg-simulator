/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardImage extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5555548043685844676L;
	private final int height = 177;
	private final int width = 254;
	/**
	 * Create the panel.
	 */
	public CardImage(ImageIcon icon) {
		super();
		setLayout(new BorderLayout(0, 0));
		
		setMaximumSize(new Dimension(width, height));

		
		JLabel lblImage = new JLabel();
		lblImage.setIcon(icon);
		add(lblImage, BorderLayout.CENTER);
	}

}
