package dm.tests.cards;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.exceptions.EffectMonsterWithNoEffectException;
import dm.tests.cards.abstracts.CardMonsterTests;

public class CardMonsterEffectTests extends CardMonsterTests {

	@Override
	@Before
	public void initCard() {
		setCard(new MonsterEffectCard(name, description, null, type, atribute,level, originalAttack, originalDefense,
				new Effect(), copies_number));
	}

	@Test(expected = EffectMonsterWithNoEffectException.class)
	public void checkIfEffectIsNotNull() {
		/** Uma carta monstro normal não pode ter um efeito definido. */
		setCard(new MonsterEffectCard(name, description, null, type, atribute,level, originalAttack, originalDefense, null,
				copies_number));

	}
}