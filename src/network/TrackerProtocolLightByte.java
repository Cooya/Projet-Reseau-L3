package network;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

public class TrackerProtocolLightByte extends ProtocolLightByte {

	public TrackerProtocolLightByte() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean fetchMessage(PacketReader reader, int cmd) {
		return true;
	}
	
	public void signalNewClient(InetAddress address) {
		byte[] byteAddr= address.getAddress();//address.getBytes("UTF-8");
		synchronized(outBuffLock) {
			prepare(NODE, INTSIZE+byteAddr.length);
			outBuffer.append(byteAddr.length).append(byteAddr);
		}
	}

}
