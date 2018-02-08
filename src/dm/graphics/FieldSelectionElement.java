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
	
	public void addSelectionElements(int x_offset) {
		int special_zone_x = 608 + x_offset;
		int special_zone_height = 59;
		int special_zone_width = 39;
		//ADDING
		SelectionGraphicElement extra1 = new SelectionGraphicElement(299 + 61 + x_offset,295, 54, 61);
		SelectionGraphicElement extra2 = new SelectionGraphicElement(298 + 61 + 121 + x_offset,295, 54, 61);
		SelectionGraphicElement deck1 = new SelectionGraphicElement(special_zone_x,474, special_zone_width, special_zone_height);
		SelectionGraphicElement grave1 = new SelectionGraphicElement(special_zone_x,404, special_zone_width, special_zone_height);
		SelectionGraphicElement banned1 = new SelectionGraphicElement(special_zone_x,332, special_zone_width, special_zone_height);
		SelectionGraphicElement field2 = new SelectionGraphicElement(special_zone_x,188, special_zone_width, special_zone_height);
		SelectionGraphicElement extra_deck2 = new SelectionGraphicElement(special_zone_x,118, special_zone_width, special_zone_height);
		SelectionGraphicElement deck2 = new SelectionGraphicElement(special_zone_x- 360,118, special_zone_width, special_zone_height);
		SelectionGraphicElement grave2 = new SelectionGraphicElement(special_zone_x- 360,188, special_zone_width, special_zone_height);
		SelectionGraphicElement	banned2 = new SelectionGraphicElement(special_zone_x- 360,260, special_zone_width, special_zone_height);
		SelectionGraphicElement field1 = new SelectionGraphicElement(special_zone_x- 360,404, special_zone_width, special_zone_height);
		SelectionGraphicElement extra_deck1 = new SelectionGraphicElement(special_zone_x- 360,474, special_zone_width, special_zone_height);	
		
		addSelectionGraphicElement(extra1);
		addSelectionGraphicElement(extra2);
		addSelectionGraphicElement(deck1);
		addSelectionGraphicElement(grave1);
		addSelectionGraphicElement(banned1);
		addSelectionGraphicElement(field2);
		addSelectionGraphicElement(extra_deck2);
		addSelectionGraphicElement(deck2);
		addSelectionGraphicElement(grave2);
		addSelectionGraphicElement(banned2);
		addSelectionGraphicElement(field1);
		addSelectionGraphicElement(extra_deck1);
		
		int zone_x = 302 + x_offset;
		int zone_distance = 5;
		int zone_spell1_y = 434;
		int zone_spell2_y = 155;
		int zone_monster1_y = 369;
		int zone_monster2_y = 221;
		int zone_width = 54;
		int zone_height = 61;
		
		//MONSTERSPELLZONE
		//MONSTER1
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new SelectionGraphicElement(zone_x + (zone_width + zone_distance)*i, zone_monster1_y, zone_width, zone_height));
		}
		//MONSTER2
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new SelectionGraphicElement(zone_x + (zone_width + zone_distance)*i, zone_monster2_y, zone_width, zone_height));
		}
		//SPELL1
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new SelectionGraphicElement(zone_x + (zone_width + zone_distance)*i, zone_spell1_y, zone_width, zone_height));
		}
		//SPELL2
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new SelectionGraphicElement(zone_x + (zone_width + zone_distance)*i, zone_spell2_y, zone_width, zone_height));
		}
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
