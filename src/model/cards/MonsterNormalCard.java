package model.cards;

import config.constants.ColorPicture;
import config.constants.FilesConstants;
import config.constants.MonsterAttribute;
import config.constants.MonsterType;
import model.cards.abstracts.MonsterCard;

/**
 * Classe Monstro normal, representa monstros sem efeito. Sua cor é amarelada.
 * Ela não contém efeitos no construtor, portanto, seu efeito sempre será null.
 * 
 * @author Simão
 */

public class MonsterNormalCard extends MonsterCard implements NormalDeckCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2858687724595096561L;

	public MonsterNormalCard(String name, String description, String picture, MonsterType spellcaster, MonsterAttribute attribute,int level,
			int originalAttack, int originalDeffense, int copies_number) {
		super(name, description, ColorPicture.NORMAL, picture, spellcaster, attribute, level,originalAttack, originalDeffense, null,
				copies_number);
	}

	public MonsterNormalCard(String name, String description, MonsterType spellcaster, MonsterAttribute attribute,int level,
			int originalAttack, int originalDeffense, int copies_number) {
		this(name, description,name + ".jpg", spellcaster, attribute,level, originalAttack, originalDeffense,
				copies_number);
	}

	
	public MonsterNormalCard() {
		super(nameGenerator(), "Carta padrão para testes", ColorPicture.NORMAL,  FilesConstants.DEFAULT_MONTER_CARD_IMAGE, MonsterType.AQUA, MonsterAttribute.DARK,4, 0, 0, null, 3);
	}

	public MonsterNormalCard(int copies_number) {
		super(nameGenerator(), "Carta padrão para testes", ColorPicture.NORMAL, FilesConstants.DEFAULT_MONTER_CARD_IMAGE, MonsterType.AQUA, MonsterAttribute.DARK,4, 0, 0, null, copies_number);
	}

}
