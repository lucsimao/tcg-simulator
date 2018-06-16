package views;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import views.components.ElementView;

public class MainView extends GameView {

	private ArrayList<ElementView> elements;

	public MainView(int width, int height) {
		super(width, height);
		this.elements = new ArrayList<ElementView>();
	}

	public void addElement(ElementView view) {
		this.elements.add(view);
	}

	public void removeElement(ElementView view) {
		this.elements.remove(view);
	}

	public ElementView getElement(int index) {
		return this.elements.get(index);
	}

	@Override
	void tick(Set<String> teclas, double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	void key(String tecla) {
		// TODO Auto-generated method stub

	}

	@Override
	void mouse(MouseEvent mouseEvent) {
		try {
			for (ElementView e : elements)
				e.clickAction(mouseEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	void draw(Screen screen) {
		for (ElementView view : elements) {
			view.drawItself(screen);
		}
	}

	@Override
	void move(MouseEvent mouseEvent) {
		try {
			for (ElementView e : elements)
				e.hoverAction(mouseEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
