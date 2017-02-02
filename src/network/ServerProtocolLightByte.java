package network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumContent;
import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;
import aquarium.util.ByteBuff;

public class ServerProtocolLightByte extends ProtocolLightByte implements ServerProtocol {
	
	public ServerProtocolLightByte() {
		super();
		_aquarium = null;
	}
	
	public ServerProtocolLightByte(Aquarium aquarium/*, SocketAddress addr*/) {
		//address = addr;
		this();
		_aquarium = aquarium;
	}
	
	protected boolean fetchMessage(PacketReader reader, int cmd) {
		int imageId;
		switch (cmd) {
		case HELLO :
			sendItems();
			break;
		case ASKIMAGE :
			imageId = reader.getInt();
			sendImage(imageId);
			break;
		default:
			return false;
		}
		
		return true;
	}
	
	public ServerProtocol sendItems() {
		AquariumContent mine = _aquarium.getMine();
		
		synchronized(_aquarium.getLockContent()) {
			for(Map.Entry<Integer, AquariumItem> item : mine)
				addItem(item.getKey(), item.getValue());
		}
		
		return this;
	}
	
	/*public ServerProtocol alterItems() {
		AquariumContent mine = _aquarium.getMine();
		for(Map.Entry<Integer, AquariumItem> item : mine) {
			if(item.getValue() instanceof MobileItem)
				alterItem(item.getKey(), item.getValue());
		}
		
		return this;
	}*/
	
	public ServerProtocol addItem(int id, AquariumItem item) {
		synchronized(outBuffLock) {
			prepare(ADD, 5*INTSIZE);
			outBuffer.append(id).append(item.getWidth()).append(item.getImageId()).append(item.getPosition().x).append(item.getPosition().y);
		}
		return this;
	}
	
	public ServerProtocol alterItemPos(int id, MobileItem item) {
		synchronized(outBuffLock) {
			prepare(ALTERPOS, 3*INTSIZE+BOOLSIZE+DOUBLESIZE);
			outBuffer.append(id).append(item.getPosition().x).append(item.getPosition().y).append(item.getDirection()).append(item.getAngle());
		}
		return this;
	}
	
	public ServerProtocol alterItemWidth(int id, MobileItem item) {
		synchronized(outBuffLock) {
			prepare(ALTERWIDTH, 2*INTSIZE);
			outBuffer.append(id).append(item.getWidth());
		}
		return this;
	}
	
	public ServerProtocol removeItem(int id) {
		synchronized(outBuffLock) {
			prepare(REMOVE, INTSIZE);
			outBuffer.append(id);
		}
		return this;
	}
	
	
	public ServerProtocol sendImage(int imageId) {
		String filename;
		
		synchronized(_aquarium.getLockContent()) {
			filename = "bin/"+_aquarium.getMine().getFilenameImage(imageId);
		}
		
		File file = new File(filename);

		//System.out.println("bin/"+_aquarium.getMine().getFilenameImage(imageId));
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
        	FileInputStream fis = new FileInputStream(file);
        	byte[] buf = new byte[1024];
        	
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            
            fis.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        byte[] bytes = bos.toByteArray();
        
        System.out.println(bytes.length);
		
        synchronized(outBuffLock) {
        	prepare(IMAGE, (int)bytes.length+2*INTSIZE);
        	outBuffer.append(imageId).append((int)bytes.length).append(bytes);
        }
        
		return this;
	}
	
	private Aquarium _aquarium;
	//private SocketAddress address;
}
