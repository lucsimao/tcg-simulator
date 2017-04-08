package dm.fields.elements.decks;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import dm.cards.abstracts.Card;
import dm.exceptions.CardNotFoundException;

public abstract class FieldElement<GenericCard>{
	private Stack<GenericCard> cards;
	public FieldElement(){
		cards = new Stack<>();
	}
	
	//Retorna a lista de cartas do deck. 
	//Ele é protegido, pois somente filhos pode acessá-la
	protected Stack<GenericCard> getCards() {
		return cards;
	}
	
	// Embaralha o deck
	public void shuffle() {
		Collections.shuffle(getCards());
	}
	
	// Retorna o tamanho do deck
	public int size() {
		return getCards().size();
	}

	// Verifica se o deck está vazio
	public boolean isEmpty() {
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
	
	public GenericCard remove(GenericCard card)
	{
		if(getCards().contains(card))
		{
			getCards().remove(card);
			return card;
		}
		throw new CardNotFoundException("This card does not exist in this deck");
	}
	
	public abstract void putCard(GenericCard card);
	
}
