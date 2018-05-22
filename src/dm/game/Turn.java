package dm.game;

import dm.game.phases.DrawPhase;
import dm.game.phases.Phase;

/**
 * Classe Jogada 
 * 
 * @author Simão
 */
public class Turn {

	private Phase phase;
	private Player player;
	
	public Turn(Player player) {
		this.player = player;
		this.phase = new DrawPhase(player);
	}
	
}
