package dm.game;

import dm.cards.SpellCard;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.exceptions.ActivateException;
import dm.exceptions.NormalSummonException;
import dm.exceptions.SetException;
import dm.exceptions.SpecialSummonException;

public class BattlePhase extends Phase {

	
	
	public BattlePhase() {
		super();
	}
		
	public void attack(MonsterCard attacking, Player player_attacked, MonsterCard attacked) {
		if(attacking.canAttack())
			getPlayer().attack(attacking, player_attacked, attacked);
	}
	
}
