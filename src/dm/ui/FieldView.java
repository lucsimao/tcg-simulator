/** 
* @author Simão 
* @version 0.1 - 1 de mai de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class FieldView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7091431371699384724L;

	private final int height = 240;
	private final int width =640 ;
	
	/**
	 * Create the panel.
	 */
	public FieldView() {
		super();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));
	}

}
