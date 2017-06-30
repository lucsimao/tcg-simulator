/** 
* @author Simão 
* @version 0.1 - 28 de jun de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import dm.cards.abstracts.Card;
import dm.constants.FilesConstants;
import dm.fields.elements.decks.NormalDeck;
import dm.files.DeckDao;
import dm.interfaces.NormalDeckCard;

public class DeckView extends JPanel {

	private final static int height = 480;
	private final static int width = 480;

	private static final long serialVersionUID = 1L;

	private CardView cardView;

	private JList<Object> list;
	private Vector<NormalDeckCard> vector;
	private NormalDeck deck;
	private JButton loadDeckButton;
	private JButton createDeckButton;
	private JButton saveDeckButton;
	private JTextField createDeckTextField;

	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);

		DeckView fv = new DeckView();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}

	public DeckView() {
		setLayout(new BorderLayout());
		cardView = new CardView(null);

		this.deck = new NormalDeck();

		vector = new Vector<>(deck.getCardsList());
		this.list = new JList<>();
		list.setListData(vector);
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
		add(cardView, "East");
		add(new JScrollPane(list), "West");
		loadDeckButton = new JButton("LoadDeck");

		createDeckButton = new JButton("Create Deck");
		saveDeckButton = new JButton("SaveDeck");
		JPanel grid = new JPanel(new GridLayout(2, 2, 3, 3));
		createDeckTextField = new JTextField();
		grid.add(createDeckTextField);
		grid.add(createDeckButton);
		grid.add(loadDeckButton);
		grid.add(saveDeckButton);
		add(grid, "South");

		saveDeckButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveDeck(deck, createDeckTextField.getText());
			}
		});

		createDeckButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// createDeck(deck);
			}
		});

		loadDeckButton.addActionListener(new ActionListener() {

			private JFileChooser chooser;

			@Override
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser(FilesConstants.DECK);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("YGO type deck", "ygo");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					createDeckTextField.setText(extractName(chooser.getSelectedFile().getName()));
					loadDeck(chooser.getSelectedFile());
					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());

					repaint();

				}
			}

			private String extractName(String name) {
				StringTokenizer stringTokenizer = new StringTokenizer(name, ".");
				name = stringTokenizer.nextToken();
				return name;
			}
		});

	}

	protected void loadDeck(File deckName) {
		DeckDao deckDao = new DeckDao();
		try {
			deck = deckDao.loadDeck(deckName);
			vector = new Vector<>(deck.getCardsList());
			list.setListData(vector);
			list.validate();
			repaint();
		} catch (ClassNotFoundException | IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected void saveDeck(NormalDeck deck, String deckName) {
		DeckDao deckDao = new DeckDao();
		try {
			deckDao.saveDeck(deck, deckName);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void addDoubleClickListener(MouseAdapter mouseAdapter) {
		this.list.addMouseListener(mouseAdapter);
	}

	public void addSelected(NormalDeckCard selected) {
		this.deck.putCard((NormalDeckCard) selected);
		System.out.println("Adicionou");
		vector.add(selected);
		list.setListData(vector);
		repaint();
		list.revalidate();
	}

	public void removeSelected(Object selected) {
		this.deck.remove((NormalDeckCard) selected);
		vector.remove(selected);
		list.setListData(vector);
		System.out.println("Removeu");
		repaint();
	}

	public Object getSelected() {
		return list.getSelectedValue();
	}

	@SuppressWarnings("deprecation")
	public int getDeckSize() {
		return this.list.countComponents();
	}

}
