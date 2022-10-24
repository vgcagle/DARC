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

                    line = line.strip()
                    
                    doc = nlp(line)

                    org_list = []
                    org_count = 0

                    for ent in doc.ents:
                        if ent.label_ == "ORG":
                            org_count +=1
                            org_list.append(ent.text)
                        
                    # sentence masking
                    if org_count == 2:
                        masked_sent = line.replace(org_list[0], "NEFROM")
                        masked_sent = masked_sent.replace("NEFROM's", "NEFROM")
                        masked_sent = masked_sent.replace(org_list[1], "NETOOO")
                        
                        if dir == "A_sup_B":
                            sms_file.write(f"{masked_sent},True\n")
                        else:
                            sms_file.write(f"{masked_sent},False\n")
                    else:
                        continue
                    