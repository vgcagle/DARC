// Mute boxes 

class Mute_box {
  String id; 
  String hc; // hierarchy cluster (agent)
  float x_coord, y_coord;
  float x_width, y_height;

  Mute_box(String i, String a, float xc, float yc, float xw, float yh) {
    id = i;
    hc = a;
    x_coord = xc; y_coord = yc;
    x_width = xw; y_height = yh;
  } 
}


// Scene elements

class Scene {
  String id; 
  String type;
  String print;
  String rec_level;

  String mapping_init  = "NULL";
  String mapping_end = "NULL";
  
  ArrayList agents; 

  NonTerminalInSequence scene_mt;

  Scene(String i, String t, String p) {
    id = i;
    type = t;
    print = p;
    agents = new ArrayList(); 
    rec_level = "-1";
  } 
  
  float[] return_lm_rm() {
    float[] lm_rm = {-1, -1};
    int index_mapping_init = searchALindex_item (nonterminals_in_sequence, mapping_init, "NTS");
    if (index_mapping_init!=-1) {
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(index_mapping_init);
      if (searchALindex_item (terminals_in_sequence, nt.daughters_init, "TS") != -1) {
        TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, nt.daughters_init, nt.id, "TS"));
        lm_rm[0] = t.d_t.x_coord - unit_width/2 - unit_border; 
      } else {
        lm_rm[0] = nt.d_unit.x_coord - unit_width/2 - unit_border; 
      }
      // println("--- Drawing scene " + s.id + " from init " + nt.daughters_init);
      nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, mapping_end, "NTS"));
      if (searchALindex_item (terminals_in_sequence, nt.daughters_end, "TS") != -1) {
        TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, nt.daughters_init, nt.id, "TS"));
        lm_rm[1] = t.d_t.x_coord + unit_width/2 + unit_border; 
      } else {
        lm_rm[1] = nt.d_unit.x_coord + unit_width/2 + unit_border; 
      }
    }
  return lm_rm;
  }
}

Scene nullScene = new Scene("Null Scene Id", "SCE", "Null Scene Print"); 

// Act elements

class Act {
  String id; 
  String type;
  String print;

  String mapping_init  = "NULL";
  String mapping_end = "NULL";

  ArrayList agents; 

  NonTerminalInSequence act_mt;

  Act(String i, String t, String p) {
    id = i;
    type = t;
    print = p;
    agents = new ArrayList(); 
  } 
}

// Play elements

class Play {
  String id; 
  String type;
  String print;

  String mapping_init  = "NULL";
  String mapping_end = "NULL";

  ArrayList agents; 

  NonTerminalInSequence play_mt;

  Play(String i, String t, String p) {
    id = i;
    type = t;
    print = p;
    agents = new ArrayList(); 
  } 
}


// ****************** Hierarchy Elements *****************


class Hierel {
  String id; 
  String hcluster;
  String type;
  String print;
  String rec_level; 
  String accomplished;
  boolean barred;

  String mapping_init  = "NULL";
  String mapping_init_set  = "NULL";
  String mapping_end = "NULL";
  String mapping_end_set = "NULL";

  NonTerminalInHierarchies hierel_mt;
  Boolean only_empty_terminal_daughters; // T/F
  ArrayList daughter_list;
  // indices of daughters with leftmost/rightmost projections in the sequence (for later insertion of XT in sequence)
  int leftmost_projected_daughter=-1; 
  int rightmost_projected_daughter=-1; 

  Hierel(String i, String hc, String t, String p, String rl) {
    id = i;
    hcluster = hc;
    type = t;
    print = p;
    rec_level = rl;
    
    daughter_list = new ArrayList();
  } 

  float[] return_lm_rm() {
    float[] lm_rm = {-1, -1};
    // retrieve the non terminal corresponding to mapping_init
    int ind_mapping_init = searchALindex_item (nonterminals_in_sequence, mapping_init, "NTS");
    if (ind_mapping_init!=-1) {
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(ind_mapping_init);
      if (searchALindex_item (terminals_in_sequence, nt.daughters_init, "TS") != -1) {
        TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, nt.daughters_init, nt.id, "TS"));
        lm_rm[0] = t.d_t.x_coord - unit_width/2 - unit_border; 
      } else {
        lm_rm[0] = nt.d_unit.x_coord - unit_width/2 - unit_border; 
      }
      // println("--- Drawing scene " + s.id + " from init " + nt.daughters_init);
      nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, mapping_end, "NTS"));
      if (searchALindex_item (terminals_in_sequence, nt.daughters_end, "TS") != -1) {
        TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, nt.daughters_init, nt.id, "TS"));
        lm_rm[1] = t.d_t.x_coord + unit_width/2 + unit_border; 
      } else {
        lm_rm[1] = nt.d_unit.x_coord + unit_width/2 + unit_border; 
      }
    }
    return lm_rm;
  }

  boolean is_null () {
    return (only_empty_terminal_daughters) ? true : false;
  }

  boolean is_only_init_mapped () {
    return (!mapping_init.equals("NULL") && mapping_end.equals("NULL")) ? true : false;
  }

  boolean is_visualized () {
    return (hierel_mt.d_nt!=null) ? true : false;
  }

  boolean is_visualizable () {
    return (!type.equals("NULL")) ? true : false;
  }

  // retrieve hierel rightmost index in the sequence 
  int retrieve_rightmost_sequence_index () {
    if (rightmost_projected_daughter == -1) {return -1;};
    int res_index = -1;
    // println("retrieve_rightmost_sequence_index of hierel " + id);
    // if the rightmost_projected_daughter is terminal, retrieve its mapping terminal in the sequence and return its index in the timeline
    Daughter rm_projected_d = (Daughter) this.daughter_list.get(this.rightmost_projected_daughter);
    if (rm_projected_d.type.equals("TD")) {
      TerminalInHierarchies t_in_h = (TerminalInHierarchies) terminals_in_hierarchies.get(
                                 searchALindex_item_set(terminals_in_hierarchies, rm_projected_d.id, rm_projected_d.set, "TH"));
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(
                                 searchALindex_item_set(terminals_in_sequence, t_in_h.projectionT, t_in_h.projectionNT, "TS"));
      res_index = searchALindex_item_set(sequence, t.id, t.nt, "SE");
    } 
    //else {
    //  NonTerminalDaughter ntd = (NonTerminalDaughter) 
    //                            nonterminal_daughters.get(
    //                              searchALindex_item_set(nonterminal_daughters, rm_projected_d.id, this.id, "NTD"));
    //  Hierel he1 = (Hierel) hierels.get(searchALindex_item(hierels, ntd.id, "HE")); // retrieve the hierel
    //  res_index = he1.retrieve_rightmost_sequence_index(); // call recursively the procedure
    //};  
    return res_index;
  } // END retrieve_rightmost_sequence_index

  // retrieve hierel leftmost index in the sequence 
  int retrieve_leftmost_sequence_index () {
    if (leftmost_projected_daughter == -1) {return -1;};
    int res_index = -1;
    // if the leftmost_projected_daughter is terminal, retrieve its mapping terminal in the sequence and return its index in the timeline
    Daughter lm_projected_d = (Daughter) daughter_list.get(leftmost_projected_daughter);
    if (lm_projected_d.type.equals("TD")) {
      TerminalInHierarchies t_in_h = (TerminalInHierarchies) terminals_in_hierarchies.get(
                                 searchALindex_item_set(terminals_in_hierarchies, lm_projected_d.id, lm_projected_d.set, "TH"));
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(
                                 searchALindex_item_set(terminals_in_sequence, t_in_h.projectionT, t_in_h.projectionNT, "TS"));
      res_index = searchALindex_item_set(sequence, t.id, t.nt, "SE");
    } 
    //else {
    //  NonTerminalDaughter ntd = (NonTerminalDaughter) 
    //                            nonterminal_daughters.get(
    //                              searchALindex_item_set(nonterminal_daughters, lm_projected_d.id, this.id, "NTD"));
    //  Hierel he1 = (Hierel) hierels.get(searchALindex_item(hierels, ntd.id, "HE")); // retrieve the hierel
    //  res_index = he1.retrieve_leftmost_sequence_index(); // call recursively the procedure
    //  // println("... leftmost_sequence_index (NTD) " + res_index);
    //};  
    return res_index;
  } // END retrieve_leftmost_sequence_index


  int[] augment_sequence_with_xt (int leftmost_sequence_index, int rightmost_sequence_index) {
    println("- augment_sequence_with_xt from Hierel " + id + " mapping_init " + mapping_init + " and mapping_end " + mapping_end + "; sequence indices: " + leftmost_sequence_index + ", " + rightmost_sequence_index);
    int[] sequence_indices = {-1, -1};
    // if this hierel is null, i.e. only empty daughters, 
    // then create null terminal in sequence and null terminal in hierarchies and update daughter and sequence indices 
    if (this.is_null()) { 
      int insertion_index = leftmost_sequence_index; // initialize the insertion index 
      println("-- AUGMENTING SEQUENCE with Null Terminal: " + this.id + " at position ... "); // + leftmost_sequence_index);
      // TerminalInSequence(String i, String n, String t, String p)
      TerminalInSequence t_null_S = new TerminalInSequence("TS_null"+this.id, "NULL", "UN", "NULL Terminal", "+0"); 
      // println("---- created new null terminal in sequence: " + t_null_S.id);
      terminals_in_sequence.add(t_null_S); 
      // TerminalInHierarchies(String i, String set, String hierel, String t, String p, String at, String ant, String rl)
      TerminalInHierarchies t_null_H = new TerminalInHierarchies("TH_null"+id, "NULL", id, "UN", "NULL Terminal", t_null_S.id, "NULL", "NULL"); 
      // println("---- created new null terminal in hierarchy: " + t_null_H.id);
      terminals_in_hierarchies.add(t_null_H); 
      // if this null hierel is mapped onto some sequence element (a non terminal)
      if (this.is_only_init_mapped()) {
        print(" ONLY INIT MAPPED "); 
        println(" NonTerminalInSequence " + this.mapping_init); 
        
        NonTerminalInSequence nts = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item(nonterminals_in_sequence, this.mapping_init, "NTS"));
        insertion_index = searchALindex_item_set(sequence, nts.daughters_end, nts.id, "SE") + 1;
      }
      Sequence_element se = (Sequence_element) sequence.get(insertion_index-1); print(" ... " + insertion_index + ", after element " + se.id); 
      // Sequence_element(String i, String n) -- insert a new sequence element on the left of the current index
      sequence.add(insertion_index, new Sequence_element(t_null_S.id, "NULL")); 
      sequence_indices[0] = insertion_index; sequence_indices[1] = insertion_index;
      // println("---- added: " + t_null_S.id + " to sequence in position " + sequence_indices[1]);
      // Daughter(String i, String m, String t) -- create a new daughter for such hierel
      daughter_list.add(new Daughter(t_null_H.id, "NULL", id, "TD")); 
      only_empty_terminal_daughters = false; 
      leftmost_projected_daughter = 0;  rightmost_projected_daughter = 0;
    } else {  
      // if this hierel is NOT null, i.e. it has some NON empty daughters, 
      // augment sequence with projections on the left of it
      int[] indices = this.augment_sequence_with_xt_left (this.retrieve_leftmost_sequence_index(), this.leftmost_projected_daughter); 
      sequence_indices[0] = indices[0];
      // augment sequence with projections on the right of it
      indices = this.augment_sequence_with_xt_right1 (indices[0], this.leftmost_projected_daughter);   
      sequence_indices[1] = indices[0];
    }
    println("-- Definitive hierel: " + id + ", with leftmost daughter " + leftmost_projected_daughter + " and rightmost daughter " + rightmost_projected_daughter);
    return sequence_indices;
  }

  int[] augment_sequence_with_xt_right1 (int init_sequence_index, int init_daughter_index) {
    int[] indices = {init_sequence_index, init_daughter_index};
    int sequence_index = init_sequence_index;
    println("-- AUGMENTING HIEREL TO THE RIGHT: " + this.id + "; from daughter index " + init_daughter_index + " and sequence index " + sequence_index);
    TerminalInSequence t_S = null;
    // for each daughter after the init daughter 
    for (int j = leftmost_projected_daughter; j < daughter_list.size(); j++) {
      Daughter d = (Daughter) daughter_list.get(j); 
      // if this daughter is NON Terminal, retrieve the hierel, and ...  
      // if (d.type.equals("NTD")) {
        //NonTerminalDaughter ntd = (NonTerminalDaughter) nonterminal_daughters.get(
        //                            searchALindex_item_set(nonterminal_daughters, d.id, id, "NTD"));
        //Hierel he = (Hierel) hierels.get(searchALindex_item(hierels, d.id, "HE"));
        //// if this NTD is NOT totally projected, call recursively on the next sequence index on the right
        //if (ntd.totally_projected.equals("F")) { 
        //  println("---- NonTerminalDaughter: " + d.id + " and Hierel: " + he.id + ", NON totally projected");
        //  int[] sequence_indices = he.augment_sequence_with_xt (sequence_index+1, sequence_index+1);
        //  sequence_index = sequence_indices[1]; // update to the rightmost sequence index
        //} else { // else, if this NTD IS totally projected, just retrieve the extreme sequence indices
        //  println("---- NonTerminalDaughter: " + d.id + " and Hierel: " + he.id + ", totally projected");
        //  sequence_index = he.retrieve_rightmost_sequence_index(); 
        //};  
        //// at the return of recursion or the indices update, get the sequence element at the curr_rightmost_sequence_index ...
        //Sequence_element se = (Sequence_element) sequence.get(sequence_index); 
        //t_S = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set(terminals_in_sequence, se.id, se.nt, "TS"));
        //println(" ---- Sequence element " + se.id + " at sequence_index: " + sequence_index);
      //} else { // d.type.equals("TD"), this daughter is a terminal 
      if (d.type.equals("TD")) {
        println("---- TerminalDaughter: " + d.id);
        // retrieve the terminal
        TerminalInHierarchies t_H = (TerminalInHierarchies) 
                                      terminals_in_hierarchies.get(
                                        searchALindex_item_set(terminals_in_hierarchies, d.id, d.set, "TH")); 
        if (t_H.projectionT.equals("NULL")) { // if it is null, create a new sequence element 
          // TerminalInSequence(String i, String n, String t, String p); create a new TerminalInSequence 
          t_S = new TerminalInSequence(t_H.id, this.id, "UN", t_H.print, t_H.rec_level); 
          t_H.projectionT = t_S.id; t_H.projectionNT = id; 
          terminals_in_sequence.add(t_S); 
          // Sequence_element(String i, String n); add the new terminal to sequence
          sequence.add(++sequence_index, new Sequence_element(t_S.id, t_S.nt)); 
        } else { // if it is non null, just update the sequence index
          t_S = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set(terminals_in_sequence, t_H.projectionT, t_H.projectionNT, "TS"));
          sequence_index = searchALindex_item_set(sequence, t_S.id, t_S.nt, "SE");
        }; // 
      };  
      // ... and update the mapping_end
      this.mapping_end = t_S.id; this.mapping_end_set = t_S.nt;
      println("--- new current rightmost sequence index: " + sequence_index + " with element " + this.mapping_end + " " + this.mapping_end_set);
      this.rightmost_projected_daughter = j;
      indices[0] = sequence_index; indices[1] = this.rightmost_projected_daughter ;
    }  // END FOR each daughter
    return indices;
  }  
  
  int[] augment_sequence_with_xt_left (int init_sequence_index, int daughter_index) {
    int[] indices = {init_sequence_index, daughter_index};
    int sequence_index = init_sequence_index; // init the sequence index to the init index 
    println("- AUGMENTING FROM HIEREL TO THE LEFT: " + this.id + "; leftmost sequence index: " + sequence_index + "; leftmost daughter index: " + daughter_index);
    // retrieve the leftmost daughter index
    int orig_leftmost_daughter = this.leftmost_projected_daughter;
    int curr_leftmost_sequence_index = this.retrieve_leftmost_sequence_index();
    // println("- check current leftmost sequence index: " + curr_leftmost_sequence_index);
    // int curr_rightmost_sequence_index = this.retrieve_rightmost_sequence_index();
    TerminalInSequence t_S = null; 
    // for each daughter before leftmost daughter 
    for (int j = orig_leftmost_daughter-1; j > -1; j--) {
      Daughter d = (Daughter) daughter_list.get(j); 
      // if this daughter is NON Terminal, retrieve the hierel, and call recursively 
      //if (d.type.equals("NTD")) {
      //  // println("---- NonTerminalDaughter: " + d.id);
      //  NonTerminalDaughter ntd = (NonTerminalDaughter) nonterminal_daughters.get(
      //                              searchALindex_item_set(nonterminal_daughters, d.id, this.id, "NTD"));
      //  Hierel he = (Hierel) hierels.get(searchALindex_item(hierels, d.id, "HE"));
      //  if (ntd.totally_projected.equals("F")) { // if not projected, project it
      //    int[] sequence_indices = he.augment_sequence_with_xt (sequence_index-1, sequence_index-1);
      //    sequence_index = sequence_indices[0];
      //  } else {
      //    sequence_index = he.retrieve_leftmost_sequence_index(); 
      //  };  
      //  // then, get the corresponding sequence element ...
      //  Sequence_element se = (Sequence_element) sequence.get(sequence_index); 
      //  t_S = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set(terminals_in_sequence, se.id, se.nt, "TS"));
      //} else { // d.type.equals("TD"), this daughter is a terminal 
      if (d.type.equals("TD")) {
        // println("---- TerminalDaughter: " + d.id);
        // retrieve the terminal in hierarchies and in sequence
        TerminalInHierarchies t_H = (TerminalInHierarchies) 
                                      terminals_in_hierarchies.get(
                                        searchALindex_item_set(terminals_in_hierarchies, d.id, d.set, "TH")); 
        if (t_H.projectionT.equals("NULL")) {
          // TerminalInSequence(String i, String n, String t, String p); create a new TerminalInSequence 
          t_S = new TerminalInSequence(t_H.id, this.id, "UN", t_H.print, t_H.rec_level); 
          t_H.projectionT = t_S.id; t_H.projectionNT = this.id; 
          terminals_in_sequence.add(t_S); 
          // Sequence_element(String i, String n); add the new terminal to sequence
          // if (sequence_indices[0] == -1 || sequence_indices[0] == 0) {sequence_indices[0] = 0;} else {sequence_indices[0]--;};
          if (sequence_index == -1) {sequence_index = 0;} 
          sequence.add(sequence_index, new Sequence_element(t_S.id, t_S.nt));
        } else { // END IF (t_H.projectedT.equals("NULL"))
          t_S = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set(terminals_in_sequence, d.id, this.id, "TH")); 
          sequence_index = searchALindex_item_set(sequence, t_S.id, t_S.nt, "SE");   
        }       
      }
      // ... and set the mapping_init
      this.mapping_init = t_S.id; this.mapping_init_set = t_S.nt;
      println("--- new current leftmost sequence index: " + curr_leftmost_sequence_index + " with element " + this.mapping_init + " " + this.mapping_init_set);          
      this.leftmost_projected_daughter = j;
      indices[0] = sequence_index; indices[1] = this.leftmost_projected_daughter ;
    }  // END FOR each daughter
    return indices; 
  } 

  // position the drawing objects for a hierel
  float[] x_positioning (float leftmost, float rightmost) {
    float lm_rm[] = {leftmost, rightmost};
    // print("\n ------ Positioning hierel " + this.id);
    if (daughter_list.size()>0) {
      for (int i=0; i < daughter_list.size(); i++) { // for each daughter 
        Daughter d = (Daughter) daughter_list.get(i);
        // print("\n --------- Positioning daughter " + d.id + " of hierel " + id); 
        if (d.type.equals("TD")) { // if terminal ... DrawingObj_v d_t;
          TerminalInHierarchies t_h = (TerminalInHierarchies) terminals_in_hierarchies.get(searchALindex_item_set(terminals_in_hierarchies, d.id, d.set, "TH"));
          TerminalInSequence t_s = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set(terminals_in_sequence, t_h.projectionT, t_h.projectionNT, "TS"));
          // if (t_s.type.equals("UN")) {barred = true;}; TO BE MOVED ELSEWHERE
          if (lm_rm[0]==-1 || lm_rm[0] > t_s.d_t.x_coord - unit_width/2 - unit_border) {lm_rm[0] = t_s.d_t.x_coord - unit_width/2 - unit_border;}; 
          if (lm_rm[1] < t_s.d_t.x_coord + unit_width/2 + unit_border) {lm_rm[1] = t_s.d_t.x_coord + unit_width/2 + unit_border;};
        } 
        //else { // if NON terminal daughter, ... 
        //  NonTerminalDaughter nt_dg = (NonTerminalDaughter) nonterminal_daughters.get(searchALindex_item_set(nonterminal_daughters, d.id, this.id, "NTD"));
        //  Hierel he = (Hierel) (hierels.get(searchALindex_item(hierels, nt_dg.id, "HE")));
        //  if (lm_rm[0]==-1 || lm_rm[0] > he.hierel_mt.d_nt.x_coord - unit_width/2 - unit_border) {lm_rm[0] = he.hierel_mt.d_nt.x_coord - unit_width/2 - unit_border;}; 
        //  if (lm_rm[1] < he.hierel_mt.d_nt.x_coord + unit_width/2 + unit_border) {lm_rm[1] = he.hierel_mt.d_nt.x_coord + unit_width/2 + unit_border;};
        //} // END IF
      } // END FOR
    } // END IF THERE ARE DAUGHTERS
    else // ALIGNMENT ON UNITS (NON TERMINALS)
    {
      if (!(mapping_init.equals("NULL"))) {
        NonTerminalInSequence nt_s = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item(nonterminals_in_sequence, mapping_init, "NTS"));
        lm_rm[0] = nt_s.d_unit.x_coord - unit_width/2 - unit_border; 
        if (!(mapping_end.equals("NULL"))) {        
          NonTerminalInSequence nt_s2 = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item(nonterminals_in_sequence, mapping_end, "NTS"));      
          lm_rm[1] = nt_s2.d_unit.x_coord + unit_width/2 + unit_border;
        }
      }
    }

    return lm_rm;
  }  // END positioning


  void create_daughter(int daughter_index, String daughter_id, String daughter_set, String daughter_he) {  
    TerminalInHierarchies s = (TerminalInHierarchies) // take the TerminalInHierarchies 
                                  terminals_in_hierarchies.get(searchALindex_item_set (terminals_in_hierarchies, daughter_id, daughter_set, "TH"));
    // print("\n   Loading terminal " + s.id); 
    s.he=id; // update container hierel       
    Daughter d = new Daughter(s.id, s.set, s.he, "TD"); // create the daughter 
    println("  ... created projected daughter " + d.id); 
    // update leftmost and rightmost daughters 
    daughter_list.add(d);
  } // END create_projected_daughter

} // END CLASS Hierel
