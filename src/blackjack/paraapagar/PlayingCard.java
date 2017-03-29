package blackjack.paraapagar;

import cards.Card;



public class PlayingCard implements Card, Comparable<PlayingCard>{
    //Atributos
    private String name;
    private String suit;
    private int value;
    
    public PlayingCard(int value,   String nipe)
    {
        this.suit=nipe;
        this.value = value;
        this.name= setName(value);
    }

    private String setName(int v)
    {
        if(v>1&&v<11)
        {
            return String.valueOf(value);
        }
        else
        {
            switch(v)
            {
                case 11:return "J";
                case 12:return "Q"; 
                case 13:return "K"; 
                case 1:return "A";      
            }
        }
        return null;
    }
    
    public void setValue(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
    
    @Override
	public String getName()
    {
        return name;
    }

    public String getSuit() {
        return suit;
    }  
    
    @Override
	public int compareTo(PlayingCard c)
    {
         if(this.value < c.getValue())
            return -1;
         if(this.value > c.getValue())
            return 1;
         return 0;
    }
}
