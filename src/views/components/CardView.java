package views.components;

import java.awt.event.MouseEvent;

import config.constants.FilesConstants;
import views.Screen;

public class CardView extends ElementView {

	public CardView(String picture, int x, int y, int width, int height) {
		super(picture, x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawItself(Screen screen) {
		// TODO Auto-generated method stub
		screen.imageScaled(FilesConstants.CARDS_IMG_DIR + getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
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
