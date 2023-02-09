
import pandas as pd

pd.set_option('display.max.columns',100)
pd.set_option('display.width',190)

ra = pd.read_csv("character_relation_annotations.tsv",sep="\t")
ra.drop(['annotator','author','detail'],axis=1,inplace=True)

print("Overview:")
print(ra)
print("==================================================")
print("Anna K:")
print(ra[ra.title=='Anna Karenina'].drop('title',axis=1))
