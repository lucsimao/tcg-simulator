package dm.game;

import dm.exceptions.EndOfTurnException;
import dm.game.phases.BattlePhase;
import dm.game.phases.DrawPhase;
import dm.game.phases.EndPhase;
import dm.game.phases.MainPhase;
import dm.game.phases.Phase;
import dm.game.phases.StandByPhase;

/**
 * Classe Jogada 
 * 
 * @author Simão
 * Classe Rodada, ela gerencia e cada fase de uma rodada.
 * 
 * */
public class Turn {

	private Phase phase;
	private Player player;
	
	public Turn(Player player) {
		phase = new DrawPhase(player);
		this.player = player;
	}
	
	public void changePhase(Phase phase) {
		this.phase = phase;
	}
	
	/**Para seguir o fluxo de avanço de phases*/
	public void advancePhase() {
		if (phase.getClass().equals(DrawPhase.class)) {
			this.phase = new StandByPhase(player);
		}else if (phase.getClass().equals(StandByPhase.class)) {
			this.phase = new MainPhase(player);
		}else if (phase.getClass().equals(MainPhase.class)) {
			this.phase = new BattlePhase(player);
		}else if (phase.getClass().equals(BattlePhase.class)) {
			this.phase = new EndPhase(player);
		}else if (phase.getClass().equals(EndPhase.class)) {
			throw new EndOfTurnException();
		}
			
	}

	public Phase getPhase() {
		return this.phase;
	}
}
