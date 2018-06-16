/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package view.ui.subviews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import config.constants.FilesConstants;
import config.constants.MonsterAttribute;
import config.constants.MonsterType;
import model.cards.MonsterNormalCard;
import model.cards.abstracts.Card;

public class CardView extends JPanel {

	private static final long serialVersionUID = -1929371154966471210L;

	private final static int width = 217;
	private final static int height = 480;

	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
		Card card2 = new MonsterNormalCard("Exodia", "O guerreiro proibido", "exodia.jpg", MonsterType.SPELLCASTER,
				MonsterAttribute.DARK,8, 2500, 2100, 0);

		f.getContentPane().add(new CardView(card2));
		f.setBounds(0, 0, width, height);

	}

	/**
	 * Create the panel.
	 */
	public CardView(Card card) {
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setBackground(Color.darkGray);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setCard(card);
	}

	public void setCard(Card card) {
		setLayout(new BorderLayout(10, 10));

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		try {
			panel_1.add(new CardImage(new File(FilesConstants.CARDS_IMG_DIR + card.getPicture()), 177, 250));
		} catch (Exception e) {
			panel_1.add(
					new CardImage(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD), 177, 250));
		}

		CardDescriptionPanel panel = new CardDescriptionPanel(card);
		panel.setEditable(false);
		add(panel, BorderLayout.CENTER);
		validate();
	}

}
