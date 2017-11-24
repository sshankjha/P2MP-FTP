package util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class RTTCalculator {

	static Logger logger = Logger.getLogger(RTTCalculator.class);

	/**
	 * Calculates RTT for all the receivers and returns the value in ms.
	 * 
	 * @param receivers
	 *            array of hostnames
	 * @return maximum RTT among all the RTTs
	 */
	public static long getRTT(String[] receivers) {
		Double countForPing = 3.0;
		List<Double> rttForAllReceivers = new ArrayList<Double>();
		for (String receiver : receivers) {
			double totalTime = 0;
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
		Double maxRTT = Collections.max(rttForAllReceivers);
		return new Double((maxRTT + 2) * 1.3).longValue();
	}
}
