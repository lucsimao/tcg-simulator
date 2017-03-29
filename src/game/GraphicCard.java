package game;

import javax.swing.ImageIcon;

import blackjack.paraapagar.PlayingCard;

public class GraphicCard extends Element {
	
	private final String path = "/sprites/costas.png";
	
	public GraphicCard(PlayingCard card)
	{
		super();
		setCard(card);
	}
	
	private void setCard(PlayingCard card)
	{
		setImage(new ImageIcon(getClass().getResource(path)).getImage());
	}
	
}
