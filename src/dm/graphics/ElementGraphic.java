package dm.graphics;

import java.awt.event.MouseEvent;

import dm.cards.abstracts.Card;

public abstract class ElementGraphic {

	private int x;
	private int y;
	private int width;
	private int height;
	private float alpha;
	private Card card;

	public ElementGraphic(Card card,int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.card = card;
		this.alpha = 1;
	}
	
	
	public float getAlpha() {
		return alpha;
	}


	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}


	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void drawItself(Screen screen){
		screen.imageScaled(card.getPicture(), 0, 0,width ,height, 0,x,y,this.alpha);
	}
	
	boolean isOverIt(int x,int y) {
		if(x>=this.x && x<=this.x+width && y>=this.y&&y<=this.y + height)
			return true;
		return false;
	}
	
	public abstract void hoverAction(MouseEvent mouseEvent);
	public abstract void ClickAction(MouseEvent mouseEvent);
}
