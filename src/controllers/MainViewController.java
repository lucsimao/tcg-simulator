//TODO
////package controllers;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//import model.cards.abstracts.Card;
//import model.cards.abstracts.MonsterCard;
//import model.cards.abstracts.NonMonsterCard;
//import model.fields.Field;
//import model.game.Player;
//import views.Yugioh;
//import views.components.ButtonView;
//import views.components.CardDetailsView;
//import views.components.CardInHandView;
//import views.components.ElementView;
//import views.components.FieldSelectionElementView;
//import views.components.HandView;
//import views.components.WindowView;
//
//public class MainViewController {
//
//	private final int card_dis_x  = Math.round(getWidth()/13.24f);
//	private final int padding = Math.round(getWidth()/144);
//	private final int card_view_width = Math.round(getWidth()/6.836f);
//	private final int card_view_height = Math.round(getHeight()/3.469f);
//	//Botões laterais
//	private final int panel_button_x = Math.round(getWidth()/1.16788f);
//	private final int panel_button_y = Math.round(getHeight()/3.4625f);
//	private final int panel_distance = Math.round(getHeight()/10.833333f);
//	private final int button_width = Math.round(getWidth()/9f);
//	private final int button_height = Math.round(getHeight()/13f);
//	
//	
//	private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Yugioh.class.getName());
//	private Player player1;
//	private Player player2;
//	
//	CardInHandView card;
//	@SuppressWarnings("unused")
//	private Field field1;
//	@SuppressWarnings("unused")
//	private Field field2;
//	ArrayList<Card> hand;
//	private HandView handGraphic;	
//	private ArrayList<ElementView> elements;
//	private FieldSelectionElementView fieldSelectionElement;
//	
//	
//	WindowView w;
//	private int x_offset;
//	private int height;
//	private int width;
//	
//	private Yugioh mainView;
//	
//	public MainViewController(int height,int width) {
//		
//		this.height = height;
//		this.width = width; 
//		this.mainView = new Yugioh()
////		super((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
//		x_offset = 0;
////		Card c = new MonsterEffectCard();
////		card = new CardGraphicHand(c, padding + 460,padding + 700,card_view_width/2, card_view_height/2);
//		this.player1 = player1;
//		this.player2 = player2;
//		
//
//		
//		field1 = player1.getField();
//		field2 = player2.getField();
//		player1.shuffleDeck();
//		player1.firstDraw();
//		this.elements = new ArrayList<ElementView>();
//		
//		w = new WindowView(0, 0, getWidth()*2/9,getHeight());
//	
//		ButtonView draw = new ButtonView("DRAW",panel_button_x ,panel_button_y + 0*panel_distance,button_width,button_height);
//		ButtonView M1 = new ButtonView("M1",panel_button_x ,panel_button_y + 1*panel_distance,button_width,button_height);
//		ButtonView BP = new ButtonView("BP",panel_button_x ,panel_button_y + 2*panel_distance,button_width,button_height);
//		ButtonView M2 = new ButtonView("M2",panel_button_x ,panel_button_y + 3*panel_distance,button_width,button_height);
//		ButtonView EP = new ButtonView("EP",panel_button_x ,panel_button_y + 4*panel_distance,button_width,button_height);
//		ButtonView CLR = new ButtonView("Clear",panel_button_x ,panel_button_y + 5*panel_distance,button_width,button_height);
//		
//		draw.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				player1.draw();
//			}
//		});
//		CLR.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				for(Card c : player1.getHand().getCardsList())
//				{
//					if(c instanceof MonsterCard)
//						player1.getField().returnFromHandToDeck((MonsterCard) c);
//					else
//						player1.getField().returnFromHandToDeck((NonMonsterCard) c);
//				}
//				player1.getField().clearMonstersDestroying();
//				player1.getField().clearNonMonstersDestroying();
//				
//				player1.shuffleDeck();
//				player1.firstDraw();
//			}
//		});
//		x_offset = w.getWidth()/2 - draw.getWidth()/2;
//		
////		int special_zone_x = w.getWidth()*608/900 + x_offset;
//////		int special_zone_x = 608 + x_offset;
////		int special_zone_height = w.getHeight()*59/600;
////		int special_zone_width = w.getWidth()*39/900;
////		int normal_zone_height = w.getHeight()*61/600;
////		int normal_zone_width = w.getWidth()*54/900;
//		
////		SelectionGraphicElement extra1 = new SelectionGraphicElement(299 + 61 + x_offset,295, normal_zone_width,normal_zone_height);
////		SelectionGraphicElement extra2 = new SelectionGraphicElement(298 + 61 + 121 + x_offset,295, 54, 61);
////		SelectionGraphicElement deck1 = new SelectionGraphicElement(special_zone_x,474, special_zone_width, special_zone_height);
////		SelectionGraphicElement grave1 = new SelectionGraphicElement(special_zone_x,404, special_zone_width, special_zone_height);
////		SelectionGraphicElement banned1 = new SelectionGraphicElement(special_zone_x,332, special_zone_width, special_zone_height);
////		SelectionGraphicElement field2 = new SelectionGraphicElement(special_zone_x,188, special_zone_width, special_zone_height);
////		SelectionGraphicElement extra_deck2 = new SelectionGraphicElement(special_zone_x,118, special_zone_width, special_zone_height);
////		SelectionGraphicElement deck2 = new SelectionGraphicElement(special_zone_x- 360,118, special_zone_width, special_zone_height);
////		SelectionGraphicElement grave2 = new SelectionGraphicElement(special_zone_x- 360,188, special_zone_width, special_zone_height);
////		SelectionGraphicElement	banned2 = new SelectionGraphicElement(special_zone_x- 360,260, special_zone_width, special_zone_height);
////		SelectionGraphicElement field1 = new SelectionGraphicElement(special_zone_x- 360,404, special_zone_width, special_zone_height);
////		SelectionGraphicElement extra_deck1 = new SelectionGraphicElement(special_zone_x- 360,474, special_zone_width, special_zone_height);
//		
//		CardDetailsView cardDetailsGraphic = new CardDetailsView(0,0,getWidth(),getHeight());
//		this.handGraphic = new HandView(cardDetailsGraphic,player1, padding + Math.round(getWidth()/3.13f) + x_offset,Math.round(getHeight() -  card_view_height*3/8), card_view_width/2, card_view_height/2, card_dis_x);
//		fieldSelectionElement = new FieldSelectionElementView(cardDetailsGraphic,player1,player2,getWidth(),getHeight());
//		fieldSelectionElement.addSelectionElements(x_offset);
////		fieldSelectionElement.addSelectionGraphicElement(extra1);
////		fieldSelectionElement.addSelectionGraphicElement(extra2);
////		fieldSelectionElement.addSelectionGraphicElement(deck1);
////		fieldSelectionElement.addSelectionGraphicElement(grave1);
////		fieldSelectionElement.addSelectionGraphicElement(banned1);
////		fieldSelectionElement.addSelectionGraphicElement(field2);
////		fieldSelectionElement.addSelectionGraphicElement(extra_deck2);
////		fieldSelectionElement.addSelectionGraphicElement(deck2);
////		fieldSelectionElement.addSelectionGraphicElement(grave2);
////		fieldSelectionElement.addSelectionGraphicElement(banned2);
////		fieldSelectionElement.addSelectionGraphicElement(field1);
////		fieldSelectionElement.addSelectionGraphicElement(extra_deck1);
//		
//		
//		elements.add(fieldSelectionElement);
//		elements.add(draw);
//		elements.add(M1);
//		elements.add(BP);
//		elements.add(M2);
//		elements.add(EP);
//		elements.add(CLR);
//		elements.add(cardDetailsGraphic);
//		elements.add(handGraphic);
//		
//	}
//	
//}
