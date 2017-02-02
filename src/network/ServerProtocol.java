package network;

import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;

public interface ServerProtocol extends Protocol {
	
	public ServerProtocol addItem(int id, AquariumItem item);
	public ServerProtocol alterItemPos(int id, MobileItem item);
	public ServerProtocol alterItemWidth(int id, MobileItem item);
	public ServerProtocol removeItem(int id);
}
