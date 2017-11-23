#!/bin/bash
mvn package
cd target

java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abcd.txt 1 & PID_S=$!
java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient localhost 7735 abc.txt 1400 & PID_C=$!

wait $PID_S
wait $PID_C
echo "Test Finished"