package dm.game;

import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.exceptions.ActivateException;
import dm.exceptions.NormalSummonException;
import dm.exceptions.SetException;
import dm.exceptions.SpecialSummonException;

public class MainPhase extends Phase {

	private int normal_summon_count;
	private int special_summon_count;
	private int set_monster_count;
	private int activate_non_monster_count;
	private int set_non_monster_count;
	
	private boolean can_normal_summon;
	private boolean can_special_summon;
	private boolean can_activate_non_monster;
	private boolean can_set_monster;
	private boolean can_set_non_monster;
	
	
	public MainPhase() {
		super();
		this.normal_summon_count = 0;
		this.special_summon_count = 0;
		this.set_monster_count = 0;
		this.activate_non_monster_count = 0;
		this.set_non_monster_count = 0;
		
		this.can_normal_summon = true;
		this.can_activate_non_monster = true;
		this.can_special_summon = true;
		this.can_set_monster = true;
		this.can_set_non_monster = true;
	}
	
	public void normalSummon(MonsterCard monsterCard) throws NormalSummonException{
		if(can_normal_summon) {
			getPlayer().summon(monsterCard);
			can_normal_summon = false;
			can_set_monster = false;
			normal_summon_count ++;
		}
		else
			throw new NormalSummonException("Normal summon já realizada");
	}
	
	public void activateCard( NonMonsterCard card) throws ActivateException {
		if(can_activate_non_monster) {
			getPlayer().activate(card);
			activate_non_monster_count++;
		}
		else
			throw new ActivateException("Ativação não permitida");
	
	}
	
	public void setCard(NonMonsterCard card) throws SetException  {
		if (can_set_non_monster) {
			getPlayer().set(card);
			set_non_monster_count++;
		} else
			throw new SetException("Colocação não permitida");
	}
	
	public void setCard(MonsterCard card) throws SetException{
		if(can_set_monster) {
			getPlayer().set(card);
			can_set_monster = false;
			can_normal_summon = false;
			set_monster_count ++;
		}
		else
			throw new SetException("Colocação já realizada");
	}
	
	public void specialSummon(MonsterCard card) throws SpecialSummonException {
		if(can_special_summon) {
			getPlayer().summon(card);
			special_summon_count ++;
		}else
			throw new SpecialSummonException("Special Summon não permitida");
	}
	
}
