package dm.fields.elements.decks;

import dm.cards.abstracts.Card;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;
import dm.interfaces.NormalDeckCard;

/*From @Simao
 * Deck normal, ele diferencia pois aceita cartas de diversos tipos, menos cartas de extra deck.
 * */
public class NormalDeck extends Deck<NormalDeckCard> {
	private static final int MAX_CARDS = 60;
	private static final int MIN_CARDS = 40;

	// Adiciona uma carta ao deck
	@Override
	public void putCard(NormalDeckCard card) {
		if(getCards().size()<=MAX_CARDS)
			if (countCards(card) < card.getCopiesNumber())
				getCards().push(card);
			else
				throw new MaxCardCopiesException("You can only have " + ((Card) card).getCopiesNumber() + " copies of this card");
		else
			throw new MaxDeckSizeException("You can have only " + MAX_CARDS + " cards on your deck");		
	}
		
	//Verifica se o deck está dentro do número esperado de cartas
	public boolean isPlayable(){
		return (this.size()>= MIN_CARDS && this.size()<= MAX_CARDS);
	}
	
}
