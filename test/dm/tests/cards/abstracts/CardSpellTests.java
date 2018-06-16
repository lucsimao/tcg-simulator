package dm.tests.cards.abstracts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import config.constants.CardType;
import config.constants.ColorPicture;
import config.constants.SpellType;
import config.exceptions.NoEffectException;
import model.cards.Effect;
import model.cards.SpellCard;

public abstract class CardSpellTests extends CardTests<SpellCard> {

	protected static String name = "Dark Hole";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static SpellType type = SpellType.NORMAL;
	protected static int copies_number = 3;
	protected static Effect effect = new Effect();
	protected static String picture = null;

	protected static CardType cardType = CardType.SPELL;
	protected static ColorPicture colorPicture = ColorPicture.SPELL;

	@Override
	@Before
	public void initCard() {
		try {
			setCard(new SpellCard(name, description, picture, effect, type, copies_number));
		} catch (NoEffectException e) {
		}
	}

	@Test
	public void checkAttributesOnInitialCondition() {
		assertEquals(name, getCard().getName());
		assertEquals(description, getCard().getDescription());
		assertEquals(type, getCard().getType());
		assertEquals(cardType, getCard().getCardType());
		assertEquals(colorPicture, getCard().getColorPicture());
		assertEquals(copies_number, getCard().getCopiesNumber());
	}

}
