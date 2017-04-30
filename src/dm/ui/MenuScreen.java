/** 
* @author Simão 
* @version 0.1 - 28 de abr de 2017
* 
*/
package dm.ui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MenuScreen extends JPanel{
	private static final long serialVersionUID = 1L;
	
    private Graphics2D g2;
    private ImageIcon background;
    
    private final String background_image = "../images/";
    
    public MenuScreen()
    {  	
        setFocusable(true);
        background = getImage(background_image + "background.png");
        setLayout(new FlowLayout(FlowLayout.CENTER));

    }
    
        
//    public void run()
//    {
//    	int x=0;
//        while(true)
//        {
//            repaint();
//             try{Thread.sleep(30);}catch(Exception e){System.out.println("Erro na Thread");};
//             x++;
//             if(x>10000)
//            	 break;
//        }
//        //repaint();
//    }
    
    @Override
    public void paint (Graphics g)
    {
        super.paint(g);
        g2 = (Graphics2D)g;
        int h = getHeight();
        int w = getWidth();

        g2.drawImage(background.getImage(),0,0,w,h, this);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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
//    
//        private class TAdapter extends KeyAdapter {
//        // DEFINE O MÉTODO DE TECLA PRESSIONADA DO JOGADOR
//        @Override
//        public void keyPressed(KeyEvent e)
//        {
//            // O JOGADOR SE MOVIMENTA
//            if(e.getKeyCode()==38)
//            {
//                y-=1;
//                //System.out.println("Cima");
//            }
//            else
//            if(e.getKeyCode()==40)
//            {
//                y+=1;
//                //System.out.println("Baixo");
//            }
//            else
//            if(e.getKeyCode()==37)
//            {
//                x-=1;
//                //System.out.println("Esquerda");
//            }
//            else
//            if(e.getKeyCode()==39)
//            {
//                x+=1;
//                //System.out.println("Direita");
//            }
//        }
//    }
}
