package dm.fields.elements.decks;

import cards.ExtraDeckCard;
import dm.cards.abstracts.DuelCard;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;

public class DuelExtraDeck extends DuelDeck<ExtraDeckCard>{

	private static final int MAX_CARDS = 15;

	// Adiciona uma carta ao deck
	@Override
	public void putCard(ExtraDeckCard card) {
		if(getCards().size()<=MAX_CARDS)
			if (countCards(card) < (card).getCopiesNumber())
				getCards().push(card);
			else
				throw new MaxCardCopiesException("You can only have " + ((DuelCard) card).getCopiesNumber() + " copies of this card");
		else
			throw new MaxDeckSizeException("You can have only " + MAX_CARDS + " cards on your deck");		
	}

	public boolean isPlayable() {
		return (this.size()<= MAX_CARDS);
	}

	public int getMaxCards() {
		return MAX_CARDS;
	}
	
}
