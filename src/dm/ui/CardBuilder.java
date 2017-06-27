/** 
* @author Simão 
* @version 0.1 - 18 de jun de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dm.cards.Effect;
import dm.cards.MonsterNormalCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.cards.abstracts.Card;
import dm.constants.FilesConstants;
import dm.files.CardDAO;

public class CardBuilder extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2593144591366996030L;

	private JTextField nameTF;
	private JLabel lblCardType;
	private JTextArea descriptionTA;

	private final static int height = 480;
	private final static int width = 640;
	private JComboBox<String> cardTypeCB;
	private JLabel lblMonsterAtribute;
	private JComboBox<String> monsterAtributeCB;
	private JComboBox<String> monsterTypeCB;
	private JLabel lblMonsterType;
	private JLabel lblImage;
	private JPanel panel;
	private JTextField pictureTF;
	private JButton choseBTN;

	private JLabel lblImageView;
	private JPanel panel_1;

	private JPanel panel_2;

	private JPanel panel_3;
	private JLabel lblMonsterAttack;
	private JSpinner attackSPN;
	private JLabel lblMonsterDefense;
	private JSpinner defenseSPN;
	private JButton createButton;

	private JComboBox<String> cbSpellType;

	private Component lblSpellType;

	private JLabel lblTrapType;

	private JComboBox<String> cbTrapType;
	private JFileChooser chooser;
	private JButton backButton;
	
	public static void main(String args[]) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);

		CardBuilder cardBuilder = new CardBuilder();

		f.getContentPane().add(cardBuilder);
		cardBuilder.setFocusable(true);
		cardBuilder.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
	}

	/**
	 * Create the panel.
	 */
	public CardBuilder() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// setLayout(new GridLayout(15, 1, 0, 0));

		panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout(10, 10));
		add(panel_2, BorderLayout.WEST);

		lblImageView = new JLabel();
		try {
			Image defaultImage;
			defaultImage = ImageIO.read(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD))
					.getScaledInstance(FilesConstants.CARD_WIDTH, FilesConstants.CARD_HEIGHT, Image.SCALE_DEFAULT);
			lblImageView.setIcon(new ImageIcon(defaultImage));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		panel_2.add(lblImageView, BorderLayout.NORTH);

		panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(10, 10));
		panel_2.add(panel_3, BorderLayout.CENTER);
		JLabel label = new JLabel("Card Description");
		panel_3.add(label, BorderLayout.NORTH);

		descriptionTA = new JTextArea();
		descriptionTA.setLineWrap(true);
		JScrollPane sp = new JScrollPane(descriptionTA);
		panel_3.add(sp, BorderLayout.CENTER);

		panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(7, 1, 25, 25));
		add(panel_1, BorderLayout.CENTER);

		lblImage = new JLabel("Image");
		panel_1.add(lblImage);

		panel = new JPanel();
		panel_1.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		pictureTF = new JTextField();
		panel.add(pictureTF);
		pictureTF.setColumns(10);

		choseBTN = new JButton("Choose");
		panel.add(choseBTN);

		JLabel lblCardName = new JLabel("Card Name");
		panel_1.add(lblCardName);

		nameTF = new JTextField();
		panel_1.add(nameTF);
		nameTF.setColumns(10);

		lblCardType = new JLabel("Card Type");
		panel_1.add(lblCardType);

		cardTypeCB = new JComboBox<String>();
		panel_1.add(cardTypeCB);
		cardTypeCB.setModel(new DefaultComboBoxModel<String>(new String[] { "MONSTER", "SPELL", "TRAP" }));

		lblMonsterAtribute = new JLabel("Monster Atribute");
		panel_1.add(lblMonsterAtribute);

		monsterAtributeCB = new JComboBox<String>();
		monsterAtributeCB.setModel(
				new DefaultComboBoxModel<String>(new String[] { "WATER", "FIRE", "LIGHT", "DARK", "WIND", "EARTH" }));
		panel_1.add(monsterAtributeCB);

		lblSpellType = new JLabel("Spell Type");
		// panel_1.add(lblSpellType);

		cbSpellType = new JComboBox<String>();
		cbSpellType.setModel(
				new DefaultComboBoxModel<String>(new String[] { "NORMAL", "QUICK", "CONTINOUS", "EQUIP", "FIELD" }));
		// panel_1.add(cbSpellType);

		lblTrapType = new JLabel("Trap Type");

		// panel_1.add(lblTrapType);

		cbTrapType = new JComboBox<String>();
		cbTrapType.setModel(new DefaultComboBoxModel<String>(new String[] { "NORMAL", "COUNTER", "CONTINOUS" }));
		// panel_1.add(cbTrapType);

		lblMonsterType = new JLabel("Monster Type");
		panel_1.add(lblMonsterType);

		monsterTypeCB = new JComboBox<String>();
		panel_1.add(monsterTypeCB);
		monsterTypeCB.setModel(new DefaultComboBoxModel<String>(new String[] { "SPELLCASTER", "WARRIOR", "FAIRY",
				"AQUA", "SEASERPENT", "FIEND", "DRAGON", "BEAST", "WINGEDBEAST", "BEASTWARRIOR", "PYRO" }));

		lblMonsterAttack = new JLabel("Monster Attack");
		panel_1.add(lblMonsterAttack);

		attackSPN = new JSpinner();
		attackSPN.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panel_1.add(attackSPN);

		lblMonsterDefense = new JLabel("Monster Defense");
		panel_1.add(lblMonsterDefense);

		defenseSPN = new JSpinner();
		defenseSPN.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panel_1.add(defenseSPN);

		
		JPanel panelBotao = new JPanel(new GridLayout(1, 2,5,5));
		createButton = new JButton("Create");
		add(panelBotao, BorderLayout.SOUTH);
		
		panelBotao.add(createButton);
		
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createCard();
			}
		});

		backButton = new JButton("Voltar");
		panelBotao.add(backButton);
		
		
		cardTypeCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean visM = false;
				boolean visS = false;
				boolean visT = false;
				System.out.println(e.getItem().equals("MONSTER"));
				if (e.getItem().toString().equals("MONSTER")) {
					visM = true;
				}
				if (e.getItem().toString().equals("SPELL")) {
					visS = true;
				}
				if (e.getItem().toString().equals("TRAP")) {
					visT = true;
				}
				setMonsterItemsVisible(visM);
				setSpellItemsVisible(visS);
				setTrapItemsVisible(visT);
			}
		});
		choseBTN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					pictureTF.setText(chooser.getSelectedFile().getName());
					try {
						Image cardImage = ImageIO.read(chooser.getSelectedFile()).getScaledInstance(
								FilesConstants.CARD_WIDTH, FilesConstants.CARD_HEIGHT, Image.SCALE_DEFAULT);
						lblImageView.setIcon(new ImageIcon(cardImage));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
					repaint();
				}
			}
		});
		setMonsterItemsVisible(true);
		setSpellItemsVisible(false);
		setTrapItemsVisible(false);
	}

	public void createCard() {
		try {
			Card card = null;
			if (cardTypeCB.getSelectedItem().toString().equals("MONSTER")) {
				System.out.println("Você criou um monstro!" + cardTypeCB.getSelectedIndex());
//				{
					card = new MonsterNormalCard(nameTF.getText(), descriptionTA.getText(),
							pictureTF.getText(),monsterTypeCB.getSelectedIndex(),monsterAtributeCB.getSelectedIndex(),
							(int)attackSPN.getValue(),(int)defenseSPN.getValue(),0,3);					
//				}
			} else if (cardTypeCB.getSelectedItem().toString().equals("SPELL")) {
				System.out.println("Você criou uma magia!" + cardTypeCB.getSelectedIndex());

				card = new SpellCard(nameTF.getText(), descriptionTA.getText(), pictureTF.getText(),
						new Effect(),cbSpellType.getSelectedIndex(), 3);


			} else if (cardTypeCB.getSelectedItem().toString().equals("TRAP")) {
				System.out.println("Você criou uma armadilha!" + cardTypeCB.getSelectedIndex());
				card = new TrapCard(nameTF.getText(), descriptionTA.getText(), pictureTF.getText(),
						new Effect(), cbTrapType.getSelectedIndex(), 3);
			}
			//Copia a carta para a pasta de imagens
			
			CardDAO cardDAO = new CardDAO();
			cardDAO.saveCard(card);
			Files.copy(chooser.getSelectedFile().toPath(),new File(FilesConstants.CARDS_IMG_DIR + cardDAO.getId()+".jpg").toPath(),StandardCopyOption.REPLACE_EXISTING);
	
			JOptionPane.showMessageDialog(null,"Your have saved sucefully your card","SUUUCESSO",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getSimpleName(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setMonsterItemsVisible(boolean visible) {
		if (visible) {
			panel_1.add(lblMonsterAtribute);
			panel_1.add(monsterAtributeCB);
			panel_1.add(lblMonsterType);
			panel_1.add(monsterTypeCB);
			panel_1.add(lblMonsterAttack);
			panel_1.add(attackSPN);
			panel_1.add(lblMonsterDefense);
			panel_1.add(defenseSPN);
		} else {
			panel_1.remove(lblMonsterAttack);
			panel_1.remove(lblMonsterDefense);
			panel_1.remove(attackSPN);
			panel_1.remove(defenseSPN);
			panel_1.remove(lblMonsterAtribute);
			panel_1.remove(monsterAtributeCB);
			panel_1.remove(lblMonsterType);
			panel_1.remove(monsterTypeCB);
		}
		repaint();
		// lblMonsterAttack.setVisible(visible);
		// lblMonsterDefense.setVisible(visible);
		// sp_Attack.setVisible(visible);
		// sp_Defense.setVisible(visible);
		// lblMonsterAtribute.setVisible(visible);
		// cbMonsterAtribute.setVisible(visible);
		// lblMonsterType.setVisible(visible);
		// cbMonsterType.setVisible(visible);
	}

	public void setSpellItemsVisible(boolean visible) {
		if (visible == true) {
			panel_1.add(lblSpellType);
			panel_1.add(cbSpellType);
		} else {
			panel_1.remove(lblSpellType);
			panel_1.remove(cbSpellType);
		}
		revalidate();
	}

	public void setTrapItemsVisible(boolean visible) {
		if (visible == true) {
			panel_1.add(lblTrapType);
			panel_1.add(cbTrapType);
		} else {
			panel_1.remove(lblTrapType);
			panel_1.remove(cbTrapType);
		}
		repaint();
	}

	public void addBackActionListener(ActionListener listener){
		backButton.addActionListener(listener);
	}
	
}
