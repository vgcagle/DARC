
# Basic NER with spaCy

import spacy
import pandas as pd

# Requires "python -m spacy download en_core_web_md"
nlp = spacy.load("en_core_web_md")

# So we can look at individual sentences.
nlp.add_pipe("sentencizer")

# Season 1, episode 1
with open("s01ep001.txt", encoding="utf-8") as f:
    doc = nlp(f.read())

for sent in doc.sents:
    stuff = [ 'text','pos_','lemma_','tag_','dep_', 'ent_type_', 'ent_iob_' ]
    bb = pd.DataFrame({ s: [ getattr(t,s) for t in sent ] for s in stuff })
    print(bb)
    input("Press ENTER.")

