/** 
* @author Simão 
* @version 0.1 - 1 de mai de 2017
* 
*/
package dm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dm.constants.FilesConstants;
import simao.image.ImageMixer;
import simao.image.ImageTransform;



public class FieldView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7091431371699384724L;

	private  final static int height = 400;
	private final static int width =640 ;
	private final String field_path = "field.png";
	private static final int monster_x = 147;
	private static final int monster1_y = 100 ;
	private static final int monster2_y = 239;
	private static final int extra_left_x =monster_x+354 ;
	private static final int deck1_y = monster2_y+95;

	private static final int extra_right_x = 74;
//	private static final int graveyard1;
//	private static final int graveyard1;
//	private static final int deck1;
//	private static final int deck2;
	
	public static void main(String args[]) throws IOException{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setVisible(true);
//		Card card = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
//				"magonego.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3);
//
//		Card card2 = new MonsterNormalCard("Exodia", "O guerreiro proibido",
//				"exodia.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3);
//		
		f.getContentPane().add(new FieldView());
		f.setBounds(0,0,width,height);

	}
	
	
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public FieldView() throws IOException {
		super();
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		ImageMixer im = new ImageMixer();
		BufferedImage bufferedImage = ImageIO.read(new File(FilesConstants.TEXTURES_PATH + field_path));
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62), monster_x,  monster1_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x + 70, monster2_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x + 141,  monster2_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x + 142 + 70,  monster2_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x + 143 + 70 + 70,  monster2_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62), monster_x, monster2_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62), extra_left_x,deck1_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62), extra_right_x,deck1_y);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x,monster2_y+62);
		bufferedImage = im.mixImages(bufferedImage,new File("D:/magonego.png"),new Dimension(width,height),
				new Dimension(56,62),  monster_x,monster1_y-62);
//		BufferedImage bufferedImagecard = ImageIO.read(new File("D:/magonego.png"));
		ImageTransform it = new ImageTransform();
//		ImageIcon image = new ImageIcon(it.perspectiveTransform(bufferedImage,8,width,height*3/5));
		ImageIcon image = new ImageIcon(bufferedImage);
//		ImageIcon card = new ImageIcon(it.perspectiveTransform(bufferedImagecard,1000,56,62));
		
		setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel(image);
		lblNewLabel.setBounds(0, 0, width, height);
		add(lblNewLabel);
//		JLabel lblNewLabel2 = new JLabel(card);
//		JLabel lblNewLabel3 = new JLabel(card);
//		lblNewLabel.add(lblNewLabel2);
//		lblNewLabel.add(lblNewLabel3);
//		lblNewLabel2.setBounds(148,100, 56, 62);
//		lblNewLabel3.setBounds(218,100, 54, 62);
//		lblNewLabel3.setBounds(288,100, 56, 62);
//		lblNewLabel3.setBounds(288,210, 54, 58);
//		add(lblNewLabel2);
	}

}
