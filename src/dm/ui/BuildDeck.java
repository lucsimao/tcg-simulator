/** 
* @author Simão 
* @version 0.1 - 26 de jun de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.exceptions.MaxCardCopiesException;
import dm.interfaces.NormalDeckCard;
import dm.ui.subviews.DeckView;
import dm.ui.subviews.ListCards;

public class BuildDeck extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static int height = 480;
	private final static int width = 900;

	private ListCards listView;
	private DeckView deckView;

	private JButton backButton;

	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// f.setUndecorated(true);
		f.setVisible(true);

		BuildDeck fv = new BuildDeck();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}

	/**
	 * Create the panel.
	 */
	public BuildDeck() {

		setLayout(new BorderLayout());
		listView = new ListCards();
		deckView = new DeckView();

		backButton = new JButton("Voltar");

		add(listView, "West");
		add(deckView, "East");

		deckView.addDoubleClickListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					deckView.removeSelected(deckView.getSelected());
				}
			}

		});

		listView.addDoubleClickListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						deckView.addSelected((NormalDeckCard) listView.getSelected());
					} catch (MaxCardCopiesException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
								JOptionPane.INFORMATION_MESSAGE);
					}
					System.out.println("DEBUG? " + deckView.getDeckSize());
				}
			}
		});

	}

	public void addBackActionListener(ActionListener actionListener) {
		backButton.addActionListener(actionListener);
	}

}
