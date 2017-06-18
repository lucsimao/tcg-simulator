/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

import javaxt.io.Image;

public class JImageDesktopPane extends JDesktopPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6958316071697364420L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int h = getHeight();
		int w = getWidth();
		ImageIcon image = new ImageIcon("images/background.png");
		g.drawImage(image.getImage(), 0, 0, w, h, this);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Toolkit.getDefaultToolkit().sync();
		// g.dispose();

	}

}
