package dm.fields.elements.decks;

/** Classe abstrata deck, para ter funções especiais de todos os decks.
 * Além disso, ele herda de elemento de campo, uma vez que um campo possui um deck.
 * @author Simão
 * */

public abstract class Deck<GenericCard> extends FieldElement<GenericCard> {

	public Deck(int number, GenericCard card) {
		super(number, card);
	}

	public Deck() {
		super();
	}

	// Verifica se o deck está vazio
	public boolean isDeckout() {
		return isEmpty();
	}

	// Compra uma carta do baralho
	public GenericCard drawCard() {
		return removeFromTop();
	}

}
