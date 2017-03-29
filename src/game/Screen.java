package game;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Screen extends JPanel implements Runnable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Thread animator;
    private Graphics2D g2;
    
    private int i=0;
    private int x=100,y=100;
    
    private ImageIcon background;
    private Element croupier;
    
    private final String background_image = "/sprites/";
    
    public Screen()
    {  	
        addKeyListener(new TAdapter());
        setFocusable(true);
        background = getImage(background_image + "mesa.png");
        
        croupier = new Element(background_image + "croupier.png");
        
        init();     
        
        new Thread(new Runnable()
        {
            @Override
			public void run()
            {
                while(true){
                try{Thread.sleep(1000);}catch(Exception e){System.out.println("Erro na Thread");};
                i++;
                if(i>1000)
                break;}
            }
        }).start();  
    }
    
    public void init()
    {
        if (animator == null)
        {
            animator = new Thread(this);
            animator.start();
        }
    }
    
    @Override
    public void addNotify()
    {
        super.addNotify();
        init();
    }
    
    @Override
	public void run()
    {
    	int x=0;
        while(true)
        {
            repaint();
             try{Thread.sleep(30);}catch(Exception e){System.out.println("Erro na Thread");};
             x++;
             if(x>10000)
            	 break;
        }
        //repaint();
    }
    
    @Override
    public void paint (Graphics g)
    {
        super.paint(g);
        g2 = (Graphics2D)g;
        double prop = background.getIconHeight()*1.0/background.getIconWidth();
        int h = getHeight()*5/9;
        int w = (int) (h/prop);
        //System.out.println("H: " + background.getIconHeight() + " w: " + background.getIconWidth() + "Prop : " + (double)prop);
        
//        g2.drawImage(croupier.getImage(),525,0,croupier.getIconWidth()/4,croupier.getIconHeight()/4,this);
        croupier.setHeight((int)1.0*getHeight()*3/6);
        croupier.setWidth(croupier.getHeight()/croupier.getProportion());
        
        g2.drawImage(croupier.getImage(),x,y,croupier.getHeight(),croupier.getWidth(),this);
        g2.drawImage(background.getImage(),(getWidth()-w)/2,(getHeight()-h)/2 + 100,w,h, this);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawString(i + " ",x,y);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
 
    public ImageIcon getImage(String path)
    {
    	ImageIcon image;
    	try 
    	{
			image = new ImageIcon(getClass().getResource(path)); 
		} catch (Exception e) {
			image = new ImageIcon();
		}
		return image; 
    }
    
        private class TAdapter extends KeyAdapter {
        // DEFINE O MÉTODO DE TECLA PRESSIONADA DO JOGADOR
        @Override
        public void keyPressed(KeyEvent e)
        {
            // O JOGADOR SE MOVIMENTA
            if(e.getKeyCode()==38)
            {
                y-=1;
                //System.out.println("Cima");
            }
            else
            if(e.getKeyCode()==40)
            {
                y+=1;
                //System.out.println("Baixo");
            }
            else
            if(e.getKeyCode()==37)
            {
                x-=1;
                //System.out.println("Esquerda");
            }
            else
            if(e.getKeyCode()==39)
            {
                x+=1;
                //System.out.println("Direita");
            }
        }
    }
}
