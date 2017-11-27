# P2MP-FTP
Point-to-multipoint reliable data transfer protocol using the Stop-and-Wait automatic repeat request (ARQ) scheme.

Point-to-multipoint reliable data transfer protocol using the Stop-and-Wait automatic repeat request (ARQ) scheme. The FTP protocol provides a sophisticated file transfer service, but since it uses TCP to ensure reliable data transmission it only supports the transfer of files from one sender to one receiver.  

The P2MP-FTP client will play the role of the sender that connects to a set of of P2MP-FTP servers that play the role of the receivers in the reliable data transfer. All data transfer is from sender (client) to receivers (servers).  

The client also implements the sending side of the reliable Stop-and-Wait protocol, receiving data from rdt send(), buffering the data locally, and ensuring that the data is received correctly at the server.  

The client transmits each segment separately to each of the receivers, and waits until it has received ACKs from every receiver before it can transmit the next segment. Every time a segment is transmitted, the sender sets a timeout counter. If the counter expires before ACKs from all receivers have been received, then the sender re-transmits the segment, but only to those receivers from which it has not received an ACK yet. This process repeats until all ACKs have been received (i.e., if there are n receivers, n ACKS, one from each receiver have arrived at the sender), at which time the sender proceeds to transmit the next segment.  



How to run our code?

Pre-requisites:
- Host should have java 8 and maven installed. 
- To use the given test scripts the machine should be linux (Mac or Ubuntu) as they are bash scripts. 

To run Locally: 
- If both the server and client is being run in same server, we suggest to use localTest.sh script.
- Please place the test file (to be transferred) in target folder with file name as abc.txt.
- Run the script localTest.sh
- This script will run both server and client and kill the process after the file is transferred successfully.
- The transferred file (file received by server) is named as abcd.
- Change drop probability as desired (set to 0.01 by default) 

To run on Remote Machines:
- If servers and client are in different host, we suggest to use remoteTest.sh script in the client host.
- Like before, please place the file to be transferred in target folder with name test.txt.
- Add the server list to both ip_list and array. Refer to the example provided in the comments section of the script.
- Change drop probability as desired (change value in loss_probablity in line 15).

Both of these scripts will run mvn package and run both server and client in respective machine and kill after the file has been transferred. Additionally, remoteTest.sh will also install java and maven in remote host (servers).
