package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;

public class ServerThread extends Thread {

	public ServerThread(Socket socket, Aquarium aquarium) {
		_socket = socket;
		protocol = new ServerProtocolLightByte(aquarium/*, socket.getLocalSocketAddress()*/);
	}
	
	public void run() {
		active = true;
		buffered();
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void itemPosAltered(int id, MobileItem item) {
		protocol.alterItemPos(id, item);
		send();
	}
	
	public void itemWidthAltered(int id, MobileItem item) {
		protocol.alterItemWidth(id, item);
		send();
	}
	
	public void itemAdded(int id, AquariumItem item) {
		protocol.addItem(id, item);
		send();
	}
	
	public void itemRemoved(int id) {
		protocol.removeItem(id);
		send();
	}
	
	private void buffered() {
		
		try {
			is = _socket.getInputStream();
			os = _socket.getOutputStream();
			
			byte[] buffer = new byte[16384];
			int sizeRead = -1;

			try {
				while((sizeRead = is.read(buffer)) != -1) {
					if(sizeRead != 0) {
						protocol.push(buffer, sizeRead).execute();
						send();
					}
				}
			} catch(SocketException e) {
				//e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
			/*BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			PrintStream ps = new PrintStream(os, false, "utf-8");
			
			String buffer;
		
			while(true) {
				buffer = br.readLine();
				protocol.push(buffer);
				
				String result = "a";
				ps.println(buffer);
				ps.flush();
			}*/
			
			//_socket.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void send() {
		try {
			os.write(protocol.send());
		} catch (IOException e) {
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
	
	private ServerProtocol protocol;
	private Socket _socket;
	private boolean active;
	
	private InputStream is;
	private OutputStream os;
}
