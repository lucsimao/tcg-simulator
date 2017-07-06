/** 
* @author Simão 
* @version 0.1 - 18 de jun de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
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
import dm.ui.subviews.CardImage;

public class CardBuilder extends JPanel {

	private static final long serialVersionUID = -2593144591366996030L;

	private final static int height = 480;
	private final static int width = 640;

	private JLabel lblCardType;
	private JLabel lblMonsterAtribute;
	private JLabel lblMonsterType;
	private JLabel lblImage;
	private CardImage lblCardImage;
	private JLabel lblMonsterAttack;
	private JLabel lblMonsterDefense;
	private JLabel lblSpellType;
	private JLabel lblTrapType;

	private JPanel panelImageChooser;
	private JPanel panelFields;
	private JPanel panelImage;
	private JPanel panelDescription;

	private JTextArea descriptionTA;
	private JTextField nameTF;
	private JTextField pictureTF;

	private JComboBox<String> cardTypeCB;
	private JComboBox<String> monsterAtributeCB;
	private JComboBox<String> monsterTypeCB;
	private JComboBox<String> cbSpellType;
	private JComboBox<String> cbTrapType;

	private JButton choseBTN;
	private JButton createButton;
	private JButton backButton;

	private JSpinner attackSPN;
	private JSpinner defenseSPN;

	private JFileChooser chooser;

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

	public CardBuilder() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		panelImage = new JPanel();
		panelImage.setLayout(new BorderLayout(10, 10));
		add(panelImage, BorderLayout.WEST);

		lblCardImage = new CardImage(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD),
				FilesConstants.CARD_WIDTH, FilesConstants.CARD_HEIGHT);

		panelImage.add(lblCardImage, BorderLayout.NORTH);

		panelDescription = new JPanel();
		panelDescription.setLayout(new BorderLayout(10, 10));
		panelImage.add(panelDescription, BorderLayout.CENTER);
		JLabel label = new JLabel("Card Description");
		panelDescription.add(label, BorderLayout.NORTH);

		descriptionTA = new JTextArea();
		descriptionTA.setLineWrap(true);
		JScrollPane sp = new JScrollPane(descriptionTA);
		panelDescription.add(sp, BorderLayout.CENTER);

		panelFields = new JPanel();
		panelFields.setLayout(new GridLayout(7, 1, 25, 25));
		add(panelFields, BorderLayout.CENTER);

		lblImage = new JLabel("Image");
		panelFields.add(lblImage);

		panelImageChooser = new JPanel();
		panelFields.add(panelImageChooser);
		panelImageChooser.setLayout(new GridLayout(1, 0, 0, 0));

		pictureTF = new JTextField();
		panelImageChooser.add(pictureTF);
		pictureTF.setColumns(10);

		choseBTN = new JButton("Choose");
		panelImageChooser.add(choseBTN);

		JLabel lblCardName = new JLabel("Card Name");
		panelFields.add(lblCardName);

		nameTF = new JTextField();
		panelFields.add(nameTF);
		nameTF.setColumns(10);

		lblCardType = new JLabel("Card Type");
		panelFields.add(lblCardType);

		cardTypeCB = new JComboBox<String>();
		panelFields.add(cardTypeCB);
		cardTypeCB.setModel(new DefaultComboBoxModel<String>(new String[] { "MONSTER", "SPELL", "TRAP" }));

		lblMonsterAtribute = new JLabel("Monster Atribute");
		panelFields.add(lblMonsterAtribute);

		monsterAtributeCB = new JComboBox<String>();
		monsterAtributeCB.setModel(
				new DefaultComboBoxModel<String>(new String[] { "WATER", "FIRE", "LIGHT", "DARK", "WIND", "EARTH" }));
		panelFields.add(monsterAtributeCB);

		lblSpellType = new JLabel("Spell Type");

		cbSpellType = new JComboBox<String>();
		cbSpellType.setModel(
				new DefaultComboBoxModel<String>(new String[] { "NORMAL", "QUICK", "CONTINOUS", "EQUIP", "FIELD" }));

		lblTrapType = new JLabel("Trap Type");

		cbTrapType = new JComboBox<String>();
		cbTrapType.setModel(new DefaultComboBoxModel<String>(new String[] { "NORMAL", "COUNTER", "CONTINOUS" }));

		lblMonsterType = new JLabel("Monster Type");
		panelFields.add(lblMonsterType);

		monsterTypeCB = new JComboBox<String>();
		panelFields.add(monsterTypeCB);
		monsterTypeCB.setModel(new DefaultComboBoxModel<String>(new String[] { "SPELLCASTER", "WARRIOR", "FAIRY",
				"AQUA", "SEASERPENT", "FIEND", "DRAGON", "BEAST", "WINGEDBEAST", "BEASTWARRIOR", "PYRO" }));

		lblMonsterAttack = new JLabel("Monster Attack");
		panelFields.add(lblMonsterAttack);

		attackSPN = new JSpinner();
		attackSPN.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panelFields.add(attackSPN);

		lblMonsterDefense = new JLabel("Monster Defense");
		panelFields.add(lblMonsterDefense);

		defenseSPN = new JSpinner();
		defenseSPN.setModel(new SpinnerNumberModel(0, 0, 9999, 100));
		panelFields.add(defenseSPN);

		JPanel panelBotao = new JPanel(new GridLayout(1, 2, 5, 5));
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
					Image cardImage;
					try {
						cardImage = ImageIO.read(chooser.getSelectedFile());
						lblCardImage.setIcon(cardImage);
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
				card = new MonsterNormalCard(nameTF.getText(), descriptionTA.getText(), pictureTF.getText(),
						monsterTypeCB.getSelectedIndex(), monsterAtributeCB.getSelectedIndex(),
						(int) attackSPN.getValue(), (int) defenseSPN.getValue(), 0);
			} else if (cardTypeCB.getSelectedItem().toString().equals("SPELL")) {
				System.out.println("Você criou uma magia!" + cardTypeCB.getSelectedIndex());

				card = new SpellCard(nameTF.getText(), descriptionTA.getText(), pictureTF.getText(), new Effect(),
						cbSpellType.getSelectedIndex(), 3);

			} else if (cardTypeCB.getSelectedItem().toString().equals("TRAP")) {
				System.out.println("Você criou uma armadilha!" + cardTypeCB.getSelectedIndex());
				card = new TrapCard(nameTF.getText(), descriptionTA.getText(), pictureTF.getText(), new Effect(),
						cbTrapType.getSelectedIndex(), 3);
			}
			// Copia a carta para a pasta de imagens

			CardDAO cardDAO = new CardDAO();
			cardDAO.saveCard(card);
			Files.copy(chooser.getSelectedFile().toPath(),
					new File(FilesConstants.CARDS_IMG_DIR + card.getName() + ".jpg").toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			JOptionPane.showMessageDialog(null, "Your have saved sucefully your card", "SUUUCESSO",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getSimpleName(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setMonsterItemsVisible(boolean visible) {
		if (visible) {
			panelFields.add(lblMonsterAtribute);
			panelFields.add(monsterAtributeCB);
			panelFields.add(lblMonsterType);
			panelFields.add(monsterTypeCB);
			panelFields.add(lblMonsterAttack);
			panelFields.add(attackSPN);
			panelFields.add(lblMonsterDefense);
			panelFields.add(defenseSPN);
		} else {
			panelFields.remove(lblMonsterAttack);
			panelFields.remove(lblMonsterDefense);
			panelFields.remove(attackSPN);
			panelFields.remove(defenseSPN);
			panelFields.remove(lblMonsterAtribute);
			panelFields.remove(monsterAtributeCB);
			panelFields.remove(lblMonsterType);
			panelFields.remove(monsterTypeCB);
		}
		repaint();
	}

	public void setSpellItemsVisible(boolean visible) {
		if (visible == true) {
			panelFields.add(lblSpellType);
			panelFields.add(cbSpellType);
		} else {
			panelFields.remove(lblSpellType);
			panelFields.remove(cbSpellType);
		}
		revalidate();
	}

	public void setTrapItemsVisible(boolean visible) {
		if (visible == true) {
			panelFields.add(lblTrapType);
			panelFields.add(cbTrapType);
		} else {
			panelFields.remove(lblTrapType);
			panelFields.remove(cbTrapType);
		}
		repaint();
	}

	public void addBackActionListener(ActionListener listener) {
		backButton.addActionListener(listener);
	}

}
