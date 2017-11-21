package util;

import java.nio.ByteBuffer;

public class Acknowledge {

	private int seqNum;

	public Acknowledge(int seqNum) {
		this.seqNum = seqNum;
	}
	
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public byte[] getBytes() {
		byte[] result = new byte[8];
		byte[] seqNumBytes = ByteBuffer.allocate(4).putInt(seqNum).array();
		System.arraycopy(seqNumBytes, 0, result, 0, 4);
		return result;
	}
}
