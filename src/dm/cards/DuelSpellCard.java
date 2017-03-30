package dm.cards;

import java.awt.Image;

import cards.NormalDeckCard;
import dm.cards.abstracts.DuelNonMonsterCard;
import dm.constants.CardType;
import dm.constants.ColorPicture;

public class DuelSpellCard extends DuelNonMonsterCard implements  NormalDeckCard  {
	/*Classe carta mágica*/
	public int type;

	public DuelSpellCard(String name, String description, Image picture, Effect effect,
			int type,int copies_number) {
		super(name, description,CardType.SPELL, ColorPicture.SPELL, picture, effect,copies_number);
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
