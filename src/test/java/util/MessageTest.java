package util;

import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

	@Test
	public void testMessageEncoding() {
		Message mssg = new Message(678, (short) 3, "HelloHelloHelloHello".getBytes());
		byte[] mssgBytes = mssg.getBytes();
		Message decodedMessage = new Message(mssgBytes);
		Assert.assertEquals(678, decodedMessage.getSeqNum());
		Assert.assertEquals(3, decodedMessage.getType());
		//Assert.assertTrue(decodedMessage.getChecksum() != 0);
		Assert.assertEquals("HelloHelloHelloHello", new String(decodedMessage.getData()));
	}
}
