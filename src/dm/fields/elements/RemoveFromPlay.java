package dm.fields.elements;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.fields.elements.decks.Deck;

/**
 * Uma classe para tratar os monstros removidos de jogo. Ele não tem nada de
 * especial, mas é diferenciada por conta de seu uso.
 * 
 * @author Simão
 */

public class RemoveFromPlay extends Deck<Card> {
	
	private static final long serialVersionUID = -5203621937873970199L;

	public RemoveFromPlay(int number) {
		super(number, new MonsterNormalCard());
	}

	public RemoveFromPlay() {
		super();
	}

	@Override
	public void putCard(Card card) {
		getCards().push(card);
	}
}
