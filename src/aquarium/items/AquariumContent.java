package aquarium.items;

import java.awt.Image;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AquariumContent implements Iterable<Map.Entry<Integer, AquariumItem>> {

	public AquariumContent() {
		content = new Hashtable<Integer, AquariumItem>();
		images = new ImageBank();
	}
	
	public AquariumItem get(int id) {
		AquariumItem ai = content.get(id);
		return ai;
	}
	
	public int getKey(AquariumItem ai) {
		Iterator<Map.Entry<Integer, AquariumItem>> it = iterator();
		Map.Entry<Integer, AquariumItem> current;
		while(it.hasNext()) {
			current = it.next();
			if(current.getValue() == ai)
				return current.getKey();
		}
		return -1;
	}
	
	public AquariumContent set(int id, AquariumItem val) {
		content.put(id,  val);
		if(id>=counter)
			counter = id+1;
		return this;
	}
	
	public int add(AquariumItem val) {
		int id = counter++;
		content.put(id,  val);
		return id;
	}
	
	public AquariumContent remove(int id) {
		content.remove(id);
		return this;
	}
	
	public void clear() {
		content.clear();
	}
	
	public boolean exists(int id) {
		return content.containsKey(id);
	}
	
	public Iterator<Map.Entry<Integer, AquariumItem>> iterator() {
		Set<Map.Entry<Integer, AquariumItem>> entrySet = content.entrySet();
        return entrySet.iterator();
    }
	
	public Iterable<AquariumItem> values() {
		return new ValuesIterator(content.values());
    }
	
	public Iterable<Integer> keys() {
		return new KeysIterator(content.keySet());
    }
	
	public class ValuesIterator implements Iterable<AquariumItem> {
		public ValuesIterator(Collection<AquariumItem> values) {
			_values = values;
		}
		
		public Iterator<AquariumItem> iterator() {
			return _values.iterator();
		}
		
		private Collection<AquariumItem> _values;
	}
	
	public class KeysIterator implements Iterable<Integer> {
		public KeysIterator(Set<Integer> keys) {
			_keys = keys;
		}
		
		public Iterator<Integer> iterator() {
			return _keys.iterator();
		}
		
		private Set<Integer> _keys;
	}
	
	
	public Image getImage(int id) {
		return images.get(id);
	}
	
	public boolean existsImage(int id) {
		return images.exists(id);
	}
	
	public int loadImage(String filename) {
		return images.load(filename);
	}
	
	public int loadImage(int id, byte[] data) {
		return images.load(id, data);
	}
	
	public void setAskedImage(int id) {
		images.setAsked(id);
	}
	
	public boolean isAskedImage(int id) {
		return images.isAsked(id);
	}
	
	public String getFilenameImage(int id) {
		return images.getFilename(id);
	}
	
	private Hashtable<Integer, AquariumItem> content;
	private ImageBank images;
	private int counter = 0;
}
