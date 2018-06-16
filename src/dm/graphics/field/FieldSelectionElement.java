package dm.graphics.field;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import dm.game.Player;
import dm.graphics.Screen;

/**
 * Classe responsável por mostrar as cartas em campo.
 * */
public class FieldSelectionElement extends GraphicElement {
	
	private List<SelectionGraphicElement> selectionGraphics;
	private Player player1;
	private Player player2;
	private int screenWidth;
	private int screenHeight;
	private CardDetailsGraphic cardDetailsGraphic;
	
	public FieldSelectionElement(CardDetailsGraphic cardDetailsGraphic,Player player1, Player player2, int screenWidth, int screenHeight) {
		super(null, 0, 0, 0, 0, 0);
		this.selectionGraphics = new ArrayList<SelectionGraphicElement>();
		this.player1 = player1;
		this.player2 = player2;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.cardDetailsGraphic = cardDetailsGraphic;
	}
	
	public void addSelectionGraphicElement(SelectionGraphicElement s) {
		this.selectionGraphics.add(s);
	}
	
	public void addSelectionGraphicElement(SelectionGraphicElement s, int index) {
		this.selectionGraphics.remove(index);
		this.selectionGraphics.add(index,s);;
	}
	
	public void addSelectionElements(int x_offset) {
		int special_zone_x = (Math.round(screenWidth*607f/900) + x_offset);
		int special_zone_padding = Math.round(screenWidth*(360f)/900);

		int special_zone_height = Math.round(screenHeight*59f/650);
		int special_zone_width = Math.round(screenWidth*40f/900);
		
		int extra_zone_x_1 = Math.round(screenWidth*(299f+61)/900);
		int extra_zone_x_2 = Math.round(screenWidth*(299f+61+120)/900);
		int extra_zone_y = Math.round(screenHeight*(295f)/650);

		
		
		
		
		
		int zone_x =  Math.round(screenWidth*302f/900)+ x_offset;
		int zone_distance = Math.round(screenWidth*5f/900);
		int zone_spell1_y =  Math.round(screenHeight*434f/650);
		int zone_spell2_y =  Math.round(screenHeight*155f/650);
		int zone_monster1_y =  Math.round(screenHeight*369f/650);
		int zone_monster2_y =  Math.round(screenHeight*221f/650);
		int zone_height = Math.round(screenHeight*61f/650);
		int zone_width = Math.round(screenWidth*54f/900);
		
		
		//ADDING
		SelectionGraphicElement extra1 = new SelectionGraphicElement(extra_zone_x_1 + x_offset,extra_zone_y, zone_width, zone_height);
		SelectionGraphicElement extra2 = new SelectionGraphicElement(extra_zone_x_2 + x_offset,extra_zone_y, zone_width, zone_height);
		SelectionGraphicElement deck1 = new SelectionGraphicElement(special_zone_x,Math.round(screenHeight*(474f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement grave1 = new SelectionGraphicElement(special_zone_x,Math.round(screenHeight*(404f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement banned1 = new SelectionGraphicElement(special_zone_x,Math.round(screenHeight*(332f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement field2 = new SelectionGraphicElement(special_zone_x,Math.round(screenHeight*(188f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement extra_deck2 = new SelectionGraphicElement(special_zone_x,Math.round(screenHeight*(118f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement deck2 = new SelectionGraphicElement(special_zone_x - special_zone_padding,Math.round(screenHeight*(118f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement grave2 = new SelectionGraphicElement(special_zone_x - special_zone_padding,Math.round(screenHeight*(188f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement	banned2 = new SelectionGraphicElement(special_zone_x - special_zone_padding,Math.round(screenHeight*(260f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement field1 = new SelectionGraphicElement(special_zone_x - special_zone_padding,Math.round(screenHeight*(404f)/650), special_zone_width, special_zone_height);
		SelectionGraphicElement extra_deck1 = new SelectionGraphicElement(special_zone_x - special_zone_padding,Math.round(screenHeight*(474f)/650), special_zone_width, special_zone_height);	
		
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
		
		
		//MONSTERSPELLZONE
		//MONSTER1
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new GraphicFieldZoneElement(cardDetailsGraphic,zone_x + (zone_width + zone_distance)*i, zone_monster1_y, zone_width, zone_height,player1.getMonsterZone(),i));
		}
		//MONSTER2
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new GraphicFieldZoneElement(cardDetailsGraphic,zone_x + (zone_width + zone_distance)*i, zone_monster2_y, zone_width, zone_height,player2.getMonsterZone(),i));
		}
		//SPELL1
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new GraphicFieldZoneElement(cardDetailsGraphic,zone_x + (zone_width + zone_distance)*i, zone_spell1_y, zone_width, zone_height,player1.getSpellTrapZone(),i));
		}
		//SPELL2
		for(int i=0;i<5;i++) {
			this.selectionGraphics.add(new GraphicFieldZoneElement(cardDetailsGraphic,zone_x + (zone_width + zone_distance)*i, zone_spell2_y, zone_width, zone_height,player2.getSpellTrapZone(),i));
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
