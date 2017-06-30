/** 
* @author Simão 
* @version 0.1 - 29 de jun de 2017
* 
*/
package dm.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class ActionView extends JFrame {
	private JButton btnSummon;
	private JButton btnSet;
	private JButton btnCancel;
	private Player player;
	private DisposeListener disposeListener;
	public static void main(String args[]){
		new ActionView(new Player("teste",null, new NormalDeck(), new ExtraDeck()),new MonsterNormalCard());	
	}
	
	public ActionView(Player player,Card card){
		setLayout(new GridLayout(1,3));
		setUndecorated(true);
		
		btnCancel = new JButton("Cancel");
		btnSet = new JButton("Set");
		btnSummon = new JButton("Summon");
		
		setVisible(true);
		setBounds(50, 50, 300, 100);
		
		add(btnCancel);
		add(btnSet);
		add(btnSummon);
		
		btnSummon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				player.summon((MonsterNormalCard)card);
				System.out.println(player.getField().getHand().size());
				dispose();
			}
		});
		
		btnSet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				player.set((MonsterNormalCard)card);
				dispose();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
	}

	@Override
	public void dispose() {
		super.dispose();
		this.disposeListener.actionPerformed();
	}
	
	public void addDisposeListener(DisposeListener actionListener) {
		this.disposeListener = actionListener;		
	}
	
}
