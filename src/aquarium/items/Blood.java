package aquarium.items;

public class Blood extends MortalFixedItem {
	private static final String img = "image/blood.png";

	public Blood(AquariumContent content, int width) {
		super(width - 20, img, content);
	}
}
