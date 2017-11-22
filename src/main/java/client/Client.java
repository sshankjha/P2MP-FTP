package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import util.Constants;
import util.Message;

public class Client {
	final static Logger logger = Logger.getLogger(Client.class);
	private DatagramSocket clientSocket;
	private byte[] sendData;
	int sendDataIndex = 0;
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
		if (mss < 0 || mss > 1500) {
			throw new IllegalStateException("Incorrect value for MSS");
		}
		this.mss = mss;
		this.sendData = new byte[mss];
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

	//Close the socket and send the remaining data
	public void close() {
		logger.info("Calling connection.close( )");
		byte[] data = Arrays.copyOfRange(sendData, 0, sendDataIndex);
		sendMessageToAll(data, new ArrayList<String>(), Constants.LAST);
		try {
			clientSocket.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void rdtSend(byte[] dataToSend) {
		//Add logic for filling the buffer and send it.
		int length = dataToSend.length;
		int space;
		int dataSentSize;
		int sendIndex = 0;

		while (length > 0) {
			space = mss - sendDataIndex;
			dataSentSize = Math.min(length, space);

			System.arraycopy(dataToSend, sendIndex, sendData, sendDataIndex, dataSentSize);

			sendDataIndex += dataSentSize;
			sendIndex += dataSentSize;
			length = length - dataSentSize;
			if (sendDataIndex == mss) {
				sendMessageToAll(sendData, new ArrayList<String>(), Constants.DATA);
				ackNum++;
				//need to optimize this
				sendDataIndex = 0;
			}
		}

	}

	public void sendMessageToAll(byte[] data, List<String> ackReceived, short mssgType) {
		Message mssg = new Message(ackNum, mssgType, data);
		for (String serverIp : serverIpList) {
			//If ack has alerady not been received
			if (!ackReceived.contains(serverIp)) {
				Thread t;
				try {
					t = new Thread(new SenderThread(serverPort, serverIp, clientSocket, mssg.getBytes()));
					t.start();
				} catch (UnknownHostException e) {
					logger.info(e);
				}
			}
		}
		//Code to receive Acks
		byte[] ackData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(ackData, ackData.length);
			try {
				clientSocket.receive(receivePacket);
				Message recvAck = new Message(receivePacket.getData());
				int recvAckNum = recvAck.getSeqNum();
				if (ackNum == recvAckNum) {
					ackReceived.add(receivePacket.getAddress().getHostAddress());
				}
				System.out.println("Ack Received for seq# " + recvAck.getSeqNum());
				break;
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

}
