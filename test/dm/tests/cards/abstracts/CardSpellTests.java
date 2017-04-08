package dm.tests.cards.abstracts;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.SpellCard;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.SpellType;
import dm.exceptions.NoEffectException;

public abstract class CardSpellTests extends CardTests<SpellCard> {

	protected static String name = "Dark Hole";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static int type = SpellType.NORMAL;
	protected static int copies_number = 3;
	protected static Effect effect = new Effect();
	protected static Image picture = null;
	
	protected static int cardType = CardType.SPELL;
	protected static int colorPicture = ColorPicture.SPELL;
	
	@Override
	@Before
	public void initCard(){
		try {
			setCard(new SpellCard(name, description, picture, effect,type, copies_number));
		} catch (NoEffectException e) {
		}
	}
	
	@Test
	public void checkAttributesOnInitialCondition(){
		assertEquals(name,getCard().getName());
		assertEquals(description,getCard().getDescription());
		assertEquals(type,getCard().getType());
		assertEquals(cardType,getCard().getCardType());
		assertEquals(colorPicture,getCard().getColorPicture());
		assertEquals(copies_number,getCard().getCopiesNumber());
	}
		
}
