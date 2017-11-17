package client;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;

import util.Constants;
import util.Message;

public class Client {
	final static Logger logger = Logger.getLogger(Client.class);
	private DatagramSocket clientSocket;
	private byte[] sendData;
	private String fileName;
	private int mss;
	private int serverPort;
	private List<String> serverIpList;
	private int rtt;
	private int ackNum = 0;

	public Client(String fileName, int mss, List<String> serverIpList) throws SocketException {
		this(fileName, Constants.SERVER_PORT, mss, serverIpList);

	}

	public Client(String fileName, int serverPort, int mss, List<String> serverIpList) throws SocketException {
		this.fileName = fileName;
		this.mss = mss;
		this.serverPort = serverPort;
		this.serverIpList = serverIpList;
		try {
			clientSocket = new DatagramSocket(0);
		} catch (SocketException e) {
			logger.error("Error starting server");
			logger.error(e);
			throw e;
		}
	}

	public void rdtSend(byte[] dataToSend) {
		Message mssg = new Message(100, (short) 1, dataToSend);
		for (String serverIp : serverIpList) {
			Thread t;
			try {
				t = new Thread(new SenderThread(serverPort, serverIp, clientSocket, mssg.getBytes()));
				t.start();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	//		while (true) {
	//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	//			serverSocket.receive(receivePacket);
	//			String sentence = new String(receivePacket.getData());
	//			System.out.println("RECEIVED: " + sentence);
	//			InetAddress IPAddress = receivePacket.getAddress();
	//			int port = receivePacket.getPort();
	//			String capitalizedSentence = sentence.toUpperCase();
	//			sendData = capitalizedSentence.getBytes();
	//			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	//			serverSocket.send(sendPacket);
	//	}
}
