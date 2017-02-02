package network;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tracker extends Thread {
	public static final int SERVERPORT = 3470;
	private boolean serverOpen = true;
	
	public static void main(String args[]) {
		Tracker tracker = new Tracker();
	}
	
	public Tracker() {
		threads = new ArrayList<TrackerThread>();
		addresses = new ArrayList<InetAddress>();
		start();
	}
	
	public void run() {
		threadedConnection();
	}
	
	private void threadedConnection() {
		ServerSocket listen=null;
		
		try {
			listen = new ServerSocket(SERVERPORT);
			listen.setReuseAddress(true);
			
			while(serverOpen) {
				cleanLists();
				Socket connection = listen.accept();
				InetAddress address = connection.getInetAddress()/*.getHostName()*/;
				if(address.getHostAddress().equals("127.0.0.1")) {
					address = InetAddress.getLocalHost();
					System.out.println("translation!");
				}
				System.out.println("After Translation : "+address.getHostAddress());
				TrackerThread thread = new TrackerThread(connection);
				thread.start();
				threads.add(thread);
				System.out.println(address);
				addresses.add(address);
				
				//ConnectionInfo connectionInfo = new ConnectionInfo(thread, listen.getInetAddress());
				
				//connectionsList.add(connectionInfo);
				signalNewClient(thread, address);
				sendClientsToNewThread(thread, address);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}
	
	private void cleanLists() {
		Iterator<TrackerThread> tIt = threads.iterator();
		Iterator<InetAddress> aIt = addresses.iterator();
		TrackerThread t;
		InetAddress a;
		
		while(tIt.hasNext()) {
			t = tIt.next();
			a = aIt.next();
			
			if(!t.isAlive()) {
				tIt.remove();
				aIt.remove();
			}
		}
	}
	
	private void signalNewClient(TrackerThread exThread, InetAddress address) {
		for(TrackerThread thread : threads) {
			if(thread != exThread && thread.isAlive()) {
				thread.signalNewClient(address);
				System.out.println("1Signal New client : Address +"+address+" to Thread "+thread);
			}
			else {
				if(!thread.isAlive())
					System.out.println("1notAlive");
				else
					System.out.println("1equals");
			}
		}
	}
	
	private void sendClientsToNewThread(TrackerThread thread, InetAddress exAddress) {
		for(InetAddress address : addresses)
			if(!address.equals(exAddress) && thread.isAlive()) {
				thread.signalNewClient(address);
				System.out.println("2Signal New client : Address +"+address+" to Thread "+thread);
			}
			else {
				if(!thread.isAlive())
					System.out.println("2notAlive");
				else
					System.out.println("2equals");
			}
	}
	
	public void close() {
		serverOpen = false;
		for(TrackerThread thread : threads)
			if(thread.isAlive())
				thread.close();
	}
	
	/*private class ConnectionInfo {
		public InetAddress address;
		public TrackerThread thread;
		public ConnectionInfo(TrackerThread thread, InetAddress address) {
			this.thread = thread;
			this.address = address;
		}
	}*/
	
	List<InetAddress> addresses;
	List<TrackerThread> threads;
}
