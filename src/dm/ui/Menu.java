/** 
* @author Simão 
* @version 0.1 - 29 de abr de 2017
* 
*/
package dm.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Graphics2D g2;
    private ImageIcon background;
	
	/**
	 * Create the panel.
	 */
	public Menu() {
		setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(152, 157, 298, 87);
		add(btnStart);
		
		JButton btnDeckEdit = new JButton("Deck Edit");
		btnDeckEdit.setBounds(152, 438, 298, 87);
		add(btnDeckEdit);
		
		JButton btnCardBuilder = new JButton("Card Builder");
		btnCardBuilder.setBounds(152, 39, 298, 87);
		add(btnCardBuilder);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(152, 295, 298, 87);
		add(btnExit);

		
        background = getImage("/images/background.png");

	}

    public ImageIcon getImage(String path)
    {
    	ImageIcon image;
    	try 
    	{
			image = new ImageIcon(getClass().getResource(path)); 
		} catch (Exception e) {
			image = new ImageIcon();
		}
		return image; 
    }
	
	   @Override
	    public void paint (Graphics g)
	    {
	        super.paint(g);
	        g2 = (Graphics2D)g;
	        int h = getHeight();
	        int w = getWidth();

	        g2.drawImage(background.getImage(),0,0,w,h, this);
	        
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        Toolkit.getDefaultToolkit().sync();
	        g.dispose();
	    }
	
}
