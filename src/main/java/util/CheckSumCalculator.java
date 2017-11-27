package util;

import java.io.ByteArrayInputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class CheckSumCalculator {
	
	public static short calculate(byte[] data) {
		long value = 0;
		short valueToReturn = 0;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			CheckedInputStream cis = new CheckedInputStream(bais, new Adler32());
			byte readBuffer[] = new byte[data.length];
			while (cis.read(readBuffer) >= 0) {
				value = cis.getChecksum().getValue();
				valueToReturn = (short) (value % Short.MAX_VALUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueToReturn;
	}

}
