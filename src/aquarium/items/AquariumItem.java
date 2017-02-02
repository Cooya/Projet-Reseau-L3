package aquarium.items;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;

import aquarium.gui.Aquarium;
import aquarium.util.RandomNumber;

/**
 * An AquariumItem is a visual component to be displayed in the Aquarium (e.g.,
 * stone, seaweed, fish). The class is abstract meaning that an AquariumItem has
 * to be extended in order to be instantiated.
 */
public abstract class AquariumItem {

	/**
	 * Image representation of the AquariumItem
	 */
	protected Image image;
	private int imageId;

	/**
	 * Current position of the Aquarium Item in the Aquarium
	 */
	protected Point position = new Point(0, 0);

	/**
	 * displayed width of the Aquarium Item
	 */
	protected int width;
	/**
	 * displayed height of the Aquarium Item
	 */
	protected int height;

	/**
	 * Constructs an Aquarium Item of random width between minWidth and maxWidth
	 * using the image at imagePath
	 * 
	 * @param minWidth
	 * @param maxWidth
	 * @param imagePath
	 */
	
	public AquariumItem(int minWidth, int maxWidth, String imagePath, AquariumContent content) { // utilis� par les classes Seastone et Seaweed
		this(RandomNumber.randomValue(minWidth, maxWidth), imagePath, content);
	}
	
	public AquariumItem(int width, int imgId) { // utilis� par la classe NetworkItem
		this.imageId = imgId;
		this.image = null;
		this.width = width;
	}
	
	public AquariumItem(int width, String imagePath, AquariumContent content) { // utilis� par la classe MortalFixedItem
		this.width = width;
		createImage(imagePath, content);
	}
	
	// utilis� par la classe MobileItem
	public AquariumItem(int minWidth, int maxWidth, boolean b) { // bool�en inutile, c'est juste pour �viter la duplication de constructeurs
		width = RandomNumber.randomValue(minWidth, maxWidth);
	}
	
	protected void createImage(String imagePath, AquariumContent content) {
		imageId = content.loadImage(imagePath);
		image = content.getImage(imageId);
		createIcon();
	}
	
	/*public boolean setImage(int id, AquariumContent content) {
		if(content.existsImage(id)) {
			imageId = id;
			image = content.getImage(imageId);
			createIcon();
			return true;
		}
		else
			return false;
	}*/
	
	private void createIcon() {
		ImageIcon icon = new ImageIcon(image);
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		double ratio = width / (w * 1.0);
		this.width = (int) (w * ratio);
		this.height = (int) (h * ratio);
	}
	
	public int getImageId() {
		return imageId;
	}

	/**
	 * Draw the Aquarium Item into the graphic component g at its current
	 * position
	 * 
	 * @param g
	 */
	
	public void draw(Graphics g, AquariumContent content) {
		if(image == null) {
			if(imageId!=-1 && content.existsImage(imageId)) {
				image = content.getImage(imageId);
				createIcon();
				drawImage(g);
			}
		}
		else
			drawImage(g);
	}

	public void drawImage(Graphics g) {
		g.drawImage(image, position.x, position.y, width, height, null);
	}
	
	public float getScale() {
		int w = image.getWidth(null);
		float ratio = width / (float) w;
		return ratio;
	}

	/**
	 * Update the Aquarium Item position to p
	 * 
	 * @param p
	 */
	public void setPosition(Point p) {
		position = p;
	}

	/**
	 * @return the current Position of the Aquarium Item
	 */
	public Point getPosition() {
		return new Point(position);
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(float val, AquariumContent content){
		setWidth((int) (width*val), content);
	}
	
	public void setWidth(int val, AquariumContent content){
		width=val;
		if(imageId!=-1 && content.existsImage(imageId))
			createIcon();
	}

	/**
	 * @return a Rectangle representing the outline of the Aquarium Item
	 */
	public Rectangle outline() {
		return new Rectangle(position.x, position.y, width, height);
	}

	/**
	 * @param items
	 *            a List of Aquarium items
	 * @return true if at least one Aquarium items outline is intersecting the
	 *         outline of this
	 */
	public boolean intersects(AquariumContent items) {
		for (AquariumItem ai : items.values()) {
			if (ai.outline().intersects(outline()))
				return true;
		}
		return false;
	}

	public boolean overlap(AquariumItem item) {
		Rectangle intersection = outline().intersection(item.outline());
		return ((intersection.width > outline().width / 2) || (intersection.width > item
				.outline().width / 2))
				&& ((intersection.height > outline().height / 2) || (intersection.height > item
						.outline().height / 2));
	}

	/**
	 * Will position randomly the AquariumItem (this) without overlapping an
	 * already positioned Aquarium item
	 * 
	 * @param items
	 *            Already positioned Aquarium items
	 * @return true if the AquariumItem was able to be positioned without
	 *         conflicts after 20 tries
	 */
	public boolean sink(AquariumContent items) {
		int nbTries = 0;
		while (this.intersects(items)) {
			this.setPosition(RandomNumber.randomPoint(0, Aquarium.getSizeX()
					- this.width, 0, Aquarium.getSizeY() - this.height));
			nbTries++;
			if (nbTries > 100)
				return false;
		}
		return true;
	}
	
	public void sink(AquariumItem item){
		this.setPosition(item.position);
	}
	
	public void sink(AquariumItem item, int diffX, int diffY) {
		Point p = new Point((int) item.position.getX() + diffX, (int) item.position.getY() + diffY);
		this.setPosition(p);
	}
	
	public ArrayList<Map.Entry<Integer, AquariumItem>> getCollision(AquariumContent neighbours) {
		ArrayList<Map.Entry<Integer, AquariumItem>> al = new ArrayList<Map.Entry<Integer, AquariumItem>>();
		AquariumItem other;
		for(Map.Entry<Integer, AquariumItem> item : neighbours) {
			other = item.getValue();
			if(this != other && this.outline().intersects(other.outline()))
				al.add(item);
		}
		return al;
	}
}
