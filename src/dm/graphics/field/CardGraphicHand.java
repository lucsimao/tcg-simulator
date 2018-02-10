package dm.graphics.field;

import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import org.junit.Test.None;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.FilesConstants;
import dm.game.Player;
import dm.graphics.Screen;

public class CardGraphicHand extends ElementGraphic {

	private int x_temp;
	private int y_temp;
	private int DELAY = 71;
	private Thread t;
	private Player player;
	private Card card;
	public CardGraphicHand(Player player, Card card, int x, int y, int width, int height) {
		super(card.getPicture(), x, y, width, height);
		this.x_temp = x;
		this.y_temp = y;
		t = new Thread();
		this.player = player;
		this.card = card;
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
			if(getY()==y_temp) {
				setY(getY()-20);	
				if(t.getState().equals(State.NEW)||t.getState().equals(State.TERMINATED))
				{
					blinkAnimation();
				}
//			}
			}
		}
		else {
			setY(y_temp);	
			t.stop();
			setAlpha(1);
		}
			
	
		}

	public void blinkAnimation() {
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(getAlpha()>0) {
						while(getAlpha()>0.35) {
							setAlpha((float) (getAlpha()-0.1));
//							System.out.println("ALPHA: " + getAlpha());
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						while(getAlpha()<1) {
							setAlpha((float) (getAlpha()+0.1));
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
		screen.imageScaled(FilesConstants.CARDS_IMG_DIR + getPicture(), 0, 0,getWidth() ,getHeight(), 0,getX(),getY(),getAlpha());
	}
	
	@Override
	public void clickAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
//			if(getY()==y_temp) {
//				setY(getY()-20);	
//				if(t.getState().equals(State.NEW)||t.getState().equals(State.TERMINATED))
//				{
//					blinkAnimation();
//				}
////			}
//			}
//			System.out.println("Clicou no " + card.getName());
			try {
				if(card instanceof MonsterCard) {
					if(mouseEvent.getButton()== MouseEvent.BUTTON1)
						player.summon((MonsterNormalCard) card);
					else
						if(mouseEvent.getButton()== MouseEvent.BUTTON3)
							player.set((MonsterNormalCard) card);
				}
				else if (card instanceof NonMonsterCard) {
					if(mouseEvent.getButton()== MouseEvent.BUTTON1)
						player.activate((NonMonsterCard) card);
					else
						if(mouseEvent.getButton()== MouseEvent.BUTTON3)
							player.set((NonMonsterCard) card);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		else {
////			setY(y_temp);	
////			t.stop();
////			setAlpha(1);
//		}
		
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

}
