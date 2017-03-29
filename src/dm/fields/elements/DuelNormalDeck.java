package dm.fields.elements;

import cards.NormalDeckCard;
import dm.cards.DuelCard;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;

public class DuelNormalDeck extends DuelDeck<NormalDeckCard> {
	private static final int MAX_CARDS = 60;
	private static final int MIN_CARDS = 40;

	// Adiciona uma carta ao deck
	@Override
	public void putCard(NormalDeckCard card) {
		if(getCards().size()<=MAX_CARDS)
			if (countCards(card) < card.getCopiesNumber())
				getCards().push(card);
			else
				throw new MaxCardCopiesException("You can only have " + ((DuelCard) card).getCopiesNumber() + " copies of this card");
		else
			throw new MaxDeckSizeException("You can have only " + MAX_CARDS + " cards on your deck");		
	}
		
	//Verifica se o deck está dentro do número esperado de cartas
	public boolean isPlayable(){
		return (this.size()>= MIN_CARDS && this.size()<= MAX_CARDS);
	}
	
}
