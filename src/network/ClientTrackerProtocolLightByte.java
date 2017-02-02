package network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientTrackerProtocolLightByte extends ProtocolLightByte {

	public ClientTrackerProtocolLightByte(ClientTracker clientTracker) {
		this.clientTracker = clientTracker;
	}

	@Override
	protected boolean fetchMessage(PacketReader reader, int cmd) {
		switch (cmd) {
		case NODE :
			try {
				int addressLen = reader.getInt();
				byte[] rawAddress = reader.getNext(addressLen);
				//String address = new String(rawAddress, "UTF-8");
				InetAddress connection;
				System.out.println("Received Node");
				connection = InetAddress.getByAddress(rawAddress);//.getByName(address);
					//String strAddr = connection.getHostAddress();
					System.out.println(connection.getHostAddress());
				clientTracker.newConnection(connection.getHostAddress());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			return false;
		}
		
		return true;
	}
	
	private ClientTracker clientTracker;

}
