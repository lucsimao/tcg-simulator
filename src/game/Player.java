package game;

import java.awt.Graphics2D;
import javax.swing.JPanel;

import blackjack.paraapagar.BlackJackHand;
import blackjack.paraapagar.PlayingDeck;

public class Player
{
	private BlackJackHand hand;
	private String name;
	private GraphicCard card; 
	
	public Player(PlayingDeck deck,String name)
	{
		card = new GraphicCard(hand.getHand().get(0));
		hand = new BlackJackHand(deck);
		this.name = name;
	}
	
	public void drawValues()
	{
		
	}
	
	public void drawCards(Graphics2D g2, JPanel screen)
	{
		g2.drawImage(card.getImage(),card.getX(),card.getY(),screen);
	}
	public String getName()
	{
		return name;
	}
}
