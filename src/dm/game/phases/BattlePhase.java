package dm.game.phases;

import dm.cards.abstracts.MonsterCard;
import dm.game.Player;

public class BattlePhase extends Phase {

	
	
	public BattlePhase(Player player) {
		super(player);
	}
		
	public void attack(MonsterCard attacking, Player player_attacked, MonsterCard attacked) {
		if(attacking.canAttack())
			getPlayer().attack(attacking, player_attacked, attacked);
	}
	
}
