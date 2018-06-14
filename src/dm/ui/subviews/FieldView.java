/** 
* @author Simão 
* @version 0.1 - 1 de mai de 2017
* 
*/
package dm.ui.subviews;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.cards.NormalDeckCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardState;
import dm.constants.FilesConstants;
import dm.constants.Log;
import dm.exceptions.CardNotFoundException;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;
import simao.image.ImageMixer;
import simao.image.ImageTransform;

public class FieldView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7091431371699384724L;

	private final static int height = 400;
	private final static int width = 640;
	private final String field_path = "field.png";
	private final String background_path = "background.jpg";
	private static final int monster_x = 146;
	private static final int monster2_y = 100;
	private static final int monster1_y = 239;
	private static final int extra_right_x = monster_x + 358;
	private static final int extra_left_x = 74;
	private static final int deck1_y = monster1_y + 95;
	private static final int spell1_y = monster1_y + 62;
	private static final int spell2_y = monster2_y - 62;
	private static final int graveyard1_y = deck1_y - 64;
	private static final int deck2_y = 4;
	private static final int graveyard2_y = deck2_y + 64;
	private static final int defense_bias = 3;
	private static final Dimension card_dim = new Dimension(56, 62);
	private static final Dimension card_dim_deffense = new Dimension(62, 56);

	protected static final int LEFT = -1;
	protected static final int RIGHT = 1;
	protected static final int UP = 10;
	protected static final int DOWN = -10;

	protected static final int MAX_CURSOR = 34;
	protected static final int MIN_CURSOR = 00;

	private static final String TAG = "Field";

	private int cursor;

	private Field field1;
	private Field field2;

	private JLabel lblField;
	private ImageTransform it;

	private BufferedImage bufferedImage;

	private Player player1;
	private Player player2;

	private JLabel infoLabel;

	private MonsterCard attackingCard;

	private ImageIcon screen;
	
	
	public static void main(String args[]) throws IOException {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);

		Player player1 = new Player("teste1", null, new NormalDeck(50), new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		FieldView fv = new FieldView(player1, player2);
		HandView handView = new HandView(player1);
		fv.add(handView, "South");
		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);

		// try {
		// Thread.sleep(1000);
		// fv.getField1().summonMonster((MonsterCard) card);
		// Thread.sleep(1000);
		// fv.getField1().setCard((MonsterCard) card);
		// Thread.sleep(1000);
		// fv.getField1().summonMonster((MonsterCard) card);
		// fv.getField1().changeToDefense((MonsterCard) card);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public FieldView(Player player1, Player player2) {
		super();
		this.attackingCard = null;
		it = new ImageTransform();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));
		
		player1.shuffleDeck();
		player2.shuffleDeck();
		field1 = player1.getField();
		field2 = player2.getField();
		this.player1 = player1;
		this.player2 = player2;
		this.screen = null;
		try {
			bufferedImage = ImageIO.read(new File(FilesConstants.TEXTURES_PATH + background_path));
			// bufferedImage = ImageIO.read(new
			// File(FilesConstants.TEXTURES_PATH + field_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadDeckImagesResized();
		
		// ImageIcon image = new ImageIcon(getBufferedImage());
		lblField = new JLabel();
		lblField.setBounds(0, 0, width, height);

		this.infoLabel = new JLabel("Use as setas para mover no campo e Enter para escolher uma ação");

		setLayout(new BorderLayout());
		add(infoLabel, "North");
		add(lblField);
		setFocusable(true);
		this.requestFocusInWindow();
		addKeyListener(getKeyListener());
		repaint();

	}

	public void loadDeckImagesResized() {
		ImageTransform imageTransform = new ImageTransform();
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD));
			bufferedImage = imageTransform.scaleImage(bufferedImage,card_dim.getWidth()*1.0/bufferedImage.getWidth() , card_dim.getHeight()*1.0/bufferedImage.getHeight(),AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			imageTransform.saveBufferedImageToFile(bufferedImage,FilesConstants.CARDS_IMG_DIR_RESIZED + FilesConstants.FACE_DOWN_CARD,"png");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		saveDeckResized(player1.getDeck());
		saveDeckResized(player2.getDeck());
	}
	
	public void saveDeckResized(NormalDeck deck) {
		ImageTransform imageTransform = new ImageTransform();
		BufferedImage bufferedImage;
		ArrayList<NormalDeckCard> deck_list = (ArrayList<NormalDeckCard>) deck.getCardsList();
		for(NormalDeckCard card : deck_list) {
			try {
				bufferedImage = ImageIO.read(new File(FilesConstants.CARDS_IMG_DIR + ((Card)card).getPicture()));
				double scaleX = card_dim.getWidth()*1.0/bufferedImage.getWidth();
				double scaleY = card_dim.getHeight()*1.0/bufferedImage.getHeight();
				System.out.println("SCALE x = " + scaleX);
				bufferedImage = imageTransform.scaleImage(bufferedImage, scaleX,scaleY ,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				imageTransform.saveBufferedImageToFile(bufferedImage,FilesConstants.CARDS_IMG_DIR_RESIZED +((Card)card).getPicture(),"png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}
	
	private KeyListener getKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Cursor " + cursor);

				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					moveCursor(LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					moveCursor(RIGHT);
					break;
				case KeyEvent.VK_UP:
					moveCursor(UP);
					break;
				case KeyEvent.VK_DOWN:
					moveCursor(DOWN);
					break;
				case KeyEvent.VK_ENTER:
					catchAction();
					break;

				}
			}

		};
	}

	private void catchAction() {
		if (attackingCard == null) {
			if (this.cursor > MIN_CURSOR) {
				Player player = player2;
				if (this.cursor / 10 <= MAX_CURSOR / 20)
					player = player1;
				FieldActionView fieldActionView = new FieldActionView(player, getCard(cursor));
				fieldActionView.setAttackActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						attackingCard = (MonsterCard) fieldActionView.getCard();
						if (fieldActionView.getPlayer().equals(player1))
							cursor = MIN_CURSOR + 20;
						else
							cursor = MIN_CURSOR + 10;
						fieldActionView.dispose();
						
					}
				});
			}
		} else {
		
			System.out.println("atacou");
			if (cursor >= MIN_CURSOR + 20)
				player1.attack(attackingCard, player2, (MonsterCard) getCard(cursor % 10));
			else
				player2.attack(attackingCard, player1, (MonsterCard) getCard(cursor % 10));
			attackingCard = null;
	
			if(player1.getLP()==0||player2.getLP()==0)
				JOptionPane.showMessageDialog(null, "LP do jogador chegou a zero", 
						"Você perdeu o jogo",
						JOptionPane.INFORMATION_MESSAGE);
			
			getParent().revalidate();
			Log.messageLog(TAG,"LP 1 - " + player1.getLP());
			Log.messageLog(TAG,"LP 2 - " + player2.getLP());
			repaint();
		}
		Log.messageLog(TAG, "Enter pressed");
	}

	public void moveCursor(int position) {
		if (attackingCard == null) {
			if (this.cursor < MIN_CURSOR || cursor > MAX_CURSOR) {
				this.cursor = 10;
			} else if (this.cursor + position <= MAX_CURSOR && this.cursor + position >= MIN_CURSOR
					&& (this.cursor + position) % 10 <= MAX_CURSOR % 10) {
				this.cursor += position;
				System.out.println("CURSOR: " + this.cursor);
			}
		} else {
			int cursor_init = cursor / 10 * 10;
			System.out.println(cursor_init);
			// if (this.cursor < MIN_CURSOR+10 || cursor > MAX_CURSOR-10) {
			// this.cursor = 10;}
			if (this.cursor + position >= cursor_init && this.cursor + position <= (cursor_init + MAX_CURSOR % 10)) {
				this.cursor += position;
				System.out.println("CURSOR: " + this.cursor);
			}
		}
	}

	private Card getCard(int index) {
		try {

			if (this.cursor / 10 <= MAX_CURSOR / 20) {

				if (cursor / 10 / 2 == 0) {
					return player1.getMonsterCard(cursor % 10);

				} else
					return player1.getNonMonsterCard(cursor % 10);
			} else {

				if (cursor / 10 / 2 != 0) {
					return player2.getMonsterCard(cursor % 10);
				} else
					return player2.getNonMonsterCard(cursor % 10);
			}
		} catch (ArrayIndexOutOfBoundsException | CardNotFoundException e) {
			return null;
		}
	}

	@Override
	public void paint(Graphics g) {

		try {
			screen = new ImageIcon(it.perspectiveTransform(getBufferedImage(), 6, width, height * 3 / 5));
			// lblNewLabel.setIcon(new ImageIcon(getBufferedImage()));
			lblField.setIcon(screen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.paint(g);
	}

	public BufferedImage getBufferedImage() throws IOException {
		ImageMixer im = new ImageMixer();

		// BufferedImage bufferedImage = ImageIO.read(new
		// File(FilesConstants.TEXTURES_PATH + field_path));

		// bufferedImage = loadGraves(bufferedImage, im);

		bufferedImage = im.mixImages(bufferedImage, new File(FilesConstants.TEXTURES_PATH + field_path),
				new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()),
				new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()), 0, 0);

		bufferedImage = loadMonster1(bufferedImage, im);
		bufferedImage = loadMonster2(bufferedImage, im);
		bufferedImage = loadNonMonster1(bufferedImage, im);
		bufferedImage = loadNonMonster2(bufferedImage, im);
		bufferedImage = loadGraves(bufferedImage, im);
		// bufferedImage = loadDecks(bufferedImage, im);

		return bufferedImage;
	}

	private BufferedImage loadGraves(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		try {

			File file = new File(FilesConstants.CARDS_IMG_DIR + field1.getGraveyard().top().getPicture());
			
//			boolean selected = false;
//			if (this.cursor == MIN_CURSOR + 10 + 6)
//				selected = true;
			bufferedImage = im.mixImages(bufferedImage, file, new Dimension(width, height), card_dim,
					extra_right_x, graveyard1_y);
		} catch (Exception e) {
//			 Log.errorLog(TAG, "Graveyard: " + e.getMessage());
		}
//			new File(FilesConstants.CARDS_IMG_DIR + field2.getGraveyard().top().getPicture());
//			selected = false;
//			if (this.cursor == MIN_CURSOR + 20)
//				selected = true;
		try {
		File file2 = new File(FilesConstants.CARDS_IMG_DIR + field2.getGraveyard().top().getPicture());
			bufferedImage = im.mixImages(bufferedImage,
					it.rotateImage(ImageIO.read(file2), 180, AffineTransformOp.TYPE_BICUBIC),
					new Dimension(width, height), card_dim, extra_left_x, graveyard2_y);
		} catch (Exception e) {
//			 Log.errorLog(TAG, "Graveyard: " + e.getMessage());
		}
		return bufferedImage;
	}

	private BufferedImage loadNonMonster2(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		// Spell 2
		for (int i = 0; i < 5; i++) {

			try {
				boolean selected = false;
				if (this.cursor == 30 + i)
					selected = true;
				NonMonsterCard card = field2.getNonMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + FilesConstants.FACE_DOWN_CARD);
				// boolean selected = false;
				// System.out.println("STATE " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected,bufferedImage,
							it.rotateImage(ImageIO.read(file), 180, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim, monster_x + 70 * i + i + 1, spell2_y);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected,bufferedImage,
							it.rotateImage(ImageIO.read(face_down_file), 180, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim, monster_x + 70 * i + i + 1, spell2_y);

			} catch (NullPointerException e) {
				// e.printStackTrace();
			} catch (CardNotFoundException e2) {
				// e2.printStackTrace();
			}
		}
		return bufferedImage;
	}

	private BufferedImage loadNonMonster1(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		// Spell 1
		for (int i = 0; i < 5; i++) {

			try {
				boolean selected = false;
				if (this.cursor == MIN_CURSOR + i)
					selected = true;
				NonMonsterCard card = field1.getNonMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + FilesConstants.FACE_DOWN_CARD);
				// System.out.println("STATE " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected, bufferedImage, file, new Dimension(width, height), card_dim,
							monster_x + 70 * i + i + 1, spell1_y);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected, bufferedImage, face_down_file, new Dimension(width, height),
							card_dim, monster_x + 70 * i + i + 1, spell1_y);

			} catch (NullPointerException e) {
				// e.printStackTrace();
			} catch (CardNotFoundException e2) {
				// e2.printStackTrace();
			}

			//
			// bufferedImage = im.mixImages(bufferedImage,d_file,new
			// Dimension(width,height),
			// card_dim, monster_x+70*i +i+1, spell1_y);
		}
		return bufferedImage;
	}

	private BufferedImage loadMonster2(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		// Monster 2

		for (int i = 0; i < 5; i++) {
			boolean selected = false;
			try {
				if (this.cursor == MIN_CURSOR + 20 + i)
					selected = true;
				MonsterCard card = field2.getMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + FilesConstants.FACE_DOWN_CARD);
				// System.out.println("STATE " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected, bufferedImage,
							it.rotateImage(ImageIO.read(file), 180, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim, monster_x + 70 * i + i + 1, monster2_y);
				else if (card.getState() == CardState.FACE_UP_DEFENSE_POS)
					bufferedImage = im.mixImages(selected, bufferedImage,
							it.rotateImage(ImageIO.read(file), 90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster2_y - defense_bias);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected, bufferedImage,
							it.rotateImage(ImageIO.read(face_down_file), 90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster2_y - defense_bias);
			} catch (NullPointerException e) {
				// e.printStackTrace();
			} catch (CardNotFoundException e2) {
				// e2.printStackTrace();
			}
		}
		return bufferedImage;
	}

	private BufferedImage loadMonster1(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		// Monster 1

		for (int i = 0; i < 5; i++) {

			try {
				boolean selected = false;
				if (this.cursor == MIN_CURSOR + 10 + i)
					selected = true;
				MonsterCard card = field1.getMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR_RESIZED + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
				// System.out.println("STATE " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected, bufferedImage, file, new Dimension(width, height), card_dim,
							monster_x + 70 * i + i + 1, monster1_y);
				else if (card.getState() == CardState.FACE_UP_DEFENSE_POS)
					bufferedImage = im.mixImages(selected, bufferedImage,
							it.rotateImage(ImageIO.read(file), -90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster1_y - defense_bias);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected, bufferedImage,
							it.rotateImage(ImageIO.read(face_down_file), -90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster1_y - defense_bias);
			} catch (NullPointerException e) {
				// e.printStackTrace();
			} catch (CardNotFoundException e2) {
				// e2.printStackTrace();
			}
		}
		return bufferedImage;
	}

	public void setField1(Field field) {
		this.field1 = field;
	}

	public Field getField1() {
		return this.field1;
	}

	public void setField2(Field field) {
		this.field2 = field;
	}

	public Field getField2() {
		return this.field2;
	}

}
