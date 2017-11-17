package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class SenderThread implements Runnable {
	final static Logger logger = Logger.getLogger(SenderThread.class);
	private int serverPort;
	private InetAddress serverIp;
	private DatagramSocket clientSocket;
	private byte[] data;

	public SenderThread(int serverPort, String serverIp, DatagramSocket clientSocket, byte[] data)
			throws UnknownHostException {
		super();
		this.serverPort = serverPort;
		this.serverIp = InetAddress.getByName(serverIp);
		this.clientSocket = clientSocket;
		this.data = data;
	}

	@Override
	public void run() {
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIp, serverPort);
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
