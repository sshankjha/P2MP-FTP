package main;

import org.apache.log4j.Logger;

import server.Server;

public class P2mpserver {
	final static Logger logger = Logger.getLogger(P2mpserver.class);

	public static void main(String args[]) throws Exception {
		int serverPort;
		Server server;
		float inputProbability;
		String fileToWrite;
		if (args.length != 3) {
			logger.info("Incorrect number of arguments. Please check and try again");
			System.exit(1);
		}
		serverPort = Integer.valueOf(args[0]);
		fileToWrite = args[1];
		inputProbability = Float.valueOf(args[2]);
		if (inputProbability > 1 || inputProbability < 0) {
			logger.info("Probability should be between 0 and 1. Exiting.");
			System.exit(1);
		}
		server = new Server(serverPort, inputProbability, fileToWrite);
		server.listen();

	}

}
