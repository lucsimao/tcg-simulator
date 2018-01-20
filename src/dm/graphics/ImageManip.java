package dm.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javaxt.io.Image;

public class ImageManip {
	private AffineTransform affineTransform;
	
	public ImageManip() {
		affineTransform = new AffineTransform();
	}
	
	public AffineTransform getAffineTransform() {
		return affineTransform;
	}

	public void setAffineTransform(AffineTransform affineTransform) {
		this.affineTransform = affineTransform;
	}

	/**
	 * Método que cria uma imagem em perspectiva, parecido com 3d.
	 * @param scalePersc escala que será distorcida em perspectiva
	 * @param f_width largura final da imagem.
	 * @param f_height altura final da imagem.
	 * */
	public BufferedImage perspectiveTransform(BufferedImage img,int scalePersc,int f_width,int f_height){
		Image image = new Image(img);
		int width = image.getWidth();
		int height = image.getHeight();
		image.setCorners(width/scalePersc, 0,              //UL
					width- width/scalePersc,0,         //UR
		                 width, height, //LR
		                 0, height);         //LL
		image.resize(f_width,f_height);
		return image.getBufferedImage();
		
	}
	
	public BufferedImage scaleTransform(BufferedImage image, double scale_X,double scale_Y) {
	    affineTransform.scale(scale_X,scale_Y);
	    image = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);
	    return image;
	}
	
	public BufferedImage extendTransform(BufferedImage image, double width,double height) {
	    affineTransform.scale(width*1.0/image.getWidth(),height*1.0/image.getHeight());
	    image = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);
	    return image;
	}
	
}
