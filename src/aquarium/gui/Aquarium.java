package aquarium.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JPanel;

import network.Client;
import network.ClientTracker;
import network.Server;
import aquarium.items.AquariumContent;
import aquarium.items.AquariumFriends;
import aquarium.items.AquariumItem;
import aquarium.items.Fish;
import aquarium.items.MobileItem;
import aquarium.items.MortalFixedItem;
import aquarium.items.Seastone;
import aquarium.items.Seaweed;
import aquarium.items.Shark;

/**
 * An Aquarium is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Aquarium extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Object lockFriends = new Object();
	private Object lockContent = new Object();

	/**
	 * Constant (c.f. final) common to all Aquarium instances (c.f. static)
	 * defining the background color of the Aquarium
	 */
	private static final Color backgroundColor = new Color(218, 238, 245);

	public Object getLockFriends() {
		return lockFriends;
	}

	public Object getLockContent() {
		return lockContent;
	}

	/**
	 * Constant common to all Aquarium instances defining the width of this last
	 */
	private static final int SIZE_AQUA_X = 800;
	/**
	 * Constant common to all Aquarium instances defining the height of this
	 * last
	 */
	private static final int SIZE_AQUA_Y = 600;

	/**
	 * Pixel data buffer for the Aquarium rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

	/**
	 * List of Aquarium items to be rendered in the Aquarium
	 */

	private Server server;
	private Client client;
	private ClientTracker clientTracker;

	private AquariumFriends contents;
	private AquariumContent mine;
	private AI ai = null;

	public Aquarium(boolean[] argi){ // mode sans launcher
		contents = new AquariumFriends();
		mine = new AquariumContent();

		if (argi[1])
			startServer();
		if (argi[0])
			startClient("localhost");
		if (!argi[0] && !argi[1])
			createContent();
	}
	
	public Aquarium(String mode) { // mode avec launcher
		this(mode, "localhost");
	}
	
	public Aquarium(String mode, String hostName) { // mode avec launcher
		contents = new AquariumFriends();
		mine = new AquariumContent();
		
		if(mode.equals("server"))
			startServer();
		else if(mode.equals("client"))
			startClient(hostName);
		else if (mode.equals("app")){
			startServer();
			clientTracker = new ClientTracker(this, hostName);
		}
	}
	
	public void startServer() {
		server = new Server(this);
		System.out.println("Server started.");
		createContent();
	}
	
	public void startClient(String hostName) {
		client = new Client(this, hostName);
		System.out.println("Client started.");
	}
	
	private void createContent() {
		for (int i = 0; i < 10; i++) {
			AquariumItem ai = new Seaweed(mine);
			if (ai.sink(mine))
				addItem(ai);
		}
		for (int i = 0; i < 2; i++) {
			AquariumItem ai = new Seastone(mine);
			if (ai.sink(mine))
				addItem(ai);
		}
		for (int i = 0; i < 10; i++) {
			MobileItem mi = new Fish(mine);
			if (mi.sink(mine))
				addItem(mi);
		}
		for (int i = 0; i < 3; i++) {
			MobileItem mi = new Shark(mine);
			if (mi.sink(mine))
				addItem(mi);
		}
		ai = new AI(this);
	}

	public AquariumContent getMine() {
		return mine;
	}

	public AquariumContent getContent(int i) {
		return contents.get(i);
	}
	
	public Server getServer(){
		return server;
	}

	public void setContent(int i, AquariumContent content) {
		contents.set(i, content);
	}

	public void removeContent(int i) {
		contents.remove(i);
	}

	public void addItem(AquariumItem item) {
		int id;
		synchronized(lockContent) {
			id = mine.add(item);
		}
			if (server != null)
				server.itemAdded(id, item);
	}

	public void removeItem(int id) {
		synchronized(lockContent) {
			mine.remove(id);
		}
		if (server != null)
			server.itemRemoved(id);
	}

	/**
	 * @return the width of the Aquarium
	 */
	public static int getSizeX() {
		return SIZE_AQUA_X;
	}

	/**
	 * @return the height of the Aquarium
	 */
	public static int getSizeY() {
		return SIZE_AQUA_Y;
	}
	
	public void animate(){
		if(ai != null)
			ai.execute();
		synchronized(lockContent) {
		for (Map.Entry<Integer, AquariumItem> item : mine){
			if(item.getValue() instanceof MobileItem){
				((MobileItem) item.getValue()).move(mine);
				if (server != null)
					server.itemPosAltered(item.getKey(), (MobileItem)item.getValue());
			}
		}
		}
		updateScreen();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 * Draw each Aquarium item based on new positions
	 */
	private void updateScreen() {
		if (buffer == null) {
			buffer = createImage(SIZE_AQUA_X, SIZE_AQUA_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
			graphicContext.setColor(backgroundColor);
			graphicContext.fillRect(0, 0, SIZE_AQUA_X, SIZE_AQUA_Y);

		synchronized (lockFriends) {
			for (AquariumContent aquariumContent : contents.values()) {
				synchronized (lockContent) {
					for (Map.Entry<Integer, AquariumItem> aquariumItem : aquariumContent)
						aquariumItem.getValue().draw(graphicContext, aquariumContent);
				}
			}
		}
		
		for(Map.Entry<Integer, AquariumItem> item : mine) {
			AquariumItem ai = item.getValue();
			if(ai instanceof Seaweed || ai instanceof Seastone  || ai instanceof MortalFixedItem)
				ai.draw(graphicContext, mine);
		}

		for (Map.Entry<Integer, AquariumItem> item : mine) {
			AquariumItem ai = item.getValue();
			if(!(ai instanceof Seaweed) && !(ai instanceof Seastone) && !(ai instanceof MortalFixedItem))
				ai.draw(graphicContext, mine);
		}

		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		AquariumItem ai;
		if(e.getActionCommand().equals(AquariumWindow.BUTTON1)) {
			ai = new Fish(mine);
			addItem(ai);
			ai.sink(mine);
		}
		else if(e.getActionCommand().equals(AquariumWindow.BUTTON2)) {
			ai = new Shark(mine);
			addItem(ai);
			ai.sink(mine);
		}
	}
	
	public void delete() {
		if(server != null)
			server.close();
		if(client != null)
			client.close();
		if(clientTracker != null)
			clientTracker.close();
		
		synchronized(lockFriends) {
			contents.clear();
		}
		synchronized(lockContent) {
			mine.clear();
		}
	}
}
