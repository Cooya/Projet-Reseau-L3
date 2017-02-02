package aquarium.gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.items.AquariumContent;
import aquarium.items.AquariumItem;
import aquarium.items.Blood;
import aquarium.items.Boum;
import aquarium.items.Fish;
import aquarium.items.FishEggs;
import aquarium.items.MobileItem;
import aquarium.items.MortalFixedItem;
import aquarium.items.Seastone;
import aquarium.items.Shark;
import aquarium.items.SharkEggs;


public class AI {
	private Aquarium aquarium;
	private AquariumContent mine;
	
	public AI(Aquarium a){
		aquarium = a;
		mine = aquarium.getMine();
		additionFishsLoop(mine);
	}
	
	public void execute() {
		AquariumItem ai;
		for(Map.Entry<Integer, AquariumItem> item : mine) {
			ai = item.getValue();
			if(ai instanceof MobileItem) {
				if(AI_mobileItem(item))
					return;
			}
			else if(ai instanceof Boum) {
				if(AI_boum(item))
					return;
			}
			else if(ai instanceof Blood) {
				if(AI_blood(item))
					return;
			}
			else if(ai instanceof FishEggs || ai instanceof SharkEggs) {
				if(AI_eggs(item))
					return;
			}
		}		
	}
	
	public boolean AI_mobileItem(Map.Entry<Integer, AquariumItem> item) {
		MobileItem mi = (MobileItem) item.getValue();
		mi.setAge();
		if(mi.isFemale()) { // si c'est une femelle
			mi.incReproduction();
			if (mi.getReproduction() > 500) { // et qu'elle est fï¿½conde
				makeEggs(item);
				mi.setReproduction();
				return true;
			}
		}
		if(mi instanceof Shark)
			return AI_shark(item);
		else if(mi instanceof Fish)
			return AI_fish(item);
		else
			return false;
	}

	public boolean AI_shark(Map.Entry<Integer, AquariumItem> item) {
		AquariumItem ai = item.getValue();
		MobileItem mi = (MobileItem) ai;
		AquariumItem other;
		ArrayList<Map.Entry<Integer, AquariumItem>> al = ai.getCollision(mine);
		for(Map.Entry<Integer, AquariumItem> otherItem : al) {
			other = otherItem.getValue();
			if(other instanceof Fish && mi.getCurrentTarget() == other) { // le requin atteint sa cible
				sharkAttack(item.getKey(), otherItem.getKey());
				return true;
			}
			if(other instanceof SharkEggs && !mi.isFemale()) { // collision avec des oeufs
				babyBirth(ai, otherItem);
				return true;
			}
			if(mi.getAge() > 2000) { // requin trop âgé
				itemDeath(item.getKey());
				return true;
			}
		}
		return false; 
	}

	public boolean AI_fish(Map.Entry<Integer, AquariumItem> item) {
		AquariumItem other;
		AquariumItem ai = item.getValue();
		ArrayList<Map.Entry<Integer, AquariumItem>> al = item.getValue().getCollision(mine);
		for(Map.Entry<Integer, AquariumItem> otherItem : al) {
			other = otherItem.getValue();
			if(other instanceof Seastone) { // collision avec une pierre
				itemDeath(item.getKey());
				return true;
			}
			if(other instanceof FishEggs && !((MobileItem) ai).isFemale()) { // collision avec des oeufs
				babyBirth(ai, otherItem);
				return true;
			}
		}
		return false;
	}
	
	public boolean AI_boum(Map.Entry<Integer, AquariumItem> item) {
		MortalFixedItem mfi = (MortalFixedItem) item.getValue();
		mfi.incAge();
		if(mfi.getAge() > 30) { // fin du boum (pas de fï¿½condation)
			aquarium.removeItem(item.getKey());
			return true;
		}
		return false;
	}
	
	public boolean AI_blood(Map.Entry<Integer, AquariumItem> item) {
		MortalFixedItem mfi = (MortalFixedItem) item.getValue();
		mfi.incAge();
		if(mfi.getAge() > 100) { // fin du sang
			aquarium.removeItem(item.getKey());
			return true;
		}
		return false;
	}
	
	public boolean AI_eggs(Map.Entry<Integer, AquariumItem> item) {
		MortalFixedItem mfi = (MortalFixedItem) item.getValue();
		mfi.incAge();
		if(mfi.getAge() > 500) { // mort des oeufs
			itemDeath(item.getKey());
			return true;
		}
		return false;
	}

	public void additionFishsLoop(final AquariumContent mine) {
		ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
		e.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				synchronized (aquarium.getLockContent()) {
					AquariumItem ai = new Fish(mine);
					if (ai.sink(mine)){
						aquarium.addItem(ai);
						//System.out.println("Naissance d'un poisson.");
					}
				}
			}
		}, 0, 500, TimeUnit.MILLISECONDS);
	}
	
	public void makeEggs(Map.Entry<Integer, AquariumItem> motherItem) {
		//System.out.println("Ponte d'oeufs.");
		AquariumItem mother = motherItem.getValue();
		AquariumItem ai;
		if(mother instanceof Fish)
			ai = new FishEggs(mine);
		else if(mother instanceof Shark)
			ai = new SharkEggs(mine);
		else
			return;
		ai.sink(mine.get(motherItem.getKey()), 0, -10);
		aquarium.addItem(ai);
	}

	public void babyBirth(AquariumItem father, Map.Entry<Integer, AquariumItem> eggs) {
		//System.out.println("Accouplement et naissance d'un bï¿½bï¿½.");
		AquariumItem ai;
		if(father instanceof Shark)
			ai = new Shark(mine, true);
		else if(father instanceof Fish)
			ai = new Fish(mine, true);
		else
			return;
		ai.sink(eggs.getValue());
		aquarium.removeItem(eggs.getKey());
		aquarium.addItem(ai);
	}

	public void sharkAttack(int idShark, int idFish) {
		//System.out.println("Poisson mangï¿½ par un requin.");
		AquariumItem ai = new Blood(mine, mine.get(idFish).getWidth());
		ai.sink((AquariumItem) mine.get(idFish));
		aquarium.addItem(ai);
		aquarium.removeItem(idFish);
		mine.get(idShark).setWidth(1.02f, mine);
		if(aquarium.getServer() != null)
			aquarium.getServer().itemWidthAltered(idShark, (MobileItem)mine.get(idShark));
	}
	
	public void itemDeath(int id) {
		//System.out.println("Mort d'un item.");
		AquariumItem ai = new Boum(mine, mine.get(id).getWidth());
		ai.sink((AquariumItem) mine.get(id));
		aquarium.addItem(ai);
		aquarium.removeItem(id);
	}
}
