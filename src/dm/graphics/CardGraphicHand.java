package dm.graphics;

import java.awt.event.MouseEvent;

import dm.cards.abstracts.Card;

public class CardGraphicHand extends CardGraphic {

	private int x_temp;
	private int y_temp;
	
	public CardGraphicHand(Card card, int x, int y, int width, int height) {
		super(card, x, y, width, height);
		this.x_temp = x;
		this.y_temp = y;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		
	}

	@Override
	public void ClickAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
			System.out.println("EM CIMA");
			if(getY()==y_temp)
				setY(getY()-20);
		}
		else
			setY(y_temp);		
	}


	
	
	
}
