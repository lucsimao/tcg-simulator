package views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class Screen {
    Graphics2D g;
    
    public static HashMap<String, BufferedImage> sprites = new HashMap<>();
    
    public Screen(Graphics2D g) {
    	this.g= g;
        g.setColor(Color.white);
    }
    
    public void triangle(int x1, int y1,
            int x2, int y2, int x3, int y3, Color color) {
        g.setColor(color);
        g.fillPolygon(new int[] { x1, x2, x3 },
                new int[] { y1, y2, y3 },
                3);
    }

    public void circle(int cx, int cy, int raio, Color color) {
        g.setColor(color);
        g.fillOval(cx - raio, cy - raio, raio*2, raio*2);
    }

    public void circulo(double cx, double cy, int raio, Color color) {
        circulo((int)Math.round(cx),
                (int)Math.round(cy),
                raio, color);
    }

    public void square(int x, int y, int lado, Color color) {
        g.setColor(color);
        g.fillRect(x, y, lado, lado);
    }

    public void square(double x, double y, int lado, Color color) {
        square((int)Math.round(x), (int)Math.round(y),
                lado, color);
    }

    public void rectangle(int x, int y, int largura, int altura, Color color,float alpha) {
    	g.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g.setColor(color);
        g.fillRect(x, y, largura, altura);
    }

    public void rectangle(double x, double y, int largura, int altura, Color color,float alpha) {
        rectangle((int)Math.round(x), (int)Math.round(y),
                   largura, altura, color,alpha);
    }

    public void text(String text, int x, int y, int tamanho, Color color) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, tamanho));
        g.drawString(text, x, y);
    }
    
    public void text(String text, double x, double y, int tamanho, Color color) {
        text(text, (int)Math.round(x), (int)Math.round(y), tamanho, color);
    }
    
    public void textMultiLine(String text,int x, int y, int size,int maxWidth, Color color) {
    	String str = new String( "This is a very long message that i will attempt to split into several smaller sections that will all fit on the Canvas" );
    	str = text;
        g.setFont(new Font("Arial", Font.BOLD, size));
        g.setColor(color);
    	FontMetrics fm = g.getFontMetrics();
//    	int ypos = fm.getLeading() + fm.getAscent(); // the initial y position to draw the String at
    	int ypos = y; // the initial y position to draw the String at
    	StringTokenizer st = new StringTokenizer( str ); // Split sentence into words
    	StringBuffer oneLine = new StringBuffer();
    	while( st.hasMoreTokens() )// while there are words left
    	{  String word = st.nextToken(); // get the next word
    	   if( word != null ) // error checking
    	   {  if( fm.stringWidth( oneLine.toString() + word ) < maxWidth )// is the width of the line i have + the next word small enough to fit on the canvas
    	      {  oneLine.append( word + " " ); //append word to current line
    	      }
    	      else
    	      {  g.drawString( oneLine.toString(), x, ypos ); // draw current line
    	         oneLine = new StringBuffer( word + " " ); //start a new line with the current word
    	         ypos += fm.getHeight(); // increment the y position for the drawing of the new line
    	      }
    	   }
    	}
    	if( oneLine.length() > 0 ) // if there is more text in the current line
    	{  g.drawString( oneLine.toString(), x, ypos ); // draw it!
    	}
    }
    
    public void image(String arquivo, int xa, int ya, int larg, int alt, double dir, double x, double y, float alpha) {
        if(!sprites.containsKey(arquivo)) {
            try {
                sprites.put(arquivo, ImageIO.read(new File(arquivo)));
            } catch(java.io.IOException ioex) {
                throw new RuntimeException(ioex);
            }
        }
    	AffineTransform trans = g.getTransform();
    	g.setComposite(AlphaComposite.SrcOver.derive(alpha));
    	   g.drawImage(sprites.get(arquivo), (int)Math.round(x), (int)Math.round(y), (int)Math.round(x) + larg, (int)Math.round(y) + alt,
                   xa, ya, xa + larg, ya + alt, null);
        g.setTransform(trans);
    }
        
    public void imageScaled(String arquivo, int xa, int ya, int larg, int alt, double dir, double x, double y, float alpha) {
        if(!sprites.containsKey(arquivo)) {
            try {
                sprites.put(arquivo, ImageIO.read(new File(arquivo)));
            } catch(java.io.IOException ioex) {
            	System.err.println("READ ERROR:" + arquivo);
                throw new RuntimeException(ioex);
            }
        }
        BufferedImage image = sprites.get(arquivo);
    	AffineTransform trans = g.getTransform();
    	ImageManip imageManip = new ImageManip();
    	image = imageManip.scaleTransform(image, larg*1.0/image.getWidth(),alt*1.0/image.getHeight());
    	g.rotate(dir, x + larg/2, y + alt/2);
    	g.setComposite(AlphaComposite.SrcOver.derive(alpha));
    	g.drawImage(image,(int)Math.round(x),(int)Math.round(y),null);
        g.setTransform(trans);
    }
    
    public void imageScaledPerspective(String arquivo, int xa, int ya, int larg, int alt, double dir, double x, double y) {
        if(!sprites.containsKey(arquivo)) {
            try {
                sprites.put(arquivo, ImageIO.read(new File(arquivo)));
            } catch(java.io.IOException ioex) {
            	System.err.println("READ ERROR : " + arquivo);
                throw new RuntimeException(ioex);
            }
        }
        ImageManip imageManip = new ImageManip();
        
        BufferedImage image = sprites.get(arquivo);
    	AffineTransform trans = g.getTransform();
	    trans.scale(larg*1.0/image.getWidth(),alt*1.0/image.getHeight());
	    image = new AffineTransformOp(trans, AffineTransformOp.TYPE_BICUBIC).filter(image, null);
//    	image = trans.scaleImage(image,x*1.0/image.getWidth() , y*1.0/image.getHeight(),AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    image = imageManip.perspectiveTransform(image,6, image.getWidth(), image.getHeight());
        g.rotate(dir, x + larg/2, y + alt/2);
        g.drawImage(image, (int)Math.round(x), (int)Math.round(y), (int)Math.round(x) + larg, (int)Math.round(y) + alt,
                    xa, ya, xa + larg, ya + alt, null);
        g.setTransform(trans);
    }
    

    public int getStringWidth(String text) {
    	 FontMetrics metrics = g.getFontMetrics(g.getFont());
    	 return metrics.stringWidth(text);
    }
    
    public int getStringHeight() {
   	 FontMetrics metrics = g.getFontMetrics(g.getFont());
   	 return metrics.getHeight();
   }
    public int getStringAscent() {
    	FontMetrics metrics = g.getFontMetrics(g.getFont());
    	return  metrics.getAscent();
    }
}
