
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import os

N_true  =  50   # The number of documents to generate with label "true."
N_false = 100   # The number of documents to generate with label "false."

BASE_DIR = 'data'
TRUE_DIR =  os.path.join(BASE_DIR, 'A_sup_B')
FALSE_DIR = os.path.join(BASE_DIR, 'none')
if not os.path.exists(TRUE_DIR):
    os.makedirs(TRUE_DIR)
if not os.path.exists(FALSE_DIR):
    os.makedirs(FALSE_DIR)

# Sentence patterns where A-supplies-B is present.
sent_patterns_true = [
    '__NE_FROM__ supplies __NE_TO__ with __PRODUCT__',
]

# Sentence patterns where A-supplies-B is absent. (Maybe B-supplies-A, or
# A-and-B-are-partners, or none of the above.)
sent_patterns_false = [
    '__NE_FROM__ buys __PRODUCT__ from __NE_TO__',
]


