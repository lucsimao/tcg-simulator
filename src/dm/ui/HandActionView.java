/** 
* @author Simão 
* @version 0.1 - 29 de jun de 2017
* 
*/
package dm.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.Log;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class HandActionView extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final String TAG = "ActionView: ";
	private JButton btnSummon;
	private JButton btnSet;
	private JButton btnCancel;
	private DisposeListener disposeListener;

	public static void main(String args[]) {
		new HandActionView(new Player("teste", null, new NormalDeck(), new ExtraDeck()), new MonsterNormalCard());
	}

	public HandActionView(Player player, Card card, int x, int y) {
		setLayout(new GridLayout(1, 3));
		setUndecorated(true);

		btnCancel = new JButton("Cancel");
		btnSet = new JButton("Set");
		btnSummon = new JButton("Summon");

		setVisible(true);
		setBounds(x, y, 300, 100);

		add(btnCancel);
		add(btnSet);
		add(btnSummon);

		btnSummon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(card instanceof MonsterNormalCard)
					player.summon((MonsterNormalCard) card);
				else if(card instanceof NonMonsterCard)
					player.activate((NonMonsterCard) card);
				System.out.println(player.getField().getHand().size());
				dispose();
			}
		});

		btnSet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(card instanceof MonsterNormalCard)
					player.set((MonsterNormalCard) card);
				else
					player.set((NonMonsterCard) card);
				dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				Log.messageLog(TAG, "Window Desactived");
				dispose();
			}
		});

	}

	public HandActionView(Player player, Card c) {
		this(player, c, 50, 50);
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
