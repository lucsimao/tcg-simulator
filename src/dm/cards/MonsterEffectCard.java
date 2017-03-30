package dm.cards;

import java.awt.Image;

import dm.cards.abstracts.MonsterCard;
import dm.constants.ColorPicture;
import dm.exceptions.EffectMonsterWithNoEffectException;
import dm.interfaces.NormalDeckCard;

public class MonsterEffectCard extends MonsterCard implements  NormalDeckCard  {

	public MonsterEffectCard(String name, String description, Image picture,
			int type, int atribute, int originalAttack, int originalDeffense,
			Effect effect, int copies_number) {
		super(name, description,ColorPicture.NORMAL, picture, type, atribute, originalAttack, originalDeffense, effect,copies_number);
		if(effect == null)
			throw new EffectMonsterWithNoEffectException("An effect card needs to have a effect defined");
	}
}
