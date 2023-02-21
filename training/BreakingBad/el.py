
# Entity Linking to a KB with spaCy

# Trying to somewhat follow https://spacy.io/universe/project/video-entity-linking

import spacy
from spacy.kb import KnowledgeBase, InMemoryLookupKB

# Requires "python -m spacy download en_core_web_md"
nlp = spacy.load("en_core_web_md")

names = {}
descs = {}
names[1] = "Walter White"
descs[1] = "The protagonist"
names[2] = "Jesse Pinkman"
descs[2] = "The tool"
names[3] = "Skyler White"
descs[3] = "The wife"

def create_kb(vocab):
    kb = InMemoryLookupKB(vocab=vocab, entity_vector_length=300)
    for qid in range(1,4):
        desc_doc = nlp(descs[qid])
        desc_vec = desc_doc.vector
        kb.add_entity(entity=str(qid), entity_vector=desc_vec, freq=42)

    for qid in range(1,4):
        kb.add_alias(alias=names[qid], entities=[str(qid)], probabilities=[1])
    #print(kb.get_entity_strings())
    #print(kb.get_alias_strings())
    #kb.to_disk("bb_kb")
    return kb


# Stephen says: this is messed up. The current verson of spaCy requires you to
# set the KB of an EntityLinker by giving it a callable (function) that takes a
# vocab. I think I have done this with create_kb(), above. However, trying to
# add the pipeline stage as I attempt below blows up with "empty knowledge
# base" even though I haven't gotten to the next line to set it with .set_kb().
# Can anybody figure out how to do this?
el = nlp.add_pipe("entity_linker")
el.set_kb(create_kb)
#entity_linker = nlp.create_pipe("entity_linker", config={"incl_prior":False})
#entity_linker.set_kb(create_kb)
#el = nlp.add_pipe("entity_linker", config={"incl_prior":False}, last=True)
#other_pipes = [ pipe for pipe in nlp.pipe_names if pipe != "entity_linker" ]
#with nlp.disable_pipes(*other_pipes):


with open("s01ep001.txt", encoding="utf-8") as f:
    doc = nlp(f.read())

