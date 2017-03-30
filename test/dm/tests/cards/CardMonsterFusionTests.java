package dm.tests.cards;

import org.junit.Before;
import dm.cards.DuelMonsterFusionCard;
import dm.cards.Effect;
import dm.tests.cards.abstracts.CardMonsterTests;

public class CardMonsterFusionTests extends CardMonsterTests {
	@Override
	@Before
	public void initCard(){
		setCard(new DuelMonsterFusionCard(name, description, null, type, atribute, originalAttack, originalDefense,0, new Effect(), copies_number));
	}
}