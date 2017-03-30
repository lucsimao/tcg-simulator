package dm.tests.cards.abstracts;

import org.junit.Before;

public abstract class CardTests<GenericCard> {

	private GenericCard card;
	
	@Before
	public abstract void initCard();
	
	public GenericCard getCard() {
		return card;
	}

	public void setCard(GenericCard card) {
		this.card = card;
	}
	
}
