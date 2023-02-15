
// ********************************** LOADING LIBRARIES *********************************

// get all the terminal elements: agents, incidents, actions, states
void loading_drama_repository1(XML xml) {
  println("Loading repository ... \n");

  // get the repository elements (currently, agents only, stored in hierarchy_clusters ArrayList as HCluster objects)
  XML x_repository = xml.getChild("repository");
  XML[] x_rep_elem = x_repository.getChildren("agent");
  for (int i=0; i < x_rep_elem.length; i++) { // for each element in the repository
    // String rep_elem_type = x_rep_elem[i].getString("type");
    println("Loading rep elem " + x_rep_elem[i].getString("id")); 
    // build the agent (hierarchy cluster) elements
    // if (rep_elem_type.equals("AGT")) {  // Agent(String i, String t, String p)
    // println(x_rep_elem[i].getString("id") + " in hierarchy_clusters, of size " + hierarchy_clusters.size()); 
    // Agent rep_elem = new Agent(x_rep_elem[i].getString("id"), x_rep_elem[i].getString("type"), x_rep_elem[i].getString("print")); 
    String newId = newHcId (hierarchy_clusters, x_rep_elem[i].getString("id"));
    // HCluster rep_elem = new HCluster(x_rep_elem[i].getString("id"), "HCL", x_rep_elem[i].getString("print"));       
    HCluster rep_elem = new HCluster(x_rep_elem[i].getString("id"), newId, "HCL", x_rep_elem[i].getString("print"));       
    hierarchy_clusters.add(rep_elem); 
    // }
  }
} 

String newHcId (ArrayList hc_al, String hc_id) {
  // produces a new string of three letters for the agents (or hierarchy clusters)
  String newId = hc_id.substring(0,3);
  String auxId = hc_id;
  // remove the string Agent_ from the id, if any 
  if (auxId.substring(0,6).equals("Agent_")) {auxId = auxId.substring(6);}
  else if (auxId.substring(0,3).equals("Ag_")) {auxId = auxId.substring(3);}
  // take the first three characters of what remains
  auxId = auxId.substring(0,3);
  if (searchALindex_item (hc_al, auxId, "HCS")!=-1) { // if already present as idShort, reduce to two and add a progressive number
    int i=0;
    while (searchALindex_item (hc_al, auxId.substring(0,2) + nf(i), "HCL")!=-1) {i=i+1;}
    newId = auxId.substring(0,2) + nf(i);
  } 
  else {newId = auxId;} 
  return newId;
}

// get all the non terminal elements and builds structures of non terminals and terminals
void loading_drama (XML xml) {
  println("Loading drama ... \n");

  XML x_title = xml.getChild("title"); title = x_title.getString("id"); 
  
  // load the timeline and build the timeline, the units, the story states, the incidents, the single states
  println("\n Loading timeline total ... \n");
  // initialize all the structures that host the elements
  XML x_timelines = xml.getChild("timelines");
  XML[] x_timeline = x_timelines.getChildren("timeline");
  for (int i=0; i < x_timeline.length; i++) { // for each timeline  
    if (x_timeline[i].getString("id").equals("TL_total") || 
        x_timeline[i].getString("id").equals("Timeline_1") || 
        x_timeline[i].getString("id").equals("TL_Total")) { // if timeline total
      println("\n Here is timeline total ... \n");
      XML[] x_te = x_timeline[i].getChildren("te");
      load_timeline (x_te);
    }
  }
  println("\n \n Timeline size = " + sequence.size());    
  println("Loading timeline ... completed!\n");

  // load the plans into the plan objects, with their state-action lists
  println("\n Loading plans ... \n");
  XML x_plans = xml.getChild("plans"); // plans' subtree 
  XML[] x_plan = x_plans.getChildren("plan"); // list of plans  
  load_plans(x_plans, x_plan); 
  // println("\n Hierels size " + hierels.size() + "\nHierels: ");
  // for (int i=0; i < hierels.size(); i++) {Hierel he = (Hierel) hierels.get(i); println(he.id + ", with " + he.daughter_list.size() + " daughters");}
  // println("\n Nonterminal daughters size " + nonterminal_daughters.size() + "\nNonterminal daughters: ");
  // for (int i=0; i < nonterminal_daughters.size(); i++) {print(((NonTerminalDaughter) nonterminal_daughters.get(i)).id + ", ");}
  println("\n Loading plans ... completed!\n");

  // load the scenes into scene objects
  println("\n Loading scenes ... \n");
  XML x_scenes = xml.getChild("scenes"); // scenes' subtree 
  XML[] x_scene = x_scenes.getChildren("scene"); // list of scenes  
  load_scenes(x_scenes, x_scene); 
  // println("\n Number of scenes: " + scenes.size() + "\nScenes: ");
  // for (int i=0; i < scenes.size(); i++) {Scene s = (Scene) scenes.get(i); println(s.id);}
  println("\n Loading scenes ... completed!\n");

  println("\n Loading ... completed!");
}


// load the plans into the plan objects, with their plan elements 
void load_plans(XML x_plans, XML[] x_plan) {
  for (int i = 0; i < x_plan.length; i++) { // for each plan
    // LOAD THE HIEREL HEADER ... create the plan and add to the plans arraylist 
    String print = "NULL";
    if (!x_plan[i].getString("print").equals("NULL")) {print = x_plan[i].getString("print");} else {print = x_plan[i].getString("id");}
    // Hierel p = new Hierel(x_plan[i].getString("id"), x_plan[i].getString("agent"), "NULL", x_plan[i].getString("print"), "0");
    if (!x_plan[i].getString("mapping_init").equals("NULL")) {
      Hierel p = new Hierel(x_plan[i].getString("id"), x_plan[i].getString("agent"), "NULL", print, "0");
      println("p = " + p.id);
      p.accomplished = x_plan[i].getString("accomplished");
      p.rec_level = x_plan[i].getString("rec_level");
      p.mapping_init = x_plan[i].getString("mapping_init"); p.mapping_end = x_plan[i].getString("mapping_end");
      if (p.accomplished.equals("F") || p.accomplished.equals("NULL")) {p.barred = true;} else {p.barred = false;}
      hierels.add(p); 
      HCluster hc = (HCluster) hierarchy_clusters.get(searchALindex_item(hierarchy_clusters, x_plan[i].getString("agent"), "HCL"));
      hc.plans.add(p);
      // println("\n  Loading plan " + p.id); 
      // NonTerminalInHierarchies(String i, String h, String p, String d_i, String d_e)
      NonTerminalInHierarchies mt = new NonTerminalInHierarchies(p.id, p.id, p.print, p.mapping_init, p.mapping_end); 
      nonterminals_in_hierarchies.add(mt);
      p.hierel_mt = mt;
    }
    // ... AND THE DAUGHTERS    
  } // end "for each plan"
}


// load the scenes into the scene objects, with their scene elements 
void load_scenes(XML x_scenes, XML[] x_scene) {
  for (int i = 0; i < x_scene.length; i++) { // for each plan
    // LOAD THE SCENE HEADER ... create the scene and add to the scenes arraylist 
    String print = "NULL";
    if (!x_scene[i].getString("print").equals("NULL")) {print = x_scene[i].getString("print");} else {print = x_scene[i].getString("id");}
    Scene s = new Scene(x_scene[i].getString("id"), x_scene[i].getString("type"), print);
    s.mapping_init = x_scene[i].getString("mapping_init"); s.mapping_end = x_scene[i].getString("mapping_end");
    scenes.add(s); 
    println("Loading scene " + s.id); 
    NonTerminalInSequence mt = new NonTerminalInSequence(s.id, s.print, s.mapping_init, s.mapping_end); 
    nonterminals_in_sequence.add(mt);
    s.scene_mt = mt;
  } // end "for each scene"
}

// load the timeline and build the timeline, the units, the unit states, the incidents, the single states
void load_timeline (XML[] x_te) {
  println("TIMELINE"); 
  for (int i=0; i < x_te.length; i++) { // for each timeline element
    String init = "NULL"; String end = "NULL";
    println("UNIT " + i + ": " + x_te[i].getString("id")); 
    String print = "NULL";
    if (!x_te[i].getString("print").equals("NULL")) {print = x_te[i].getString("print");} else {print = x_te[i].getString("id");}
    NonTerminalInSequence u =  new NonTerminalInSequence (x_te[i].getString("id"), print, init, end); 
    nonterminals_in_sequence.add(u); 
    // println("\n ADDED UNIT " + x_te[i].getString("id") + " TO nonterminals_in_sequence"); 
    sequence.add(new Sequence_element(u.id, "NULL"));
  } // end "for each timeline element"
}


// ------------- AUXILIARY ------------


// find the next state in the rec level order 
int next_rec_level_state_in_unit_state (XML[] x_states, String last_rec_level_s) {
  int return_index = -1; // index in x_states that will be returned
  println("\n Enter nrlsius: last rec level is " + last_rec_level_s);
  int last_rec_level = Integer.parseInt(last_rec_level_s.substring(1)); // number without the sign
  // if last_rec_level is positive look for the next equal or higher 
  if (last_rec_level_s.substring(0,1).equals("+")) {
    int return_rec_level = max_rec_level; // set to maximum rec level
    for (int i=0; i < x_states.length; i++) { // loops on all the states 
      if (x_states[i] != null && x_states[i].getString("rec_level").substring(0,1).equals("+")) { // if we are in the same, positive sign
        int cur_rec_level = Integer.parseInt(x_states[i].getString("rec_level").substring(1)); // extract the current rec level
        // if the cur rec level is greater or equal than the last found and is lesser than the rec level found until now, set the new return index
        if (cur_rec_level >= last_rec_level && cur_rec_level <= return_rec_level) { return_rec_level = cur_rec_level; return_index = i; }
      }
    }
    if (return_index != -1) {x_states[return_index] = null; return return_index; } // if found (return_index different than -1), return the index
    // if no more positive to check, look for the minimum negative (return rec level still set to the max rec level)
    return_rec_level = 0;
    // println("--- nrlsius: look for first negative; return rec level is " + return_rec_level); 
    for (int j=0; j < x_states.length; j++) { // loop on all the states 
      if (x_states[j] != null && x_states[j].getString("rec_level").substring(0,1).equals("-")) { // if we are in the same, negative sign
        int cur_rec_level = Integer.parseInt(x_states[j].getString("rec_level").substring(1)); // extract the cur rec level
        // println("--- --- nrlsius: loop for negative; cur rec level is " + cur_rec_level);
        // if the cur rec level is greater or equal than 0 the last found and is lesser than the rec level found until now, set the new return index
        if (cur_rec_level <= max_rec_level && cur_rec_level >= return_rec_level) { 
          // println("--- --- nrlsius: found good negative; cur rec level is " + cur_rec_level); 
          return_rec_level = cur_rec_level; return_index = j; 
        }
      }
    }
    if (return_index != -1) {x_states[return_index] = null; return return_index; }
  } else { // else if last_rec_level is negative look for the next equal or higher -- if (last_rec_level_s.substring(0,0).equals("-")) {
    // println("--- nrlsius: look for NEXT negative; last rec level is " + last_rec_level);
    int return_rec_level = 0;
    for (int k=0; k < x_states.length; k++) { 
      if (x_states[k] != null && x_states[k].getString("rec_level").substring(0,1).equals("-")) {
        int cur_rec_level = Integer.parseInt(x_states[k].getString("rec_level").substring(1));
        if (cur_rec_level <= last_rec_level && cur_rec_level >= return_rec_level) { return_rec_level = cur_rec_level; return_index = k; }
      }
    }
    if (return_index != -1) {x_states[return_index] = null; return return_index; }
  }
  return -1;
}  


// returns the first daughter given the projection on the sequence linear order 
int first_state_in_sequence_index (XML[] x_states) {
  // println("\n Enter next_state_in_sequence: curr sequence_index is " + sequence_index);
  int min_sequence_index = sequence.size(); 
  int daughter_index = -1; // index in x_states that will be returned
  int curr_sequence_index = -1;
  // for each state 
  int i = 0; 
  while (i < x_states.length && x_states[i]!=null) {
    // find the sequence index projected 
    if (!(x_states[i].getString("mapping")).equals("NULL")) { // if mapping of this state is non null 
      curr_sequence_index = searchALindex_item_set (sequence, x_states[i].getString("mapping"), x_states[i].getString("mapping_set"), "SE"); 
      // if this index is lesser than the current return_index, then set the return_index
      if (curr_sequence_index < min_sequence_index) {min_sequence_index = curr_sequence_index; daughter_index = i;} 
    }
    i++;
  }
  return daughter_index; 
}