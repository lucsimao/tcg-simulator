/** 
* @author Sim�o 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import dm.cards.abstracts.Card;
import java.awt.Font;

public class CardDescriptionPanel extends JPanel {
	
	private JLabel lblText;
	private final int height = 197;
	private final int width =177 ;
	
	
	public CardDescriptionPanel(Card card){
		super();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.NORTH);
		scrollPane.setMinimumSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width, height));
		JTextPane txtDescription = new JTextPane();
		txtDescription.setEditable(false);
		txtDescription.setFont(new Font("Lucida Fax", Font.BOLD, 13));
//		txtDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit."
//				+ " Ut odio augue, cursus vel nulla a, pretium faucibus mauris."
//				+ " Maecenas finibus erat sed ultrices commodo. "
//				+ "Vivamus nisi erat, tristique pulvinar finibus id, tincidunt quis nisl. "
//				+ "Nam placerat, purus ac finibus ornare, enim mi volutpat mauris, ac blandit ante est convallis eros. "
//				+ "Nulla odio nisl, pellentesque quis metus eu, venenatis euismod risus."
//				+ " Nulla leo magna, aliquam non bibendum sit amet, porta a orci. "
//				+ "Suspendisse venenatis turpis ut dolor fermentum accumsan.");
		txtDescription.setText(card.getDescription());
		txtDescription.setPreferredSize(new Dimension(width, height));
		scrollPane.setViewportView(add(txtDescription));
	}
}
