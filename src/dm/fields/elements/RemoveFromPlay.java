package dm.fields.elements;

import dm.cards.abstracts.Card;
import dm.exceptions.CardNotFoundException;
import dm.fields.elements.decks.Deck;

public class RemoveFromPlay extends Deck<Card> {
	@Override
	public void putCard(Card card) {
		getCards().push(card);
	}
	
	public Card remove(Card card)
	{
		if(getCards().contains(card))
		{
			getCards().remove(card);
			return card;
		}
		throw new CardNotFoundException("This card does not exist in this deck");
	}
	
	public Card remove(int index)
	{
		try{
			return getCards().remove(index);
		}catch (ArrayIndexOutOfBoundsException  e) {
			throw new CardNotFoundException("This card does not exist in this deck");
		}
	}
}
