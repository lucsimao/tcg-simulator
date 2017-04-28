package dm.cards;

import java.awt.Image;
import dm.cards.abstracts.MonsterCard;
import dm.constants.ColorPicture;
import dm.interfaces.ExtraDeckCard;

/** Classe Monstro de Fus�o. Representa os monstros de fus�o. 
 * Eles diferenciam o tipo gen�rico e somente o extradeck poder� cont�-los.
 * Sua cor ser� roxa.
 * Eles podem conter efeito nulo, caso sejam monstros normais e podem conter efeitos, caso 
 * sejam de efeito.
 * @author Sim�o
 * */

public class MonsterFusionCard extends MonsterCard implements ExtraDeckCard {

	public MonsterFusionCard(String name, String description, Image picture, int type, int atribute, int originalAttack,
			int originalDeffense, int state, Effect effect, int copies_number) {
		super(name, description, ColorPicture.FUSION, picture, type, atribute, originalAttack, originalDeffense, effect,
				copies_number);
	}

	public MonsterFusionCard(int copies_number) {
		super(null, null, ColorPicture.FUSION, null, 0, 0, 0, 0, null, copies_number);
	}

}
