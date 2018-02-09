package dm.graphics.field;

import java.awt.event.MouseEvent;

import dm.graphics.Screen;

public abstract class ElementGraphic{
	private String picture;
	private int x;
	private int y;
	private int width;
	private int height;
	private float alpha;
	
	public ElementGraphic(String picture,int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.picture = picture;
		this.alpha = 1;
	}
	
	public ElementGraphic(String picture,int x, int y, int width, int height,float alpha) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.picture = picture;
		this.alpha = alpha;
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

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	boolean isOverIt(int x,int y) {
		if(x>=this.x && x<=this.x+width && y>=this.y&&y<=this.y + height)
			return true;
		return false;
	}
	
	public void drawItself(Screen screen){
		screen.imageScaled(getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
	}
	public abstract void hoverAction(MouseEvent mouseEvent);
	public abstract void clickAction(MouseEvent mouseEvent);
	public abstract void pressedAction(MouseEvent mouseEvent);
}
