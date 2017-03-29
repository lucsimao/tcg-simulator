package blackjack.paraapagar;

import java.util.List;
import java.util.ArrayList;

public class BlackJackHand
{
    private PlayingDeck deck;
    private List<PlayingCard> hand;
    private int score;
    public BlackJackHand()
    {
        deck = new PlayingDeck();
        hand = new ArrayList<PlayingCard>();        
        score=0;
    }
    
    public BlackJackHand(PlayingDeck deck)
    {
        this.deck = deck;
        hand = new ArrayList<PlayingCard>();        
        score=0;
    }
    
    public void Buy()
    {
       if(hand.size() < 6 && !bust() && !bJack())
       {
           hand.add(deck.drawCard(0));
           calcScore();
           for(int i = 0; i<hand.size();i++)
           {
               if(hand.get(i).getName()=="A")
               {
                   if(score-hand.get(i).getValue()+11<=21)
                   {
                       hand.get(i).setValue(11);
                   }
                   else
                    hand.get(i).setValue(1);
                    calcScore();
               }
           }
       }
    }
    
    public void clearHand()
    {
        hand.clear();
        score = 0;
    }
    
    public List<PlayingCard> getHand()
    {
    	return hand;
    }
    
    private void calcScore()
    {
           score=0;
           for(int i=0;i<hand.size();i++)
           {
               if(hand.get(i).getName()!="A" && hand.get(i).getValue()>10)
               {
                   hand.get(i).setValue(10);
               }
               score += hand.get(i).getValue();
           }
    }
    
    private boolean bust()
    {
        if(score>21) return true; 
        else return false;
    }
    
    private boolean bJack()
    {
        if(score==21) return true;
        else return false;
    }
}
