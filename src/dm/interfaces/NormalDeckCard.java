package dm.interfaces;

/** @author Simão */
public interface NormalDeckCard extends Cloneable{
	int getCopiesNumber();
	public Object clone() throws CloneNotSupportedException ;
}
