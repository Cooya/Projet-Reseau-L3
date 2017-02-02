package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import aquarium.util.ByteBuff;
import aquarium.gui.Aquarium;;

public class Client extends Thread {

	public Client(Aquarium aquarium, String hostName) {
		protocol = new ClientProtocolLightByte(aquarium, this);
		this.hostName = hostName;
 		start();
	}
	
	public void run() {
		startClient();
	}
	
	public void write() {
		try {
			os.write(protocol.send());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startClient() {
		try {
			_socket = new Socket(hostName, SERVERPORT);
			
			is = _socket.getInputStream();
			os = _socket.getOutputStream();
			
			protocol.hello();
			//protocol.test(203, 206);
			
			os.write(protocol.send());
			
			byte[] buffer = new byte[16384];
			int sizeRead = -1;
			
			try {
				while((sizeRead = is.read(buffer)) != -1) {
					if(sizeRead != 0) {
						protocol.push(buffer, sizeRead).execute();
					}
				}
			} catch(SocketException e) {
				System.out.println(e.getMessage());
			} finally {
				protocol.delete();
			}
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n"+e.getMessage());
			if(!(e instanceof IOException))
				e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public static void main(String args[]) {
		Client client  = new Client();
	}*/
	
	public static final int SERVERPORT = 3474;
	private Socket _socket;
	private ClientProtocol protocol;
	private String hostName;
	
	private InputStream is;
	private OutputStream os;
}
