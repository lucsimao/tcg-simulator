package dm.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Set;

import dm.cards.MonsterEffectCard;
import dm.cards.abstracts.Card;

public class Yugioh implements Game{

	private int width;
	private int height;
	CardGraphic card;
	
	public Yugioh() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = (int) screenSize.getWidth();
		this.height = (int) screenSize.getHeight();
		
		int card_width  = Math.round(getWidth()/27.69f);
		int card_height = Math.round(getHeight()/11.7f) ;
		int card_dis_x  = Math.round(getWidth()/15.16f);
		int padding = Math.round(getWidth()/144);
		int card_view_width = Math.round(getWidth()/8.136f);
		int card_view_height = Math.round(getHeight()/3.469f);
		Card c = new MonsterEffectCard();
		card = new CardGraphicHand(c, padding + 460,padding + 700,card_view_width/2, card_view_height/2);
		
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
	public void mouse(MouseEvent mouseEvent) {
		if(mouseEvent.getButton()== MouseEvent.BUTTON1)
			if(card.isOverIt(mouseEvent.getX(),mouseEvent.getY()))
				System.out.println("EM CIMA");
			else
				System.out.println("FORA");
	}
	
	@Override
	public void move(MouseEvent mouseEvent) {
		try {
			card.ClickAction(mouseEvent);
//		else
//			System.out.println("FORA");
		}catch (Exception e) {
			System.out.println("EXCEPTION");
		}
	}
	
	public void varrerField() {
		//TODO
	}
	
	public void varrerMao() {
		 
	}
	
	@Override
	public void draw(Screen screen) {
		
		int card_width  = Math.round(getWidth()/27.69f);
		int card_height = Math.round(getHeight()/11.7f) ;
		int card_dis_x  = Math.round(getWidth()/15.16f);
		int padding = Math.round(getWidth()/144);
		int card_view_width = Math.round(getWidth()/8.136f);
		int card_view_height = Math.round(getHeight()/3.469f);
		screen.imageScaled("images/textures/background.jpg", 0, 0, getWidth(), getHeight(), 0,0, 0);
//		screen.imageScaledPerspective("images/textures/field2.png", 0, 0, getWidth()*2/3, getHeight()/2, 0,getWidth()/2 - getWidth()/3, getHeight()/4);
//		System.out.println("TAMANHO DA TELA: WIDTH: "+ getWidth() + "  HEIGHT: " + getHeight());
		screen.imageScaled("images/textures/field2.png", 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()/2 - getWidth()*3/10, getHeight()/2 - getHeight()/3);
		
		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width, card_view_height,0, padding,padding);
		screen.text("EXODIA O CAPIROTEX", 10,290, 20, Color.WHITE);
//		for(int i=0;i<5;i++) {
//			
//			Card c = new MonsterEffectCard();
//			card = new CardGraphicField(c,Math.round(getWidth()/2.89f) + card_dis_x * i ,Math.round(getHeight()/2.94f), card_width ,card_height);
//			card.drawItself(screen);
//			
////			screen.imageScaled("images/cards/default.jpg", 0, 0,card_width ,card_height, 0,Math.round(getWidth()/2.89) + card_dis_x * i,Math.round(getHeight()/2.94));
//		}
		
//		for(int i=0;i<5;i++) {
//			screen.imageScaled("images/cards/facedown.png", 0, 0,card_width ,card_height, 0,Math.round(getWidth()/2.89) + card_dis_x * i,Math.round(getHeight()/4.15));
//		}
//		for(int i=0;i<5;i++) {
//			screen.imageScaled("images/cards/facedown.png", 0, 0,card_width ,card_height, 0,Math.round(getWidth()/2.89) + card_dis_x * i,Math.round(getHeight()/1.7445));
//		}
//		for(int i=0;i<5;i++) {
//			screen.imageScaled("images/cards/facedown.png", 0, 0,card_width ,card_height, 0,Math.round(getWidth()/2.89) + card_dis_x * i,Math.round(getHeight()/1.4832));
//		}
//		Card c = new MonsterEffectCard();
//		card = new CardGraphicHand(c, padding + 460,padding + 700,card_view_width/2, card_view_height/2);
		card.drawItself(screen);
		
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 460,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 560,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 660,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 760,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 860,padding + 700);
//		System.out.println("TAMANHO: " + (Math.round(getHeight()/1.7445) + 89));
	}
	
	public static void main(String args[]) {
		new Engine(new Yugioh());
	}
	
}
