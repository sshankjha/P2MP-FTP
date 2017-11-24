mvn package
#Code to install maven
array=( "159.203.129.188")

#for ip in "${array[@]}"
#do
#	ssh -i ~/.ssh/id_rsa root@$ip 'apt-get update -y'
#    ssh -i ~/.ssh/id_rsa root@$ip 'apt-get install -y default-jdk'
#    ssh -i ~/.ssh/id_rsa root@$ip 'apt-get install -y maven'
#done

#for ip in "${array[@]}"
#do
#	ssh -i ~/.ssh/id_rsa root@$ip 'killall java'
#    ssh -i ~/.ssh/id_rsa root@$ip 'rm -rf code'
#    ssh -i ~/.ssh/id_rsa root@$ip 'mkdir code'
#    scp -i ~/.ssh/id_rsa ~/Desktop/ip_proj2/P2MP-FTP/target/P2MP-FTP-0.1-jar-with-dependencies.jar root@$ip:code/P2MP-FTP-0.1-jar-with-dependencies.jar
#    scp -i ~/.ssh/id_rsa ~/Desktop/ip_proj2/P2MP-FTP/target/abc.txt root@$ip:code/abc.txt
#done


for ip in "${array[@]}"
do
	ssh -i ~/.ssh/id_rsa root@$ip 'killall java'
    ssh -i ~/.ssh/id_rsa root@$ip 'cd code;rm -rf abcd'
    ssh -i ~/.ssh/id_rsa root@$ip 'cd code;java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abcd 0.05' &
done

cd target
killall java
java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient 159.203.129.188 7735 abc.txt 500

for ip in "${array[@]}"
do
    ssh -i ~/.ssh/id_rsa root@$ip 'cd code;diff abc.txt abcd'
done

