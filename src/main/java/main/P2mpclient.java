package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import client.Client;

public class P2mpclient {
	final static Logger logger = Logger.getLogger(P2mpclient.class);

	public static void main(String[] args) {
		//p2mpclient server-1 server-2 server-3 server-port# file-name MSS
		Client client;
		int serverPort;
		List<String> serverIpList;
		String fileName;
		int mss;

		if (args.length < 4) {
			logger.error(
					"Incorrect number of arguments: p2mpclient server-1 server-2 server-3 server-port# file-name MSS");
			System.exit(1);
		}
		//Parsing command line arguments - Start
		serverIpList = new ArrayList<>();
		for (int iter = 0; iter < args.length - 3; iter++) {
			serverIpList.add(args[iter]);
		}
		serverPort = Integer.parseInt(args[args.length - 3]);
		fileName = args[args.length - 2];
		Path file = new File(fileName).toPath();
		mss = Integer.parseInt(args[args.length - 1]);
		//Parsing command line arguments - End

		try {
			client = new Client(fileName, serverPort, mss, serverIpList);

			//Reading the file contents and sending it - Start
			try (FileInputStream buf = new FileInputStream(fileName)) {
				byte[] ch = new byte[1];
				while (buf.read(ch) != -1) {
					client.rdtSend(ch);
				}
				//for (String line : (Iterable<String>) lines::iterator) {
				//}
			} catch (IOException e1) {
				logger.info(e1);
			}
			//Reading the file contents and sending it - End
			//Have something like client.close()
			//client.rdtSend("hello".getBytes());
		} catch (SocketException e) {
			logger.error(e);
		}
	}

}
