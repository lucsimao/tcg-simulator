/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JInternalFrame;

public class Frame extends JFrame {

	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JImageDesktopPane desktopPane = new JImageDesktopPane();
		contentPane.add(desktopPane, BorderLayout.CENTER);
		desktopPane.setLayout(new BorderLayout(0, 0));
		desktopPane.setBorder(new EmptyBorder(getHeight()/10,getWidth()/4,getHeight()/10,getWidth()/4));
		
		JPanel panel = new JPanel();
		desktopPane.add(panel, BorderLayout.CENTER);
	}

}
