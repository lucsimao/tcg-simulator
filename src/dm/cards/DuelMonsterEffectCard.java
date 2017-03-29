package dm.cards;

import java.awt.Image;

import cards.NormalDeckCard;
import constants.ColorPicture;
import dm.exceptions.EffectMonsterWithNoEffectException;

public class DuelMonsterEffectCard extends DuelMonsterCard implements  NormalDeckCard  {

	public DuelMonsterEffectCard(String name, String description, Image picture,
			int type, int atribute, int originalAttack, int originalDeffense,
			Effect effect, int copies_number) {
		super(name, description,ColorPicture.NORMAL, picture, type, atribute, originalAttack, originalDeffense, effect,copies_number);
		if(effect == null)
			throw new EffectMonsterWithNoEffectException("An effect card needs to have a effect defined");
	}
}
