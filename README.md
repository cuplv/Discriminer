# Discriminer: Discriminating traces with Time

## Description
Dicriminer is a combination of dynamic analysis and machine learning techniques which helps users to learn a discriminant model about program trace based on external observation. [First](https://github.com/cuplv/Discriminer/blob/master/README.md#data-extraction), a program is analyzed and a data set of the program behaviors is obtained using dynamic instrumentation techniques. The data set features can be function calls, basic block, branches and other internal program features. In addition, the data set should include an observable feature like time of execution or network packet information for each record (each record of data is equal to some features of a program trace). Note that due to probabilistic modeling, the data should include 10 times of observations for the same record, or it may include mean and standard deviation for each record. [In the next step](https://github.com/cuplv/Discriminer/blob/master/README.md#data-clustering-label-and-weight-calculations), given this data as input, the clustering part will divide the data into numbers of clusters (specified by the user) based on distance measures like euclidean distance using the mean of each record. As a result, each record is assigned to a class label based on a range of execution time. The clustering procedure considers a stochastic distribution for each observation, and it calculates a probability (weight) to be assigned to different clusters. For example, suppose t1 is a trace which has time observation such that the mean of execution time is 3.6 seconds and standard deviation is 161 as follow:
![Alt](weight_definition.jpg)
The clustering based on mean will assign it to both Cluster_1 and Cluster_0 with different weights 78 and 22, respectively. This is due to probabilistic behaviors of time (we consider time as a Guassian Distibution). The output data of clustering step will include the internal features of the program, class label (assigned cluster), and weight for each record. This data will be input for classification step ([final step](https://github.com/cuplv/Discriminer/blob/master/README.md#learning-classifier)). A classifier model like decision tree will learn a model which discriminates traces. As each label is equal to a cluster (for example a time range of execution), the classifier distinguishes traces based on the observations. If the inside features are function calls, for instance, the classifier will learn a model which explains timing differences based on function calls. As label 1 shows a range of time like [t1,t2], the model predicts that calling function f1 in a trace will determine the execution time of program trace. This observation can lead to confidentiality violation as described in the following: 
[Discriminating trace using Time!](https://docs.google.com/a/colorado.edu/viewer?a=v&pid=sites&srcid=Y29sb3JhZG8uZWR1fHNhZWlkLXRpenBhei1uaWFyaXxneDpjODY1NzIyZmMxNGYxMGU) </br>
### Steps: [Data Extraction](https://github.com/cuplv/Discriminer/blob/master/README.md#data-extraction) -> [Data Clustering](https://github.com/cuplv/Discriminer/blob/master/README.md#data-clustering-label-and-weight-calculations) -> [Learning Classifier](https://github.com/cuplv/Discriminer/blob/master/README.md#learning-classifier)

### Goal: Program Confidentiality Analysis in presence of Side-Channel
#### Requirement:
Python 2.7 or more </br>
Libraries: Numpy, Pandas, matplotlib, sklearn, and scipy </br>

## Data Extraction
The input data for Discriminer should be put inside **Clustering_input**. The format of the file should be **.csv**. In order to extract data from a target program, you can use standard dynamic tools like **Soot**. However, these tools are very broad and support many functions. We will provide you a compact tool which collects necessary data from a target program (coming soon). 
The input data in **.csv** format should have three main parts: 1- ID for each record (unique number) 2- 10 features named **"T1"**...**"T10"** which set to the values of 10 measurements of the same record. For example, you may execute a program with the same input 10 times, and **T1**...**T10** are time recorded in each execution. This is one way to introduce observations like time to the input data set. As another way, you can include two features named **"mean"** and **"std"** which are the mean and standard deviation for each record. If your records are constant, you can set all **"T1"**...**"T10"** to constant value, or if you use **"mean"** and **"std"** features, the mean feature should set to the constant value and standard deviation should set to  0. 3- Features about program inside behaviors. For example, these features can be function names, and their values can be the number of times the functions are called. 
You can find some examples of the input data inside **Clustering_input**. These data include both ways to represent the behaviors (using either **"T1"** ...**"T10"** or **"mean"** **"std"**). 
## Data Clustering (label and weight calculations)
After putting data inside **Clustering_input**, you can run **Cluster.py** to cluster data and calculate required features for classification step (to run: python Cluster.py). The program asks users about the name of the input file (just enter the file name without any relative or absolute address and any type). Then, it will ask whether you have the features **"T1"**...**"T10"**. If the answer is "no", you should put **"mean"** and **std** features inside the data. Then, it will ask about the name of output files namely clustering plot name and clustering result data set. The following is the result of clustering plot for a benchmark named SnapBuddy_2 (axis y is mean of execution time and axis x is data ID): 
![Alt](Clustering_results/cluster_result_data_snapBuddy1.png) </br>
As explained earlier, **"mean"** feature is the distance measurement to cluster data. So. the plot shows data clustering based on the mean measurement of each record. Then, the program calculates boundaries between clusters. The boundaries are calculated as follow: first, it sorts all data inside different clusters. Then, it picks the maximum value of cluster n and the minimum value of cluster n+1. The boundary line is the average of these two numbers. In the case of t1, the boundary line between cluster_0 and cluster_1 is about 3.5 seconds. Considering the measurement (like time) is normal random variable, we calculate the probability of a record to assign all clusters. For this aim, we calculate **Cumulative distribution function** of normal distribution in every cluster boundaries. For example, cfd of t1 is 0.78 to be assigned to Cluster_1 and 0.22 in the case of Cluster_0. So, t1 will replicate two times with the same ID. One labeled Clsuter_0 and weighted 22 and another labeled Cluster_1 and weighted 78. The rest of the features will be the same for both of them. In this fashion, the output of clustering procedure will be calculated and fed to classification step (as input). The output file will go into **Clustering_results** in **.csv** format. For classification step, you need to copy the result file inside **Clustering_results** into **Classification_input**.       
## Learning Classifier
The classification procedure considers input file to be inside **Classification_input**. You can run the program by typing following command: python Classify.py 
The program asks about input file name (name which is inside **Classification_input** folder without any type). As it uses [K-fold cross_validation](http://scikit-learn.org/stable/modules/cross_validation.html) to split learning and testing data, the program asks you to enter K. Note that if you press enter without any number, the default value will be considered. As the classifier is a decision tree, the program asks if the user prefers to bound the depth of decision tree to a constant value. The program applies decision tree learning model and calculates accuracy for the model. Finally, it will produce three top accurate decision trees in **.dot** format inside **Classification_results** and print out the accuracy of each tree at the end of program execution. Users can enter the following command in terminal to produce final trees (for example, suppose the output file is cluster_result_data_gabfeed2_tree0.dot): </br>
dot -Tpng Classification_results/cluster_result_data_gabfeed2_tree0.dot -o Classification_results/cluster_result_data_gabfeed2_tree0.png </br>
And the result decision tree will look like the following (partial decision tree, see the full one inside **Classification_results**): 
![Alt](sample_decision_tree_1.png) </br>
The decision tree can be seen as a set of discrimination formulae. For example, it says that if **"image.BufferedImageOp.filter"** is not called, then the record belongs to Cluster_0 (As it has much more samples in comparison to other clusters). The another example is the one which assigns records to last cluster namely Cluster_5. If **"image.BufferedImageOp.filter"** and **"image.OilFilterPixel"** are called, while **"lang.Math.sqrt"** is not called, then the data record should be assigned to Cluster_5. 
## Run Discriminer (Non-Interactive)
### Clustering Phase
python Cluster.py --filename Clustering_input/data_snapBuddy1.csv --measurements yes --clusters 6 --output cluster_result_snapBuddy1 
### Classification Phase
python Classify.py --filename cluster_result_snapBuddy1.csv --kfolds 20 --output snapBuddy1_decisionTree
### View Clustering Results
open cluster_result_snapBuddy1.png
### View Decision Trees
dot -Tpng snapBuddy1_decisionTree_tree0.dot -o tree.png     (instead of tree0, you can have tree1 or tree2)  </br>
open tree.png  
## Run Discriminer (Interactive)
In this part, we show the steps to run Discriminer for provided inputs:
### Clustering Phase
Please make sure that the input data is inside **Clustering_input** and it has id, T1,..,T10, function features. Note that instead of T1, ...,T10, you can provide mean and std features. Here, we show the process for SnapBuddy data set: </br> 
1. python Cluster.py  </br>
2. (Enter the name of your input data set (.csv) without file type): data_snapBuddy1  </br>
3. (Are 10-measurements included in file as features T1 ... T10 (yes(y)/no(n))?) yes  </br>
4. (Enter number of clusters to divide data set (default is 2):) 6   </br>
5. (Enter the name of plot file for clustering:) plot_data_snapBuddy1   </br>
6. (Enter the name of output data set (.csv) file without file type:) csv_data_snapBuddy1   </br>
The result of clustering process will be available inside **Clustering_results** with the specified name of **output** and **.csv** files.  </br> 
### Classification Phase
The input file for classification should include id, function features, wegith, and lablel. Please make sure that your input data for classification is inside **Classification_input**. For this reason, we first copy the result of clustering process from **Clustering_results** to **Classification_input**.  </br> 
1. cp Clustering_results/csv_data_snapBuddy1.csv Classification_input  </br>
2. python Classify.py   </br>
3. (Enter the name of your input data set (.csv) without file type:) csv_data_snapBuddy1   </br>
4. (Please enter the number of random folds for cross-validation step (default is 20):) (press enter)  </br>
5. (Please enter the maximum depth of tree (do not specify any number if default value of algorithm is the best)?) (press enter)   </br>
The result of classification process will be available inside **Classification_results** in the format of .dot file for three most accurate decision trres (the accuracy for each of them will be shown in the output of classify.py).  </br>
Enter the following commands to convert them to .png file:  </br>
6. dot -Tpng Classification_results/csv_data_snapBuddy1_tree0.dot -o tree0.png  </br>
7. dot -Tpng Classification_results/csv_data_snapBuddy1_tree1.dot -o tree1.png  </br>
8. dot -Tpng Classification_results/csv_data_snapBuddy1_tree2.dot -o tree2.png  </br>
</br> 
For the GabFeed, the process of clustering and classifying will be the same (using data_gabfeed2 as input for clusrering), except that you should specify the maximum depth of tree in step 5 of classifying equal to 3 to get the highest accuracy. 
