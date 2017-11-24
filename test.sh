#!/bin/bash
mvn package
cd target

java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abcd 0.8 & PID_S=$!
java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient 127.0.0.1 7735 abc.txt 10 & PID_C=$!

wait $PID_S
wait $PID_C
echo "Test Finished"