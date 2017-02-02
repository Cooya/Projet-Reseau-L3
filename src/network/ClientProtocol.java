package network;

public interface ClientProtocol extends Protocol {
	public ClientProtocol hello();
	public ClientProtocol askImage(int imageId);
	
	public void delete();
}
