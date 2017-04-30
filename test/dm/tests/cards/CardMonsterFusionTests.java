package dm.tests.cards;

import org.junit.Before;
import dm.cards.MonsterFusionCard;
import dm.cards.Effect;
import dm.tests.cards.abstracts.CardMonsterTests;

public class CardMonsterFusionTests extends CardMonsterTests {
	@Override
	@Before
	public void initCard() {
		setCard(new MonsterFusionCard(name, description, null, type, atribute, originalAttack, originalDefense, 0,
				new Effect(), copies_number));
	}
}