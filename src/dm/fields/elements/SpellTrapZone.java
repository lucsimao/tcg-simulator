package dm.fields.elements;

import constants.CardState;
import dm.cards.DuelCard;
import dm.cards.DuelMonsterCard;
import dm.exceptions.CardNotFoundException;

public class SpellTrapZone extends CardZone{
	
	public void setMonster(DuelCard spellTrapCard, int index) {
		putCard(spellTrapCard, index);	
		spellTrapCard.setState(CardState.FACE_DOWN);
	}
	
	public DuelCard remove(DuelMonsterCard spellTrapCard) {
		for(int i=0;i<ZONE_SIZE;i++)
			if(spellTrapCard.equals(getCards()[i]))
			{	
				return remove(i);
			}
		throw new CardNotFoundException("Card was not found");
	}
}
