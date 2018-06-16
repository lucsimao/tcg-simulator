package dm.tests.views;

import static org.junit.Assert.assertEquals;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import views.Engine;
import views.MainView;
import views.components.ButtonView;
import views.components.FieldBackgroundView;
import views.components.WindowView;

public class WindowViewTests {

	private JFrame jframe;

	@Before
	public void init() {
		
		// jframe.setBackground(Color.white);
	}

	@Test
	public void creatWindowView() throws InterruptedException {
		MainView mainView = new MainView(900,600);	
		mainView.addElement(new FieldBackgroundView(500, 0,900*2/9, 600),MainView.BACKGROUND);
		mainView.addElement(new WindowView(0, 0, 900*2/9, 600),MainView.EAST_BAR);
		new Engine(mainView);
		Thread.sleep(5000);
	}
	
/*	@Test
	public void createButton(){
		ButtonView button = new ButtonView(100, 100, 100,100);
//		jframe.add(button);
		assertEquals(true,false);
	}*/
	
}
