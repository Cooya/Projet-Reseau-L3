package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.ArrayList;

import aquarium.gui.Aquarium;

public class ClientTracker extends Thread {

	public ClientTracker(Aquarium aquarium, String hostName) {
		this.aquarium = aquarium;
		clients = new ArrayList<Client>();
		protocol = new ClientTrackerProtocolLightByte(this);
		this.hostName = hostName;
 		start();
	}
	
	public void newConnection(String hostName) {
		clients.add(new Client(aquarium, hostName));
		System.out.println("Client started.");
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
			}
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n"+e.getMessage());
			if(!(e instanceof IOException))
				e.printStackTrace();
		}
	}
	
	/*public static void main(String args[]) {
		Client client  = new Client();
	}*/
	
	public void close() {
		try {
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Client client : clients)
			client.close();
	}
	
	public static final int SERVERPORT = 3470;
	private Socket _socket;
	private ClientTrackerProtocolLightByte protocol;
	private Aquarium aquarium;
	private List<Client> clients;
	private String hostName;
	
	private InputStream is;
	private OutputStream os;
}
