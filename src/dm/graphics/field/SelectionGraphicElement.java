package dm.graphics.field;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.lang.Thread.State;

import dm.graphics.Screen;

public class SelectionGraphicElement extends ElementGraphic {

	private final int DELAY = 71;
	protected Thread thread;
	private Color color;
	
	public SelectionGraphicElement(int x, int y, int width, int height, float alpha, Color color) {
		super(null, x, y, width, 0);
		this.color = color;
		thread = new Thread();
	}

	public SelectionGraphicElement(int x, int y, int width, int height) {
		super(null, x, y, width, height, 0);
		this.color = Color.white;
		thread = new Thread();
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void hoverAction(MouseEvent mouseEvent) {
//		System.out.println(thread.getState());
		int x = mouseEvent.getX();
		int y = mouseEvent.getY();

		if (x >= getX() && x <= getX() + getWidth()) {
			if ((y >= getY() && y <= getY() + getHeight())) {
				if (thread.getState().equals(State.NEW) || thread.getState().equals(State.TERMINATED)) {
//					System.out.println("SELECTIONGRAPHIC - Chamando thread");
					blinkAnimation();
				}
			}else {
				setAlpha(0);
				thread.stop();
			}
				
		} else {
//			System.out.println("SELECTIONGRAPHIC - Parando Thread");
			setAlpha(0);
			thread.stop();
		}
	}
	
	@Override
	public void clickAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressedAction(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

	public void blinkAnimation() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					setAlpha(0);
//					if (getAlpha() == 0) {
						while (getAlpha() < 0.8) {
							setAlpha((float) (getAlpha() + 0.1));
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						while (getAlpha() > 0.1) {
							setAlpha((float) (getAlpha() - 0.1));
							// System.out.println("ALPHA: " + getAlpha());
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				}
//			}
		});
		thread.start();
	}

	@Override
	public void drawItself(Screen screen) {
		screen.rectangle(getX(), getY(), getWidth(), getHeight(), color, getAlpha());
	}

}
