package dm.graphics;

import java.util.Set;

public class Yugioh implements Game{

	@Override
	public String getTitle() {
		return "Yu-gi-oh!";
	}

	@Override
	public int getWidth() {
		return 640;
	}

	@Override
	public int getHeight() {
		return 480;
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
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) {
		new Engine(new Yugioh());
	}
	
}
