package views.components;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import model.game.Player;
import views.Screen;

public class HandView extends ElementView {

	private Player player;
	private List<CardInHandView> elements;
	private int distance;
	
	private CardDetailsView cardDetailsGraphic;
	
	public HandView(CardDetailsView cardDetailsGraphic, Player player,int x,int y,int width,int height, int distance) {
		super(null, x,y,width,height);
		this.player = player;
		this.elements = new ArrayList<CardInHandView>();
		this.distance = distance;
		this.cardDetailsGraphic = cardDetailsGraphic;
		updateHand();
	}
	
	
	
	private void updateHand() {
		elements = new ArrayList<CardInHandView>();
//		try {
//			distance = (600-player.getHand().size()*getWidth()/2)/(player.getHand().size());
//			System.out.println(getWidth());
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
		for(int i = 0;i<player.getHand().size();i++) {
			int size = player.getHand().size();
			int handWidth = distance*size;
//			CardGraphicHand card = new CardGraphicHand(cardDetailsGraphic,player,player.getHand().getCardsList().get(i), getX() + i*distance - size*15 ,getY(),getWidth(),getHeight());
			//TODO
			CardInHandView card = new CardInHandView(cardDetailsGraphic,player,player.getHand().getCardsList().get(i), getX() + 160 + i*distance - handWidth/2  ,getY(),getWidth(),getHeight());
			if(!elements.contains(card))
				elements.add(card);
		}

		
	}



	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		for(CardInHandView c : elements) {
			c.hoverAction(mouseEvent);
		}
//		updateHand();
	}

	@Override
	public void clickAction(MouseEvent mouseEvent) {
		for(CardInHandView c : elements) {
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
		for(CardInHandView c : elements) {
			c.drawItself(screen);
		}
	}
	
	@Override
	public void setX(int x) {
		super.setX(x);
		updateHand();
	}
	
	@Override
	public void setY(int y) {
		super.setY(y);
		updateHand();
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		updateHand();
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		updateHand();
	}
}
