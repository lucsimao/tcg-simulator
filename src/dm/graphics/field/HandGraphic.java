package dm.graphics.field;

import java.awt.event.MouseEvent;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

import dm.cards.abstracts.Card;
import dm.game.Player;
import dm.graphics.Screen;

public class HandGraphic extends ElementGraphic {

	private Player player;
	private List<CardGraphicHand> elements;
	private int distance;
	
	public HandGraphic(Player player,int x,int y,int width,int height, int distance) {
		super(null, x,y,width,height);
		this.player = player;
		this.elements = new ArrayList<CardGraphicHand>();
		this.distance = distance;
		updateHand();
	}
	
	
	
	private void updateHand() {
		elements = new ArrayList<CardGraphicHand>();
		for(int i = 0;i<player.getHand().size();i++) {
			CardGraphicHand card = new CardGraphicHand(player,player.getHand().getCardsList().get(i), getX() + i*distance,getY(),getWidth(),getHeight());
			if(!elements.contains(card))
				elements.add(card);
		}
	}



	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		for(CardGraphicHand c : elements) {
			c.hoverAction(mouseEvent);
		}
//		updateHand();
	}

	@Override
	public void clickAction(MouseEvent mouseEvent) {
		for(CardGraphicHand c : elements) {
			c.clickAction(mouseEvent);
		}
		updateHand();
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawItself(Screen screen) {
		for(CardGraphicHand c : elements) {
			c.drawItself(screen);
		}
	}
	
}
