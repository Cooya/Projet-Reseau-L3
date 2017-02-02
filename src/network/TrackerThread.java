package network;

import java.net.*;
import java.io.*;

public class TrackerThread extends Thread {

	public TrackerThread(Socket socket) {
		_socket = socket;
		protocol = new TrackerProtocolLightByte();
	}
	
	public void run() {
		buffered();
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
				System.out.println(e.getMessage());
			}
			
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
	
	public void signalNewClient(InetAddress address) {
		protocol.signalNewClient(address);
		send();
	}
	
	public void close() {
		try {
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private TrackerProtocolLightByte protocol;
	private Socket _socket;
	
	private InputStream is;
	private OutputStream os;
}
