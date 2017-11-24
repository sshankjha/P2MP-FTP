package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import util.Constants;
import util.Message;
import util.RTTCalculator;

public class Client {
	final static Logger logger = Logger.getLogger(Client.class);
	private DatagramSocket clientSocket;
	private byte[] sendData;
	int sendDataIndex = 0;
	private String fileName;
	private int mss;
	private int serverPort;
	private List<String> serverIpList;
	private long rtt;
	private int ackNum = 0;

	public Client(String fileName, int mss, List<String> serverIpList) throws SocketException {
		this(fileName, Constants.SERVER_PORT, mss, serverIpList);
	}

	public Client(String fileName, int serverPort, int mss, List<String> serverIpList) throws SocketException {
		this.fileName = fileName;
		rtt = RTTCalculator.getRTT(serverIpList.toArray(new String[0]));
		logger.info("Selected RTT value is : " + rtt + " ms");
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
		logger.info("Calling connection.close( )" + sendDataIndex);
		byte[] data = new byte[sendDataIndex];
		System.arraycopy(sendData, 0, data, 0, sendDataIndex);
		sendReliable(data, Constants.LAST);
		//sendMessageToAll(data, new ArrayList<String>(), Constants.LAST, ackNum);
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
				sendReliable(sendData, Constants.DATA);
				ackNum++;
				sendDataIndex = 0;
			}
		}

	}

	private void sendReliable(byte[] data, short mssgType) {
		logger.info("Sent packet " + this.ackNum);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		List<String> ackReceived = new ArrayList<String>();
		sendMessageToAll(data, ackReceived, mssgType, ackNum);
		//Submit timer
		Future<Void> future = executor.submit(new Task(clientSocket, ackNum, ackReceived, serverIpList));
		startTimer(future, data, ackReceived, mssgType, ackNum);
		executor.shutdownNow();
	}

	private void startTimer(Future<Void> future, byte[] data, List<String> ackReceived, short mssgType, int ackNum) {
		try {
			future.get(rtt, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			//future.cancel(true);
			if (ackReceived.size() != serverIpList.size()) {
				logger.error("Timout Occured for packet " + ackNum + " - Trying to resend");
				sendMessageToAll(data, ackReceived, mssgType, ackNum);
				startTimer(future, data, ackReceived, mssgType, ackNum);
			}
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (ExecutionException e) {
			logger.error(e);
		}
	}

	private void sendMessageToAll(byte[] data, List<String> ackReceived, short mssgType, int ackNum) {
		Message mssg = new Message(ackNum, mssgType, data);
		logger.info("Sending number " + mssg.getBytes().length + " of bytes");
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
	}

}

class Task implements Callable<Void> {
	private DatagramSocket clientSocket;
	private int ackNumber;
	List<String> ackReceivedList;
	List<String> serverList;
	final static Logger logger = Logger.getLogger(Task.class);

	public Task(DatagramSocket clientSocket, int ackNum, List<String> ackReceived, List<String> serverList) {
		super();
		this.clientSocket = clientSocket;
		this.ackNumber = ackNum;
		this.ackReceivedList = ackReceived;
		this.serverList = serverList;
	}

	@Override
	public Void call() throws Exception {
		byte[] ackData = new byte[1024];
		logger.error("");
		while (!Thread.interrupted()) {
			if (serverList.size() == ackReceivedList.size()) {
				logger.info("Breaking from while loop");
				break;
			}
			DatagramPacket receivePacket = new DatagramPacket(ackData, ackData.length);
			try {
				clientSocket.receive(receivePacket);
				Message recvAck = new Message(receivePacket.getData(), receivePacket.getLength());
				int recvAckNum = recvAck.getSeqNum();
				if (ackNumber == recvAckNum) {
					ackReceivedList.add(receivePacket.getAddress().getHostAddress());
				}
				//Add check to break out of loop
				logger.info("Ack for packet " + recvAckNum + " received, Expecting: " + ackNumber + " from "
						+ receivePacket.getAddress().getHostAddress());
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}
}
