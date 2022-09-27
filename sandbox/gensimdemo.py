
from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
import string

punctuation_stripper = str.maketrans('', '', string.punctuation)

with open("yourtextfile.txt",encoding="utf-8") as f:
    lines = [ l.translate(punctuation_stripper).lower().split()
        for l in f.readlines() if len(l) > 5 ]

mymodel = Word2Vec(sentences=lines,min_count=1,vector_size=3)
vectors = mymodel.wv
print("The entire list of words, in alphabetical order, is:")
print(sorted(vectors.index_to_key))

word = input("\nEnter a word (or 'done'): ")
while word != "done":
    if word in vectors:
        print(f"The most similar words are:")
        for w, similarity in vectors.most_similar(word,topn=5):
            print(f"{w}: {similarity:.3f}")
    else:
        print(f"\"{word}\" is not in the corpus.")
    word = input("\nEnter a word (or 'done'): ")

