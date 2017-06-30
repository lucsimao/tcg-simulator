/** 
* @author Simão 
* @version 0.1 - 29 de jun de 2017
* 
*/
package dm.ui;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.cards.MonsterNormalCard;
import dm.ui.subviews.CardImage;

public class HandView extends JPanel {
	
	private static final long serialVersionUID = 2661005663882276893L;
	
	private final static int height = 120;
	private final static int width = 640;
	
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);


		HandView fv = new HandView();

		
		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(100, 100, width, height);
	}
	
	//TODO
	private int number;
	private int selected;
	
	public HandView(){
	
		setLayout(new GridLayout(1, 5,3,3));
		ImageIcon icon = new ImageIcon(new MonsterNormalCard().getPicture());
//		CardImage c = new CardImage(icon);
//		add(c,100,100);
		
	}
	
}
