package network;

import java.awt.Point;
import java.nio.ByteBuffer;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumContent;
import aquarium.items.NetworkItem;
import aquarium.util.ByteBuff;

public abstract class ProtocolLightByte implements Protocol {

	public ProtocolLightByte() {
		inBuffer = new ByteBuff();
		outBuffer = new ByteBuff();
		outBuffLock = new Object();
	}

	protected void prepare(int cmd, int argsSize) {
		/*
		 * System.out.println("Preparing Command :" + cmd + " (Size: " +
		 * (argsSize + INTSIZE) + ")");
		 */
		outBuffer.append((int) (argsSize + INTSIZE)).append(cmd); // ajout
																	// taille +
																	// commande
	}

	public byte[] send() {
		byte[] tmp;
		synchronized(outBuffLock) {
			tmp = outBuffer.get();
			outBuffer.empty();
		}
		// System.out.println("Sending Command");
		return tmp;
	}

	public Protocol push(byte[] command, int size) {
		inBuffer.append(command, 0, size);
		return this;
	}

	public Protocol execute() {
		PacketReader reader = new PacketReader(inBuffer);
		boolean executed = false;

		do {
			if (reader.isComplete(INTSIZE)) {
				int cmdSize = reader.getInt();

				if (reader.isComplete(cmdSize)) {
					
					executed = true;
					int cmd = reader.getInt();

					
					 System.out.println("Received Message: " + cmd +
					 " (Size: " + cmdSize + ")");
					 
					 
					if(!fetchMessage(reader, cmd))
						reader.getNext(cmdSize-INTSIZE);
					
				} else {
					
					/*System.out
							.println("Incomplete Message Args (Theoric Size: "
									+ cmdSize + ", Real size: "
									+ reader.dumpRemainingSize() + ")");*/
					
					inBuffer.set(cmdSize).append(reader.getAll());
					
					executed = false;
					break;
				}
			} else {
				inBuffer.set(reader.getAll());
				System.out.println("Incomplete Message");
				executed = false;
				break;
			}

		} while (!reader.isEOF());

		if (executed)
			inBuffer.empty();
		
		return this;
	}
	
	protected abstract boolean fetchMessage(PacketReader reader, int cmd);
	
	protected Object outBuffLock;
	protected ByteBuff inBuffer, outBuffer;
}
