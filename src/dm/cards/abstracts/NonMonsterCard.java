package dm.cards.abstracts;

import dm.cards.Effect;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.FilesConstants;
import dm.exceptions.NoEffectException;

/**
 * Cartas que não são monstros, ou seja, mágicas e armadilhas. Elas são
 * obrigadas a ter efeitos.
 * 
 * @author Simão
 */

public abstract class NonMonsterCard extends Card {

	private static final long serialVersionUID = -6096715673931737766L;

	public NonMonsterCard(String name, String description, CardType cardType, ColorPicture spell, String picture,
			Effect effect, int copies_number) throws NoEffectException {
		super(name, description, cardType, spell, picture, effect, copies_number);
		if (effect == null)
			throw new NoEffectException("An effect card needs to have a effect defined");
	}

	@Override
	public String getPicture() {
		if (picture != null)
			return picture;
		else
			return FilesConstants.DEFAULT_NON_MONSTER_CARD_IMAGE;
	}

}
