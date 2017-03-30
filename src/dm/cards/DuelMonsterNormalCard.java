package dm.cards;

import java.awt.Image;

import dm.cards.abstracts.MonsterCard;
import dm.constants.ColorPicture;
import dm.interfaces.NormalDeckCard;

public class DuelMonsterNormalCard extends MonsterCard implements NormalDeckCard  {

	public DuelMonsterNormalCard(String name, String description, Image picture,
			int type, int atribute, int originalAttack, int originalDeffense,
			int state, int copies_number) {
		super(name, description,ColorPicture.NORMAL, picture, type, atribute, originalAttack, originalDeffense,
			 null,copies_number);
	}
}
