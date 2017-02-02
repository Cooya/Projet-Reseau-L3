package aquarium.items;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class NetworkItem extends AquariumItem {
	//private static final int MIN_WIDTH = 30, MAX_WIDTH = 60;
	//private static final String img = "image/shark.png";
	
	boolean direction;
	double angle;

	public NetworkItem(int width, int imageId, int posx, int posy) {
		super(width, imageId);
		//super(width, "image/shark.png", content);
		setPosition(new Point(posx, posy));
		direction = true;
		angle = 0.;
	}
	
	public void drawImage(Graphics g) {
		float scale = getScale();
		AffineTransform at = new AffineTransform();
		if(!direction) {
			at.scale(-1, 1);
			at.translate(-width, 0);
			at.translate(-position.x, position.y);
		}
		else
			at.translate(position.x, position.y);
		at.scale(scale, scale);
		//at.translate(position.x*(1/scale), position.y*(1/scale));
		at.rotate(angle, image.getWidth(null)/2, image.getHeight(null)/2);
		Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, at, null);
        
		
		/*if(this instanceof MobileItem && !((MobileItem) this).getDirection())
			g.drawImage(image, position.x+width, position.y, -width, height, null);
		else
			g.drawImage(image, position.x, position.y, width, height, null);*/
		
	}

	public boolean isDirection() {
		return direction;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}
