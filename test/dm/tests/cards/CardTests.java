package dm.tests.cards;

import org.junit.Before;

public abstract class CardTests<GenericCard> {

	private GenericCard card;
	
	public GenericCard getCard() {
		return card;
	}

	public void setCard(GenericCard card) {
		this.card = card;
	}



	@Before
	public abstract void initCard();
	
	
	
	
}
