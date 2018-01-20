package dm.graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Set;

public class Yugioh implements Game{

	private int width;
	private int height;
	
	public Yugioh() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = (int) screenSize.getWidth();
		this.height = (int) screenSize.getHeight();
	}
	
	@Override
	public String getTitle() {
		return "Yu-gi-oh!";
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void tick(Set<String> teclas, double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void key(String tecla) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Screen screen) {
		screen.imageScaled("images/textures/background2.jpg", 0, 0, getWidth(), getHeight(), 0,0, 0);
		screen.imageScaledPerspective("images/textures/field2.png", 0, 0, getWidth()*2/3, getHeight()/2, 0,getWidth()/2 - getWidth()/3, getHeight()/4);
	}

	public static void main(String args[]) {
		new Engine(new Yugioh());
	}
	
}
