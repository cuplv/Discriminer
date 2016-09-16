import numpy as np
from sklearn import preprocessing, cross_validation, neighbors, tree
import pandas as pd
import random
from sklearn.tree import DecisionTreeClassifier
from sklearn.utils import shuffle
from sklearn import decomposition

filename = raw_input("Enter the name of your input data set (.csv) without file type: ")
kfolds = raw_input("Please enter the number of random folds for cross-validation step (default is 20)? ")
if(kfolds == ""):
    kfolds_numbers = 20
else:
    try:
        kfolds_numbers = int(kfolds)
    except ValueError:
        print("That's not an int!")
max_depth_tree = raw_input("Please enter the maximum depth of tree (do not specify any number if default value of algorithm is the best)? ")
if(max_depth_tree == ""):
    max_depth_tree_num = None
else:
    try:
        max_depth_tree_num = int(max_depth_tree)
    except ValueError:
        print("That's not an int!")

df = pd.read_csv("Classification_input/" + filename + ".csv",index_col = 'id')
header = list(df.columns.values)
header.remove('label')
header.remove('weight')


X = np.array(df.drop(['label'],1))
y = np.array(df['label'])
X, y = shuffle(X, y, random_state=0)

accuracy_max = 0

for i in range(3):
    kf = cross_validation.KFold(len(X), n_folds=kfolds_numbers, shuffle=True, random_state=None)
    for train_index, test_index in kf:
    #    X_train, X_test, y_train, y_test = cross_validation.train_test_split(X,y,test_size=0.2)
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        
        train_weights = X_train[:,-1]
        X_train = np.delete(X_train,-1,1)
        
        test_weights = X_test[:,-1]
        X_test = np.delete(X_test,-1,1)
        
        clf_temp = DecisionTreeClassifier(criterion='gini',splitter='best',max_depth=max_depth_tree_num)
        clf_temp.fit(X_train,y_train,train_weights)
        accuracy = clf_temp.score(X_test,y_test,test_weights)
        if(accuracy > accuracy_max):
            accuracy_max = accuracy
            clf = clf_temp

    out = "Classification_results/" + filename +'_tree'+str(i)+'.dot'
    tree.export_graphviz(clf,out_file=out,feature_names=header)
    print_out ='accuracy ' + filename +'_tree'+str(i) + ': '
    print_out = print_out + str(accuracy_max)
    print(print_out)
    accuracy_max = 0

print("\n The program generates three trees with highest accuracy. Please run: dot -Tpng Classification_results/" + filename +"_treen.dot" + " -o tree.png to see the final decision tree. Please note that treen is tree0, tree1, or tree2. \n")
