package dm.game;

import dm.cards.abstracts.MonsterCard;

public class BattlePhase extends Phase {

	
	
	public BattlePhase() {
		super();
	}
		
	public void attack(MonsterCard attacking, Player player_attacked, MonsterCard attacked) {
		if(attacking.canAttack())
			getPlayer().attack(attacking, player_attacked, attacked);
	}
	
}
