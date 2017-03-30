package dm.fields.elements;

import dm.cards.abstracts.DuelCard;
import dm.fields.elements.decks.DuelDeck;

public class DuelHand  extends DuelDeck<DuelCard>{

	private final int MAX_CARDS = 7;
	
	private int maxCards;
	private boolean isHandPlayable;
	
	public DuelHand() {
		this.maxCards = MAX_CARDS;
		isHandPlayable = true;
	}
	
	@Override
	public void putCard(DuelCard card) {
		System.out.println("Colocou a carta");
		getCards().push(card);
		if(getCards().size()<=maxCards)
			isHandPlayable = true;
		else
			isHandPlayable = false;
	}

	@Override
	public DuelCard drawCard() {
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
