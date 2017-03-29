package game;

import java.awt.Image;

import javax.swing.ImageIcon;



public class Element {

	private Image image;
	private int x;
	private int y;
	private int height;
	private int width;
	private int proportion;
	private int rotation;
	private boolean mirrorX;
	private boolean mirrorY;
	private boolean visibility;
	private boolean destruction;
	
	public Element()
	{
		visibility = true;
		destruction = false;
	}
	
	public Element(String image_name)
	{
		try
		{
			setImage(new ImageIcon(getClass().getResource(image_name)).getImage());
		}catch(Exception e)
		{
			System.out.println("Imagem não encontrada");
		}
		visibility = true;
		destruction = false;
	}
	
	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public boolean isDestruction() {
		return destruction;
	}

	public void setDestruction(boolean destruction) {
		this.destruction = destruction;
	}

	public boolean isMirrorX() {
		return mirrorX;
	}
	public void setMirrorX(boolean mirrorX) {
		this.mirrorX = mirrorX;
	}
	public boolean isMirrorY() {
		return mirrorY;
	}
	public void setMirrorY(boolean mirrorY) {
		this.mirrorY = mirrorY;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		proportion = (int) (image.getHeight(null)*1.0/image.getWidth(null));
		this.image = image;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public int getProportion()
	{
		return proportion;
	}
	
}
