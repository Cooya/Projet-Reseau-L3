package aquarium.items;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.Map;

public class ImageBank {
	
	public ImageBank() {
		content = new Hashtable<Integer, Image>();
		asked = new Hashtable<Integer, Boolean>();
		filenames = new Hashtable<Integer, String>();
		counter = 0;
	}
	
	public Image get(int id) {
		return content.get(id);
	}
	
	public boolean exists(int id) {
		return content.containsKey(id);
	}
	
	public int load(String filename) {
		int id = -1;
		
		/*for(int i=0; i<filenames.size(); i++)
			if(filenames.get(i).equals(filename)) {
				id = i;
				break;
			}*/
		for(Map.Entry<Integer, String> entry : filenames.entrySet())
			if(entry.getValue().equals(filename)) {
				id = entry.getKey();
				break;
			}
		
		if(id == -1) {
			id = counter++;
			Image img = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource(filename));
			content.put(id, img);
			filenames.put(id, filename);
		}
		
		return id;
	}
	
	public int load(int id, byte[] data) {
		//int id = counter++;
		Image img = Toolkit.getDefaultToolkit().createImage(data);
		content.put(id, img);
		return id;
	}
	
	public void setAsked(int id) {
		asked.put(id, true);
	}
	
	public boolean isAsked(int id) {
		return asked.containsKey(id) && asked.get(id);
	}
	
	public String getFilename(int id) {
		return filenames.get(id);
	}
	
	private Hashtable<Integer, Image> content;
	private Hashtable<Integer, Boolean> asked;
	//private List<String> filenames;
	private Hashtable<Integer, String> filenames;
	private int counter;
}
