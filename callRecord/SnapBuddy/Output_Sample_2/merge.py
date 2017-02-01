import pandas as pd
import numpy as np
a = pd.read_csv("result_time.csv",header=0)
b = pd.read_csv("results_raw_data_results.txt.csv",header=0)
# The first row is the authentication trace - exclude it!
b = b[1:]
id_array = np.array(b['id'])
b['id'] = [x-1 for x in id_array]
id_array = np.array(a['id'])
a['id'] = [x-1 for x in id_array]
merged = pd.merge(a, b, left_on = 'id', right_on = 'id')
merged.to_csv("final_result.csv", index=False)
