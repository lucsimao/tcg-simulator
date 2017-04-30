package dm.fields.elements;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.fields.elements.decks.Deck;

/**
 * Classe cemitério, ela não possui nada de diferente em sua estrutura, porém,
 * deve ser diferenciada por conta de seu uso.
 * 
 * @author Simão
 */
public class Graveyard extends Deck<Card> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7173474563513436801L;

	public Graveyard(int number) {
		super(number, new MonsterNormalCard(3));
	}

	public Graveyard() {
		super();
	}

	@Override
	public void putCard(Card card) {
		getCards().push(card);
	}

}
