package blackjack.paraapagar;

import java.util.List;

import cards.Deck;

import java.util.Collections;
import java.util.ArrayList;

public class PlayingDeck implements Deck
{
    private List<PlayingCard> cards;
    private String hearts,spades,diamonds, clubs;
    public PlayingDeck()
    {
        cards = new ArrayList<PlayingCard>();
        hearts = "Copas";
        diamonds = "Ouro";
        clubs = "Paus";
        spades = "Espadas";
        fillDeck();
        //shuffleDeck();
    }
    
    public PlayingDeck(String hearts, String spades, String diamonds, String clubs)
    {
        cards = new ArrayList<PlayingCard>();
        fillDeck();
        shuffleDeck();
    }
    
    public void fillDeck()
    {
        cards.clear();
        fillSuit(hearts);
        fillSuit(spades);
        fillSuit(diamonds);
        fillSuit(clubs);
    }

    @Override
	public void shuffleDeck()
    {
        Collections.shuffle(cards);
    }
    
    public PlayingCard drawCard(int position)
    {
        PlayingCard c;
        c=cards.get(0);
        cards.remove(0);
        return c;
    }
          
    private void fillSuit(String suit)
    {
        for(int i=1;i<14;i++)
        {
            cards.add(new PlayingCard(i,suit));
        }
    }
    
    public List<PlayingCard> getCards()
    {
        return cards;
    }
}
