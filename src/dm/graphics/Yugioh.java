package dm.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import dm.cards.abstracts.Card;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.files.DeckDao;
import dm.game.Player;
import dm.graphics.field.ButtonGraphic;
import dm.graphics.field.CardDetailsGraphic;
import dm.graphics.field.CardGraphicHand;
import dm.graphics.field.ElementGraphic;
import dm.graphics.field.FieldSelectionElement;
import dm.graphics.field.HandGraphic;
import dm.graphics.field.WindowGraphic;

public class Yugioh extends Game{

	private final int card_dis_x  = Math.round(getWidth()/15.16f);
	private final int padding = Math.round(getWidth()/144);
	private final int card_view_width = Math.round(getWidth()/6.836f);
	private final int card_view_height = Math.round(getHeight()/3.469f);
	//Botões laterais
	private final int panel_button_x = Math.round(getWidth()/1.16788f);
	private final int panel_button_y = Math.round(getHeight()/3.4625f);
	private final int panel_distance = Math.round(getHeight()/10.833333f);
	private final int button_width = Math.round(getWidth()/9f);
	private final int button_height = Math.round(getHeight()/13f);
	
	
	
	private Player player1;
	private Player player2;
	
	CardGraphicHand card;
	private Field field1;
	private Field field2;
	ArrayList<Card> hand;
	private HandGraphic handGraphic;	
	private ArrayList<ElementGraphic> elements;
	private FieldSelectionElement fieldSelectionElement;
	
	WindowGraphic w;
	private int x_offset;
	
	public Yugioh(Player player1, Player player2) {
//		super((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		super(900,650);
		x_offset = 0;
//		Card c = new MonsterEffectCard();
//		card = new CardGraphicHand(c, padding + 460,padding + 700,card_view_width/2, card_view_height/2);
		this.player1 = player1;
		this.player2 = player2;
		

		
		field1 = player1.getField();
		field2 = player2.getField();
		player1.shuffleDeck();
		player1.firstDraw();
//		player1.activate((NonMonsterCard) player1.getHand().getCardsList().get(4));
//		player1.set((NonMonsterCard) player1.getHand().getCardsList().get(3));
//		System.out.println(player1.getField().getSpellTrapZone().getCard(0).getState());
//		System.out.println(player1.getField().getSpellTrapZone().getCard(1).getState());
//		List<Card> list = player1.getHand().getCardsList();
//		for(Card c : list) {
//			if(c instanceof MonsterCard)
//				player1.summon((MonsterNormalCard) c);
//		}
		
		this.elements = new ArrayList<ElementGraphic>();
		

//		selectionGraphicElement.setColor(Color.RED);
		
		w = new WindowGraphic(0, 0, getWidth()*2/9,getHeight());
	
		ButtonGraphic draw = new ButtonGraphic("DRAW",panel_button_x ,panel_button_y + 0*panel_distance,button_width,button_height);
		ButtonGraphic M1 = new ButtonGraphic("M1",panel_button_x ,panel_button_y + 1*panel_distance,button_width,button_height);
		ButtonGraphic BP = new ButtonGraphic("BP",panel_button_x ,panel_button_y + 2*panel_distance,button_width,button_height);
		ButtonGraphic M2 = new ButtonGraphic("M2",panel_button_x ,panel_button_y + 3*panel_distance,button_width,button_height);
		ButtonGraphic EP = new ButtonGraphic("EP",panel_button_x ,panel_button_y + 4*panel_distance,button_width,button_height);
		draw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player1.draw();
			}
		});
		x_offset = w.getWidth()/2 - draw.getWidth()/2;
//		hand = (ArrayList<Card>) player1.getField().getHand();
//		createCards(hand, padding + Math.round(getWidth()/3.13f) + x_offset,Math.round(getHeight() -  card_view_height*3/8), card_view_width/2, card_view_height/2, card_dis_x);
		
		
//		int special_zone_x = w.getWidth()*608/900 + x_offset;
////		int special_zone_x = 608 + x_offset;
//		int special_zone_height = w.getHeight()*59/600;
//		int special_zone_width = w.getWidth()*39/900;
//		int normal_zone_height = w.getHeight()*61/600;
//		int normal_zone_width = w.getWidth()*54/900;
		
//		SelectionGraphicElement extra1 = new SelectionGraphicElement(299 + 61 + x_offset,295, normal_zone_width,normal_zone_height);
//		SelectionGraphicElement extra2 = new SelectionGraphicElement(298 + 61 + 121 + x_offset,295, 54, 61);
//		SelectionGraphicElement deck1 = new SelectionGraphicElement(special_zone_x,474, special_zone_width, special_zone_height);
//		SelectionGraphicElement grave1 = new SelectionGraphicElement(special_zone_x,404, special_zone_width, special_zone_height);
//		SelectionGraphicElement banned1 = new SelectionGraphicElement(special_zone_x,332, special_zone_width, special_zone_height);
//		SelectionGraphicElement field2 = new SelectionGraphicElement(special_zone_x,188, special_zone_width, special_zone_height);
//		SelectionGraphicElement extra_deck2 = new SelectionGraphicElement(special_zone_x,118, special_zone_width, special_zone_height);
//		SelectionGraphicElement deck2 = new SelectionGraphicElement(special_zone_x- 360,118, special_zone_width, special_zone_height);
//		SelectionGraphicElement grave2 = new SelectionGraphicElement(special_zone_x- 360,188, special_zone_width, special_zone_height);
//		SelectionGraphicElement	banned2 = new SelectionGraphicElement(special_zone_x- 360,260, special_zone_width, special_zone_height);
//		SelectionGraphicElement field1 = new SelectionGraphicElement(special_zone_x- 360,404, special_zone_width, special_zone_height);
//		SelectionGraphicElement extra_deck1 = new SelectionGraphicElement(special_zone_x- 360,474, special_zone_width, special_zone_height);
		
		CardDetailsGraphic cardDetailsGraphic = new CardDetailsGraphic(0,0,getWidth(),getHeight());
		this.handGraphic = new HandGraphic(cardDetailsGraphic,player1, padding + Math.round(getWidth()/3.13f) + x_offset,Math.round(getHeight() -  card_view_height*3/8), card_view_width/2, card_view_height/2, card_dis_x);
		fieldSelectionElement = new FieldSelectionElement(cardDetailsGraphic,player1,player2,getWidth(),getHeight());
		fieldSelectionElement.addSelectionElements(x_offset);
//		fieldSelectionElement.addSelectionGraphicElement(extra1);
//		fieldSelectionElement.addSelectionGraphicElement(extra2);
//		fieldSelectionElement.addSelectionGraphicElement(deck1);
//		fieldSelectionElement.addSelectionGraphicElement(grave1);
//		fieldSelectionElement.addSelectionGraphicElement(banned1);
//		fieldSelectionElement.addSelectionGraphicElement(field2);
//		fieldSelectionElement.addSelectionGraphicElement(extra_deck2);
//		fieldSelectionElement.addSelectionGraphicElement(deck2);
//		fieldSelectionElement.addSelectionGraphicElement(grave2);
//		fieldSelectionElement.addSelectionGraphicElement(banned2);
//		fieldSelectionElement.addSelectionGraphicElement(field1);
//		fieldSelectionElement.addSelectionGraphicElement(extra_deck1);
		
		
		elements.add(fieldSelectionElement);
		elements.add(draw);
		elements.add(M1);
		elements.add(BP);
		elements.add(M2);
		elements.add(EP);
		elements.add(handGraphic);
		elements.add(cardDetailsGraphic);
		
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
//		if(mouseEvent.getButton()== MouseEvent.BUTTON1)
//		{
//		
//			if(mouseEvent.is) {
//				System.out.println("MOUSE");
			try {
				for(ElementGraphic e : elements)
					e.clickAction(mouseEvent);
//			else
//				System.out.println("FORA");
			}catch (Exception e) {
				e.printStackTrace();
			}
//			}
//		}
	}
	
	@Override
	public void move(MouseEvent mouseEvent) {
		try {
			for(ElementGraphic e : elements)
				e.hoverAction(mouseEvent);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void createCards(ArrayList<Card> cards,int x,int y,int width,int height, int distance) {
//		for(int i = 0;i<cards.size();i++) {
//			card = new CardGraphicHand(player1,cards.get(i), x + i*distance,y,width,height);
//			if(!elements.contains(card))
//				elements.add(card);
//		}
//	}
	
	private void drawCards(Screen screen,ArrayList<ElementGraphic> elements2) {
		for(ElementGraphic e : elements2)
			e.drawItself(screen);
	}

	
	@Override
	public void draw(Screen screen) {		
		//		c, padding + 460,padding + 700,card_view_width/2, card_view_height/2
		
		
//		int padding = Math.round(getWidth()/144);
//		int card_view_width = Math.round(getWidth()/8.136f);
//		int card_view_height = Math.round(getHeight()/3.469f);
		//IMAGEM DE FUNDO
		screen.imageScaled("images/textures/background.jpg", 0, 0, getWidth(), getHeight(), 0,0, 0,1);
//		screen.imageScaledPerspective("images/textures/field2.png", 0, 0, getWidth()*2/3, getHeight()/2, 0,getWidth()/2 - getWidth()/3, getHeight()/4);
//		System.out.println("TAMANHO DA TELA: WIDTH: "+ getWidth() + "  HEIGHT: " + getHeight());
		//FIELD
//		screen.rectangle(getWidth()*23/90 , getHeight()/2 - getHeight()/3,getWidth()*3/5, getHeight()*2/3, Color.BLACK,0.65f);
//		screen.imageScaled("images/theme/summon.png", 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()*23/90 , getHeight()/2 - getHeight()/3,1f);
		screen.imageScaled("images/textures/field3.png", 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()*23/90 , getHeight()/2 - getHeight()/3,0.7f);
		
		/**Magicas player 1 */

//		
//		int special_zone_x = 608 + x_offset;
//		int special_zone_height = 59;
//		int special_zone_width = 39;
//		int deck_1_y = 473;
//		//DECK 1
//		screen.rectangle(special_zone_x ,474, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//GRAVE1
//		screen.rectangle(special_zone_x,404, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//BAN1
//		screen.rectangle(special_zone_x,332, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//FIELD2 
//		screen.rectangle(special_zone_x,188, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//EXTRA2
//		screen.rectangle(special_zone_x,118, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//DECK2
//		screen.rectangle(special_zone_x - 360,118, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//GRAVE2
//		screen.rectangle(special_zone_x - 360,188, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//BAN2
//		screen.rectangle(special_zone_x - 360,260, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//FIELD1
//		screen.rectangle(special_zone_x - 360,404, special_zone_width, special_zone_height, Color.RED,0.4f);
//		//EXTRA1
//		screen.rectangle(special_zone_x - 360,474, special_zone_width, special_zone_height, Color.RED,0.4f);
		
		//		screen.rectangle(299 + 60,434, 58, 64, Color.RED,0.4f);
//		//EXTRA ZONE1
//		screen.rectangle(299 + 61 + x_offset,295, 54, 61, Color.RED,0.4f);
//		//EXTRA ZONE2
//		screen.rectangle(299 + 61 + 121,295, 54, 61, Color.RED,0.4f);
		
//		screen.rectangle(299 + 120,434, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 179,434, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 239,434, 58, 66, Color.WHITE,0.4f);
//		
//		/**Monstros player 1 */
//		screen.rectangle(299,369, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 60,369, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 120,369, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 179,369, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 239,369, 58, 66, Color.WHITE,0.4f);
//		
//		/**Monstros player 2 */
//		screen.rectangle(299,218, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 60,218, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 120,218, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 179,218, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 239,218, 58, 66, Color.WHITE,0.4f);
//		/**Magias player 2 */
//		screen.rectangle(299,153, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 60,153, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 120,153, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 179,153, 58, 66, Color.WHITE,0.4f);
//		screen.rectangle(299 + 239,153, 58, 66, Color.WHITE,0.4f);
		
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
//			screen.imageScaled("images/cards/facedown.png", 0, 0,card_width ,card_height, 0,Math.round(getWidth()/2.89)+ x_offset + card_dis_x * i,Math.round(getHeight()/4.12),1);
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
		
//		CardGraphic g = new CardGraphic(FilesConstants.DEFAULT_MONTER_CARD_IMAGE, 0,0, 177, 256);
//		w.addElement(g);
		w.drawItself(screen);
//		WindowGraphic w2 = new WindowGraphic(750, 0, getWidth()/4,getHeight());
//		w2.drawItself(screen);
//		screen.imageScaled(FilesConstants.CARDS_IMG_DIR + FilesConstants.DEFAULT_MONTER_CARD_IMAGE,0,0,178,250,0,10,45,1);
//		screen.imageScaled(FilesConstants.THEME_PATH + "tabControl.png",0,0,getWidth()*2/9 - 10,340,0,5,300,1);
//		screen.imageScaled(FilesConstants.THEME_PATH + "darkTab.png",0,0,getWidth()*2/9 - 10,35,0,5,300,1);
//		screen.text("EXODIA, THE FORBIDDEN ONE",15,322,12,Color.WHITE);
//		screen.text("Spellcaster/Effect",15,350,12,Color.BLACK);
//		screen.text("Attribute DARK",15,361,12,Color.BLACK);
//		screen.text("Level 3",15,372,12,Color.BLACK);
//		screen.text("1000 / 1000",15,383,12,Color.BLACK);
////		screen.textMultiLine();
//		screen.textMultiLine("When you have \"Right Arm of the Forbidden One\",\"Left Arm of the Forbidden One\",\"Right Leg of the Forbidden One\",\"Left Leg of the Forbidden One\", in addition to this card in your hand, you win the duel. ",15,404,12,getWidth()*2/9 - 30,Color.BLACK);
		fieldSelectionElement.drawItself(screen);
		drawCards(screen,elements);
		//EFEITOS DE CARTA
//		screen.imageScaled(FilesConstants.THEME_PATH + "normalSummon.jpg",0,0,28,38,0,280,535,1);
//		screen.imageScaled(FilesConstants.THEME_PATH + "setCard.png",0,0,28,38,0,310,535,1);
//		screen.imageScaled(FilesConstants.THEME_PATH + "summon.png",0,0,35,35,0,340,535,1);
//		screen.text("SUMMON",292,557,12,Color.WHITE);

		
//		draw.drawItself(screen);
//		M1.drawItself(screen);
//		BP.drawItself(screen);
//		M2.drawItself(screen);
//		EP.drawItself(screen);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 460,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 560,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 660,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 760,padding + 700);
//		screen.imageScaled("images/cards/default.jpg", 0, 0, card_view_width/2, card_view_height/2,0, padding + 860,padding + 700);
//		System.out.println("TAMANHO: " + (Math.round(getHeight()/1.7445) + 89));
	}	
	
//	protected NormalDeck loadDeck(File deckName) {
//		DeckDao deckDao = new DeckDao();
//		try {
//			return deckDao.loadDeck(deckName);
//		} catch (ClassNotFoundException | IOException ex) {
//			JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getName(),
//					JOptionPane.INFORMATION_MESSAGE);
//		}
//		return null;
//	}
//	
	@Override
	public void tick(Set<String> teclas, double dt) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) throws FileNotFoundException, ClassNotFoundException, IOException {
		DeckDao deckDao = new DeckDao();
		File file = new File("deck/baralho.ygo");
		NormalDeck deck = deckDao.loadDeck(file);
		Player player1 = new Player("teste1", null, deck, new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		new Engine(new Yugioh(player1,player2));
	}
	

	
}
