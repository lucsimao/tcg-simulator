package game;

import javax.swing.JFrame;

public class Main extends JFrame implements Default {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Main()
	{
		//Dimension d = new Dimension(Default.SCREEN_WIDTH,Default.SCREEN_HEIGHT);
		setExtendedState(MAXIMIZED_BOTH);
//		setUndecorated(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		getContentPane().add("North",new JButton("Cu"));
//		getContentPane().add("East",new JButton("Cu") );
//		getContentPane().add("West",new JButton("Cu") );
//		getContentPane().add("South",new JButton("Cu") );
		getContentPane().add("Center",new Screen());
		setVisible(true);
//		setSize(d);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public static void main(String[] args) 
	{
		new Main();
	}
	
}
