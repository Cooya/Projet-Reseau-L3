package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;

public class Server extends Thread {
	public static final int SERVERPORT = 3474;
	private boolean serverOpen = true;
	
	/*public static void main(String args[]) {
		Server echoTCP = new Server();
		echoTCP.threadedConnection();
	}*/
	
	public Server(Aquarium aquarium) {
		_aquarium = aquarium;
		threads = new ArrayList<ServerThread>();
		start();
		//createScheduler();
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
				cleanList();
				ServerThread thread = new ServerThread(listen.accept(), _aquarium);
				threads.add(thread);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}
	
	private void cleanList() {
		Iterator<ServerThread> tIt = threads.iterator();
		ServerThread t;
		
		while(tIt.hasNext()) {
			t = tIt.next();
			
			if(!t.isAlive())
				tIt.remove();
		}
	}
	
	public void itemAdded(int id, AquariumItem item) {
		for(ServerThread thread : threads)
			if(thread.isAlive())
				thread.itemAdded(id, item);
	}
	
	public void itemPosAltered(int id, MobileItem item) {
		for(ServerThread thread : threads)
			if(thread.isAlive())
				thread.itemPosAltered(id, item);
	}
	
	public void itemWidthAltered(int id, MobileItem item) {
		for(ServerThread thread : threads)
			if(thread.isAlive())
				thread.itemWidthAltered(id, item);
	}
	
	public void itemRemoved(int id) {
		for(ServerThread thread : threads)
			if(thread.isAlive())
				thread.itemRemoved(id);
	}
	
	public void close() {
		serverOpen = false;
		for(ServerThread thread : threads)
			if(thread.isAlive())
				thread.close();
	}
	
	private List<ServerThread> threads;
	private Aquarium _aquarium;
}
