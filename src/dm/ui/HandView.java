/** 
* @author Simão 
* @version 0.1 - 29 de jun de 2017
* 
*/
package dm.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.cards.abstracts.Card;
import dm.constants.FilesConstants;
import dm.fields.elements.Hand;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;
import dm.ui.subviews.CardImage;

public class HandView extends JPanel {
	
	private static final long serialVersionUID = 2661005663882276893L;
	
	private final static int height = 150;
	private final static int width = 540;
	
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);


		HandView fv = new HandView(new Player("teste",null,new NormalDeck(30),new ExtraDeck()));

		
		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(100, 100, width, height);
	}
	
	//TODO
	private int number;
	private int selected;
	private Player player;
	private GridLayout gridLayout;
	private JButton drawButton;
	//Just for tests
	public HandView(Hand hand){
	
		setLayout(new GridLayout(1, 6,3,3));
		for(Card c : hand.getCardsList()){
			add(new CardImage(new File(FilesConstants.CARDS_IMG_DIR,c.getPicture()), FilesConstants.HAND_CARD_WIDTH,FilesConstants.HAND_CARD_HEIGHT));
		}
	}
	
	public HandView(Player player){
		this.player = player;
		this.gridLayout = new GridLayout(1,5 ,3,3);
		this.drawButton = new JButton("Draw");
		
		player.firstDraw();


		setHand();
		setLayout(gridLayout);
		drawButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("LOG - COMPRANDO CARTAS");
					player.draw();
					setHand();
				}
			});		
	}
	
	private void setHand() {
		removeAll();
		add(drawButton);
		for(Card c : player.getField().getHand().getCardsList()){
			CardImage cardImage = new CardImage(new File(FilesConstants.CARDS_IMG_DIR,c.getPicture()), 
					FilesConstants.HAND_CARD_WIDTH,FilesConstants.HAND_CARD_HEIGHT);
			add(cardImage);
			cardImage.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ActionView a = new ActionView(player, c);
					a.addDisposeListener(new DisposeListener() {
						
						@Override
						public void actionPerformed() {
							setHand();

						}

					});
				}
			});
			
		}
		this.gridLayout = new GridLayout(1,player.getField().getHand().size() ,3,3);
		revalidate();
	}
	
	
	
	
}
