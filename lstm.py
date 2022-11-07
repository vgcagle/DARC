# -*- coding: utf-8 -*-
"""
Created on Mon Oct 31 14:22:47 2022

@author: vgcag
"""

import numpy as np
from tensorflow import keras
from tensorflow.keras import layers
from sklearn import preprocessing


from flair.embeddings import WordEmbeddings
from flair.data import Sentence
from sklearn.metrics import f1_score
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score 
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split


from distutils.command.clean import clean
from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from sklearn.decomposition import PCA
from matplotlib import pyplot as plt
import numpy as np
import string

from gensim_embeddings import get_word_embeds
import gensim
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from gensim.models.keyedvectors import KeyedVectors
import gensim.downloader as api
from matplotlib import pyplot as plt
import numpy as np
import string
from sklearn.preprocessing import LabelBinarizer

from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import LSTM
from keras.utils import np_utils
from keras.optimizers import Adam

from keras.layers import Flatten
from keras.layers import Embedding


np.set_printoptions(suppress=True)






X,y = get_word_embeds()

#Make the False/True labels 0/1 
lb = LabelBinarizer(neg_label=0, pos_label=1)
y = lb.fit_transform(y)

import tensorflow as tf

X = np.reshape(X, X.shape + (1,))
Xtrain, Xtest, ytrain, ytest = train_test_split(X, y, train_size=.8,
    shuffle=True)

# lstm = tf.keras.layers.LSTM(16, return_sequences=True, return_state=True)
# whole_seq_output, final_memory_state, final_carry_state = lstm(X)
#print(final_memory_state)


classifier = Sequential()

#Adding the input LSTM network layer
classifier.add(LSTM(128, input_shape=(Xtrain.shape[1:]), return_sequences=True))
classifier.add(Dropout(0.5))
#Adding a second LSTM network layer
#classifier.add(LSTM(128))

#Adding a dense hidden layer
classifier.add(Dense(16, activation='tanh'))
classifier.add(Dropout(0.5))

#Adding the output layer
classifier.add(Dense(1, activation='softmax'))

#Compiling the network
classifier.compile( loss='sparse_categorical_crossentropy',
              optimizer=Adam(learning_rate=0.001, decay=1e-6),
              metrics=['accuracy'] )

#Fitting the data to the model
classifier.fit(Xtrain,
         ytrain,
          epochs=3,
          validation_data=(Xtest, ytest))#Compiling the network
classifier.compile( loss='sparse_categorical_crossentropy',
              optimizer=Adam(learning_rate=0.001, decay=1e-6),
              metrics=['accuracy'] )

#Fitting the data to the model
classifier.fit(Xtrain,
         ytrain,
          epochs=3,
          validation_data=(Xtest, ytest))

test_loss, test_acc = classifier.evaluate(Xtest, ytest)
print('Test Loss: {}'.format(test_loss))
print('Test Accuracy: {}'.format(test_acc))

#ypred = classifier.predict(Xtest)

# for ndx, pred in enumerate(ypred):
#     if pred[0] < 0.5:
#         ypred[ndx] = int(0)
#     else:
#         ypred[ndx] = int(1)
#predicted_cat = 
#need to make these values 0 and 1 
# print(confusion_matrix(ytest,ypred))
# print(f1_score(ytest, ypred))
# print(precision_score(ytest, ypred))
# print(recall_score(ytest, ypred))

# vocab_size= 2000
# max_length = 2
# classifier.add(Embedding(vocab_size, 8, input_length=max_length))