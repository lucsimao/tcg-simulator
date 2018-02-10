package dm.graphics.field;

import java.awt.Color;
import java.awt.event.MouseEvent;

import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.constants.FilesConstants;
import dm.graphics.Screen;

public class CardDetailsGraphic extends ElementGraphic {

	private Card card;
	
	public CardDetailsGraphic( int x, int y, int width, int height) {
		super(null, x, y, width, height);
		this.card = null;
	}
	
	public CardDetailsGraphic( int x, int y, int width, int height,float alpha) {
		super(null, x, y, width, height,alpha);
		this.card = null;
	}
	
	public void setCard(Card card) {
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	
	@Override
	public void drawItself(Screen screen) {
		screen.imageScaled(FilesConstants.THEME_PATH + "tabControl.png",0,0,getWidth()*2/9 - 10,340,0,5,300,1);
		screen.imageScaled(FilesConstants.THEME_PATH + "darkTab.png",0,0,getWidth()*2/9 - 10,35,0,5,300,1);
		if(card == null || card.getPicture() == null) {
			screen.imageScaled(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD,0,0,178,250,0,10,45,1);
			
		}else {
			screen.imageScaled(FilesConstants.CARDS_IMG_DIR + card.getPicture(),0,0,178,250,0,10,45,1);

			screen.text(card.getName().toUpperCase(),15,322,12,Color.WHITE);
			if(card instanceof MonsterCard) {
//				screen.text(((MonsterCard)card).getType(),15,350,12,Color.BLACK);
//				screen.text("Attribute DARK",15,361,12,Color.BLACK);
//				screen.text("Level " + ((MonsterCard)card),15,372,12,Color.BLACK);
				screen.text(((MonsterCard)card).getOriginalAttack() + " / " + ((MonsterCard)card).getOriginalDefense(),15,383,12,Color.BLACK);
			}
			screen.textMultiLine(card.getDescription(),15,404,12,getWidth()*2/9 - 30,Color.BLACK);
		}

		

//		screen.textMultiLine();
//		screen.textMultiLine("When you have \"Right Arm of the Forbidden One\",\"Left Arm of the Forbidden One\",\"Right Leg of the Forbidden One\",\"Left Leg of the Forbidden One\", in addition to this card in your hand, you win the duel. ",15,404,12,getWidth()*2/9 - 30,Color.BLACK);

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
