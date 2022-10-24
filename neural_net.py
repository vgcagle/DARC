"""
Created on Tue Oct 11 17:43:14 2022

@author: vgcag
"""

import numpy as np
from pandas import array
import tensorflow as tf
from tensorflow import keras
from keras import layers

from sklearn import preprocessing
from sklearn.metrics import f1_score
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score 
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split

import numpy as np

from gensim_embeddings import get_word_embeds
import numpy as np
from sklearn.preprocessing import LabelBinarizer

np.set_printoptions(suppress=True)

X, y = get_word_embeds()
tf_X = tf.convert_to_tensor(X, dtype=tf.float32)
# print(f"first y: {y}")
# Make the False/True labels 0/1 
lb = LabelBinarizer(neg_label=0, pos_label=1)
y = lb.fit_transform(y)
# print(f"second y: {y}")


Xtrain, Xtest, ytrain, ytest = train_test_split(tf_X, y, train_size=.8,
    shuffle=True)
#build the model 
model = keras.Sequential([
    layers.Dense(48, activation="relu"),
    layers.Dense(48, activation="relu"),
    layers.Dense(1, activation="sigmoid")
])

#Compile the model
model.compile(optimizer="rmsprop",
              loss="binary_crossentropy",
              metrics=["accuracy"])

model.fit(Xtrain, ytrain, epochs=50, batch_size=512)
results = model.evaluate(Xtest, ytest)

ypred = model.predict(Xtest)

for ndx, pred in enumerate(ypred):
    # print(pred, pred[0])
    if pred[0] < 0.5:
        ypred[ndx] = int(0)
    else:
        ypred[ndx] = int(1)

#need to make these values 0 and 1 
print(f"Confusion Matrix:\n{confusion_matrix(ytest,ypred)}")
print(f"F1 Score: {f1_score(ytest, ypred)}")
print(f"Precision Score: {precision_score(ytest, ypred)}")
print(f"Recall Score: {recall_score(ytest, ypred)}")