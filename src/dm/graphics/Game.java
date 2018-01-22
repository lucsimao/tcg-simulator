package dm.graphics;

import java.awt.event.MouseEvent;

public abstract class Game {
	
    private int width;
	private int height;
	
	public Game(int width, int height) {
		this.width = width;
		this.height = height;
	}
	abstract void tick(java.util.Set<String> teclas, double dt);
    abstract void key(String tecla);
    abstract void draw(Screen screen);
    abstract void mouse(MouseEvent mouseEvent);
    abstract void move(MouseEvent mouseEvent);
    
	public String getTitle() {
		return "Yu-gi-oh!";
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return height;
	}
    
    
}
