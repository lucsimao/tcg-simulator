package dm.game;

import java.awt.Image;

import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;

public class Player {

	private static final int INIT_LIFE_POINTS = 8000;
	private static final int NUMBER_INITIAL_HAND = 5;
	
	private Field field;
	private int lp;
	private Image avatar;
	private String name;
	
	public Player(String name,Image avatar,NormalDeck deck, ExtraDeck extraDeck){
		lp = INIT_LIFE_POINTS;
		this.name = name;
		this.avatar = avatar;
		this.field = new Field(deck, extraDeck);
	}
	
	public void firstDraw() {
		for(int i=0;i<NUMBER_INITIAL_HAND;i++)
		{
			field.draw();
		}
	}

	public void draw() {
		field.draw();
	}
		
	/*Métodos de contagem*/
	public int countHandCards() {return field.countHandCards();}
	public int countDeckCards() {return field.countDeckCards();}
	/*Métodos de getters and setters*/
	public Image getAvatar() {return avatar;}
	public void setAvatar(Image avatar) {this.avatar = avatar;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public NormalDeck getDeck() {return field.getDeck();}
	public ExtraDeck getExtraDeck() {return field.getExtraDeck();}
	public int getLP(){return this.lp;}
	public void increaseLp(int increment){this.lp += increment;}
	public void decreaseLp(int decrement){this.lp -= decrement;}
	public void setLp(int lp){this.lp=lp;}
	
}
