# Discriminer: A tool of discriminating program traces (runs) based on observations
### Goal: Program Confidentiality Analysis in presence of Side-Channel

#### Requirement:
Python 2.7 or more
Libraries: Numpy, Pandas, matplotlib, sklearn, and scipy

## Description
Dicriminer is a combination of program analysis and machine learning tool which helps users to learn a classification model about program traces. First, a program is analyzed and input data is obtained using dynamic analysis. This data features can be function call, basic block, branches and so on. In addition, the data should include an observable feature like time of execution or memory consumption for each record (each record of data is equal to a program trace). Note that due to probabilistic moldeling, the data should include 10 time of observations for the same trace (record), or it may include mean and standard deviation for each tarce (record). Given this data as input, the clustering part will divide the data into number of clusters (specified by the user) based on distance measures like euclidean distance using mean of each record. As a result, each record is assigned to a cluster based on its mean. In the next step, the clustering procedure considers a stochastic distribution for each observation, and it calculates a probability (weight) for the record if it assigns to different clusters. For example, suppose t1 is a trace which has time observation such that mean of execution time is 3.6 seconds and standard deviation is 161 as follow:
![Alt](weight_definition.jpg)
The clustering based on mean will assign it to Cluster_1, but due to probabilistic behavior of time (assume to be normal random variable), we also consider the probability to be assigned to other clusters like Cluster_0 and add weight feature to each record to show it. 
