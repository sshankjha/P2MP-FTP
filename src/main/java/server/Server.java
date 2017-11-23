package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import org.apache.log4j.Logger;

import util.Constants;
import util.Message;

public class Server {

	static Logger logger = Logger.getLogger(Server.class);
	private int serverPort;
	private int inputProbability;//out of 100
	private DatagramSocket serverSocket;
	private byte[] receiveData;
	private String fileToWrite;
	private int currentAck = 0;

	public Server(int serverPort, float inputProbability, String fileToWrite) throws SocketException {
		super();
		this.serverPort = serverPort;
		this.inputProbability = (int) (inputProbability * 100);
		this.fileToWrite = fileToWrite;
		serverSocket = new DatagramSocket(this.serverPort);
	}

	public void listen() throws IOException {
		int randomNumber;
		//Open the file to write
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToWrite, false))) {

			while (true) {
				receiveData = new byte[1500];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					serverSocket.receive(receivePacket);
				} catch (IOException e) {
					logger.error(e);
					throw e;
				}
				Message recvMessage = new Message(receivePacket.getData());
				String data = new String(recvMessage.getData());
				int seqNumber = recvMessage.getSeqNum();
				randomNumber = new Random().nextInt(100) + 1;
				boolean toDrop = randomNumber <= inputProbability;
				//boolean toDrop = false;
				if (toDrop) {
					logger.info("Packet " + seqNumber + " dropped.");
					continue;
				}

				logger.info("Packet " + seqNumber + " received.");
				logger.info(recvMessage);
				//Only write if the packet is has the expected seq number.
				if (currentAck == seqNumber) {
					bw.write(data);
					currentAck++;
				}

				InetAddress senderIP = receivePacket.getAddress();
				int senderPort = receivePacket.getPort();
				boolean isAckSent = sendAck(senderIP, senderPort, seqNumber);
				if (isAckSent) {
					logger.info("Ack for packet " + seqNumber + " sent");
				}
				if (recvMessage.getType() == Constants.LAST) {
					break;
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	private boolean sendAck(InetAddress senderIP, int senderPort, int seqNumber) {
		Message ack = new Message(seqNumber, Constants.ACK, new byte[0]);
		DatagramPacket ackPacket = new DatagramPacket(ack.getBytes(), ack.getBytes().length, senderIP, senderPort);
		try {
			serverSocket.send(ackPacket);
			return true;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}
}