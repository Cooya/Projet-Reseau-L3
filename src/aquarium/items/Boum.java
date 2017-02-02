package aquarium.items;

public class Boum extends MortalFixedItem {
	private static final String img = "image/boum.png";

	public Boum(AquariumContent content, int width) {
		super(width, img, content);
	}
}