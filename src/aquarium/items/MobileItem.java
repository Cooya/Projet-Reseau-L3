package aquarium.items;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.List;

import aquarium.util.RandomNumber;

/**
 * A MobileItem is an AquariumItem capable of changing its position (e.g., fish)
 */
public abstract class MobileItem extends AquariumItem {

	/**
	 * Destination point of the current movement
	 */
	private AquariumItem destination = null;
	private int age;
	private boolean gender;
	private boolean direction;
	private double angle;
	private int reproduction;
	
	public MobileItem(int minWidth, int maxWidth, String imagePathM, String imagePathF, AquariumContent content) {
		super(minWidth, maxWidth, true);
		age = 0;
		gender = getRandomGender();
		reproduction = 0;
		if(gender)
			createImage(imagePathF, content);
		else
			createImage(imagePathM, content);
	}
	
	public MobileItem(int width, String imagePathM, String imagePathF, AquariumContent content) {
		this(width, width, imagePathM, imagePathF, content);
	}

	/**
	 * @param neighbours
	 *            List of other Aquarium items in the Aquarium
	 * @return the position of one of the neighbours
	 */
	public abstract AquariumItem getNewTarget(AquariumContent neighbours);

	public abstract AquariumItem getCurrentTarget();

	/**
	 * @return the maximal width allowed for a MobileItem
	 */
	public abstract int getMaximalWidth();

	/**
	 * Makes a move towards the destination which is inversely proportional to
	 * the size of the Mobile Item
	 * 
	 * @param destination
	 *            Position to be reached
	 */
	private void moveToTarget() {
		double v = (double) getMaximalWidth() / (double) getWidth();
		int dx = (destination.getPosition().x - getPosition().x);
		int dy = (destination.getPosition().y - getPosition().y);
		double direction = (float) Math.atan2(dy, dx);
		if(v<0.5)
			v=0.5;
		double speed;
		if (this instanceof Shark)
			speed = 2.0 + v;
		else
			speed = 1.0 + v;
		int modx = (int) (speed * Math.cos(direction));
		int mody = (int) (speed * Math.sin(direction));
		Point p = getPosition();
		p.translate(modx, mody);
		setPosition(p);
		setDirection();
		computeAngle();
	}

	/**
	 * @return the current destination Point
	 */
	public AquariumItem getCurrentDestination() {
		return destination;
	}

	/**
	 * Moves to the current destination, if reached, change to another
	 * destination using {@link MobileItem#target(List)} method on neighbours
	 * parameter
	 * 
	 * @param neighbours
	 */
	public void move(AquariumContent neighbours) {
		destination = getCurrentTarget();
		if (neighbours.getKey(destination) == -1 || overlap(destination))
			destination = getNewTarget(neighbours);
		moveToTarget();
	}

	public void setDestination(AquariumItem destination) {
		this.destination = destination;
	}
	
	public void setAge(){
		age++;
	}
	
	public int getAge(){
		return age;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean getDirection(){
		return direction;
	}
	
	public void setDirection(){
		if (destination.getPosition().getX()>=getPosition().getX())
			direction=true;
		else
			direction=false;
	}
	
	public void computeAngle() {
		//double adj = destination.getPosition().x - position.x;
		double opp = destination.getPosition().y - position.y;
		double hyp = Math.sqrt(Math.pow(destination.getPosition().x - position.x,2) + Math.pow(destination.getPosition().y - position.y,2));
		//if(hyp != 0.)
			angle = Math.asin(opp/hyp);
		/*else
			angle = Math.acos(adj/hyp);*/
	}
	
	public boolean getRandomGender(){
		if(RandomNumber.randomValue(0,1)==1)
			return true;
		return false;
	}
	
	public boolean isFemale(){
		return gender;
	}
	
	public int getReproduction(){
		return reproduction;
	}
	
	public void incReproduction(){
		reproduction++;
	}
	
	public void setReproduction(){
		reproduction = 0;
	}
	
	public void drawImage(Graphics g) {
		float scale = getScale();
		AffineTransform at = new AffineTransform();
		if(!direction) {
			at.scale(-1, 1);
			at.translate(-position.x - width, position.y);
		}
		else
			at.translate(position.x, position.y);
		at.scale(scale, scale);
		//at.translate(position.x*(1/scale), position.y*(1/scale));
		at.rotate(angle, image.getWidth(null)/2, image.getHeight(null)/2);
		Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, at, null);
	}
}
