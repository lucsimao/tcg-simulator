package dm.graphics;

import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import dm.cards.abstracts.Card;

public class CardGraphicHand extends ElementGraphic {

	private int x_temp;
	private int y_temp;
	private int DELAY = 71;
	private Thread t;
	public CardGraphicHand(Card card, int x, int y, int width, int height) {
		super(card, x, y, width, height);
		this.x_temp = x;
		this.y_temp = y;
		blinkAnimation();
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {
		if(isOverIt(mouseEvent.getX(),mouseEvent.getY())) {
//			System.out.println("EM CIMA");
			if(getY()==y_temp) {
				setY(getY()-20);
//			System.out.println(t.getState());
//			if(!t.isAlive()) {
		
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
							System.out.println("ALPHA: " + getAlpha());
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
	public void ClickAction(MouseEvent mouseEvent) {
		
	}

}
