package dm.game.phases;

import dm.game.Player;

public class Phase {

	private Player player;

	public Phase(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
