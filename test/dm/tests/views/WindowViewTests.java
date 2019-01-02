package dm.tests.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import model.fields.elements.decks.ExtraDeck;
import model.fields.elements.decks.NormalDeck;
import model.files.DeckDao;
import model.game.Player;
import views.Engine;
import views.MainView;
import views.components.ButtonView;
import views.components.CardDetailsView;
import views.components.FieldBackgroundView;
import views.components.HandView;
import views.components.WindowView;

public class WindowViewTests {

	private JFrame jframe;

	@Before
	public void init() {
		
		// jframe.setBackground(Color.white);
	}

	@Test
	public void creatWindowView() throws InterruptedException, FileNotFoundException, ClassNotFoundException, IOException {
		
		DeckDao deckDao = new DeckDao();
		File file = new File("deck/baralho.ygo");
		NormalDeck deck = deckDao.loadDeck(file);
		Player player1 = new Player("teste1", null, deck, new ExtraDeck());
		Player player2 = new Player("teste2", null, new NormalDeck(50), new ExtraDeck());
		player1.shuffleDeck();
		player1.firstDraw();
		
		
		final int card_dis_x  = Math.round(900/13.24f);
		final int padding = Math.round(900/144);
		final int card_view_width = Math.round(900/6.836f);
		final int card_view_height = Math.round(600/3.469f);
		//Botões laterais
		final int panel_button_x = Math.round(900/1.16788f);
		final int panel_button_y = Math.round(600/3.4625f);
		final int panel_distance = Math.round(600/10.833333f);
		final int button_width = Math.round(900/9f);
		final int button_height = Math.round(900/13f);


		ButtonView draw = new ButtonView("DRAW",panel_button_x ,panel_button_y + 0*panel_distance,button_width,button_height);
		ButtonView M1 = new ButtonView("M1",panel_button_x ,panel_button_y + 1*panel_distance,button_width,button_height);
		ButtonView BP = new ButtonView("BP",panel_button_x ,panel_button_y + 2*panel_distance,button_width,button_height);
		ButtonView M2 = new ButtonView("M2",panel_button_x ,panel_button_y + 3*panel_distance,button_width,button_height);
		ButtonView EP = new ButtonView("EP",panel_button_x ,panel_button_y + 4*panel_distance,button_width,button_height);
		ButtonView CLR = new ButtonView("Clear",panel_button_x ,panel_button_y + 5*panel_distance,button_width,button_height);
		
		CardDetailsView cardDetailsGraphic = new CardDetailsView(0,0,900,600);
		int  x_offset = 900*2/9/2 - Math.round(900/13.24f)/2;
		HandView handGraphic = new HandView(cardDetailsGraphic,player1, 0 ,0, card_view_width/2, card_view_height/2, card_dis_x);
		

		
		
		MainView mainView = new MainView(900,600);	
		mainView.addElement(new FieldBackgroundView(500, 0,900*2/9, 600),MainView.BACKGROUND);
		mainView.addElement(new WindowView(0, 0, 900*2/9, 600),MainView.EAST_BAR);
		mainView.addElement(draw);
		
		draw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player1.draw();
			}
		});
		
		
		
		
		
		mainView.addElement(handGraphic,MainView.BOTTOM);
		new Engine(mainView);
		Thread.sleep(5000);
	}

}
