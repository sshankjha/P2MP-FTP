#!/bin/bash
mvn package

#Code to install maven
ssh_key_loc="~/.ssh/id_rsa"
ssh_user_name="root"

#ip_list="ip1 ip2 ip3"
ip_list="138.197.22.178"

#array=( "ip1" "ip2" "ip3")
array=( "138.197.22.178")
num_of_test_runs=1
mss=1000
loss_probablity=0.01

echo "####################Installing Dependencies####################"

for ip in "${array[@]}"
do
	ssh -i $ssh_key_loc $ssh_user_name@$ip 'apt-get update -y'
    ssh -i $ssh_key_loc $ssh_user_name@$ip 'apt-get install -y default-jdk'
    ssh -i $ssh_key_loc $ssh_user_name@$ip 'apt-get install -y maven'
done

echo "####################Copying Binary/Executable####################"
for ip in "${array[@]}"
do
	ssh -i $ssh_key_loc $ssh_user_name@$ip 'killall java'
    ssh -i $ssh_key_loc $ssh_user_name@$ip 'rm -rf code'
    ssh -i $ssh_key_loc $ssh_user_name@$ip 'mkdir code'
    scp -i $ssh_key_loc ./target/P2MP-FTP-0.1-jar-with-dependencies.jar $ssh_user_name@$ip:code/P2MP-FTP-0.1-jar-with-dependencies.jar
    scp -i $ssh_key_loc ./test.txt $ssh_user_name@$ip:code/test.txt
done

cp test.txt target/test.txt
cd target
for (( i=1; i<=$num_of_test_runs; i++))
	do
		for ip in "${array[@]}"
            do
                echo "####################Stopping already running java processes on client " $ip " ####################"
                ssh -i $ssh_key_loc $ssh_user_name@$ip 'killall java'
                ssh -i $ssh_key_loc $ssh_user_name@$ip 'cd code;rm -rf testRecvd'
                ssh -i $ssh_key_loc $ssh_user_name@$ip 'cd code;java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 testRecvd '"$loss_probablity"'' &
            done

            echo "####################Stopping already running java processes on client####################"
            killall java
            java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient $ip_list 7735 test.txt $mss > logs_$i.txt
            for ip in "${array[@]}"
            do
                ssh -i $ssh_key_loc $ssh_user_name@$ip 'cd code;diff test.txt testRecvd' > file_diff_"$ip"_"$i".txt
            done
done

echo "####################Test Finished####################"