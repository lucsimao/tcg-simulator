package dm.game.phases;

import dm.exceptions.PlayerCannotDrawException;
import dm.game.Player;

public class DrawPhase extends Phase {
	
	Player player;
	private boolean can_draw;
	private boolean draw_counter;
	
	public DrawPhase(Player player) {
		super(player);
		can_draw = true;
		player.draw();	
		can_draw = false;
	}
	
	public DrawPhase(Player player,boolean can_draw) {
		super(player);
		this.can_draw = can_draw;
		if(can_draw)
			player.draw();
		else 
			throw new PlayerCannotDrawException("Player cannnot draw now.");
	}
	
}
