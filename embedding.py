from flair.embeddings import WordEmbeddings
from flair.data import Sentence

# cleans masked_sents.txt file 
fin = open ("masked_sents.txt", "r")
fout = open("masked_sents_revised.txt","wt")

for index, line in enumerate(fin):
    if index%2==0:
        line = line.replace('__NE_FROM__','NEFROM')
        line = line.replace('__NE_TO__', 'NETOOO')
        line = line.replace('.','')
        fout.write(line)

# a function that takes a file with one sentence per line as input.
# each sentence becomes a list of arrays, where each array is a 100-dim
# word embedding. 
# the output is a list of sentences (a list of lists)
def get_word_embeddings(f):
    
    f = open(f,"r")
    
    # uses pretrained GloVe embeddings, these could later be changed to
    # stacked embeddings
    glove_embedding = WordEmbeddings('glove')
    
    list_of_sentences = []
    words_in_sentence = []
    
    # reads each line in the file, embeds words, passes them to a numpy array,
    # and constructs a list of numpy arrays to represent the entire sentence.
    # it does this for the first 30 lines in the file; this can easily be
    # changed to read the whole file with a more powerful machine
    for x in range(0,30):
        line = f.readline()
        if line != '' and line != 'None':
            sentence = Sentence(line)
            glove_embedding.embed(sentence)
            for token in sentence:
                emb_token = token.embedding.numpy()
                words_in_sentence.append(emb_token)
                list_of_sentences.append(words_in_sentence)
                
    print(list_of_sentences)
    return list_of_sentences

get_word_embeddings("masked_sents_revised.txt")
    
        
    