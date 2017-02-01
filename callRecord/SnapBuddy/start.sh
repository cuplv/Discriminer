#!/bin/bash
file = "./raw_data_results.txt"
file1 = "./results_raw_data_results.txt.csv"
file2 = "./result_time.csv"
file3 = "./final_result.csv"

./bin/snapbuddyhost_1 -p 8080 -d data/ -k ServersPrivateKey.txt -w ServersPasswordKey.txt &
sleep 15

if [ -f "$file2"]
then
    rm -rf ./result_time.csv
else
    echo "The result_time.csv file does not exist"
fi
echo "It is measuring timing for each user. Please wait...!"
perl extractTimeofEachUserInInvite.pl > result_time.csv
kill $(ps aux | grep "com.cyberpointllc.stac.host.Main" | awk '{print $2}')

./bin/snapbuddyhost_callRecord -p 8080 -d data/ -k ServersPrivateKey.txt -w ServersPasswordKey.txt &
sleep 15

if [ -f "$file"]
then
    rm -rf ./raw_data_results.txt
else
    echo "The raw_data_results.txt file does not exist"
fi

perl visitUsersPublicPages.pl
kill $(ps aux | grep "com.cyberpointllc.stac.host.Main" | awk '{print $2}')
sleep 15

if [ -f "$file1"]
then
    rm -rf ./results_raw_data_results.txt.csv
else
    echo "The results_raw_data_results.txt.csv file does not exist"
fi

java -cp lib/callRecord-1.jar stac.discriminer.parser.parserBoolean raw_data_results.txt

if [ -f "$file3"]
then
    rm -rf ./final_result.csv
else
    echo "The final_result.csv file does not exist"
fi

python merge.py
echo "Please find the final data set in final_result.csv"