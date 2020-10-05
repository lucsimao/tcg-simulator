package dm.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterNormalCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.cards.abstracts.Card;
import dm.constants.CardLevel;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.constants.SpellType;
import dm.constants.TrapType;

public class Seed {

	private final static Logger LOGGER = Logger.getLogger(Seed.class.getName());
	
	public static void main(String args[]) {
		Seed seed = new Seed();
		seed.seedDataBase();
	}
	
	private ArrayList<Card> cards;
	
	public Seed() {
		cards = new ArrayList<Card>();
	}
	
	public void seedDataBase() {
		CardDAO cardDAO = new CardDAO();
		try {
			cardDAO.clearFile();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		fillCards();
		for(Card card : cards) {
			try {
				cardDAO.saveCard(card);
			} catch (Exception e) {
				// TODO: erro
				LOGGER.severe("ERRO AO ADICIONAR CARTA: " + card.getName() + " \n" + e.getMessage() + "\n");
			}
		}
		LOGGER.info("Database seeded succeffully.");
	}
	
	private void fillCards() {
		cards.add(new MonsterNormalCard("Dark Magician","The ultimate wizard in terms of attack and defense.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,7,2500,2100,3));
		cards.add(new MonsterNormalCard("Summoned Skull","A fiend with dark powers for confusing the enemy. Among the Fiend-Type monsters, this monster boasts considerable force.\r\n" + 
				"\r\n" + 
				"(This card is always treated as an \"Archfiend\" card.)", MonsterType.FIEND,MonsterAttribute.DARK,1,2500,1200,3));
		cards.add(new MonsterNormalCard("Red-Eyes B Dragon","A ferocious dragon with a deadly attack.", MonsterType.DRAGON,MonsterAttribute.DARK,7,2400,2000,3));
		cards.add(new MonsterNormalCard("Right Arm of The Forbidden One","A forbidden right arm sealed by magic. Whosoever breaks this seal will know infinite power.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,1,200,300,1));
		cards.add(new MonsterNormalCard("Right Leg of The Forbidden One","A forbidden right leg sealed by magic. Whosoever breaks this seal will know infinite power.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,1,200,300,1));
		cards.add(new MonsterNormalCard("Left Arm of The Forbidden One","A forbidden left arm sealed by magic. Whosoever breaks this seal will know infinite power.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,1,200,300,1));
		cards.add(new MonsterNormalCard("Left Leg of The Forbidden One","A forbidden left leg sealed by magic. Whosoever breaks this seal will know infinite power.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,1,200,300,1));
		cards.add(new MonsterEffectCard("Exodia The Forbidden One","If you have \"Right Leg of the Forbidden One\", \"Left Leg of the Forbidden One\", \"Right Arm of the Forbidden One\" and \"Left Arm of the Forbidden One\" in addition to this card in your hand, you win the Duel.", MonsterType.SPELLCASTER,MonsterAttribute.DARK,3,1000,1000,new Effect(), 1));
		//Mermails
		cards.add(new MonsterEffectCard("Mermail Abyssmegalo","You can discard 2 other WATER monsters to the Graveyard; Special Summon this card from your hand. When you do: You can add 1 \"Abyss-\" Spell/Trap Card from your Deck to your hand. You can Tribute 1 other face-up Attack Position WATER monster; this card can make a second attack during each Battle Phase this turn.",
				MonsterType.SEASERPENT,MonsterAttribute.WATER,CardLevel.SEVEN,2400,1900,new Effect(),3));
		cards.add(new MonsterEffectCard("Mermail Abyssteus","You can discard 1 other WATER monster to the GY; Special Summon this card from your hand. If Summoned this way: Add 1 Level 4 or lower \"Mermail\" monster from your Deck to your hand. You can only use this effect of \"Mermail Abyssteus\" once per turn.",
				MonsterType.AQUA,MonsterAttribute.WATER,CardLevel.SEVEN,1700,2400,new Effect(),3));
		cards.add(new MonsterEffectCard("Mermail Abyssgunde","If this card is discarded to the Graveyard: You can target 1 \"Mermail\" monster in your Graveyard, except \"Mermail Abyssgunde\"; Special Summon that target. You can only use the effect of \"Mermail Abyssgunde\" once per turn.",
				MonsterType.AQUA,MonsterAttribute.WATER,CardLevel.THREE,1400,800,new Effect(),3));
		//Atlanteans
		cards.add(new MonsterEffectCard("Deep Sea Diva","When this card is Normal Summoned: You can Special Summon 1 Level 3 or lower Sea Serpent-Type monster from your Deck.",
				MonsterType.SEASERPENT,MonsterAttribute.WATER,CardLevel.TWO,200,400,new Effect(),1));
		cards.add(new MonsterEffectCard("Atlantean Marksman","When this card inflicts battle damage to your opponent: You can Special Summon 1 Level 4 or lower \"Atlantean\" Sea Serpent-Type monster from your Deck, except \"Atlantean Marksman\". When this card is sent to the Graveyard to activate a WATER monster's effect: Target 1 Set card your opponent controls; destroy that target.",
				MonsterType.SEASERPENT,MonsterAttribute.WATER,CardLevel.THREE,1400,0,new Effect(),3));
		cards.add(new MonsterEffectCard("Atlantean Heavy Infantry","During your Main Phase, you can Normal Summon 1 Level 4 or lower Sea Serpent-Type monster in addition to your Normal Summon/Set. (You can only gain this effect once per turn.) When this card is sent to the Graveyard to activate a WATER monster's effect: Target 1 face-up card your opponent controls; destroy that target.",
				MonsterType.SEASERPENT,MonsterAttribute.WATER,CardLevel.TWO,0,1600,new Effect(),3));
		cards.add(new MonsterEffectCard("Atlantean Dragoons","All Level 3 or lower Sea Serpent-Type monsters you control can attack your opponent directly. When this card is sent to the Graveyard to activate a WATER monster's effect: Add 1 Sea Serpent-Type monster from your Deck to your hand, except \"Atlantean Dragoons\".",
				MonsterType.SEASERPENT,MonsterAttribute.WATER,CardLevel.FOUR,1800,0,new Effect(),2));
	
		//Mágicas
		cards.add(new SpellCard("A Legendary Ocean", "(This card's name is always treated as \"Umi\".)\r\n" + 
				"All WATER monsters on the field gain 200 ATK/DEF. Reduce the Level of all WATER monsters in both players' hands and on the field by 1",new Effect(),SpellType.FIELD, 3));
		cards.add(new SpellCard("Change of Heart","Target 1 monster your opponent controls; take control of it until the End Phase.", new Effect(), SpellType.NORMAL, 0));
		cards.add(new SpellCard("Nobleman of Crossout","Target 1 face-down monster on the field; destroy that target, and if you do, banish it, then, if it was a Flip monster, each player reveals their Main Deck, then banishes all cards from it with that monster's name.", new Effect(), SpellType.NORMAL, 3));
		//Armadilhas
		cards.add(new TrapCard("Trap Hole","When your opponent Normal or Flip Summons 1 monster with 1000 or more ATK: Target that monster; destroy that target.", new Effect(),TrapType.NORMAL,3));
		cards.add(new TrapCard("Skull Dice","Roll a six-sided die. All monsters your opponent currently controls lose ATK and DEF equal to the result x 100, until the End Phase.", new Effect(),TrapType.NORMAL,3));
		cards.add(new TrapCard("Kunai With Chain","Activate 1 or both of these effects (simultaneously).\r\n" + 
				"- When an opponent's monster declares an attack: Target the attacking monster; change that target to Defense Position.\r\n" + 
				"- Target 1 face-up monster you control; equip this card to that target. It gains 500 ATK.", new Effect(),TrapType.NORMAL,3));
	}
	
}
