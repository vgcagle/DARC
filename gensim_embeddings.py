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

    # sklearn PCA decomposition for word vectors
    pca = PCA(n_components=2)
    sent_vecs = []

    for sent in cleaned_sents_wo_labels:
        curr_sent = [list(model.wv[word]) for word in sent]

        # sent_pca = pca.fit_transform(curr_sent)

        sent_vecs.append(curr_sent)

    max_sent_len = len(max(sent_vecs, key=len))
    # print(max_sent_len)
    for ele in sent_vecs:
        difference = max_sent_len - len(ele)
        for i in range(difference):
            ele.append([0.0, 0.0, 0.0, 0.0])

    #sent_vecs = np.array([np.array(features_2d).flatten() for features_2d in sent_vecs])
    #result = pca.fit_transform(sent_vecs)

    # creating scatter plot for vectors
    #plt.scatter(result[:, 0], result[:, 1])

    words = list(model.wv.index_to_key)
        
    #for ndx, sent in enumerate(cleaned_sents_wo_labels):
    #    plt.annotate(" ".join(sent), xy=(result[ndx, 0], result[ndx, 1]))

    # plt.show()

    vx, vy = np.array(sent_vecs), np.array([sent[-1] for ndx, sent in enumerate(cleaned_sents_w_labels)])

    return vx, vy

X, y = get_word_embeds()
print(X)
