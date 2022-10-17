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
    model = Word2Vec(sentences=cleaned_sents_wo_labels, min_count=1, vector_size=30) # word2vec model
    vectors = model.wv.vectors # all word vectors in model

    X = vectors

    # sklearn PCA decomposition for word vectors
    pca = PCA(n_components=2)
    result = pca.fit_transform(X)


    # creating scatter plot for vectors
    plt.scatter(result[:, 0], result[:, 1])

    words = list(model.wv.index_to_key)

    # print(len(words))

    for ndx, word in enumerate(words):
        plt.annotate(word, xy=(result[ndx, 0], result[ndx, 1]))

    vx, vy = np.array([vector for vector in pca.components_]), np.array([sent[-1] for ndx, sent in enumerate(cleaned_sents_w_labels)])

    return vx, vy

