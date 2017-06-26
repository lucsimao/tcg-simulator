/** 
* @author Simão 
* @version 0.1 - 26 de jun de 2017
* 
*/
package dm.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.cards.MonsterFusionCard;
import dm.cards.abstracts.Card;

public class ListCards extends JPanel {

	private final static int height = 400;
	private final static int width = 640;
	
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);


		ListCards fv = new ListCards();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}
	
	/**
	 * Create the panel.
	 */
	public ListCards() {
		
		List<Card> card = new ArrayList<>();
		card.add(new MonsterFusionCard(3));
		card.add(new MonsterFusionCard(3));
		card.add(new MonsterFusionCard(3));
		
		JList list = new JList();
		list.add("Componente", new JLabel("BATATA"));
		add(list);

	}

}
