package dm.game.phases;

import dm.exceptions.PlayerCannotDrawException;
import dm.game.Player;

public class DrawPhase extends Phase {
	
	Player player;
	private boolean can_draw;
	private int draw_counter;
	
	public DrawPhase(Player player) {
		super(player);
		setCan_draw(true);
		player.draw();	
		setCan_draw(false);
	}
	
	public DrawPhase(Player player,boolean can_draw) {
		super(player);
		this.setCan_draw(can_draw);
		if(can_draw)
			player.draw();
		else 
			throw new PlayerCannotDrawException("Player cannnot draw now.");
	}

	public boolean isCan_draw() {
		return can_draw;
	}

	public void setCan_draw(boolean can_draw) {
		this.can_draw = can_draw;
	}

	public int getDraw_counter() {
		return draw_counter;
	}

	public void setDraw_counter(int draw_counter) {
		this.draw_counter = draw_counter;
	}
	
}
