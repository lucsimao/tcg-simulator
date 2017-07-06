/** 
* @author Simão 
* @version 0.1 - 30 de jun de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class GameView extends JPanel {

	private static final long serialVersionUID = 1L;
	private final static int height = 640;
	private final static int width = 720;

	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
		f.setFocusable(true);

		Player player1 = new Player("teste1", null, new NormalDeck(50), new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		GameView gv = new GameView(player1, player2);
		f.getContentPane().add(gv);
		gv.setFocusable(true);
		gv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);

	}

	private Player player1;
	private Player player2;
	private FieldViewMelhorado fieldView;
	private HandView hand1;
	private HandView hand2;
	private JButton drawButton;
	private JButton attackButton;
	private JButton changePositionButton;
	private JLabel lp2;
	private JLabel lp1;

	public GameView(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;

		fieldView = new FieldViewMelhorado(player1, player2);

		hand1 = new HandView(player1);
		hand2 = new HandView(player2);

		setLayout(new BorderLayout());
		add(hand2, "North");
		add(hand1, "South");
		JPanel panelP1 = new JPanel(new GridLayout(2, 1));
		JPanel panelP2 = new JPanel(new GridLayout(2, 1));
		JLabel player1Name = new JLabel(player1.getName());
		JLabel player2Name = new JLabel(player1.getName());
		lp1 = new JLabel(player1.getLP() + "");
		lp2 = new JLabel(player2.getLP() + "");
		panelP1.add(player1Name);
		panelP1.add(lp1);
		panelP2.add(player2Name);
		panelP2.add(lp2);
		add(panelP1, "East");
		add(panelP2, "West");
		add(fieldView, "Center");
		fieldView.setFocusable(true);
		fieldView.requestFocusInWindow();
		// fieldView.setFocusable(true);
		// fieldView.requestFocusInWindow();
		this.addKeyListener(fieldView.getKeyListeners()[0]);
	}

	@Override
	public void repaint() {
		if (lp1 != null && lp2 != null) {
			lp1.setText(player1.getLP() + "");
			lp2.setText(player2.getLP() + "");
		}
		super.revalidate();
	}
}