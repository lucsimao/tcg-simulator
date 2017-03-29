package dm.tests.cards;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelMonsterEffectCard;
import dm.cards.Effect;
import dm.exceptions.EffectMonsterWithNoEffectException;

public class CardMonsterEffectTests extends CardMonsterTests {
	
	@Override
	@Before
	public void initCard(){
		setCard(new DuelMonsterEffectCard(name, description, null, type, atribute, originalAttack, originalDefense,new Effect(), copies_number));
	}
	
	@Test
	public void checkIfEffectIsNotNull(){
		/**Uma carta monstro normal não pode ter um efeito definido.*/
		setCard(null);
		try{
			setCard(new DuelMonsterEffectCard(name, description, null, type, atribute, originalAttack, originalDefense,null, copies_number));
		}catch (EffectMonsterWithNoEffectException e) {
//			System.out.println(getCard());
		}
	}
}