package util;

public class Message {

	private int seqNum;
	private short checksum;
	private short type;
	private byte[] data;

	public Message(int seqNum, short checksum, short type, byte[] data) {
		super();
		this.seqNum = seqNum;
		this.checksum = checksum;
		this.type = type;
		this.data = data;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public short getCheksum() {
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
		System.arraycopy(seqNum, 0, result, 0, 4);
		System.arraycopy(type, 0, result, 4, 2);
		System.arraycopy(checksum, 0, result, 6, 2);
		System.arraycopy(data, 0, result, 8, data.length);
		return result;
	}
}
