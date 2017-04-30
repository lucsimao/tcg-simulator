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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JInternalFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

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
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JImageDesktopPane desktopPane = new JImageDesktopPane();
		contentPane.add(desktopPane, BorderLayout.CENTER);
		desktopPane.setLayout(new BorderLayout(0, 0));
		desktopPane.setBorder(new EmptyBorder((getHeight()-360)/2, (getWidth()-240)/2, (getHeight()-360)/2, (getWidth()-240)/2));
	
		
		JPanel panel = new JPanel();
		desktopPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		panel.setBorder(new EmptyBorder(10, 20,10,20));
		
		JLabel lblNewLabel = new JLabel("Yu Gi OH");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);
		
		JButton btnStart = new JButton("Start");
		panel.add(btnStart);
		
		JButton btnEditDeck = new JButton("EditDeck");
		panel.add(btnEditDeck);
		
		JButton btnCardBuilder = new JButton("CardBuilder");
		panel.add(btnCardBuilder);
		
		JButton btnExit = new JButton("Exit");
		panel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);   
			}
		});
	}

}
