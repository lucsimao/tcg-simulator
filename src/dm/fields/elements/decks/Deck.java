package dm.fields.elements.decks;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import dm.exceptions.CardNotFoundException;
import dm.exceptions.CardsOutException;

public abstract class Deck<GenericCard>{
	private Stack<GenericCard> cards;
	public Deck() {
		cards = new Stack<>();
	}
	
	// Embaralha o deck
	public void shuffleDeck() {
		Collections.shuffle(getCards());
	}
	
	//Retorna a lista de cartas do deck. 
	//Ele é protegido, pois somente filhos pode acessá-la
	protected Stack<GenericCard> getCards() {
		return cards;
	}
	
	// Retorna o tamanho do deck
	public int size() {
		return getCards().size();
	}

	// Mostra a carta no topo do baralho
	public GenericCard top() {
		return getCards().peek();
	}
	
	// Verifica se o deck está vazio
	public boolean isDeckout() {
		return getCards().isEmpty();
	}
	
	// Método para contar as cartas na pilha
	public int countCards(GenericCard card) {
		Iterator<GenericCard> i = getCards().iterator();
		int number = 0;
		while (i.hasNext()) {
			if (i.next().equals(card))
				number++;
		}
		return number;
	}
	
	// Compra uma carta do baralho
	public GenericCard drawCard() {
		if(getCards().size()==0)
			throw new CardsOutException("Empty deck.");
		else
		return getCards().pop();
	}
	
	public void moveCardToTop(GenericCard card)
	{
		Stack<GenericCard> cards = this.getCards();
		if(cards.contains(card))
		{
			cards.remove(card);
			cards.push(card);
		}
		else
			throw new CardNotFoundException("Card not found on this deck");
	}
	
	public abstract void putCard(GenericCard card);

	
}
