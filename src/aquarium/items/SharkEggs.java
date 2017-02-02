package aquarium.items;

public class SharkEggs extends MortalFixedItem {
	private static final String img = "image/eggs_s.png";
	private static final int WIDTH = 30;
	
	public SharkEggs(AquariumContent content) {
		super(WIDTH, img, content);
	}
}
