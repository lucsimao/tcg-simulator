package dm.tests.graphic;

import static org.junit.Assert.assertEquals;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;

import dm.graphics.field.ButtonGraphic;
import junit.framework.Assert;

public class EngineTests {

	private JFrame jframe;

	@Before
	public void init() {
		jframe = new JFrame();
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.setBounds(0, 0, 640, 480);
		// jframe.setBackground(Color.white);
	}

	@Test
	public void createCardView() throws InterruptedException {
//
//		jframe.add(new CardView(new MonsterEffectCard()));
//		Thread.sleep(5000);
	}
	
	@Test
	public void createButton(){
		ButtonGraphic button = new ButtonGraphic(100, 100, 100,100);
//		jframe.add(button);
		assertEquals(true,false);
	}
	
}
