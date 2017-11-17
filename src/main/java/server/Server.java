package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import org.apache.log4j.Logger;

import util.Message;

public class Server {

	static Logger logger = Logger.getLogger(Server.class);
	private int serverPort;
	private float inputProbability;
	private DatagramSocket serverSocket;
	private byte[] receiveData = new byte[1024];
	private String fileToWrite;

	public Server(int serverPort, float inputProbability, String fileToWrite) throws SocketException {
		super();
		this.serverPort = serverPort;
		this.inputProbability = inputProbability;
		this.fileToWrite = fileToWrite;
		serverSocket = new DatagramSocket(serverPort);
	}

	public void listen() throws IOException {
		float randomNumber;
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e);
				throw e;
			}
			Message recvMessage = new Message(receivePacket.getData());
			logger.info("RECEIVED: " + new String(recvMessage.getData()));
			randomNumber = new Random().nextFloat();
			if (randomNumber <= inputProbability) {
				logger.info("Packet dropped.");
				continue;
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			//String capitalizedSentence = sentence.toUpperCase();
			//sendData = capitalizedSentence.getBytes();
			//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			//serverSocket.send(sendPacket);
			//FileUtil.saveToFile(sentence, fileToWrite);

		}
	}
}