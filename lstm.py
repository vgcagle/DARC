# -*- coding: utf-8 -*-
"""
Created on Mon Oct 31 14:22:47 2022

@author: vgcag
"""

import numpy as np
import tensorflow as tf
from tensorflow import keras
from keras import layers
from sklearn import preprocessing

from sklearn.preprocessing import LabelBinarizer
from sklearn.metrics import f1_score
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score 
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split

from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from matplotlib import pyplot as plt

from gensim_embeddings import get_word_embeds

from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import LSTM
from keras.optimizers import Adam


np.set_printoptions(suppress=True)

X,y = get_word_embeds()

X = np.asarray(X).astype("float32")

#Make the False/True labels 0/1 
lb = LabelBinarizer(neg_label=0, pos_label=1)
y = lb.fit_transform(y)

#Reshape X to correct size for lstm
Xtrain, Xtest, ytrain, ytest = train_test_split(X, y, train_size=.8,
    shuffle=True)

#Build model 
classifier = Sequential()
#Adding the input LSTM network layer
classifier.add(LSTM(128, input_shape=(Xtrain.shape[1:]), return_sequences=True))
classifier.add(Dropout(0.5))

#classifier.add(Embedding(100, 32, input_length=2))
#Adding a second LSTM network layer
#classifier.add(LSTM(128))

#Adding a dense hidden layer
classifier.add(Dense(16, activation='tanh'))
classifier.add(Dropout(0.5))

#Adding the output layer
classifier.add(Dense(1, activation='sigmoid'))
#Compiling the network
classifier.compile(loss='binary_crossentropy',
                    optimizer=Adam(learning_rate=0.001, decay=1e-6),
                    metrics=['accuracy'] )

#Fitting the data to the model
classifier.fit(Xtrain,
            ytrain,
            epochs=22,
            batch_size=32)
            #validation_data=(Xtest, ytest))

ypred = classifier.predict(Xtrain)

test_loss, test_acc = classifier.evaluate(Xtest, ytest)
print('Test Loss: {}'.format(test_loss))
print('Test Accuracy: {}'.format(test_acc))
