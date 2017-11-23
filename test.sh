#!/bin/bash
mvn package
cd target

java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abcd.txt 0.6 & PID_S=$!
java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient 127.0.0.1 7735 abc.txt 500 & PID_C=$!

wait $PID_S
wait $PID_C
echo "Test Finished"