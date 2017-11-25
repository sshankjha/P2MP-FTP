mvn package
#Code to install maven
array=( "159.203.129.188" "45.55.169.199" "45.55.154.214")

#for ip in "${array[@]}"
#do
#	ssh -i ~/.ssh/id_rsa root@$ip 'apt-get update -y'
#    ssh -i ~/.ssh/id_rsa root@$ip 'apt-get install -y default-jdk'
#    ssh -i ~/.ssh/id_rsa root@$ip 'apt-get install -y maven'
#done

for ip in "${array[@]}"
do
	ssh -i ~/.ssh/id_rsa root@$ip 'killall java'
    ssh -i ~/.ssh/id_rsa root@$ip 'rm -rf code'
    ssh -i ~/.ssh/id_rsa root@$ip 'mkdir code'
    scp -i ~/.ssh/id_rsa ~/Desktop/ip_proj2/P2MP-FTP/target/P2MP-FTP-0.1-jar-with-dependencies.jar root@$ip:code/P2MP-FTP-0.1-jar-with-dependencies.jar
    scp -i ~/.ssh/id_rsa ~/Desktop/ip_proj2/P2MP-FTP/target/abc.txt root@$ip:code/abc.txt
done

cd target
for (( i=1; i<=2; i++))
	do
		for ip in "${array[@]}"
            do
                ssh -i ~/.ssh/id_rsa root@$ip 'killall java'
                ssh -i ~/.ssh/id_rsa root@$ip 'cd code;rm -rf abcd'
                ssh -i ~/.ssh/id_rsa root@$ip 'cd code;java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpserver 7735 abcd 0.10' &
            done

           
            killall java
            java -cp P2MP-FTP-0.1-jar-with-dependencies.jar main/P2mpclient 159.203.129.188 45.55.169.199 45.55.154.214 7735 abc.txt 500 > t3c10_$i.txt
            k=0
            for ip in "${array[@]}"
            do
                k=$((k+1))
                ssh -i ~/.ssh/id_rsa root@$ip 'cd code;diff abc.txt abcd' > t3c10_diff_"$k"_"$i".txt
            done
done