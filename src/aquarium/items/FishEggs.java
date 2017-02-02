package aquarium.items;

public class FishEggs extends MortalFixedItem {
	private static final String img = "image/eggs_f.png";
	private static final int WIDTH = 25;
	
	public FishEggs(AquariumContent content) {
		super(WIDTH, img, content);
	}
}
