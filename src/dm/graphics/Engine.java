package dm.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Engine {
	protected Canvas canvas;
	protected BufferStrategy strategy;
    protected Game game;
    protected JPanel panel;
    protected JFrame container;
    protected Screen screen;
    private final String FRAME_NAME = "YU GI OH";
    
    public TreeSet<String> keySet = new TreeSet<String>();
    
    public Engine(Game game) {
		this.game = game;
		canvas = new Canvas();
		container = new JFrame(FRAME_NAME);
        panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(
                game.getWidth(),game.getHeight()));
        panel.setLayout(null);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Rectangle bounds = gs[gs.length-1].getDefaultConfiguration().getBounds();
//      setToFullScreen ();
// 	   	removeBoard();
        container.setResizable(false);
        container.setBounds(bounds.x+(bounds.width - game.getWidth())/2,
                bounds.y+(bounds.height - game.getHeight())/2,
                game.getWidth(),game.getHeight());
        canvas.setBounds(0,0,game.getWidth(),game.getHeight());
        panel.add(canvas);                 
        canvas.setIgnoreRepaint(true);
        container.pack();
        container.setVisible(true);
        try {
			container.setIconImage(ImageIO.read(new File("images/textures/icon.gif")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        addListeners();
        
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        game.setHeight(panel.getHeight());
        game.setWidth(panel.getWidth());
        mainLoop();
    }
    
    
    protected void setToFullScreen() {
    	container.setExtendedState(container.getExtendedState() | JFrame.MAXIMIZED_BOTH); // Para ficar com tela cheia
    }   
    protected void removeBoard() {
    	container.setUndecorated(true);
    }
	
	private void addListeners() {
        
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent evt) {
                keySet.add(keyString(evt));
            }
            @Override
            public void keyReleased(KeyEvent evt) {
                keySet.remove(keyString(evt));
            }
            @Override
            public void keyTyped(KeyEvent evt) {
                game.key(keyString(evt));
            }
        });           
        
       
        canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
//				System.out.println("CLICOU");
				game.mouse(mouseEvent);
				
			}
		});
        
        canvas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				game.move(mouseEvent);
				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
	}    	
	private static String keyString(KeyEvent evt) {
	        if(evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
	            return String.valueOf(evt.getKeyChar()).toLowerCase();
	        } else {
	            switch(evt.getKeyCode()) {
	            case KeyEvent.VK_ALT: return "alt";
	            case KeyEvent.VK_CONTROL: return "control";
	            case KeyEvent.VK_SHIFT: return "shift";
	            case KeyEvent.VK_LEFT: return "left";
	            case KeyEvent.VK_RIGHT: return "right";
	            case KeyEvent.VK_UP: return "up";
	            case KeyEvent.VK_DOWN: return "down";
	            case KeyEvent.VK_ENTER: return "enter";
	            case KeyEvent.VK_DELETE: return "delete";
	            case KeyEvent.VK_TAB: return "tab";
	            case KeyEvent.VK_WINDOWS: return "windows";
	            case KeyEvent.VK_BACK_SPACE: return "backspace";
	            case KeyEvent.VK_ALT_GRAPH: return "altgr";
	            case KeyEvent.VK_F1: return "F1";
	            case KeyEvent.VK_F2: return "F2";
	            case KeyEvent.VK_F3: return "F3";
	            case KeyEvent.VK_F4: return "F4";
	            case KeyEvent.VK_F5: return "F5";
	            case KeyEvent.VK_F6: return "F6";
	            case KeyEvent.VK_F7: return "F7";
	            case KeyEvent.VK_F8: return "F8";
	            case KeyEvent.VK_F9: return "F9";
	            case KeyEvent.VK_F10: return "F10";
	            case KeyEvent.VK_F11: return "F11";
	            case KeyEvent.VK_F12: return "F12";
	            default: return "";
	            }
	        }
	    }
	
	public int getWidth() {
		return panel.getWidth();
	}	
	public int getHeight() {
		return panel.getHeight();
	}
	
    private void mainLoop() {
        Timer t = new Timer(5, new ActionListener() {
            public long t0;
            public void actionPerformed(ActionEvent evt) {
                game.setHeight(panel.getHeight());
                game.setWidth(panel.getWidth());
            	
                long t1 = System.currentTimeMillis();
                if(t0 == 0)
                    t0 = t1;
                if(t1 > t0) {
                    double dt = (t1 - t0) / 1000.0;
                    t0 = t1;
                    game.tick(keySet, dt);     
                    Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
                    g.setColor(Color.black);
                    g.fillRect(0,0,game.getWidth(),
                          game.getHeight());
                    screen = new Screen(g);
                    game.draw(screen);
                    strategy.show();
                }
            }
        });
            
        t.start();
    }
	
}

