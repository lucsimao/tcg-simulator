package dm.fields.elements.decks;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import dm.exceptions.CardNotFoundException;
import dm.exceptions.CardsOutException;

/**
 * SuperClasse abstrata Elemento de Campo, ela é a mãe de todos os elementos.
 * Possui os métodos padrão que todos os elementos devem ter.
 * @author Simão*/
public abstract class FieldElement<GenericCard> {

	// Atributos
	private Stack<GenericCard> cards;// Uma pilha que representas as cartas
										// contidas nesse elemento

	// Construtor básico de inicialização
	public FieldElement() {
		cards = new Stack<>();
	}

	public FieldElement(int number, GenericCard card) {
		cards = new Stack<>();
		for (int i = 0; i < number; i++) {
			putCard(card);
		}
	}

	// Retorna a lista de cartas da pilha.
	// Ele é protegido, pois somente filhos pode acessá-la.
	protected Stack<GenericCard> getCards() {
		return cards;
	}

	// Mostra a carta no topo da pilha
	public GenericCard top() {
		return getCards().peek();
	}

	// Embaralha a pilha
	public void shuffle() {
		Collections.shuffle(getCards());
	}

	// Retorna o tamanho da pilha
	public int size() {
		return getCards().size();
	}

	// Verifica se a pilha está vazia
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

	// Método que utiliza remove e push para levar uma carta qualquer para o
	// topo da pilha
	public void moveCardToTop(GenericCard card) {
		Stack<GenericCard> cards = this.getCards();
		if (cards.contains(card)) {
			cards.remove(card);
			cards.push(card);
		} else
			throw new CardNotFoundException("Card not found on this deck");
	}

	// Método que remove uma carta qualquer da pilha
	public GenericCard remove(GenericCard card) {
		if (getCards().contains(card)) {
			getCards().remove(card);
			return card;
		}
		throw new CardNotFoundException("This card does not exist in this deck");
	}

	// Método que remove uma carta do início da pilha (pop)
	public GenericCard removeFromTop() {
		if (getCards().size() == 0)
			throw new CardsOutException("Empty deck.");
		else
			return getCards().pop();
	}

	// Método abstrato que representa colocar uma carta na pilha, cada classe
	// deve
	// implementá-la de forma diferente.
	public abstract void putCard(GenericCard card);

}
