
import pandas as pd

pd.set_option('display.max.columns',100)
pd.set_option('display.width',190)

ra = pd.read_csv("character_relation_annotations.tsv",sep="\t")
bb = pd.read_csv("breakingBad.csv").reset_index()
sv = pd.read_csv("siliconValley.csv").reset_index()
all = pd.concat([ra,bb,sv])
all.drop(['index','annotator','author','detail'],axis=1,inplace=True)

print("Overview:")
print(all)
print("==================================================")
print("Anna K:")
print(all[all.title=='Anna Karenina'].drop('title',axis=1))
print("==================================================")
print("BB:")
print(all[all.title=='Breaking Bad S1E1'].drop('title',axis=1))
print("==================================================")
print("SV:")
print(all[all.title=='Silicon Valley S1'].drop('title',axis=1))
