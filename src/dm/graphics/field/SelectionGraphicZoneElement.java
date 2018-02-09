package dm.graphics.field;

import java.awt.Color;

import dm.exceptions.CardNotFoundException;
import dm.fields.elements.zones.CardZone;
import dm.graphics.Screen;

public class SelectionGraphicZoneElement extends SelectionGraphicElement {

	private CardZone cardZone;
	private int index;

	public SelectionGraphicZoneElement(int x, int y, int width, int height, float alpha, Color color, CardZone cardZone,
			int index) {
		super(x, y, width, height, alpha, color);
		this.cardZone = cardZone;
		this.index = index;
	}

	public SelectionGraphicZoneElement(int x, int y, int width, int height, CardZone cardZone, int index) {
		super(x, y, width, height);
		this.cardZone = cardZone;
		this.index = index;
	}

	@Override
	public void drawItself(Screen screen) {
		super.drawItself(screen);
		try {
			screen.imageScaled(cardZone.getCard(index).getPicture(), 0, 0, getWidth()*8/9, getHeight()*8/9, 0, getX() + getWidth()/18 , getY() + getHeight()/18,
					1f);
		} catch (CardNotFoundException e2) {
			System.out.println("Zona " + index + " não possui cartas no momento");
		} catch (Exception e) {
			System.out.println("SELECTIONGRAPHIC - Exceção não esperada");
		}
	}
}
