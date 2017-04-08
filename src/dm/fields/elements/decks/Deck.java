package dm.fields.elements.decks;

import dm.exceptions.CardsOutException;

public abstract class Deck<GenericCard> extends FieldElement<GenericCard>{


	// Mostra a carta no topo do baralho
	public GenericCard top() {
		return getCards().peek();
	}
	
	// Verifica se o deck está vazio
	public boolean isDeckout() {
		return isEmpty();
	}
	
	// Compra uma carta do baralho
	public GenericCard drawCard() {
		if(getCards().size()==0)
			throw new CardsOutException("Empty deck.");
		else
		return getCards().pop();
	}
		
}
