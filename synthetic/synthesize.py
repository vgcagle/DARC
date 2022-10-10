
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import os
import sys

rng = np.random.default_rng(seed=45)

N_true  =  500   # The number of documents to generate with label "true."
N_false = 1000   # The number of documents to generate with label "false."

BASE_DIR = 'data'
TRUE_DIR =  os.path.join(BASE_DIR, 'A_sup_B')
FALSE_DIR = os.path.join(BASE_DIR, 'none')

def prepare(directory):
    if not os.path.exists(directory):
        os.makedirs(directory)
    if len(os.listdir(directory)) != 0:
        response = input(f"Warning! Directory {directory} non-empty. Delete? ")
        if response.lower().startswith("y"):
            for f in os.listdir(directory):
                os.remove(os.path.join(directory,f))
        else:
            sys.exit("Aborting.")
                
prepare(TRUE_DIR)
prepare(FALSE_DIR)

# Sentence patterns where A-supplies-B is present.
sent_patterns_true = [
'__NE_FROM__ supplies __NE_TO__ with __PRODUCT__.',
'__NE_FROM__ sells __NE_TO__ __PRODUCT__.',
'On Wednesdays, __NE_FROM__ sells __NE_TO__ __PRODUCT__.',
'__NE_FROM__ sells __PRODUCT__ to __NE_TO__.',
'On Fridays, __NE_FROM__ sells __PRODUCT__ __NE_TO__.',
"One of __NE_FROM__'s biggest customers for its __PRODUCT__ is __NE_TO__.",
"One of __NE_FROM__'s oldest customers for its __PRODUCT__ is __NE_TO__.",
"One of __NE_FROM__'s most reliable buyers for its __PRODUCT__ is __NE_TO__.",
"One of __NE_FROM__'s biggest consumers of its __PRODUCT__ is __NE_TO__.",
"One of __NE_FROM__'s oldest consumers of its __PRODUCT__ is __NE_TO__.",
"One of __NE_FROM__'s steadiest consumers of its __PRODUCT__ is __NE_TO__.",
'Recently, __NE_FROM__ has been getting its __PRODUCT__ from __NE_TO__.',
]

# Sentence patterns where A-supplies-B is absent. (Maybe B-supplies-A, or
# A-and-B-are-partners, or none of the above.)
sent_patterns_false = [
    '__NE_FROM__ buys __PRODUCT__ from __NE_TO__.',
    '__NE_FROM__ gets its __PRODUCT__ from __NE_TO__.',
    'Recently, __NE_FROM__ has been buying its __PRODUCT__ from __NE_TO__.',
    'Recently, __NE_FROM__ has been using __NE_TO__ for __PRODUCT__.',
    '__NE_FROM__ and __NE_TO__ compete to sell __PRODUCT__.',
    "__NE_FROM__'s biggest competitor in __PRODUCT__ sales is __NE_TO__.",
    '__NE_FROM__ and __NE_TO__ joined forces to sell __PRODUCT__.',
    '__NE_FROM__ and __NE_TO__ are two of the biggest buyers of __PRODUCT__.',
    '__NE_FROM__ and __NE_TO__ are two of the biggest sellers of __PRODUCT__.',
    '__NE_FROM__ and __NE_TO__ are two important buyers of __PRODUCT__.',
    '__NE_FROM__ and __NE_TO__ are two important sellers of __PRODUCT__.',
    '__NE_FROM__ used to provide __NE_TO__ with __PRODUCT__.',
    '__NE_FROM__ used to provide __PRODUCT__ to __NE_TO__.',
    '__NE_FROM__ used to sell __NE_TO__ with __PRODUCT__.',
    '__NE_FROM__ used to sell __PRODUCT__ to __NE_TO__.',
    '__NE_FROM__ no longer provides __PRODUCT__ to __NE_TO__.',
    '__NE_FROM__ no longer sells __PRODUCT__ to __NE_TO__.',
]

# Organizations.
orgs = [ 'Microsoft','UMW','Google','Intel','Walmart','Target','Wegmans' ]

# Products.
count_noun_prods = [ 'microchip','chip','apple','computer','application',
    'carburetor' ]
mass_noun_prods = [ 'peanut butter','fuel','oil','natural gas' ]



def create_docs(N, directory, patterns, starting_docnum):
    for i in range(starting_docnum, starting_docnum+N):
        filename = os.path.join(directory,f"doc_{i:04d}.txt")
        from_org = rng.choice(orgs)
        to_org = from_org
        while from_org == to_org:
            to_org = rng.choice(orgs)
        if rng.random() < .7:
            product = rng.choice(count_noun_prods) + "s"
        else:
            product = rng.choice(mass_noun_prods)
        sentence = rng.choice(patterns).replace(
            '__NE_FROM__', from_org).replace(
            '__NE_TO__', to_org).replace(
            '__PRODUCT__', product)
        with open(filename, "w", encoding="utf-8") as f:
            print(sentence, file=f)
    print(f"{N} documents written.")

docnum = 1
create_docs(N_true, TRUE_DIR, sent_patterns_true, docnum)
create_docs(N_false, FALSE_DIR, sent_patterns_false, docnum)


# The Biden administration on Friday moved to choke off Beijing's supply of
# microchips used in advanced computing and military applications
