from flair.data import Sentence
from flair.models import SequenceTagger
import os

__author__ = "Ryan Persinger"

masked_sent_list = []


def get_amt_of_org_tags(sentence):

    org_count = 0

    for entity in sentence.get_spans('ner'):
        org_name =  str(entity.text)
        perc = round(float(entity.get_label("ner").score) * 100, 3)
        print(f"{perc}% sure that {org_name} is an organization.")

        if "ORG" == entity.get_label("ner").value:
            org_count += 1
    
    return org_count


def mask_sentence(sentence: str):

    #initial_sentence
    init_sentence = sentence.strip()
    print(f"initial sentence: {init_sentence}")

    # make a sentence
    sentence = Sentence(init_sentence)

    # load the NER tagger
    tagger = SequenceTagger.load("ner-large")

    # run NER over sentence
    tagger.predict(sentence)
    # print(sentence.get_labels())
    
    org_count = get_amt_of_org_tags(sentence)

    if org_count != 2:
        return "Sentence reject"

    org_list = []

    for entity in sentence.get_spans('ner'):
        NER_label = str(entity.get_label("ner").value)
        
        if NER_label == "ORG":
            org_name = str(entity.text)
            org_list.append(org_name)

    init_sentence = init_sentence.replace(org_list[0], "NEFROM") # change first org to NEFROM
    masked_sent = init_sentence.replace(org_list[1], "NETOOO") # change second org to NETOOO
    # masked_sent_list.append(masked_sent)

    # print(masked_sent)
    return masked_sent


dir_path = os.path.dirname(os.path.realpath(__file__)) # get path to directory containing synthetic data
dir_path = os.path.join(dir_path, "synthetic") # join path to data directory
directory = os.path.join(dir_path, "data")
# print(directory)
ms_file = open("masked_sents.txt", "w")

for dir in os.listdir(directory): # loop through data directory
    subdir = os.path.join(directory, dir) # get sub-directory
    for filename in os.listdir(subdir): # loop through files in current sub-directory
        f = os.path.join(subdir, filename) # get filename paths

        if os.path.isfile(f): # check if path is a file
            with open(f) as txt_file: # open file
                lines = txt_file.readlines() # read the lines of a file
                
                for line in lines: # loop through the lines in the file
                    ms = mask_sentence(line) # run the mask_sentence function with the current line as an argument

                    if ms is None:
                        continue

                    print(ms) # print the masked sentence
                    ms_sent = Sentence(ms)
                    
                    if str(dir) == "A_sup_B":
                        ms_sent.add_label("relation", "true")
                    else:
                        ms_sent.add_label("relation", "false")

                    print(ms_sent.labels)

                    for label in ms_sent.labels:
                        ms_file.write(f"{ms_sent.to_plain_string()},{label.value}\n")

ms_file.close()