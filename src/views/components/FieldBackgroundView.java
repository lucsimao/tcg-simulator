package views.components;

import java.awt.event.MouseEvent;

import views.Screen;

public class FieldBackgroundView extends ElementView{

	
	
	private static final String BACKGROUND_PATH = "images/textures/background.jpg";
	private static final String FIELD_MAT_PATH = "images/textures/field3.png";

	public FieldBackgroundView(int x, int y, int width, int height) {
		super(null, x, y, width, height);
		// TODO Auto-generated constructor stub
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

	@Override
	public void drawItself(Screen screen) {
		//IMAGEM DE FUNDO
		screen.imageScaled(BACKGROUND_PATH, 0, 0, getWidth(), getHeight(), 0,getX(),getY(),1);
		screen.imageScaled(FIELD_MAT_PATH, 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()*23/90 , getHeight()/2 - getHeight()/3,0.7f);
	}
	
}
