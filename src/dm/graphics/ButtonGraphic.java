package dm.graphics;

import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import dm.constants.FilesConstants;

public class ButtonGraphic extends ElementGraphic {

	private String text;
	private Color color;
	private int textSize;
	
	public ButtonGraphic(int x, int y, int width, int height) {
		super(FilesConstants.BUTTON_IMAGE,x, y, width, height);
		this.text = "BUTTON";
		this.color = Color.BLACK;
		this.textSize = 12;
	}

	public ButtonGraphic(String text,int x, int y, int width, int height) {
		super(FilesConstants.BUTTON_IMAGE,x, y, width,height);
		this.text = text;
		this.color = Color.BLACK;
		this.textSize = 12;
	}

	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void drawItself(Screen screen) {
		screen.imageScaled(FilesConstants.THEME_PATH + getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
		screen.text(text, getX() + (getWidth()-screen.getStringWidth(text))/2,getY() + (getHeight()-screen.getStringHeight())/2 + screen.getStringAscent(), textSize, color);
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
			setPicture(FilesConstants.BUTTON_HOVER_IMAGE);
		}else
			setPicture(FilesConstants.BUTTON_IMAGE);
	}

	@Override
	public void clickAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
			setPicture(FilesConstants.BUTTON_PRESSED_IMAGE);
		}else
			setPicture(FilesConstants.BUTTON_IMAGE);
		
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}




	
}
