package aquarium.items;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AquariumFriends implements Iterable<Map.Entry<Integer, AquariumContent>> {

	public AquariumFriends() {
		content = new Hashtable<Integer, AquariumContent>();
	}
	
	public AquariumContent get(int id) {
		return content.get(id);
	}
	
	public AquariumFriends set(int id, AquariumContent val) {
		content.put(id,  val);
		return this;
	}
	
	public AquariumFriends remove(int id) {
		content.remove(id);
		return this;
	}
	
	public void clear() {
		content.clear();
	}
	
	public Iterator<Map.Entry<Integer, AquariumContent>> iterator() {
		Set<Map.Entry<Integer, AquariumContent>> entrySet = content.entrySet();
        return entrySet.iterator();
    }
	
	public Iterable<AquariumContent> values() {
		return new ValuesIterator(content.values());
    }
	
	public class ValuesIterator implements Iterable<AquariumContent> {
		public ValuesIterator(Collection<AquariumContent> values) {
			_values = values;
		}
		
		public Iterator<AquariumContent> iterator() {
			return _values.iterator();
		}
		
		private Collection<AquariumContent> _values;
	}
	
	private Hashtable<Integer, AquariumContent> content;
}
