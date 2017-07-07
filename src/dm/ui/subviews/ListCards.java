/** 
* @author Simão 
* @version 0.1 - 26 de jun de 2017
* 
*/
package dm.ui.subviews;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dm.cards.abstracts.Card;
import dm.files.CardDAO;

public class ListCards extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static int height = 480;
	private final static int width = 640;

	private CardView cardView;

	private JButton backButton;

	private JList<?> list;

	public static void main(String args[]) {
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

		setLayout(new BorderLayout());
		cardView = new CardView(null);

		List<Card> cards = null;
		try {
			cards = new CardDAO().readAllFile();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		add(cardView, "West");

		list = new JList<>(new Vector<Card>(cards));
		list.setVisibleRowCount(10);
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof Card) {
					// Here value will be of the Type 'CD'
					((JLabel) renderer).setText(((Card) value).getName());
				}
				return renderer;
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				cardView.setCard((Card) list.getSelectedValue());
			}
		});

		backButton = new JButton("Voltar");
		add(backButton, "South");
		add(new JScrollPane(list));

	}

	public void addBackActionListener(ActionListener actionListener) {
		backButton.addActionListener(actionListener);

	}

	public void addDoubleClickListener(MouseAdapter mouseAdapter) {
		this.list.addMouseListener(mouseAdapter);
	}

	public Object getSelected() {
		return list.getSelectedValue();
	}

}
