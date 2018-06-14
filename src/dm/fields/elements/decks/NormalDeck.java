package dm.fields.elements.decks;

import dm.cards.MonsterNormalCard;
import dm.cards.NormalDeckCard;
import dm.cards.abstracts.Card;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;

/**
 * Deck normal, ele diferencia pois aceita cartas de diversos tipos, menos
 * cartas de extra deck.
 * 
 * @author Simão
 */
public class NormalDeck extends Deck<NormalDeckCard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403267991951466425L;

	private static final int MAX_CARDS = 60;
	private static final int MIN_CARDS = 40;

	public NormalDeck(int number) {
		for (int i = 0; i < number; i++) {
			putCard(new MonsterNormalCard());
		}
	}

	public NormalDeck() {
		super();
	}

	// Adiciona uma carta ao deck
	@Override
	public void putCard(NormalDeckCard card) {
		if (getCards().size() <= MAX_CARDS)
			if (countCards(card) < card.getCopiesNumber())
				try {
					getCards().push((NormalDeckCard) card.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				throw new MaxCardCopiesException(
						"You can only have " + ((Card) card).getCopiesNumber() + " copies of this card");
		else
			throw new MaxDeckSizeException("You can have only " + MAX_CARDS + " cards on your deck");
	}

	// Verifica se o deck está dentro do número esperado de cartas
	public boolean isPlayable() {
		return (this.size() >= MIN_CARDS && this.size() <= MAX_CARDS);
	}

	public NormalDeckCard search(String name) {
		for(NormalDeckCard card : this.getCardsList())
			if(((Card)card).getName().equals(name))
				return card;
		throw new CardNotFoundException("Card was not found in this search");
	}

}
