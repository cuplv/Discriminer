# Discriminer: A tool of discriminating program traces (runs) based on observations
### Goal: Program Confidentiality Analysis in presence of Side-Channel

#### Requirement:
Python 2.7 or more </br>
Libraries: Numpy, Pandas, matplotlib, sklearn, and scipy </br>

## Overall Description
Dicriminer is a combination of program analysis and machine learning tool which helps users to learn a classification model about program traces. First, a program is analyzed and input data is obtained using dynamic analysis. This data features can be function call, basic block, branches and so on. In addition, the data should include an observable feature like time of execution or memory consumption for each record (each record of data is equal to a program trace). Note that due to probabilistic moldeling, the data should include 10 time of observations for the same trace (record), or it may include mean and standard deviation for each tarce (record). Given this data as input, the clustering part will divide the data into number of clusters (specified by the user) based on distance measures like euclidean distance using mean of each record. As a result, each record is assigned to a cluster based on its mean. In the next step, the clustering procedure considers a stochastic distribution for each observation, and it calculates a probability (weight) for the record if it assigns to different clusters. For example, suppose t1 is a trace which has time observation such that mean of execution time is 3.6 seconds and standard deviation is 161 as follow:
![Alt](weight_definition.jpg)
The clustering based on mean will assign it to Cluster_1, but due to probabilistic behavior of time (assume to be normal random variable), we also consider the probability to be assigned to other clusters like Cluster_0 and add weight feature to each record to show it. The output data of clustering step will include inside features of program like function call, label, and weight for each record. This data will be input for classification step in which a classifier model like decision tree will learn a model which classifies (discriminates) traces. As each label is equal to a cluser (for example a time range of execution), the classifier distinguishes traces based on the observations. If the inside feature is function call, the classifier will show that if function f1() is called, then the trace should be assigned to label 1. As label 1 shows the range of time [t1,t2], we can learn that if function f1() is called, then it will take t1 to t2 seconds for trace to be terminated. This observation can lead to confidentiality violation as described in the following: 
[Discriminating trace using Time!](https://docs.google.com/a/colorado.edu/viewer?a=v&pid=sites&srcid=Y29sb3JhZG8uZWR1fHNhZWlkLXRpenBhei1uaWFyaXxneDpjODY1NzIyZmMxNGYxMGU) </br>
## Data Extraction
The input data for Discriminer should put inside 

