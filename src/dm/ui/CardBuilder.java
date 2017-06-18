/** 
* @author Simão 
* @version 0.1 - 18 de jun de 2017
* 
*/
package dm.ui;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dm.constants.FilesConstants;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class CardBuilder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2593144591366996030L;
	
	private JTextField cbCardName;
	private JLabel lblCardType;
	private JTextArea tACardDescription;

	private final static int height = 400;
	private final static int width = 640;
	private JComboBox<String> cbCardType;
	private JLabel lblMonsterAtribute;
	private JComboBox<String> cbMonsterAtribute;
	private JComboBox<String> cbMonsterType;
	private JLabel lblMonsterType;
	private JLabel lblImage;
	private JPanel panel;
	private JTextField tfImage;
	private JButton btnChoose;

	private JLabel lblImageView;
	private JPanel panel_1;

	private JPanel panel_2;

	private JPanel panel_3;
	private JLabel lblMonsterAttack;
	private JSpinner sp_Attack;
	private JLabel lblMonsterDefense;
	private JSpinner sp_Defense;
	private JButton button;

	private JComboBox<String> cbSpellType;

	private Component lblSpellType;

	private JLabel lblTrapType;

	private JComboBox<String> cbTrapType;
	
	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
		
		CardBuilder fv = new CardBuilder();
		
		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}
	
	/**
	 * Create the panel.
	 */
	public CardBuilder() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		//		setLayout(new GridLayout(15, 1, 0, 0));
			
		panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout(10,10));
		add(panel_2,BorderLayout.WEST);
		
		lblImageView = new JLabel();
		try {
			Image defaultImage;
			defaultImage = ImageIO.read(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD))
					.getScaledInstance(FilesConstants.CARD_WIDTH, FilesConstants.CARD_HEIGHT, Image.SCALE_DEFAULT);
			lblImageView.setIcon(new ImageIcon(defaultImage));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		panel_2.add(lblImageView,BorderLayout.NORTH);
		
		panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(10,10));
		panel_2.add(panel_3,BorderLayout.CENTER);
		JLabel label = new JLabel("Card Description");
		panel_3.add(label,BorderLayout.NORTH);
		
		tACardDescription = new JTextArea();
		tACardDescription.setLineWrap(true);
		JScrollPane sp = new JScrollPane(tACardDescription); 
		panel_3.add(sp,BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(7, 1, 25, 25));
		add(panel_1,BorderLayout.CENTER);
		
		lblImage = new JLabel("Image");
		panel_1.add(lblImage);
		
		panel = new JPanel();
		panel_1.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		tfImage = new JTextField();
		panel.add(tfImage);
		tfImage.setColumns(10);
		
		btnChoose = new JButton("Choose");
		panel.add(btnChoose);
		
		
		JLabel lblCardName = new JLabel("Card Name");
		panel_1.add(lblCardName);
		
		cbCardName = new JTextField();
		panel_1.add(cbCardName);
		cbCardName.setColumns(10);
		


		
		lblCardType = new JLabel("Card Type");
		panel_1.add(lblCardType);
		
		cbCardType = new JComboBox<String>();
		panel_1.add(cbCardType);
		cbCardType.setModel(new DefaultComboBoxModel<String>(new String[] {"MONSTER","SPELL","TRAP"}));
				
		lblMonsterAtribute = new JLabel("Monster Atribute");
		panel_1.add(lblMonsterAtribute);
		
		cbMonsterAtribute = new JComboBox<String>();
		cbMonsterAtribute.setModel(new DefaultComboBoxModel<String>
		(new String[] {"WATER","FIRE","LIGHT","DARK","WIND","EARTH"}));
		panel_1.add(cbMonsterAtribute);
		
		lblSpellType = new JLabel("Spell Type");
//		panel_1.add(lblSpellType);
		
		cbSpellType = new JComboBox<String>();
		cbSpellType.setModel(new DefaultComboBoxModel<String>(new String[] {"NORMAL","QUICK","CONTINOUS","EQUIP","FIELD"}));
//		panel_1.add(cbSpellType);
		
		lblTrapType = new JLabel("Trap Type");
		
//		panel_1.add(lblTrapType);
		
		cbTrapType = new JComboBox<String>();
		cbTrapType.setModel(new DefaultComboBoxModel<String>(new String[] {"NORMAL","COUNTER","CONTINOUS"}));
//		panel_1.add(cbTrapType);
		
		
		lblMonsterType = new JLabel("Monster Type");
		panel_1.add(lblMonsterType);
		
		cbMonsterType = new JComboBox<String>();
		panel_1.add(cbMonsterType);
		cbMonsterType.setModel(new DefaultComboBoxModel<String>
		(new String[] {"SPELLCASTER","WARRIOR","FAIRY","AQUA","SEASERPENT","FIEND","DRAGON","BEAST","WINGEDBEAST","BEASTWARRIOR","PYRO"}));
		
		lblMonsterAttack = new JLabel("Monster Attack");
		panel_1.add(lblMonsterAttack);
		
		sp_Attack = new JSpinner();
		sp_Attack.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panel_1.add(sp_Attack);
		
		lblMonsterDefense = new JLabel("Monster Defense");
		panel_1.add(lblMonsterDefense);
		
		sp_Defense = new JSpinner();
		sp_Defense.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panel_1.add(sp_Defense);
		
		button = new JButton("Create");
		add(button,BorderLayout.SOUTH);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbCardType.getSelectedItem().equals("MONSTER")){
					System.out.println("Você criou um monstro!" + cbCardType.getSelectedIndex());
				}
				
			}
		});
		
		cbCardType.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visM = false;
				boolean visS = false;
				boolean visT = false;
				System.out.println(e.getItem().equals("MONSTER"));
				if(e.getItem().toString().equals("MONSTER")){
					visM = true;
				}
				if(e.getItem().toString().equals("SPELL")){
					visS = true;
				}
				if(e.getItem().toString().equals("TRAP")){
					visT = true;
				}
				setMonsterItemsVisible(visM);
				setSpellItemsVisible(visS);
				setTrapItemsVisible(visT);
			}
		});
		btnChoose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "JPG & PNG Images", "jpg", "png");
				    chooser.setFileFilter(filter);
				    int returnVal = chooser.showOpenDialog(getParent());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	tfImage.setText(chooser.getSelectedFile().getName());
				    	try {
							Image image = ImageIO.read(chooser.getSelectedFile()).getScaledInstance(FilesConstants.CARD_WIDTH,FilesConstants.CARD_HEIGHT,Image.SCALE_DEFAULT);
							lblImageView.setIcon(new ImageIcon(image));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    	
				       System.out.println("You chose to open this file: " +
				            chooser.getSelectedFile().getName());
				       repaint();
				    }
			}
		});
		setMonsterItemsVisible(true);	
		setSpellItemsVisible(false);	
		setTrapItemsVisible(false);	
	}

	public void setMonsterItemsVisible(boolean visible){
		if(visible){		
		panel_1.add(lblMonsterAtribute);
		panel_1.add(cbMonsterAtribute);
		panel_1.add(lblMonsterType);
		panel_1.add(cbMonsterType);
		panel_1.add(lblMonsterAttack);
		panel_1.add(sp_Attack);
		panel_1.add(lblMonsterDefense);
		panel_1.add(sp_Defense);
		}else
		{
			panel_1.remove(lblMonsterAttack);
			panel_1.remove(lblMonsterDefense);
			panel_1.remove(sp_Attack);
			panel_1.remove(sp_Defense);
			panel_1.remove(lblMonsterAtribute);
			panel_1.remove(cbMonsterAtribute);
			panel_1.remove(lblMonsterType);
			panel_1.remove(cbMonsterType);
		}
		repaint();	
//		lblMonsterAttack.setVisible(visible);
//		lblMonsterDefense.setVisible(visible);
//		sp_Attack.setVisible(visible);
//		sp_Defense.setVisible(visible);
//		lblMonsterAtribute.setVisible(visible);
//		cbMonsterAtribute.setVisible(visible);
//		lblMonsterType.setVisible(visible);
//		cbMonsterType.setVisible(visible);
	}

	public void setSpellItemsVisible(boolean visible){
		if(visible == true){
			panel_1.add(lblSpellType);
			panel_1.add(cbSpellType);}
		else{
			panel_1.remove(lblSpellType);
			panel_1.remove(cbSpellType);
			}
		revalidate();
	}
	
	public void setTrapItemsVisible(boolean visible){
		if(visible == true){
			panel_1.add(lblTrapType);
			panel_1.add(cbTrapType);
			}
		else{
			panel_1.remove(lblTrapType);
			panel_1.remove(cbTrapType);
			}
		repaint();
	}
	
}
