/** 
* @author Sim�o 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.constants.FilesConstants;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;

public class CardView extends JPanel {
	
	private final static int width = 217;
	private final static int height = 480;
	
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
		Card card = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				"magonego.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3);

		Card card2 = new MonsterNormalCard("Exodia", "O guerreiro proibido",
				"exodia.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3);
		
		f.getContentPane().add(new CardView(card2));
		f.setBounds(0,0,width,height);

	}
	
	/**
	 * Create the panel.
	 */
	public CardView(Card card) {
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setBackground(Color.darkGray);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		ImageIcon icon = new ImageIcon(FilesConstants.CARDS_IMG_DIR + card.getPicture());
		setLayout(new BorderLayout(10,10));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		CardImage panel2 = new CardImage(icon);
		panel_1.add(panel2);
		
		CardDescriptionPanel panel = new CardDescriptionPanel(card);
		add(panel, BorderLayout.CENTER);
	}

}
