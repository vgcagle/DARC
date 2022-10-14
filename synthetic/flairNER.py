from flair.data import Sentence
from flair.models import SequenceTagger
import re
import os

__author__ = "Ryan Persinger"

masked_sent_list = []

org_regex = r"\"(.*?)\"" # regex pattern to pull organization name from NER tag
perc_regex = r"\([0-1].[0-9]*\)" # regex pattern to pull the percentage from NER tag


def get_amt_of_org_tags(sentence):

    org_count = 0

    for ndx in range(len(sentence.get_labels())): # loop through sentence labels (sentence.get_labels() returns the NER tags as a python list type)

        NER_tag = str(sentence.get_labels()[ndx]) # casting NER tag at current index to string
        

        reg_res = re.search(org_regex, NER_tag) # find the ORG name using regex
        org_name = reg_res.group(0) # org name found by regex pattern
        org_name = org_name.replace("\"", "") # remove quotes from organization name

        reg_res = re.search(perc_regex, NER_tag) # find percentage in NER_tag using regex pattern
        perc = reg_res.group(0) # percentage found by regex pattern
        perc = perc.replace("(", "")
        perc = perc.replace(")", "")
        perc = float(perc)

        print(f"{round(perc * 100, 3)}% sure that {org_name} is an organization.")

        if "ORG" in NER_tag: # if ORG tag is in the NER tag
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
        return

    org_list = []
    
    for ndx in range(len(sentence.get_labels())):
        NER_tag = str(sentence.get_labels()[ndx]) # casting NER tag at current index to string
        

        reg_res = re.search(org_regex, NER_tag) # find the ORG name using regex
        org_name = reg_res.group(0) # org name found by regex pattern
        org_name = org_name.replace("\"", "") # remove quotes from organization name
        org_list.append(org_name)

    init_sentence = init_sentence.replace(org_list[0], "NEFROM") # change first org to NEFROM
    init_sentence = init_sentence.replace("NEFROM's", "NEFROM") # removing apostrophe s
    masked_sent = init_sentence.replace(org_list[1], "NETOOO") # change second org to NETOOO
    masked_sent_list.append(masked_sent)

    # print(masked_sent)
    return masked_sent


dir_path = os.path.dirname(os.path.realpath(__file__)) # get path to directory containing synthetic data
directory = os.path.join(dir_path, "data") # join path to data directory

for dir in os.listdir(directory): # loop through data directory
    subdir = os.path.join(directory, dir) # get sub-directory
    for filename in os.listdir(subdir): # loop through files in current sub-directory
        f = os.path.join(subdir, filename) # get filename paths

        if os.path.isfile(f): # check if path is a file
            with open(f) as txt_file: # open file
                lines = txt_file.readlines() # read the lines of a file
                
                for line in lines: # loop through the lines in the file

                    ms = mask_sentence(line) # run the mask_sentence function with the current line as an argument
                    print(ms) # print the masked sentence

ms_file = open("masked_sents.txt", "a")

for sent in masked_sent_list:
    ms_file.write(f"{sent}\n")

ms_file.close()