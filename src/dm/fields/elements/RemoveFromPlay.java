package dm.fields.elements;

import dm.cards.DuelCard;
import dm.exceptions.CardNotFoundException;

public class RemoveFromPlay extends DuelDeck<DuelCard> {
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
