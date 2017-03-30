package dm.tests.cards;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelSpellCard;
import dm.cards.Effect;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.SpellType;

public abstract class CardSpellTests extends CardTests<DuelSpellCard> {

	protected static String name = "Dark Hole";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static int type = SpellType.NORMAL;
	protected static int copies_number = 3;
	protected static Effect effect = null;
	protected static Image picture = null;
	
	protected static int cardType = CardType.SPELL;
	protected static int colorPicture = ColorPicture.SPELL;
	
	@Override
	@Before
	public void initCard(){
		setCard(new DuelSpellCard(name, description, picture, effect,type, copies_number));
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
