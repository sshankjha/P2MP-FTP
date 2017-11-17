package util;

public class Message {

	private int seqNum;
	private short cheksum;
	private short type;
	private byte[] data;

	public Message(int seqNum, short cheksum, short type, byte[] data) {
		super();
		this.seqNum = seqNum;
		this.cheksum = cheksum;
		this.type = type;
		this.data = data;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public short getCheksum() {
		return cheksum;
	}

	public short getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}

	public void getBytes() {
		byte[] result = new byte[data.length + 8];
	}
}
