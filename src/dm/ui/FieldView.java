/** 
* @author Simão 
* @version 0.1 - 1 de mai de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardState;
import dm.constants.FilesConstants;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.CardNotFoundException;
import dm.fields.Field;
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
	
	protected static final int MAX_CURSOR = 36;
	protected static final int MIN_CURSOR = 00;
	
	private int cursor;
	
	private Field field1;
	private Field field2;

	private JLabel lblField;
	private ImageTransform it;

	public static void main(String args[]) throws IOException {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
		Card card = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				"magonego.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3);
		// card.setState(CardState.FACE_UP_DEFENSE_POS);
//		System.out.println(card.getState());
		// Card card2 = new MonsterNormalCard("Exodia", "O guerreiro proibido",
		// "exodia.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500,
		// 2100, 0, 3);
		//
		FieldView fv = new FieldView();

		f.getContentPane().add(fv);
		fv.setFocusable(true);
		fv.requestFocusInWindow();
		f.setBounds(0, 0, width, height);
		try {
			Thread.sleep(1000);
			fv.getField1().summonMonster((MonsterCard) card);
			Thread.sleep(1000);
			fv.getField1().setCard((MonsterCard) card);
			Thread.sleep(1000);
			fv.getField1().summonMonster((MonsterCard) card);
			fv.getField1().changeToDefense((MonsterCard) card);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the panel.
	 * 
	 * @throws IOException
	 */
	public FieldView() throws IOException {
		super();
		it = new ImageTransform();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));

		field1 = new Field();
		field2 = new Field();
		// ImageIcon image = new ImageIcon(getBufferedImage());
		lblField = new JLabel();
		lblField.setBounds(0, 0, width, height);
		add(lblField);
		setFocusable(true);
		this.requestFocusInWindow();
		addKeyListener(getKeyListener());
		repaint();

	}

	private KeyListener getKeyListener() {
		return new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
//				System.out.println("SOLTOU");

				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					moveCursor(LEFT);
//					System.out.println("LEFT");
				}
				if(e.getKeyCode() ==KeyEvent.VK_RIGHT)
				{
					moveCursor(RIGHT);
//					System.out.println("RIGHT");
				}
				if(e.getKeyCode() ==KeyEvent.VK_UP){
					moveCursor(UP);
//					System.out.println("UP");
				}
				if(e.getKeyCode() ==KeyEvent.VK_DOWN){
					moveCursor(DOWN);
//					System.out.println("DOWN");
				}
				if(e.getKeyCode() ==KeyEvent.VK_ENTER){
					System.out.println("ENTER");
				}
				if(e.getKeyCode() ==KeyEvent.VK_BACK_SPACE){
					System.out.println("BACK");
				}
			}


		};
	}
	public void moveCursor(int position) {
		if(this.cursor<MIN_CURSOR||cursor>MAX_CURSOR){
			this.cursor = 10;
		}else if(this.cursor+ position<=MAX_CURSOR&&this.cursor + position>=MIN_CURSOR&&(this.cursor+position)%10<=7)
				this.cursor += position;
			System.out.println("CURSOR: " + this.cursor);
	}
	@Override
	public void paint(Graphics g) {

		try {
			// lblNewLabel.setIcon(new ImageIcon(getBufferedImage()));
			lblField.setIcon(new ImageIcon(it.perspectiveTransform(getBufferedImage(), 6, width, height * 3 / 5)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.paint(g);
	}

	public BufferedImage getBufferedImage() throws IOException {
		ImageMixer im = new ImageMixer();

		BufferedImage bufferedImage = ImageIO.read(new File(FilesConstants.TEXTURES_PATH + field_path));

		bufferedImage = loadGraves(bufferedImage, im);
		bufferedImage = loadDecks(bufferedImage, im);

		bufferedImage = loadMonster1(bufferedImage, im);
		bufferedImage = loadMonster2(bufferedImage, im);
		bufferedImage = loadNonMonster1(bufferedImage, im);
		bufferedImage = loadNonMonster2(bufferedImage, im);

		return bufferedImage;
	}

	private BufferedImage loadDecks(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		File file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
		// Extradeck 1
		boolean selected = false;
		if(this.cursor == MIN_CURSOR)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim, extra_left_x,
				deck1_y);
		// Deck 1
		// for (int i = 0; i < field1.getDeck().size(); i++)
		// bufferedImage = im.mixImages(bufferedImage, file, new
		// Dimension(width, height), card_dim, extra_right_x + i / 8,
		// deck1_y - i/5);
		selected = false;
		if(this.cursor == MIN_CURSOR + 6)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim, extra_right_x,
				deck1_y);
		// Deck 2
		// for (int i = 0; i < field1.getDeck().size(); i++)
		// bufferedImage = im.mixImages(bufferedImage, file, new
		// Dimension(width, height), card_dim, extra_left_x - i/8/2,
		// deck2_y - i / 5);
		selected = false;
		if(this.cursor == MIN_CURSOR + 30)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim, extra_left_x,
				deck2_y);
		// ExtraDeck 2
		selected = false;
		if(this.cursor == MIN_CURSOR + 30 + 6)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim, extra_right_x,
				deck2_y);
		// Field 2
		// bufferedImage = im.mixImages(bufferedImage, file, new
		// Dimension(width, height), card_dim, extra_right_x,
		// graveyard2_y);
		// Field 1
		// bufferedImage = im.mixImages(bufferedImage, file, new
		// Dimension(width, height), card_dim, extra_left_x,
		// graveyard1_y);

		return bufferedImage;
	}

	private BufferedImage loadGraves(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		File file = new File(FilesConstants.CARDS_IMG_DIR + field1.getGraveyard().top().getPicture());
		// for (int i = 0; i < field1.getGraveyard().size(); i++)
		boolean selected = false;
		if(this.cursor == MIN_CURSOR + 10 + 6)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim, extra_right_x,
				graveyard1_y);
		new File(FilesConstants.CARDS_IMG_DIR + field2.getGraveyard().top().getPicture());
		selected = false;
		if(this.cursor == MIN_CURSOR + 20)
			selected = true;
		bufferedImage = im.mixImages(selected,bufferedImage,
				it.rotateImage(ImageIO.read(file), 180, AffineTransformOp.TYPE_BICUBIC), new Dimension(width, height),
				card_dim, extra_left_x, graveyard2_y);
		return bufferedImage;
	}

	private BufferedImage loadNonMonster2(BufferedImage bufferedImage, ImageMixer im) throws IOException {
		// Spell 2
		for (int i = 0; i < 5; i++) {

			try {
				boolean selected = false;
				if(this.cursor== 30+i+1)
					selected = true;
				NonMonsterCard card = field1.getNonMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
//				System.out.println("STATE  " + card.getState());
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
				boolean selected =false;
				if(this.cursor== MIN_CURSOR + i + 1)
					selected = true;
				NonMonsterCard card = field1.getNonMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
//				System.out.println("STATE  " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim,
							monster_x + 70 * i + i + 1, spell1_y);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected,bufferedImage, face_down_file, new Dimension(width, height), card_dim,
							monster_x + 70 * i + i + 1, spell1_y);

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
				if(this.cursor== MIN_CURSOR + 20 + i + 1)
					selected = true;
				MonsterCard card = field2.getMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
//				System.out.println("STATE  " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected,bufferedImage,
							it.rotateImage(ImageIO.read(file), 180, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim, monster_x + 70 * i + i + 1, monster2_y);
				else if (card.getState() == CardState.FACE_UP_DEFENSE_POS)
					bufferedImage = im.mixImages(selected,bufferedImage,
							it.rotateImage(ImageIO.read(file), 90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster2_y - defense_bias);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected,bufferedImage,
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
				if(this.cursor== MIN_CURSOR +10+ i + 1)
					selected = true;
				MonsterCard card = field1.getMonsterCard(i);
				File file = new File(FilesConstants.CARDS_IMG_DIR + card.getPicture());
				File face_down_file = new File(FilesConstants.CARDS_IMG_DIR + FilesConstants.FACE_DOWN_CARD);
//				System.out.println("STATE  " + card.getState());
				if (card.getState() == CardState.FACE_UP_ATTACK)
					bufferedImage = im.mixImages(selected,bufferedImage, file, new Dimension(width, height), card_dim,
							monster_x + 70 * i + i + 1, monster1_y);
				else if (card.getState() == CardState.FACE_UP_DEFENSE_POS)
					bufferedImage = im.mixImages(selected,bufferedImage,
							it.rotateImage(ImageIO.read(file), -90, AffineTransformOp.TYPE_BICUBIC),
							new Dimension(width, height), card_dim_deffense, monster_x + 70 * i + i + 1 - defense_bias,
							monster1_y - defense_bias);
				else if (card.getState() == CardState.FACE_DOWN)
					bufferedImage = im.mixImages(selected,bufferedImage,
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
