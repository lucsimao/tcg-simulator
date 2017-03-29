package dm.fields.elements;

import cards.NormalDeckCard;
import dm.cards.DuelCard;

public class DuelHand  extends DuelDeck<NormalDeckCard>{

	private final int MAX_CARDS = 7;
	
	private int maxCards;
	private boolean isHandPlayable;
	
	public DuelHand() {
		this.maxCards = MAX_CARDS;
		isHandPlayable = true;
	}
	
	@Override
	public void putCard(NormalDeckCard card) {
		System.out.println("Colocou a carta");
		getCards().push(card);
		if(getCards().size()<=maxCards)
			isHandPlayable = true;
		else
			isHandPlayable = false;
	}

	@Override
	public NormalDeckCard drawCard() {
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
