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

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

public class JImageDesktopPane extends JDesktopPane {
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g = (Graphics2D) g;
		int h = getHeight();
		int w = getWidth();
		ImageIcon image = new ImageIcon(getClass().getResource(("/images/background.png")));
		g.drawImage(image.getImage(), 0, 0, w, h, this);

		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Toolkit.getDefaultToolkit().sync();
//		g.dispose();
			
	}	
	
}
