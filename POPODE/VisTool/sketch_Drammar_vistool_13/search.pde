
// Linear search on strings 
int search (String set[], String searchkey) {
  for (int i=0; i<set.length; i++) {
    //println("search: " + set[i] + ", " + searchkey);
    if (set[i].equals(searchkey)) {return i;}
  }
  return -1;
}


// Linear search on ArrayList equaling strings
int searchALindex_item (ArrayList a_l, String str, String type) {
  // println("searchAL: " + type + ", " + str);
  for (int i=0; i < a_l.size(); i++) {
    Object o_i = a_l.get(i);
    if (type.equals("SE")) {if (((Sequence_element) o_i).id.equals(str)){return i;};};
    if (type.equals("HE")) {if (((Hierel) o_i).id.equals(str)){return i;};};
    if (type.equals("NTS")) {if (((NonTerminalInSequence) o_i).id.equals(str)){return i;};};
    if (type.equals("NTH")) {if (((NonTerminalInHierarchies) o_i).id.equals(str)){return i;};};
    if (type.equals("TS")) {if (((TerminalInSequence) o_i).id.equals(str)){return i;};};
    if (type.equals("TH")) {if (((TerminalInHierarchies) o_i).id.equals(str)){return i;};};
    // if (type.equals("NTD")) {if (((NonTerminalDaughter) o_i).id.equals(str)) {return i;};};
    if (type.equals("TD")) {if (((TerminalInHierarchies) o_i).id.equals(str)){return i;};};
    if (type.equals("HCL")) {if (((HCluster) o_i).id.equals(str)) {return i;};};
    if (type.equals("HCS")) {if (((HCluster) o_i).idShort.equals(str)) {return i;};};
    if (type.equals("STR")) {if (((String) o_i).equals(str)) {return i;};};
  } // end for
  return -1;
}

// Linear search on ArrayList equaling strings on item and set
int searchALindex_item_set (ArrayList a_l, String item, String set, String type) {
  // println("searchAL: " + type + ", " + str);
  for (int i=0; i < a_l.size(); i++) {
    Object o_i = a_l.get(i);
    if (type.equals("TS")) {if (((TerminalInSequence) o_i).id.equals(item) && ((TerminalInSequence) o_i).nt.equals(set)) {return i;};}; 
    if (type.equals("TH")) {if (((TerminalInHierarchies) o_i).id.equals(item) && ((TerminalInHierarchies) o_i).set.equals(set)) {return i;};}; 
    // if (type.equals("NTD")) {if (((NonTerminalDaughter) o_i).id.equals(item) && ((NonTerminalDaughter) o_i).he.equals(set)) {return i;};}; 
    if (type.equals("SE")) {if (((Sequence_element) o_i).id.equals(item) && ((Sequence_element) o_i).nt.equals(set)) {return i;};}; 
  } // end for
  return -1;
}
