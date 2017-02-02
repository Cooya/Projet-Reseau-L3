package aquarium.items;


/**
 * Seaweed element
 */
public class Seaweed extends AquariumItem {

	private static final int MIN_WIDTH = 30, MAX_WIDTH = 100;
	private static String img = "image/seaweed.png";
	
	public Seaweed(AquariumContent content) {
		super(MIN_WIDTH, MAX_WIDTH, img, content);
	}	
}
