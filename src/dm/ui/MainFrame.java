/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dm.ui.subviews.JImageDesktopPane;
import singleinstance.SingleInstance;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -508584232966549415L;

	private static final int width = 640;

	private static final int height = 480;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SingleInstance.checkIfInstanceExists();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
					frame.setBounds(100, 100, width, height);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
//		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		setUndecorated(true);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, width,height);
		
		contentPane = defaultContentPane();
		setContentPane(contentPane);

		JImageDesktopPane desktopPane = new JImageDesktopPane();
		contentPane.add(desktopPane, BorderLayout.CENTER);
		desktopPane.setLayout(new BorderLayout(0, 0));
		desktopPane.setBorder(new EmptyBorder((getHeight() - 360) / 2, (getWidth() - 240) / 2, (getHeight() - 360) / 2,
				(getWidth() - 240) / 2));

		JPanel panel = new JPanel();
		desktopPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		panel.setBorder(new EmptyBorder(10, 20, 10, 20));

		JLabel lblNewLabel = new JLabel("Yu Gi OH");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);

		JButton btnStart = new JButton("Start");
		panel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setContentPane(new JPanel());
				JButton b = new JButton("BUTAO");
				getContentPane().add(b);
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						setContentPane(contentPane);
						validate();

					}
				});

				validate();
			}

		});

		JButton btnEditDeck = new JButton("EditDeck");
		panel.add(btnEditDeck);

		JButton btnViewCards = new JButton("ViewCards");
		panel.add(btnViewCards);
		
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
		
		btnCardBuilder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel localContentPane  = defaultContentPane();
				setContentPane(localContentPane);
				CardBuilder builder = new CardBuilder();
				getContentPane().add(builder);
				builder.addBackActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setContentPane(contentPane);
						validate();
						
					}
				});
				validate();
			}
		});
		btnViewCards.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel localContentPane  = defaultContentPane();
				setContentPane(localContentPane);

				ListCards builder = new ListCards();
				getContentPane().add(builder);
				builder.addBackActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setContentPane(contentPane);
						validate();
						
					}
				});
				validate();
			}
		});
		
		
	}

	public JPanel defaultContentPane(){
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		return contentPane;
	}
	
}
