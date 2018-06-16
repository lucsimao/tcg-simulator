package model.cards;

/** @author Simão */
public interface NormalDeckCard extends Cloneable{
	int getCopiesNumber();
	public Object clone() throws CloneNotSupportedException ;
}
