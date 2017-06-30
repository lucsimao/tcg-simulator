/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui.subviews;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import dm.cards.abstracts.Card;

public class CardDescriptionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -110315107751626480L;
	private final int height = 197;
	private final int width = 177;
	private JTextPane txtDescription;

	public CardDescriptionPanel(Card card) {
		super();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.NORTH);
		scrollPane.setMinimumSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width, height));
		txtDescription = new JTextPane();
//		txtDescription.setEditable(false);
		txtDescription.setFont(new Font("Lucida Fax", Font.BOLD, 13));
		// txtDescription.setText("Lorem ipsum dolor sit amet, consectetur
		// adipiscing elit."
		// + " Ut odio augue, cursus vel nulla a, pretium faucibus mauris."
		// + " Maecenas finibus erat sed ultrices commodo. "
		// + "Vivamus nisi erat, tristique pulvinar finibus id, tincidunt quis
		// nisl. "
		// + "Nam placerat, purus ac finibus ornare, enim mi volutpat mauris, ac
		// blandit ante est convallis eros. "
		// + "Nulla odio nisl, pellentesque quis metus eu, venenatis euismod
		// risus."
		// + " Nulla leo magna, aliquam non bibendum sit amet, porta a orci. "
		// + "Suspendisse venenatis turpis ut dolor fermentum accumsan.");
		try{
			txtDescription.setText(card.getDescription());
		}catch(Exception e){
			txtDescription.setText("Default description");
		}
		txtDescription.setPreferredSize(new Dimension(width, height));
		scrollPane.setViewportView(add(txtDescription));
	}

	public CardDescriptionPanel() {
		super();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.NORTH);
		scrollPane.setMinimumSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width, height));
		txtDescription = new JTextPane();
//		txtDescription.setEditable(false);
		txtDescription.setFont(new Font("Lucida Fax", Font.BOLD, 13));
		// txtDescription.setText("Lorem ipsum dolor sit amet, consectetur
		// adipiscing elit."
		// + " Ut odio augue, cursus vel nulla a, pretium faucibus mauris."
		// + " Maecenas finibus erat sed ultrices commodo. "
		// + "Vivamus nisi erat, tristique pulvinar finibus id, tincidunt quis
		// nisl. "
		// + "Nam placerat, purus ac finibus ornare, enim mi volutpat mauris, ac
		// blandit ante est convallis eros. "
		// + "Nulla odio nisl, pellentesque quis metus eu, venenatis euismod
		// risus."
		// + " Nulla leo magna, aliquam non bibendum sit amet, porta a orci. "
		// + "Suspendisse venenatis turpis ut dolor fermentum accumsan.");
//		txtDescription.setText(card.getDescription());
		txtDescription.setPreferredSize(new Dimension(width, height));
		scrollPane.setViewportView(add(txtDescription));
	}
	
	public void setEditable(boolean b) {
		this.txtDescription.setEditable(b);
		
	}
	
}
