package aquarium.items;

public class MortalFixedItem extends AquariumItem {
	private int age;
	
	public MortalFixedItem(int width, String image, AquariumContent content) {
		super(width, image, content);
		age = 0;
	}
	
	public void incAge() {
		age++;
	}

	public int getAge() {
		return age;
	}
}
