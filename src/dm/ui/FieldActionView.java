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
import dm.cards.abstracts.MonsterCard;
import dm.constants.CardState;
import dm.constants.Log;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class FieldActionView extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final String TAG = "ActionView: ";
	private JButton btnAttack;
	private JButton btnChangePosition;
	private Player player;
	private Card card;

	public static void main(String args[]) {
		new FieldActionView(new Player("teste", null, new NormalDeck(), new ExtraDeck()), new MonsterNormalCard());
	}

	public FieldActionView(Player player, Card card, int x, int y) {
		setLayout(new GridLayout(1, 3));
		setUndecorated(true);
		this.player = player;
		this.card = card;

		btnChangePosition = new JButton("ToAttack");
		add(btnChangePosition);

		if (card.getState() == CardState.FACE_DOWN)
			btnChangePosition.setText("Flip");
		else if (card.getState() == CardState.FACE_UP_ATTACK) {
			btnChangePosition.setText("ToDefense");
			btnAttack = new JButton("Attack");
			add(btnAttack);

		}
		setVisible(true);
		setBounds(x, y, 300, 100);

		btnChangePosition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (card.getState() == CardState.FACE_UP_ATTACK)
					player.changeToDefense((MonsterCard) card);
				else
					player.changeToAttack((MonsterCard) card);
				dispose();
			}
		});

		// btnCancel.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// dispose();
		// }
		// });

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				Log.messageLog(TAG, "Window Desactived");
				dispose();
			}
		});

	}

	public FieldActionView(Player player, Card c) {
		this(player, c, 50, 50);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public void addDisposeListener(DisposeListener actionListener) {
	}

	public void setAttackActionListener(ActionListener actionListener) {
		if (btnAttack != null) {
			btnAttack.addActionListener(actionListener);
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public Card getCard() {
		return this.card;
	}
}
