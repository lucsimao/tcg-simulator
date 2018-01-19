package dm.graphics;

public interface Game {
    String getTitle();
    int getWidth();
    int getHeight();
    void tick(java.util.Set<String> teclas, double dt);
    void key(String tecla);
    void draw(Screen screen);
}
