package aquarium.items;
/*
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
*/
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Shark extends MobileItem {
	
	private static final int MIN_WIDTH = 70, MAX_WIDTH = 110;
	private static final String img_m = "image/shark_m.png";
	private static final String img_f = "image/shark_f.png";
	private AquariumItem target=null;
	
	public Shark(AquariumContent content) {
		super(MIN_WIDTH, MAX_WIDTH, img_m, img_f, content);
	}
	
	// constructeur pour les b�b�s
	public Shark(AquariumContent content, boolean minWidth) { // bool�en inutile, c'est juste pour �viter la duplication de constructeurs
		super(MIN_WIDTH, img_m, img_f, content);
	}
	
	public int getMaximalWidth() {
		return MAX_WIDTH;
	}
	
	public int getMinimalWidth(){
		return MIN_WIDTH;
	}
	
	protected void setNewTarget(AquariumItem a){
		target=a;
	}

	@Override
	public AquariumItem getNewTarget(AquariumContent neighbours) {
		
		Random randomizer = new Random();
		List<AquariumItem> selects = new ArrayList<AquariumItem>();
		
		for (Map.Entry<Integer, AquariumItem> item : neighbours)
			if (item.getValue() instanceof Fish)
				selects.add(item.getValue());
		
		if(selects.size()==0)
			for (Map.Entry<Integer, AquariumItem> item : neighbours)
				selects.add(item.getValue());			
		
		target = selects.get(randomizer.nextInt(selects.size()));
		return target;
		
		/*
		AquariumItem target = this;
		AquariumItem other;
		double minDistance = 999999;
		double currentDistance;
		for(Map.Entry<Integer, AquariumItem> otherItem : neighbours) {
			other = otherItem.getValue();
			if(other instanceof Fish) {
				currentDistance = Math.sqrt(Math.pow(other.getPosition().x - this.getPosition().x, 2) + Math.pow(other.getPosition().y - this.getPosition().y, 2));
				if(currentDistance < minDistance) {
					minDistance = currentDistance;
					target = other;
				}
			}
		}
		this.target = target;
		return target;	
		*/	
	}

	@Override
	public AquariumItem getCurrentTarget() {
		return target;
	}
}