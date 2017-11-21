package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import org.apache.log4j.Logger;

import util.Acknowledge;
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
				logger.error(e);
				throw e;
			}
			Message recvMessage = new Message(receivePacket.getData());
			int seqNumber = recvMessage.getSeqNum();
			randomNumber = new Random().nextFloat();
			if (randomNumber <= inputProbability) {
				logger.info("Packet " + seqNumber + " dropped.");
				continue;
			} else {
				logger.info("Packet " + seqNumber + " received.");
			}
			InetAddress senderIP = receivePacket.getAddress();
			int senderPort = receivePacket.getPort();
			boolean isAckSent = sendAck(senderIP, senderPort, seqNumber);
			if (isAckSent) {
				logger.info("Ack for packet " + seqNumber + "sent");
			}
			// FileUtil.saveToFile(sentence, fileToWrite);
		}
	}

	private boolean sendAck(InetAddress senderIP, int senderPort, int seqNumber) {
		Acknowledge ack = new Acknowledge(seqNumber);
		DatagramPacket ackPacket = new DatagramPacket(ack.getBytes(), ack.getBytes().length, senderIP, senderPort);
		try {
			serverSocket.send(ackPacket);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}