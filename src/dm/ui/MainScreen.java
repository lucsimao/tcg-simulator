/** 
* @author Simão 
* @version 0.1 - 28 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]){
		MainScreen screen = new MainScreen();
	}
	public MainScreen()
	{
		//Dimension d = new Dimension(Default.SCREEN_WIDTH,Default.SCREEN_HEIGHT);
		setExtendedState(MAXIMIZED_BOTH);
//		setUndecorated(true)	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		Menu menu = new Menu();
		getContentPane().add("Center",menu);
		menu.repaint();
//		getContentPane().add("North",new BackgroundLabel());
//		getContentPane().add("Center",new JButton("teste"));
		setVisible(true);
//		setSize(d);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	
}
