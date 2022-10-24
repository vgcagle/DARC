import os
import spacy
from spacy.vectors import Vectors
from spacy.vocab import Vocab
# import nltk
# from nltk.stem.snowball import SnowballStemmer

nlp = spacy.load("en_core_web_lg")
# sprint(nlp.pipe_names)

dir_path = os.path.dirname(os.path.realpath(__file__)) # get path to directory containing synthetic data
dir_path = os.path.join(dir_path, "synthetic") # join path to data directory
directory = os.path.join(dir_path, "data")
sms_file = open("spacy_masked_sents.txt", "w")

for dir in os.listdir(directory): # loop through data directory
    subdir = os.path.join(directory, dir) # get sub-directory
    for filename in os.listdir(subdir): # loop through files in current sub-directory
        f = os.path.join(subdir, filename) # get filename paths
        if os.path.isfile(f): # check if path is a file
            with open(f) as txt_file: # open file
                lines = txt_file.readlines() # read the lines of a file

                for line in lines:
                    
                    doc = nlp(line.strip())

                    org_list = []
                    org_count = 0

                    for ent in doc.ents:
                        if ent.label_ == "ORG":
                            org_count +=1
                            org_list.append(ent.text)
                        
                    # sentence masking
                    if org_count == 2:
                        # masked_sent = line.replace(f"{org_list[0]}'s", "NEFROM")
                        masked_sent = line.replace(f"{org_list[0]}", "NEFROM")
                        masked_sent = masked_sent.replace(org_list[1], "NETOOO")
                    else:
                        continue

                    
                    # stemmer = SnowballStemmer(language='english')
                    # masked_sent = " ".join([stemmer.stem(word) for word in masked_sent.split()])
                    # print(masked_sent)

                    # processing the masked sentence
                    new_doc = nlp(masked_sent.strip())

                    # print([(key, new_doc.vocab.strings[key]) for key in new_doc.vocab.vectors.keys()])

                    # removing stop words and getting word lemmas
                    final_masked_sent = " ".join([token.lemma_ for token in new_doc if not token.is_punct])
                    # final_masked_sent = masked_sent.replace("nefrom", "NEFROM")
                    # final_masked_sent = final_masked_sent.replace("netooo", "NETOOO")
                    print(final_masked_sent)

                    if dir == "A_sup_B":
                        sms_file.write(f"{final_masked_sent},True\n")
                    else:
                        sms_file.write(f"{final_masked_sent},False\n")