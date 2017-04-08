package dm.fields.elements;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.fields.elements.decks.Deck;

/*From @Simao
 * Uma classe para tratar os monstros removidos de jogo.
 * Ele não tem nada de especial, mas é diferenciada por conta de seu uso.
*/

public class RemoveFromPlay extends Deck<Card> {
	
	public RemoveFromPlay(int number) {
		super(number, new MonsterNormalCard(3));
	}

	public RemoveFromPlay() {
		super();
	}

	@Override
	public void putCard(Card card) {
		getCards().push(card);
	}
}
