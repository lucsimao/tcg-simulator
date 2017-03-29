package cards;

public interface Deck<GenericCard>
{
    public void shuffleDeck();
    public GenericCard drawCard();
    public void putCard(GenericCard card);
}