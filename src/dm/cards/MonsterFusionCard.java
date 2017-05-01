package dm.cards;

import dm.cards.abstracts.MonsterCard;
import dm.constants.ColorPicture;
import dm.interfaces.ExtraDeckCard;

/**
 * Classe Monstro de Fusão. Representa os monstros de fusão. Eles diferenciam o
 * tipo genérico e somente o extradeck poderá contê-los. Sua cor será roxa. Eles
 * podem conter efeito nulo, caso sejam monstros normais e podem conter efeitos,
 * caso sejam de efeito.
 * 
 * @author Simão
 */

public class MonsterFusionCard extends MonsterCard implements ExtraDeckCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7850575761434909539L;

	public MonsterFusionCard(String name, String description, String picture, int type, int atribute,
			int originalAttack, int originalDeffense, int state, Effect effect, int copies_number) {
		super(name, description, ColorPicture.FUSION, picture, type, atribute, originalAttack, originalDeffense, effect,
				copies_number);
	}

	public MonsterFusionCard(int copies_number) {
		super(null, null, ColorPicture.FUSION, null, 0, 0, 0, 0, null, copies_number);
	}

}
