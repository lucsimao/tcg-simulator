package dm.graphics;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class FieldSelectionElement extends ElementGraphic {

	private List<SelectionGraphicElement> selectionGraphics;
	
	public FieldSelectionElement() {
		super(null, 0, 0, 0, 0, 0);
		this.selectionGraphics = new ArrayList<SelectionGraphicElement>();
	}

	public void addSelectionGraphicElement(SelectionGraphicElement s) {
		this.selectionGraphics.add(s);
	}
	
	public void addSelectionGraphicElement(SelectionGraphicElement s, int index) {
		this.selectionGraphics.remove(index);
		this.selectionGraphics.add(index,s);;
	}
	
	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		for(SelectionGraphicElement s : selectionGraphics)
			s.hoverAction(mouseEvent);
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
		for(SelectionGraphicElement s : selectionGraphics) {
			s.drawItself(screen);
		}
	}


}
