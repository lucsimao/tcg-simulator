package dm.graphics;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import dm.cards.abstracts.Card;

public class SelectionGraphicElement extends ElementGraphic {

	private int spell_player1_y;
	private int spell_player2_y;
	private int monster_player1_y;
	private int monster_player2_y;
	private int card_x;
	private int card_distance;
	private int deck_x;
	private int extra_y;
	private int extra1_x;
	private int extra2_x;

	private Thread t;
	private final int DELAY = 70;
	private Color color;

	public SelectionGraphicElement(int width, int height, int card_x, int card_distance, int spell_player1_y,
			int spell_player2_y, int monster_player1_y, int monster_player2_y, int extra_y, int extra1_x,
			int extra2_x) {
		super(null,-width, -height, width, height, 1);
		this.spell_player1_y = spell_player1_y;
		this.spell_player2_y = spell_player2_y;
		this.monster_player1_y = monster_player1_y;
		this.monster_player2_y = monster_player2_y;
		this.card_x = card_x;
		this.card_distance = card_distance;
		this.color = Color.WHITE;

		this.extra_y = extra_y;
		this.extra2_x = extra2_x;
		this.extra1_x = extra1_x;

		t = new Thread();
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {

		int x = mouseEvent.getX();
		int y = mouseEvent.getY();

		// setY(spell_player1_y);
		// if(x>=card_x && x<=((card_x + getWidth())))
		// {
		// setX(card_x);
		//
		// if(t.getState().equals(State.NEW)||t.getState().equals(State.TERMINATED))
		// {
		// blinkAnimation();
		// }
		// }else
		// if(x>=(card_x + getWidth()+ card_distance) && x<=(card_x + 2*getWidth() +
		// card_distance)) {
		// setX(card_x +getWidth() + card_distance);
		// if(t.getState().equals(State.NEW)||t.getState().equals(State.TERMINATED))
		// {
		// blinkAnimation();
		// }
		// }else {
		//
		// t.stop();
		// setAlpha(0);}
		varrerY(x, y);
		// varrerY(x,y);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private void varrerY(int x, int y) {
		if (y >= spell_player2_y && y <= spell_player2_y + getHeight())
			animar(x, spell_player2_y);
		else if (y >= monster_player2_y && y <= monster_player2_y + getHeight())
			animar(x, monster_player2_y);
		else if (y >= monster_player1_y && y <= monster_player1_y + getHeight())
			animar(x, monster_player1_y);
		else if (y >= spell_player1_y && y <= spell_player1_y + getHeight())
			animar(x, spell_player1_y);
		else if (y >= extra_y && y <= extra_y + getHeight()) {
			animarExtra(x, extra_y);
		} else {
			setAlpha(0);
			t.stop();
		}
	}

	public void animar(int x, int y) {
		setY(y);
		varrerX(x, y);
	}

	public void animarExtra(int x, int y) {
		setY(y);
		if (x >= extra1_x && x <= extra1_x + getWidth()) {
			setX(extra1_x);
			if (t.getState().equals(State.NEW) || t.getState().equals(State.TERMINATED)) {
				blinkAnimation();
			}
		} else if (x >= extra2_x && x <= extra2_x + getWidth()) {
			setX(extra2_x);
			if (t.getState().equals(State.NEW) || t.getState().equals(State.TERMINATED)) {
				blinkAnimation();
			}
		} else {
			t.stop();
			setAlpha(0);
		}
	}

	int varrerX(int x, int y) {
		for (int i = 0; i < 5; i++) {
			if (x >= card_x + i * (card_distance + getWidth())
					&& x <= card_x + (i + 1) * (getWidth()) + i * card_distance) {
				setX(card_x + i * (getWidth() + card_distance));
				if (t.getState().equals(State.NEW) || t.getState().equals(State.TERMINATED)) {
					blinkAnimation();
				}
				i = 10;
				return 0;
			}
		}
		// if(x>= card_x + i * (card_distance + getWidth()) && x<= card_x + (i+1) *
		// (getWidth()) + i * card_distance)
		t.stop();
		setAlpha(0);
		return 0;
	}

	public void blinkAnimation() {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					setAlpha(1);
					if (getAlpha() > 0) {
						while (getAlpha() > 0.1) {
							setAlpha((float) (getAlpha() - 0.1));
							System.out.println("ALPHA: " + getAlpha());
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						while (getAlpha() < 0.8) {
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
	public void clickAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawItself(Screen screen) {
		screen.rectangle(getX(), getY(), getWidth(), getHeight(), color, getAlpha());
	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

}
