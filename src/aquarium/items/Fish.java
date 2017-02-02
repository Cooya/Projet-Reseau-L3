package aquarium.items;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fish extends MobileItem {
	
	private static final int MIN_WIDTH = 50, MAX_WIDTH = 70;
	private static final String img_m = "image/fish_m.png";
	private static final String img_f = "image/fish_f.png";
	private AquariumItem target=null;
	
	public Fish(AquariumContent content) {
		super(MIN_WIDTH, MAX_WIDTH, img_m, img_f, content);
	}
	
	// constructeur pour les bébés
	public Fish(AquariumContent content, boolean minWidth) { // booléen inutile, c'est juste pour éviter la duplication de constructeurs
		super(MIN_WIDTH, img_m, img_f, content);
	}
	
	public int getMaximalWidth() {
		return MAX_WIDTH;
	}
	
	protected void setNewTarget(AquariumItem a){
		target=a;
	}

	@Override
	public AquariumItem getNewTarget(AquariumContent neighbours) {
		Random randomizer = new Random();
		List<AquariumItem> selects = new ArrayList<AquariumItem>();
		
		for (Map.Entry<Integer, AquariumItem> item : neighbours)
			if (item.getValue() instanceof Seaweed)
				selects.add(item.getValue());
		
		target= selects.get(randomizer.nextInt(selects.size()));
		return target;
	}

	@Override
	public AquariumItem getCurrentTarget() {
		return target;
	}
}