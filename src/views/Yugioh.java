package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import model.cards.abstracts.Card;
import model.cards.abstracts.MonsterCard;
import model.cards.abstracts.NonMonsterCard;
import model.fields.Field;
import model.fields.elements.decks.ExtraDeck;
import model.fields.elements.decks.NormalDeck;
import model.files.DeckDao;
import model.game.Player;
import views.components.ButtonView;
import views.components.CardDetailsView;
import views.components.CardInHandView;
import views.components.ElementView;
import views.components.FieldSelectionElementView;
import views.components.HandView;
import views.components.WindowView;

public class Yugioh extends GameView{

	private final int card_dis_x  = Math.round(getWidth()/13.24f);
	private final int padding = Math.round(getWidth()/144);
	private final int card_view_width = Math.round(getWidth()/6.836f);
	private final int card_view_height = Math.round(getHeight()/3.469f);
	//Botões laterais
	private final int panel_button_x = Math.round(getWidth()/1.16788f);
	private final int panel_button_y = Math.round(getHeight()/3.4625f);
	private final int panel_distance = Math.round(getHeight()/10.833333f);
	private final int button_width = Math.round(getWidth()/9f);
	private final int button_height = Math.round(getHeight()/13f);
	
	private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Yugioh.class.getName());
	private Player player1;
	private Player player2;
	
	CardInHandView card;
	@SuppressWarnings("unused")
	private Field field1;
	@SuppressWarnings("unused")
	private Field field2;
	ArrayList<Card> hand;
	private HandView handGraphic;	
	private ArrayList<ElementView> elements;
	private FieldSelectionElementView fieldSelectionElement;
	
	WindowView w;
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
		this.elements = new ArrayList<ElementView>();
		
		w = new WindowView(0, 0, getWidth()*2/9,getHeight());
	
		ButtonView draw = new ButtonView("DRAW",panel_button_x ,panel_button_y + 0*panel_distance,button_width,button_height);
		ButtonView M1 = new ButtonView("M1",panel_button_x ,panel_button_y + 1*panel_distance,button_width,button_height);
		ButtonView BP = new ButtonView("BP",panel_button_x ,panel_button_y + 2*panel_distance,button_width,button_height);
		ButtonView M2 = new ButtonView("M2",panel_button_x ,panel_button_y + 3*panel_distance,button_width,button_height);
		ButtonView EP = new ButtonView("EP",panel_button_x ,panel_button_y + 4*panel_distance,button_width,button_height);
		ButtonView CLR = new ButtonView("Clear",panel_button_x ,panel_button_y + 5*panel_distance,button_width,button_height);
		
		draw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player1.draw();
			}
		});
		CLR.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Card c : player1.getHand().getCardsList())
				{
					if(c instanceof MonsterCard)
						player1.getField().returnFromHandToDeck((MonsterCard) c);
					else
						player1.getField().returnFromHandToDeck((NonMonsterCard) c);
				}
				player1.getField().clearMonstersDestroying();
				player1.getField().clearNonMonstersDestroying();
				
				player1.shuffleDeck();
				player1.firstDraw();
			}
		});
		x_offset = w.getWidth()/2 - draw.getWidth()/2;
		
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
		
		CardDetailsView cardDetailsGraphic = new CardDetailsView(0,0,getWidth(),getHeight());
		this.handGraphic = new HandView(cardDetailsGraphic,player1, padding + Math.round(getWidth()/3.13f) + x_offset,Math.round(getHeight() -  card_view_height*3/8), card_view_width/2, card_view_height/2, card_dis_x);
		fieldSelectionElement = new FieldSelectionElementView(cardDetailsGraphic,player1,player2,getWidth(),getHeight());
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
		elements.add(CLR);
		elements.add(cardDetailsGraphic);
		elements.add(handGraphic);
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
			try {
				for(ElementView e : elements)
					e.clickAction(mouseEvent);
			}catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	@Override
	public void move(MouseEvent mouseEvent) {
		try {
			for(ElementView e : elements)
				e.hoverAction(mouseEvent);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private void drawCards(Screen screen,ArrayList<ElementView> elements2) {
		for(ElementView e : elements2)
			e.drawItself(screen);
	}

	
	@Override
	public void draw(Screen screen) {		
		
		//IMAGEM DE FUNDO
		screen.imageScaled("images/textures/background.jpg", 0, 0, getWidth(), getHeight(), 0,0, 0,1);
		screen.imageScaled("images/textures/field3.png", 0, 0, getWidth()*3/5, getHeight()*2/3, 0,getWidth()*23/90 , getHeight()/2 - getHeight()/3,0.7f);
		
		/**Magicas player 1 */


		w.drawItself(screen);
		fieldSelectionElement.drawItself(screen);
		drawCards(screen,elements);
	}	
	
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
