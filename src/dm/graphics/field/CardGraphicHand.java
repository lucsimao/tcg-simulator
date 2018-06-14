package dm.graphics.field;

import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.FilesConstants;
import dm.exceptions.MonsterCannotBeSummonedException;
import dm.exceptions.ZoneOccupedException;
import dm.game.Player;
import dm.graphics.Screen;

public class CardGraphicHand extends GraphicElement {

	// private int x_temp;
	private int y_temp;
	private int DELAY = 71;
	private Thread t;
	private Player player;
	private Card card;
	private CardDetailsGraphic cardDetailsGraphic;

	public CardGraphicHand(CardDetailsGraphic cardDetailsGraphic, Player player, Card card, int x, int y, int width,
			int height) {
		super(card.getPicture(), x, y, width, height);
		// this.x_temp = x;
		this.y_temp = y;
		t = new Thread();
		this.player = player;
		this.card = card;
		this.cardDetailsGraphic = cardDetailsGraphic;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		if (isOverIt(mouseEvent.getX(), mouseEvent.getY())) {
			if (getY() == y_temp) {
				setY(getY() - 20);
				if (t.getState().equals(State.NEW) || t.getState().equals(State.TERMINATED)) {
					this.cardDetailsGraphic.setCard(card);
					blinkAnimation();
				}
				// }
			}
		} else {
			setY(y_temp);
			t.stop();
			setAlpha(1);
		}

	}

	public void blinkAnimation() {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (getAlpha() > 0) {
						while (getAlpha() > 0.35) {
							setAlpha((float) (getAlpha() - 0.1));
							// System.out.println("ALPHA: " + getAlpha());
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						while (getAlpha() < 1) {
							setAlpha((float) (getAlpha() + 0.1));
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}
			}
		});
		t.start();
	}

	@Override
	public void drawItself(Screen screen) {
		screen.imageScaled(FilesConstants.CARDS_IMG_DIR + getPicture(), 0, 0, getWidth(), getHeight(), 0, getX(),
				getY(), getAlpha());
	}

	@Override
	public void clickAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
			try {
				if(card instanceof MonsterCard) { 
					if(mouseEvent.getButton()== MouseEvent.BUTTON1)
						player.summon((MonsterCard) card);
					else
						if(mouseEvent.getButton()== MouseEvent.BUTTON3)
							player.set((MonsterCard) card);
				}
				else if (card instanceof NonMonsterCard) {
					if(mouseEvent.getButton()== MouseEvent.BUTTON1)
						player.activate((NonMonsterCard) card);
					else
						if(mouseEvent.getButton()== MouseEvent.BUTTON3)
							player.set((NonMonsterCard) card);
				}
			}catch (MonsterCannotBeSummonedException monsterCannotBeSummonedException) {
				JOptionPane pane = new JOptionPane();
				pane.setMessage(monsterCannotBeSummonedException.getMessage()); // Configure
				JDialog dialog = pane.createDialog(monsterCannotBeSummonedException.getClass().getSimpleName());
				dialog.show();
			}catch (ZoneOccupedException zoneOccupedException) {
				JOptionPane pane = new JOptionPane();
				pane.setMessage(zoneOccupedException.getMessage()); // Configure
				JDialog dialog = pane.createDialog(zoneOccupedException.getClass().getSimpleName());
				dialog.show();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

}
