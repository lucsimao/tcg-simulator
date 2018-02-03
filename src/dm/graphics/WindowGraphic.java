package dm.graphics;

import java.awt.event.MouseEvent;

import dm.constants.FilesConstants;

public class WindowGraphic extends ElementGraphic{

	public WindowGraphic(int x, int y, int width, int height) {
		super(FilesConstants.WINDOW_IMAGE,x, y, width, height);
	}

	@Override
	public void drawItself(Screen screen) {
		screen.imageScaled(FilesConstants.THEME_PATH + getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
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
