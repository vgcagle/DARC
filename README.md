# Getting Up to Speed with the Project So Far...

There are a few files in the repo that are more or less deprecated. The main areas that you new guys need to worry about are ``spacyNER.py``, ``gensim_embeddings.py``, ``lstm.py``, and the directory named ``synthetic``.

(Side note: the directory named ``sandbox`` has some nice code to give you a general idea of how gensim word embeddings work)

## ``Sandbox`` directory instructions

The file ``gensimdemo.py`` has this line of code in it. Replace the ``"yourtextfile.txt`` with any text file you might have on your computer and run the the code.

When you run the program you should be given a prompt to enter a word in the vocabulary of your given text file. When you enter your very interesting word it will return the top 5 most similar words.

```py
with open("yourtextfile.txt",encoding="utf-8") as f:
```

## ``Synthetic`` directory instructions

Run the ``synthesize.py`` and it should create two brand new directories for your viewing pleasure.

One directory created is named ``A_sup_B`` which contains a text file of A supplies B relationships between companies.

The other directory is named ``none`` where the A supplies B relationships are absent.

## Sentence Masking Instructions

Now that you have the synthetic dataset you are free to run ``spacyNER.py``. This will create the ``spacy_masked_sents.txt`` text file which masks the synthetic dataset sentences and labels them as True or False.

## Word Embeddings

Now armed with the ``spacy_masked_sents.txt`` file you are free to run ``lstm.py``. This file will run the ``gensim_embeddings.py`` file which gives you the word embeddings of the masked sentences along with their labels. These word embeddings are handed off to the LSTM and the program returns the accuracy and loss of the test data which are indicators of how well the LSTM is classifying the masked sentences.