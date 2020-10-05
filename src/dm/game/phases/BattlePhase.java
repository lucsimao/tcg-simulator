package dm.game.phases;

import dm.cards.abstracts.MonsterCard;
import dm.exceptions.MonsterCannotAttackException;
import dm.game.Player;

public class BattlePhase extends Phase {

	
	
	public BattlePhase(Player player) {
		super(player);
	}
		
	public void attack(MonsterCard attacking, Player player_attacked, MonsterCard attacked) {
		if(canAttack(attacking)) {
			getPlayer().attack(attacking, player_attacked, attacked);
			attacking.incrementAttacksCount();
		}else {
			if(attacking.getAttacksCount()>0)
				throw new MonsterCannotAttackException("Monstro já atacou essa rodada");
			throw new MonsterCannotAttackException("Monstro não pode atacar");
		}
	}

	private boolean canAttack(MonsterCard attacking) {
		return attacking.getAttacksCount()<attacking.getMaxAttacks();
	}
	
}
