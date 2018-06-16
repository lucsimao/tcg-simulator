package views.components;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import views.Screen;

public abstract class ElementView{
	private String picture;
	private int x;
	private int y;
	private int width;
	private int height;
	private float alpha;
	private Dimension colisionBox;
	
	public ElementView(String picture,int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.picture = picture;
		this.alpha = 1;
		this.colisionBox = new Dimension(this.width, this.height);
	}
	
	public ElementView(String picture,int x, int y, int width, int height,float alpha) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.picture = picture;
		this.alpha = alpha;
	}

	/***
	 * Verifica se o mouse a posição x e y estão em cima da estrutura.
	 * */
	boolean isOverIt(int x,int y) {
		if(x>=this.x && x<=this.x+colisionBox.getWidth() && y>=this.y&&y<=this.y + colisionBox.getHeight())
			return true;
		return false;
	}
	
	public void drawItself(Screen screen){
		screen.imageScaled(getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
	}
	
	//Abstracts Methods
	public abstract void hoverAction(MouseEvent mouseEvent);
	public abstract void clickAction(MouseEvent mouseEvent);
	public abstract void pressedAction(MouseEvent mouseEvent);
	//Abstracts Methods
	
	//Getters Setters
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
	public Dimension getColisionBox() {
		return colisionBox;
	}
	public void setColisionBox(Dimension colisionBox) {
		this.colisionBox = colisionBox;
	}
	//Getters Setters
	
}
