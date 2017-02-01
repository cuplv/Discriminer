#/bin/sh
# start.sh for GabFeed
file = "./raw_data_results.txt"
file1 = "./results_raw_data_results.txt.csv"
file2 = "./result_time.csv"
file3 = "./final_result.csv"

if [ -f "$file"]
then
    rm -rf ./raw_data_results.txt
else
    echo "The raw_data_results.txt file does not exist"
fi

if [ -f "$file1"]
then
    rm -rf ./results_raw_data_results.txt.csv
else
    echo "The results_raw_data_results.txt.csv file does not exist"
fi

if [ -f "$file2"]
then
    rm -rf ./result_time.csv
else
    echo "The result_time.csv file does not exist"
fi

perl run_many_times.pl $1
java -cp callRecord-1.jar stac.discriminer.parser.parserInteger raw_data_results.txt
if [ -f "$file3"]
then
    rm -rf ./final_result.csv
else
    echo "The final_result.csv file does not exist"
fi

python merge.py
echo "Please find the final data set in final_result.csv"

if [ -d $1]
then
    sleep 1
else
    mkdir $1
fi
mv ./raw_data_results.txt ./$1/
mv ./results_raw_data_results.txt.csv ./$1/
mv ./result_time.csv ./$1/
mv ./final_result.csv ./$1/
