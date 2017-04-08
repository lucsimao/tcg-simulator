package dm.fields.elements.zones;

import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.constants.CardState;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.ZoneOccupedException;

/*From @Simao
 * Zona de Monstros. 
 * Ela � uma zona de cartas que somente aceita monstros.
 * Logo, ela pode special summon, normal summon, set ou mudar para o modo de defesa.*/

public class MonsterZone extends CardZone {

	public void setMonster(MonsterCard monsterCard, int index) {
		putCard(monsterCard, index);	
		monsterCard.setState(CardState.FACE_DOWN);
	}

	public void setMonster(MonsterCard monsterCard) {
		for(int i=0;i<5;i++)
			if(this.getCards()[i]==null)
			{
				this.getCards()[i] = monsterCard;	
				monsterCard.setState(CardState.FACE_DOWN);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}

	public void summonMonster(MonsterCard monsterCard,int index) {
		putCard(monsterCard, index);	
		monsterCard.setState(CardState.FACE_UP_ATTACK);
	}
	
	public void summonMonster(MonsterCard monsterCard) {
		for(int i=0;i<5;i++)
			if(this.getCards()[i]==null)
			{
				this.getCards()[i] = monsterCard;	
				monsterCard.setState(CardState.FACE_UP_ATTACK);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}

	public Card remove(MonsterCard monsterCard) {
		for(int i=0;i<ZONE_SIZE;i++)
			if(monsterCard.equals(getCards()[i]))
			{	
				return remove(i);
			}
		throw new CardNotFoundException("Card was not found");
	}
	
}
