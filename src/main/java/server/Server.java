package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import org.apache.log4j.Logger;

import util.FileUtil;

public class Server {

	static Logger logger = Logger.getLogger(Server.class);

	public static void main(String args[]) throws Exception {
		if (args.length != 3) {
			logger.info("Incorrect number of arguments. Please check and try again");
			System.exit(1);
		}
		int serverPort = Integer.valueOf(args[0]);
		String fileToWrite = args[1];
		float inputProbability = Float.valueOf(args[2]);
		if (inputProbability > 1 || inputProbability < 0) {
			logger.info("Probability should be between 0 and 1. Exiting.");
			System.exit(1);
		}
		float randomNumber= new Random().nextFloat();
		
		DatagramSocket serverSocket = new DatagramSocket(serverPort);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			logger.info("RECEIVED: " + sentence);
			if (randomNumber <= inputProbability) {
				logger.info("Packet dropped.");
				continue;
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			FileUtil.saveToFile(sentence, fileToWrite);
		}
	}
}