import numpy as np
from tensorflow import keras
from keras import layers

from sklearn.preprocessing import LabelBinarizer
from sklearn.metrics import f1_score
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score 
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split

from gensim_embeddings import get_word_embeds

from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import LSTM
from keras.optimizers import Adam

np.set_printoptions(suppress=True)

X,y = get_word_embeds()

#Make the False/True labels 0/1 
lb = LabelBinarizer(neg_label=0, pos_label=1)
y = lb.fit_transform(y)

import tensorflow as tf

Xtrain, Xtest, ytrain, ytest = train_test_split(X, y, train_size=.8, shuffle=True)

# Xtrain = Xtrain.reshape(Xtrain.shape[0], Xtrain.shape[1], 1)

# lstm = tf.keras.layers.LSTM(16, return_sequences=True, return_state=True)
# whole_seq_output, final_memory_state, final_carry_state = lstm(X)
#print(final_memory_state)

model = Sequential()

#Adding the input LSTM network layer
model.add(LSTM(128, input_shape=Xtrain.shape[1:], return_sequences=True))
model.add(Dropout(0.5))
#Adding a second LSTM network layer
#classifier.add(LSTM(128))

#Adding a dense hidden layer
model.add(Dense(16, activation='tanh'))
model.add(Dropout(0.5))

#Adding the output layer
model.add(Dense(1, activation='sigmoid'))

#Compiling the network
model.compile(loss='sparse_categorical_crossentropy',
              optimizer=Adam(learning_rate=0.001),
              metrics=['accuracy'] )

#Fitting the data to the model
model.fit(Xtrain,
          ytrain,
          epochs=50)

test_loss, test_acc = model.evaluate(Xtest, ytest)
print('Test Loss: {:.3f}'.format(test_loss))
print('Test Accuracy: {:.3f}'.format(test_acc))
