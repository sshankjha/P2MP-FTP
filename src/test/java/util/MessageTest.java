package util;

import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

	@Test
	public void testMessageEncoding() {
		Message mssg = new Message(678, (short) 3, "HelloHelloHelloHello".getBytes());
		byte[] mssgBytes = mssg.getBytes();
		int length = "HelloHelloHelloHello".getBytes().length + 8;
		Message decodedMessage = new Message(mssgBytes, length);
		Assert.assertEquals(678, decodedMessage.getSeqNum());
		Assert.assertEquals(3, decodedMessage.getType());
		//Assert.assertTrue(decodedMessage.getChecksum() != 0);
		Assert.assertEquals("HelloHelloHelloHello", new String(decodedMessage.getData()));
	}
}
