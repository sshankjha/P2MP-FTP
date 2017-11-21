#!/bin/bash
mvn package
cd target

java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abc.txt 1 & PID_S=$!
java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient localhost 7735 abc.txt 500 & PID_S=$!

sleep 2
killall java