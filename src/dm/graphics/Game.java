package dm.graphics;

import java.awt.event.MouseEvent;

public interface Game {
    String getTitle();
    int getWidth();
    int getHeight();
    void setHeight(int height);
    void setWidth(int width);
    void tick(java.util.Set<String> teclas, double dt);
    void key(String tecla);
    void draw(Screen screen);
    void mouse(MouseEvent mouseEvent);
	void move(MouseEvent mouseEvent);
}
