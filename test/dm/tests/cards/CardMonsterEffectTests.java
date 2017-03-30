package dm.tests.cards;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelMonsterEffectCard;
import dm.cards.Effect;
import dm.exceptions.EffectMonsterWithNoEffectException;
import dm.tests.cards.abstracts.CardMonsterTests;

public class CardMonsterEffectTests extends CardMonsterTests {
	
	@Override
	@Before
	public void initCard(){
		setCard(new DuelMonsterEffectCard(name, description, null, type, atribute, originalAttack, originalDefense,new Effect(), copies_number));
	}
	
	@Test(expected=EffectMonsterWithNoEffectException.class)
	public void checkIfEffectIsNotNull(){
		/**Uma carta monstro normal não pode ter um efeito definido.*/
		setCard(new DuelMonsterEffectCard(name, description, null, type, atribute, originalAttack, originalDefense,null, copies_number));

	}
}