
class HCluster {
  String id, idShort, type, print; // id, type (HCL), and long print
  ArrayList plans; 
  int rec_levels;
  color c = #000000;

  HCluster(String i, String iS, String t, String p) {
    id = i;
    idShort = iS;
    type = t; 
    print = p;
    plans = new ArrayList(); 
  }
  
  // compute the rec level
  void compute_rec_level () {
    rec_levels = -1;
    ArrayList ordered_plans = new ArrayList(); 
    while (ordered_plans.size() < plans.size()) { 
      int plans_cursor = 0; 
      while (plans_cursor < plans.size()) {        
        Hierel p = (Hierel) plans.get(plans_cursor);
        int rec_level_n = Integer.parseInt(p.rec_level);
        if (rec_level_n == rec_levels + 1) {ordered_plans.add(p);}
        plans_cursor++;
      }
      rec_levels = rec_levels + 1; 
    }
    if (rec_levels == -1) {rec_levels = 1;};
    plans = ordered_plans; 
    //println(" HC compute_rec_level: Agent " + id + " with rec_levels = " + rec_levels);
  }

} 


// ****************** Non Terminals in Hierarchies *****************


class NonTerminalInHierarchies {
  String id, he, print; 
  String daughters_init; // leftmost dominated daughter
  String daughters_end; // rightmost dominated daughter

  DrawingObj_h d_nt;

  NonTerminalInHierarchies(String i, String h, String p, String d_i, String d_e) {
    id = i;
    he = h;
    print = p;
    daughters_init = d_i;
    daughters_end = d_e; 
  }
} 


// ****************** Non Terminal Daughters  *****************

//class NonTerminalDaughter {
//  String id, set, he, print; 
//  String seq_or_hie; // "SEQUENCE" or "HIERARCHY"
//  String hcluster;
//  String totally_projected; // if "HIERARCHY", totally_projected can be "T" or "F"; if "SEQUENCE", "NULL"

//  DrawingObj_v_var_w d_nt_dg;

//  NonTerminalDaughter(String i, String h, String s_o_h, String hc, String p, String t_p) {
//    id = i;
//    set = "NULL";
//    he = h;
//    seq_or_hie = s_o_h;
//    hcluster = hc;
//    print = p;
//    totally_projected = t_p;
//  }
//}

// ****************** Terminals in Hierarchies  *****************

class TerminalInHierarchies {
  String id; // identifier
  String set; // set that contains the terminal (can be planstate or hierel directly)
  String he; // Hierel that contains such T 
  String type; // type ("T", "XT", "UN")
  String print; // long print 
  String projectionT; // TInS.id that is aligned to such TinH in the sequence (NULL for UN)
  String projectionNT; // NTinS projection for such TinH in the sequence (NULL for UN)
  String rec_level; // such T is at the extreme left or right of some NT; string "-" or "+" followed by a number: ... -2 -1 -0 NT +0 +1 +2 ...

  DrawingObj_v d_t;

  TerminalInHierarchies(String i, String s, String h, String t, String p, String pt, String pnt, String rl) {
    id = i;
    set = s;
    he = h;
    type = t;
    print = p;
    projectionT = pt;
    projectionNT = pnt;
    rec_level = rl;
  }

  // is such T visualized
  // boolean is_T_visualized () {
    // if (d_t != null) { println("---------- state " + id + " visualized!");}
    // return (d_t == null) ? false : true;
  // } 

}

// ****************** Sequence Elements *****************

class Sequence_element { // class that stands for all terminals in the sequence, accessed through id
  String id; 
  String nt;  // the immediate non terminal that includes it, possibly "NULL" 

  Sequence_element(String i, String n) {
    id = i; nt = n; 
  }

}

// ****************** Terminals in Sequence  *****************

class TerminalInSequence {
  String id; // identifier
  String nt; // NT that contains such T, possibly NULL 
  String type; // type ("T", "XT", "UN")
  String print; // long print 
  String rec_level;

  DrawingObj_v d_t;

  TerminalInSequence(String i, String n, String t, String p, String rl) {
    id = i;
    nt = n;
    type = t;
    print = p;
    rec_level = rl;
  }
}

// ****************** Non Terminals in Sequence *****************

class NonTerminalInSequence {
  String id, type, print; 
  String daughters_init = "NULL"; // leftmost dominated daughter
  String daughters_end = "NULL"; // rightmost dominated daughter

  DrawingObj_h d_nt;
  DrawingObj_h d_nt_a;
  
  DrawingObj_v d_unit;

  NonTerminalInSequence(String i, String p, String d_i, String d_e) {
    id = i;
    print = p;
    daughters_init = d_i;
    daughters_end = d_e; 
    type = "NTS";
  }
}


// CLASS Daughter

class Daughter {
  String id;
  String set;
  String nt_mother_id;
  String type; // "TD" Terminal, "NTD" NonTerminal
  
  Daughter(String i, String s, String m, String t) {
    id=i; set=s; nt_mother_id=m; type=t; 
  }
} // END Class Daughter  
