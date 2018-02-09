package dm.graphics.field;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import dm.constants.FilesConstants;
import dm.graphics.Screen;

public class WindowGraphic extends ElementGraphic{

	private int padding;
	private int margin;
	private ArrayList<ElementGraphic> elements;
	
	private boolean center_H;
	private boolean center_V;
	
	public WindowGraphic(int x, int y, int width, int height) {
		super(FilesConstants.WINDOW_IMAGE,x, y, width, height);
		this.elements = new ArrayList<ElementGraphic>();
		this.padding = 0;
		this.margin = 0;
		center_V = false;
		center_V = false;
	}
		
	public void setCenter_H(boolean center_H) {
		this.center_H = center_H;
	}

	public void setCenter_V(boolean center_V) {
		this.center_V = center_V;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void addElement(ElementGraphic element) {
		this.elements.add(element);
		fixElements();
	}
	
	private void fixElements() {
		for(int i=0;i<elements.size();i++) {
		
			ElementGraphic e = elements.get(i);
			if(e.getWidth()>getWidth())
				e.setWidth(getWidth());
			if(e.getHeight()>getHeight())
				e.setHeight(getHeight());
			if(center_H)
				e.setX(getX() + (getWidth() - e.getWidth())/2);
			if(center_V)
				e.setY(getY() + (getHeight() - e.getHeight())/2);
		}
	}
	
	@Override
	public void drawItself(Screen screen) {
		screen.imageScaled(FilesConstants.THEME_PATH + getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
		for(ElementGraphic e : elements) {
			e.drawItself(screen);
		}
	}
	
	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

}
