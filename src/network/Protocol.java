package network;

public interface Protocol {
	public static final int HELLO = 100;
	//public static final int BYE = 101;
	public static final int ASKIMAGE = 102;
	
	public static final int ADD = 200;
	public static final int ALTERPOS = 2010;
	public static final int ALTERWIDTH = 2011;
	public static final int REMOVE = 202;
	public static final int IMAGE = 203;
	
	public static final int NODE = 300;
	
	
	public static final int BOOLSIZE = 1;
	public static final int SHORTSIZE = 2;
	public static final int INTSIZE = 4;
	public static final int DOUBLESIZE = 8;
	
	public byte[] send();
	
	public Protocol push(byte[] command, int size);
	public Protocol execute();
}
