package dm.fields.elements;

import dm.cards.abstracts.Card;
import dm.fields.elements.decks.Deck;

public class Hand  extends Deck<Card>{

	private final int MAX_CARDS = 7;
	
	private int maxCards;
	private boolean isHandPlayable;
	
	public Hand() {
		this.maxCards = MAX_CARDS;
		isHandPlayable = true;
	}
	
	@Override
	public void putCard(Card card) {
		getCards().push(card);
		if(getCards().size()<=maxCards)
			isHandPlayable = true;
		else
			isHandPlayable = false;
	}

	@Override
	public Card drawCard() {
		if(getCards().size()<=maxCards)
			isHandPlayable = false;
		else
			isHandPlayable = true;
		return super.drawCard();
	}
	
	public boolean isHandPlayable(){
		return isHandPlayable;
	}
	
	public void setMaxCards(int maxCards){
		this.maxCards = maxCards;
	}
	
	public int getMaxCards()
	{
		return this.maxCards;
	}
	
}
