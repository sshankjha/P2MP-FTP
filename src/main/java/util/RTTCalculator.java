package util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RTTCalculator {

	/**
	 * Calculates RTT for all the receivers and returns the value in ms.
	 * 
	 * @param receivers array of hostnames
	 * @return maximum RTT among all the RTTs
	 */
	public long getRTT(String[] receivers) {
		int countForPing = 3;
		List<Long> rttForAllReceivers = new ArrayList<Long>();
		for (String receiver : receivers) {
			long totalTime = 0;
			for (int i = 0; i < countForPing; i++) {
				try {
					InetAddress inetAddress = InetAddress.getByName(receiver);
					long startTime = System.currentTimeMillis();
					if (inetAddress.isReachable(1000)) {
						totalTime += System.currentTimeMillis() - startTime;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			rttForAllReceivers.add(totalTime / 3);
		}
		return Collections.max(rttForAllReceivers);
	}
}
