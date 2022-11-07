from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from sklearn.decomposition import PCA
from matplotlib import pyplot as plt
import numpy as np
import string

def get_word_embeds():
    punctuation_stripper = str.maketrans('', '', string.punctuation)

    f = open("spacy_masked_sents.txt", "r")

    sentences = f.readlines()

    f.close()

    cleaned_sents_w_labels = [sent.strip().split(",") for sent in sentences] # cleaned sentences with labels
    # print(cleaned_sents_w_labels)
    cleaned_sents_wo_labels = [sent.translate(punctuation_stripper).replace("True", "").replace("False", "").split() for sent in sentences] # cleaned labels with labels
    # print(cleaned_sents_wo_labels)
    model = Word2Vec(sentences=cleaned_sents_wo_labels, min_count=1, vector_size=4) # word2vec model
    vectors = model.wv.vectors # all word vectors in model

    X = vectors

    sent_vecs = []

    for sent in cleaned_sents_wo_labels:
        curr_sent = np.array([np.array(model.wv[word], dtype="float") for word in sent], dtype="object")

        # sent_pca = pca.fit_transform(curr_sent)

        sent_vecs.append(curr_sent)

    vx, vy = np.array(sent_vecs, dtype="object"), np.array([sent[-1] for ndx, sent in enumerate(cleaned_sents_w_labels)])

    return vx, vy

X, y = get_word_embeds()
print(X[0])
