package dm.tests.ui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterEffectCard;
import dm.ui.CardView;

public class CardViewTests {

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

		jframe.add(new CardView(new MonsterEffectCard(3)));
		Thread.sleep(5000);
	}
}
