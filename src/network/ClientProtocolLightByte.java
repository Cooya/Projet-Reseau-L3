package network;

import java.awt.Point;
import java.nio.ByteBuffer;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumContent;
import aquarium.items.NetworkItem;
import aquarium.util.ByteBuff;

public class ClientProtocolLightByte extends ProtocolLightByte implements ClientProtocol {

	public ClientProtocolLightByte(Aquarium aquarium, Client client) {
		super();
		_aquarium = aquarium;
		friendId = friendCounter++;
		content = new AquariumContent();
		aquarium.setContent(friendId, content);
		this.client = client;
	}
	
	@Override
	protected boolean fetchMessage(PacketReader reader, int cmd) {
		int id, width, imageId, posx, posy, length;
		byte[] bytes;
		boolean dir;
		double angle;
		switch (cmd) {
		case ADD:
			id = reader.getInt();
			width = reader.getInt();
			imageId = reader.getInt();
			posx = reader.getInt();
			posy = reader.getInt();
			
			//System.out.println("    Content: " + id + " " + " " + width + " " + imageId + " " + posx + " " + posy);
			 
			synchronized (_aquarium.getLockContent()) {
				if (!content.existsImage(imageId) && !content.isAskedImage(imageId)) {
					askImage(imageId);
					client.write();
					content.setAskedImage(imageId);
				}
				content.set(id, new NetworkItem(width, imageId, posx, posy));
			}
			break;
		case ALTERPOS:
			id = reader.getInt();
			posx = reader.getInt();
			posy = reader.getInt();
			dir = reader.getBool();
			angle = reader.getDouble();
			/*
			 * System.out.println("    Content: " + id + " " + posx
			 * + " " + posy);
			 */
			synchronized (_aquarium.getLockContent()) {
				if (content.exists(id)) {
					NetworkItem item = (NetworkItem)content.get(id);
					item.setPosition(new Point(posx, posy));
					item.setDirection(dir);
					item.setAngle(angle);
				}
			}
			break;
		case ALTERWIDTH:
			id = reader.getInt();
			width = reader.getInt();
			/*
			 * System.out.println("    Content: " + id + " " + posx
			 * + " " + posy);
			 */
			synchronized (_aquarium.getLockContent()) {
				if (content.exists(id))
					content.get(id).setWidth(width, content);
			}
			break;
		case REMOVE:
			id = reader.getInt();
			/*
			 * System.out.println("    Content: " + id + " " + posx
			 * + " " + posy);
			 */
			synchronized (_aquarium.getLockContent()) {
				if (content.exists(id))
					content.remove(id);
			}
			break;
		case IMAGE:
			id = reader.getInt();
			length = reader.getInt();
			bytes = reader.getNext(length);
			content.loadImage(id, bytes);
			System.out.println("    Image :" + id);
			break;
		default:
			return false;
		}
		
		return true;
	}
	
	@Override
	public ClientProtocol hello() {
		synchronized(outBuffLock) {
			prepare(HELLO, 0);
		}
		return this;
	}
	
	@Override
	public ClientProtocol askImage(int imageId) {
		synchronized(outBuffLock) {
			prepare(ASKIMAGE, 1 * INTSIZE);
			outBuffer.append(imageId);
		}
		return this;
	}
	
	@Override
	public void delete() {
		synchronized (_aquarium.getLockFriends()) {
			_aquarium.removeContent(friendId);
		}
	}

	private Aquarium _aquarium;
	private static int friendCounter = 0;
	private int friendId;
	private AquariumContent content;
	private Client client;
}
