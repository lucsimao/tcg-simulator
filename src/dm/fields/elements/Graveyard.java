package dm.fields.elements;

import dm.cards.abstracts.DuelCard;
import dm.exceptions.CardNotFoundException;
import dm.fields.elements.decks.DuelDeck;

public class Graveyard extends DuelDeck<DuelCard>{

	@Override
	public void putCard(DuelCard card) {
		getCards().push(card);
	}
	
	public DuelCard remove(DuelCard card)
	{
		if(getCards().contains(card))
		{
			getCards().remove(card);
			return card;
		}
		throw new CardNotFoundException("This card does not exist in this deck");
	}
	
	public DuelCard remove(int index)
	{
		try{
			return getCards().remove(index);
		}catch (ArrayIndexOutOfBoundsException  e) {
			throw new CardNotFoundException("This card does not exist in this deck");
		}
	}
	
}
