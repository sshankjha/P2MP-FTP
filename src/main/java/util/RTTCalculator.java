package util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class RTTCalculator {

	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Calculates RTT for all the receivers and returns the value in ms.
	 * 
	 * @param receivers
	 *            array of hostnames
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
					} else {
						logger.info(receiver + " not reachable.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logger.info("Average RTT for " + receiver + ": " + totalTime / countForPing + " ms");
			rttForAllReceivers.add(totalTime / countForPing);
		}
		long maxRTT = Collections.max(rttForAllReceivers);
		logger.info("Returning max RTT: " + maxRTT + " ms");
		return maxRTT;
	}
}
