import pandas as pd

a = pd.read_csv("result_time.csv",header=0)
b = pd.read_csv("results_raw_data_results.txt.csv",header=0)
merged = pd.merge(a, b, left_on = 'id', right_on = 'id')
merged.to_csv("final_result.csv", index=False)
