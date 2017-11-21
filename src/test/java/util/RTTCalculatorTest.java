package util;

import org.junit.Assert;
import org.junit.Test;

public class RTTCalculatorTest {

	@Test
	public void testGetRTT() {
		RTTCalculator rttCal = new RTTCalculator();
		String[] receivers = new String[3];
		receivers[0] = "192.168.0.103";
		receivers[1] = "192.168.0.105";
		receivers[2] = "192.168.0.221";
		//Assert.assertNotEquals(0, rttCal.getRTT(receivers));
	}
}
