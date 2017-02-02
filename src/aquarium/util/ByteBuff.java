package aquarium.util;

import java.nio.ByteBuffer;

public class ByteBuff {
	
	public ByteBuff() {
		buffer = new byte[DEFSIZE];
		size = 0;
	}
	
	public ByteBuff(byte[] source) {
		this();
		append(source);
	}
	
	public ByteBuff(byte[] source, int start, int stop) {
		this();
		append(source, start, stop);
	}
	
	public ByteBuff clone() {
		return new ByteBuff(buffer, 0, size);
	}
	
	
	public ByteBuff append(byte[] source) {
		return append(source, 0, source.length);
	}
	
	public ByteBuff append(byte[] source, int start, int stop) {
		if(size+(stop-start) > buffer.length)
			enlarge(size+(stop-start));
		
		for(int i=size, j=start; j<stop; i++, j++)
			buffer[i] = source[j];
		
		size += (stop-start);
		
		return this;
	}
	
	public ByteBuff append(byte source) {
		if(size+1 > buffer.length)
			enlarge(size+1);
		
		buffer[size] = source;
		
		size ++;
		
		return this;
	}
	
	
	public byte[] get() {
		return get(0, size);
	}
	
	public byte[] get(int start, int stop) {
		byte[] newBuffer = new byte[stop-start];
		
		for(int i=0, j=start; j<stop; i++, j++)
			newBuffer[i] = buffer[j];
		
		return newBuffer;
	}
	
	public ByteBuff set(byte[] source) {
		empty();
		return append(source);
	}
	
	public ByteBuff set(byte[] source, int start, int stop) {
		empty();
		return append(source, start, stop);
	}
	
	public ByteBuff set(byte source) {
		empty();
		return append(source);
	}
	
	
	public ByteBuff empty() {
		size = 0;
		return this;
	}
	
	public int length() {
		return size;
	}
	
	public byte at(int pos) {
		if(pos < size)
			return buffer[pos];
		else
			return 0;
	}
	
	
	private void enlarge(int minSize) {
		int newLength = buffer.length*2;
		
		while(newLength < minSize)
			newLength*=2;
		
		byte[] newBuffer = new byte[newLength];
		
		for(int i=0; i<size; i++)
			newBuffer[i] = buffer[i];
		
		buffer = newBuffer;
	}
	
	
	public static int toInt(byte[] array, int pos) {
		return ByteBuffer.wrap(array, pos, 4).getInt();
	}
	public static int toInt(byte[] array) {
		return ByteBuffer.wrap(array).getInt();
	}
	
	public static short toShort(byte[] array, int pos) {
		return ByteBuffer.wrap(array, pos, 2).getShort();
	}
	public static short toShort(byte[] array) {
		return ByteBuffer.wrap(array).getShort();
	}
	
	public static boolean toBool(byte[] array, int pos) {
		byte b =  ByteBuffer.wrap(array, pos, 1).get();
		if(b==0)
			return false;
		return true;
	}
	
	public static double toDouble(byte[] array, int pos) {
		return ByteBuffer.wrap(array, pos, 8).getDouble();
	}
	
	
	public int toInt(int pos) {
		return toInt(buffer, pos);
	}
	public int toInt() {
		return toInt(buffer);
	}
	
	public short toShort(int pos) {
		return toShort(buffer, pos);
	}
	public short toShort() {
		return toShort(buffer);
	}
	
	public boolean toBool(int pos) {
		return toBool(buffer, pos);
	}
	
	public double toDouble(int pos) {
		return toDouble(buffer, pos);
	}
	
	
	public static byte[] toArray(int val) {
		return ByteBuffer.allocate(4).putInt(val).array();
	}
	public static byte[] toArray(short val) {
		return ByteBuffer.allocate(2).putShort(val).array();
	}
	public static byte[] toArray(boolean val) {
		byte b;
		if(!val)
			b = 0;
		else
			b = 1;
		return ByteBuffer.allocate(1).put(b).array();
	}
	public static byte[] toArray(double val) {
		return ByteBuffer.allocate(8).putDouble(val).array();
	}
	
	
	public ByteBuff append(int val) {
		return append(toArray(val));
	}
	public ByteBuff append(short val) {
		return append(toArray(val));
	}
	public ByteBuff append(boolean val) {
		return append(toArray(val));
	}
	public ByteBuff append(double val) {
		return append(toArray(val));
	}
	
	public ByteBuff set(int val) {
		return set(toArray(val));
	}
	public ByteBuff set(short val) {
		return set(toArray(val));
	}
	
	private byte[] buffer;
	private int size;
	private static final int DEFSIZE = 64;
}
