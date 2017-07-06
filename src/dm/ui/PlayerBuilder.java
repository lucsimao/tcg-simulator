/** 
 * @author Simão 
 * @version 0.1 - 6 de jul de 2017
 * 
 */
package dm.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import dm.constants.FilesConstants;
import dm.exceptions.MaxCardCopiesException;
import dm.fields.elements.decks.NormalDeck;
import dm.files.DeckDao;
import dm.game.Player;
import dm.interfaces.NormalDeckCard;
import dm.ui.subviews.DeckView;

public class PlayerBuilder extends JPanel {

	private static final long serialVersionUID = 1L;

	private final static int height = 480;
	private final static int width = 900;

	private ListCards listView;
	private DeckView deckView;

	private JButton btnBack;

	private NormalDeck deck1;
	private NormalDeck deck2;
	
	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// f.setUndecorated(true);
		f.setVisible(true);

		PlayerBuilder fv = new PlayerBuilder();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}

	/**
	 * Create the panel.
	 */
	public PlayerBuilder() {

		setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));
		add(mainPanel,"Center");
		add(new JLabel("Monte seus jogadores"),"North");
		JButton btnStart = new JButton("Começar");
		JButton btnBack = new JButton("Voltar");
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2,10,10));
		buttonPanel.add(btnBack);
		buttonPanel.add(btnStart);
		add(buttonPanel,"South");
		
		JPanel player1Panel = new JPanel(new GridLayout(6,2,20,20));
		JPanel player2Panel = new JPanel(new GridLayout(6,2,20,20));
		mainPanel.add(player1Panel);
		mainPanel.add(player2Panel);
		player1Panel.add(new JLabel(""));
		player1Panel.add(new JLabel(""));
		player2Panel.add(new JLabel(""));
		player2Panel.add(new JLabel(""));
		player1Panel.add(new JLabel("Jogador1"));
		player2Panel.add(new JLabel("Jogador2"));
		player1Panel.add(new JLabel(""));
		player2Panel.add(new JLabel(""));
		player1Panel.add(new JLabel("Nome"));
		player2Panel.add(new JLabel("Nome"));
		JTextField tFName1 = new JTextField();
		player1Panel.add(tFName1);
		JTextField tFName2 = new JTextField();
		player2Panel.add(tFName2);
		JLabel lbDeck1 = new JLabel("Deck");

		JLabel lbDeck2 = new JLabel("Deck");
		
		player2Panel.add(lbDeck2);
		JButton loadDeck1Button = new JButton("LoadDeck");
		JButton loadDeck2Button = new JButton("LoadDeck");
		
		player1Panel.add(loadDeck1Button);
		player1Panel.add(lbDeck1);
		player2Panel.add(loadDeck2Button);
		player2Panel.add(lbDeck2);
		
		

		loadDeck1Button.addActionListener(new ActionListener() {

			private JFileChooser chooser;

			@Override
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser(FilesConstants.DECK);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("YGO type deck", "ygo");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					lbDeck1.setText(chooser.getSelectedFile().getName());
					deck1 = loadDeck(chooser.getSelectedFile());
					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());

//					repaint();

				}
			}
			});

		
		
		loadDeck2Button.addActionListener(new ActionListener() {

				private JFileChooser chooser;

				@Override
				public void actionPerformed(ActionEvent e) {
					chooser = new JFileChooser(FilesConstants.DECK);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("YGO type deck", "ygo");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						lbDeck2.setText(chooser.getSelectedFile().getName());
						deck2 = loadDeck(chooser.getSelectedFile());
						System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
					}
				}
		});
		

		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("deu certo");
				try{
					Player player1 = new Player(tFName1.getText(),deck1);
					Player player2 = new Player(tFName2.getText(),deck2);
					
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
							JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
		

//		setLayout(new BorderLayout());
//		listView = new ListCards();
//		deckView = new DeckView();
//
//		backButton = new JButton("Voltar");
//
//		add(listView, "West");
//		add(deckView, "East");
//
//		deckView.addDoubleClickListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					deckView.removeSelected(deckView.getSelected());
//				}
//			}
//
//		});
//
//		listView.addDoubleClickListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					try {
//						deckView.addSelected((NormalDeckCard) listView.getSelected());
//					} catch (MaxCardCopiesException ex) {
//						JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
//								JOptionPane.INFORMATION_MESSAGE);
//					}
//					System.out.println("DEBUG? " + deckView.getDeckSize());
//				}
//			}
//		});
//
	}
	protected NormalDeck loadDeck(File deckName) {
		DeckDao deckDao = new DeckDao();
		try {
			return deckDao.loadDeck(deckName);
		} catch (ClassNotFoundException | IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}
	public void addBackActionListener(ActionListener actionListener) {
		btnBack.addActionListener(actionListener);
	}

}
