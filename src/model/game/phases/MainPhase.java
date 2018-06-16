package model.game.phases;

import config.exceptions.ActivateException;
import config.exceptions.NormalSummonException;
import config.exceptions.SetException;
import config.exceptions.SpecialSummonException;
import config.exceptions.TributeSummonException;
import model.cards.abstracts.MonsterCard;
import model.cards.abstracts.NonMonsterCard;
import model.game.Player;

public class MainPhase extends Phase {

	private int normal_summon_count;
	private int tribute_summon_count;
	private int special_summon_count;
	private int set_monster_count;
	private int activate_non_monster_count;
	private int set_non_monster_count;
	
	private boolean can_tribute_summon;
	private int max_normal_summon;
	private boolean can_special_summon;
	private boolean can_activate_non_monster;
	private boolean can_set_non_monster;
	
	public MainPhase(Player player) {
		super(player);
		this.normal_summon_count = 0;
		this.special_summon_count = 0;
		this.set_monster_count = 0;
		this.activate_non_monster_count = 0;
		this.set_monster_count=0;
		this.tribute_summon_count = 0;
		
		this.max_normal_summon = 1;
		this.can_activate_non_monster = true;
		this.can_special_summon = true;
		this.can_set_non_monster = true;
	}
	
	public void normalSummon(MonsterCard monsterCard) throws NormalSummonException{
		if(can_normal_summon()) {
			getPlayer().summon(monsterCard);
			normal_summon_count ++;
		}
		else
			throw new NormalSummonException("Normal summon já realizada");
	}
	
	private boolean can_normal_summon() {
		if(normal_summon_count + set_monster_count < max_normal_summon)
			return true;
		return false;
	}

	private boolean can_tribute_summon() {
		return can_normal_summon();
	}
	
	public void activateCard( NonMonsterCard card) throws ActivateException {
		if(can_activate_non_monster) {
			getPlayer().activate(card);
			inscreaseActivate_non_monster_count();
		}
		else
			throw new ActivateException("Ativação não permitida");
	
	}
	
	public void setCard(NonMonsterCard card) throws SetException  {
		if (can_set_non_monster) {
			getPlayer().set(card);
			inscreaseSet_non_monster_count();
		} else
			throw new SetException("Colocação não permitida");
	}
	
	public void setCard(MonsterCard card) throws SetException{
		if(can_normal_summon()) {
			getPlayer().set(card);
			set_monster_count ++;
		}
		else
			throw new SetException("Colocação já realizada");
	}
	
	public void specialSummon(MonsterCard card) throws SpecialSummonException {
		if(can_special_summon) {
			getPlayer().summon(card);
			inscreaseSpecial_summon_count();
		}else
			throw new SpecialSummonException("Special Summon não permitida");
	}

	public void tributeSummon(MonsterCard monsterCard, MonsterCard... tributes) {
		if(can_tribute_summon())
			getPlayer().tributeSummon(monsterCard, tributes);		
		else
			throw new TributeSummonException("Tribute Summon não permitida");
	}
	
	public int getMax_normal_summon() {
		return max_normal_summon;
	}

	public void setMax_normal_summon(int max_normal_summon) {
		this.max_normal_summon = max_normal_summon;
	}

	public boolean isCan_special_summon() {
		return can_special_summon;
	}

	public void setCan_special_summon(boolean can_special_summon) {
		this.can_special_summon = can_special_summon;
	}

	public int getTribute_summon_count() {
		return tribute_summon_count;
	}

	public void increaseTribute_summon_count() {
		this.tribute_summon_count++;
	}

	public int getSpecial_summon_count() {
		return special_summon_count;
	}

	public void inscreaseSpecial_summon_count() {
		this.special_summon_count++;
	}

	public int getActivate_non_monster_count() {
		return activate_non_monster_count;
	}

	public void inscreaseActivate_non_monster_count() {
		this.activate_non_monster_count++;
	}

	public int getSet_non_monster_count() {
		return set_non_monster_count;
	}

	public void inscreaseSet_non_monster_count() {
		this.set_non_monster_count++;
	}

	public boolean isCan_tribute_summon() {
		return can_tribute_summon;
	}

	public void setCan_tribute_summon(boolean can_tribute_summon) {
		this.can_tribute_summon = can_tribute_summon;
	}	
	
}
