package dm.fields.elements;

import constants.CardState;
import dm.cards.DuelCard;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.ZoneOccupedException;

public class CardZone {
	private DuelCard[] cards;
	private static final int ZONE_SIZE = 5;
	
	public CardZone(){
		cards = new DuelCard[ZONE_SIZE];
	}
	
	protected  DuelCard[] getCards(){
		return cards;
	}
	
	public DuelCard getCard(int index)
	{
		if(cards[index]!= null)
			return cards[index];
		else 
			throw new CardNotFoundException("Card is not found in this deck");
	}
	
	public void putCard(DuelCard card, int index)
	{
		if(cards[index] == null)
			cards[index] = card;
		else
			throw new ZoneOccupedException("The zone that you want to put a card is already in use");
	}
	
	public  DuelCard remove(int index) {
		 DuelCard card;
		if(cards[index]!=null)
		{
			card = cards[index];
			card.setState(CardState.NONE);
			cards[index]=null;
			return card;
		}
		else
			throw new CardNotFoundException("Index does not has a card attached on it.");
	}

	public void removeAll() {
		for(int i=0;i<ZONE_SIZE;i++)
			cards[i] = null;	
	}
	
	public int countCards(){
		int number =0;
		for(int i=0;i<ZONE_SIZE;i++)
		{
			if(cards[i] != null)
				number++;
		}
		return number;
	}
	
}
