/** 
* @author Simão 
* @version 0.1 - 26 de jun de 2017
* 
*/
package views.components;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import config.exceptions.MaxCardCopiesException;
import model.cards.NormalDeckCard;
import view.ui.subviews.DeckView;
import view.ui.subviews.ListCards;

public class BuildDeckView extends JPanel {

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

		BuildDeckView fv = new BuildDeckView();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}

	/**
	 * Create the panel.
	 */
	public BuildDeckView() {

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
						deckView.addSelected((NormalDeckCard) ((NormalDeckCard) listView.getSelected()).clone());
					} catch (MaxCardCopiesException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
								JOptionPane.INFORMATION_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
