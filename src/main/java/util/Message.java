package util;

import java.nio.ByteBuffer;

public class Message {

	private int seqNum;
	private short checksum;
	private short type;
	private byte[] data;

	public Message(byte[] data, int length) {
		super();
		ByteBuffer seqNumBuffer = ByteBuffer.wrap(data, 0, 4);
		this.seqNum = seqNumBuffer.getInt();

		ByteBuffer typeBuffer = ByteBuffer.wrap(data, 4, 2);
		this.type = typeBuffer.getShort();

		ByteBuffer checksumBuffer = ByteBuffer.wrap(data, 6, 2);
		this.checksum = checksumBuffer.getShort();

		this.data = new byte[length - 8];
		System.arraycopy(data, 8, this.data, 0, length - 8);

	}

	public Message(int seqNum, short type, byte[] data) {
		super();
		this.seqNum = seqNum;
		this.type = type;
		this.data = data;
	}

	

	public int getSeqNum() {
		return seqNum;
	}

	public short getChecksum() {
		return checksum;
	}

	public short getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}

	public byte[] getBytes() {
		byte[] result = new byte[data.length + 8];
		byte[] seqNumBytes = ByteBuffer.allocate(4).putInt(seqNum).array();
		System.arraycopy(seqNumBytes, 0, result, 0, 4);

		byte[] typeBytes = ByteBuffer.allocate(2).putShort(type).array();
		System.arraycopy(typeBytes, 0, result, 4, 2);

		this.checksum = CheckSumCalculator.calculate(this.data);
		byte[] checksumBytes = ByteBuffer.allocate(2).putShort(checksum).array();
		System.arraycopy(checksumBytes, 0, result, 6, 2);

		System.arraycopy(data, 0, result, 8, data.length);
		return result;
	}

	@Override
	public String toString() {
		return "\nSEQ: " + seqNum + "\nData: " + new String(data) + " \nType: " + type;
	}

}
