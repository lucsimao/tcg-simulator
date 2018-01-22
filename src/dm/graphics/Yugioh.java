package dm.graphics;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import dm.cards.MonsterEffectCard;
import dm.cards.abstracts.Card;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class Yugioh extends Game{

	private final int card_width  = Math.round(getWidth()/27.69f);
	private final int card_height = Math.round(getHeight()/11.7f) ;
	private final int card_dis_x  = Math.round(getWidth()/15.16f);
	private final int padding = Math.round(getWidth()/144);
	private final int card_view_width = Math.round(getWidth()/8.136f);
	private final int card_view_height = Math.round(getHeight()/3.469f);
	
	private Player player1;
	private Player player2;
	
	ElementGraphic card;
	private Field field1;
	private Field field2;
	ArrayList<Card> hand;
	private ArrayList<ElementGraphic> elements;
	
	public Yugioh(Player player1, Player player2) {
//		super((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		super(900,650);
		
//		Card c = new MonsterEffectCard();
//		card = new CardGraphicHand(c, padding + 460,padding + 700,card_view_width/2, card_view_height/2);
		this.player1 = player1;
		this.player2 = player2;
		
		field1 = player1.getField();
		field2 = player2.getField();
		player1.firstDraw();
		
		this.elements = new ArrayList<ElementGraphic>();
		
		hand = (ArrayList<Card>) player1.getField().getHand().getCardsList();
		createCards(hand, padding + Math.round(getWidth()/3.13f),Math.round(getHeight() -  card_view_height*2/5), card_view_width/2, card_view_height/2, card_dis_x);
	}
	
	@Override
	public void key(String tecla) {
		if(tecla.equals(" ")){
			player1.draw();
//			scorenumber.setScore(0);
//			proxCena().executa();
		}
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
			for(ElementGraphic e : elements)
				e.hoverAction(mouseEvent);
//		else
//			System.out.println("FORA");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createCards(ArrayList<Card> cards,int x,int y,int width,int height, int distance) {
		for(int i = 0;i<cards.size();i++) {
			card = new CardGraphicHand(cards.get(i), x + i*distance,y,width,height);
			if(!elements.contains(card))
				elements.add(card);
//			card.drawItself(screen);
		}
	}
	
	private void drawCards(Screen screen,ArrayList<ElementGraphic> elements) {
		for(ElementGraphic e : elements)
			e.drawItself(screen);
	}

	
	@Override
	public void draw(Screen screen) {		
		//		c, padding + 460,padding + 700,card_view_width/2, card_view_height/2
		
		
//		int padding = Math.round(getWidth()/144);
//		int card_view_width = Math.round(getWidth()/8.136f);
//		int card_view_height = Math.round(getHeight()/3.469f);
		screen.imageScaled("images/textures/background.jpg", 0, 0, getWidth(), getHeight(), 0,0, 0,1);
//		screen.imageScaledPerspective("images/textures/field2.png", 0, 0, getWidth()*2/3, getHeight()/2, 0,getWidth()/2 - getWidth()/3, getHeight()/4);
//		System.out.println("TAMANHO DA TELA: WIDTH: "+ getWidth() + "  HEIGHT: " + getHeight());
		screen.imageScaled("images/textures/field2.png", 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()/2 - getWidth()*3/10, getHeight()/2 - getHeight()/3,1);
		
		/**Magicas player 1 */
		screen.rectangle(299,434, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 60,434, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 120,434, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 179,434, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 239,434, 58, 66, Color.WHITE,0.4f);
		
		/**Monstros player 1 */
		screen.rectangle(299,369, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 60,369, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 120,369, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 179,369, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 239,369, 58, 66, Color.WHITE,0.4f);
		
		/**Monstros player 2 */
		screen.rectangle(299,218, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 60,218, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 120,218, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 179,218, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 239,218, 58, 66, Color.WHITE,0.4f);
		/**Magias player 2 */
		screen.rectangle(299,153, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 60,153, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 120,153, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 179,153, 58, 66, Color.WHITE,0.4f);
		screen.rectangle(299 + 239,153, 58, 66, Color.WHITE,0.4f);
		
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width, card_view_height,0, padding,padding);
//		screen.text("EXODIA O CAPIROTEX", 10,290, 20, Color.WHITE);
		
		
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
//		card.drawItself(screen);
		
		drawCards(screen,elements);
		
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 460,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 560,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 660,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 760,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 860,padding + 700);
//		System.out.println("TAMANHO: " + (Math.round(getHeight()/1.7445) + 89));
	}	
	
	@Override
	public void tick(Set<String> teclas, double dt) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) {
		Player player1 = new Player("teste1", null, new NormalDeck(50), new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		new Engine(new Yugioh(player1,player2));
	}
	
}
