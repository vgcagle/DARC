
import pandas as pd

pd.set_option('display.max.columns',100)
pd.set_option('display.width',190)

ra = pd.read_csv("character_relation_annotations.tsv",sep="\t")
bb = pd.read_csv("breakingBad.csv").reset_index()
all = pd.concat([ra,bb])
all.drop(['index','annotator','author','detail'],axis=1,inplace=True)

print("Overview:")
print(all)
print("==================================================")
print("Anna K:")
print(all[all.title=='Anna Karenina'].drop('title',axis=1))
