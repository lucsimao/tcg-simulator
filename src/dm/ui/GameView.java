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
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class GameView extends JPanel{


	private final static int height = 640;
	private final static int width = 720;
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);

		Player player1 = new Player("teste1", null, new NormalDeck(50), new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		GameView fv = new GameView(player1,player2);
		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);

	}
	
	private Player player1;
	private Player player2;
	private FieldView fieldView;
	private HandView hand1;
	private HandView hand2;
	private JButton drawButton;
	private JButton attackButton;
	private JButton changePositionButton;
	
	
	public GameView(Player player1, Player player2) {
		this.player1 =player1;
		this.player2 = player2;
		
		fieldView = new FieldView(player1, player2);

		
		hand1 = new HandView(player1);
		hand2 = new HandView(player2);
		
		drawButton = new JButton("Draw");
		attackButton = new JButton("Attack");
		changePositionButton = new JButton("ChangePosition");
		
		
		JPanel painelButton = new JPanel(new GridLayout(3,1));
		painelButton.add(drawButton);
		painelButton.add(attackButton);
		painelButton.add(changePositionButton);
		
		setLayout(new BorderLayout());
		add(hand2, "North");
		add(hand1, "South");
		add(fieldView, "Center");
		add(painelButton, "East");
		fieldView.setFocusable(true);
		fieldView.requestFocusInWindow();
		
	}
}