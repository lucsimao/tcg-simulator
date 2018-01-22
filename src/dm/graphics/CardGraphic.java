package dm.graphics;

import dm.cards.abstracts.Card;

public abstract class CardGraphic extends ElementGraphic {

	public CardGraphic(Card card, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.card = card;
	}
	private Card card;

	@Override
	public void drawItself(Screen screen){
		screen.imageScaled(card.getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
	}
	

}
