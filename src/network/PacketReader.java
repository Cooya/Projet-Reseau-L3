package network;
import aquarium.util.ByteBuff;
import network.Protocol;

public class PacketReader {

	public PacketReader(ByteBuff buff) {
		offset = 0;
		packet = buff.clone();
	}
	
	public int getInt() {
		int val = packet.toInt(offset);
		offset += Protocol.INTSIZE;
		return val;
	}
	
	public short getShort() {
		short val = packet.toShort(offset);
		offset += Protocol.SHORTSIZE;
		return val;
	}
	
	public boolean getBool() {
		boolean val = packet.toBool(offset);
		offset += Protocol.BOOLSIZE;
		return val;
	}
	
	public double getDouble() {
		double val = packet.toDouble(offset);
		offset += Protocol.DOUBLESIZE;
		return val;
	}
	
	public byte[] getNext(int len) {
		byte[] bytes = packet.get(offset, offset+len);
		offset += len;
		return bytes;
	}
	
	public byte[] getNextDebug(int len) {
		byte[] bytes = packet.get(offset, offset+len);
		return bytes;
	}
	
	public byte[] getAll() {
		byte[] val =  packet.get(offset, packet.length());
		offset = packet.length();
		return val;
	}
	
	public boolean isEOF() {
		return offset>=packet.length();
	}
	
	public boolean isComplete(int askedSize) {
		return offset+askedSize <= packet.length();
	}
	
	public int dumpRemainingSize() {
		return packet.length() - offset;
	}
	
	private ByteBuff packet;
	private int offset;

}
