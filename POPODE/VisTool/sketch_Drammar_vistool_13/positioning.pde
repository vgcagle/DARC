// SPACES AND POSITIONING 

//
void compute_scenes_layer_heights () {
  if (scenes.size()!=0) {
    Scene s = (Scene) scenes.get(scenes.size()-1);  
    // scenes_layer_heights = new float[int(s.rec_level)]; //+1
    if (int(s.rec_level)!=-1) {
      scenes_layer_heights = new float[int(s.rec_level)+1];     
      for (int i=0; i<scenes_layer_heights.length; i++) {
        // scenes_layer_heights[i] = scenes_height * ((exp(i+1)-exp(i)) / exp(scenes_layer_heights.length));
        scenes_layer_heights[i] = (float) scenes_height / scenes_layer_heights.length;
      }
    }
  }
}

void compute_plans_layer_heights () {
  plans_layer_heights = new float[hierarchy_clusters_in_display.size()]; 
  // count the total number of rec levels
  int rec_levels = 0;
  for (int i=0; i<hierarchy_clusters_in_display.size(); i++) {HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i); rec_levels=rec_levels+hc.rec_levels+1;} 
  // println("Total plans_height = " + plans_height + " and total rec_levels = " + rec_levels);
  // for each agent, compute proportional track
  for (int i=0; i<hierarchy_clusters_in_display.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i);
    plans_layer_heights[i] = plans_height * (hc.rec_levels+1) / rec_levels;
    // println(i + " plan height = " + plans_layer_heights[i]);
  }
}


// update all relative spaces, related to plan layers
void update_spaces () { 
  if (mute_clusters.size() != 0) {
    plans_height = (track_header_height/10)*6 - mutes_height;  // plans are 6/10 of visualization - the space for mutes (in this case);
    plans_bottom_y = plans_top_y + plans_height;     
  } else {
    plans_height = (track_header_height/10)*6;  // plans are 6/10 of visualization - no space for mutes;
    plans_bottom_y = plans_top_y + plans_height;         
  }
}


// create the timeline nodes, on which all other alignments rely 
void create_sequence_nodes() {
  println("\n \n----------- CREATE SEQUENCE NODES -----------\n"); 
  Sequence_element se_aux = (Sequence_element) sequence.get(0); // get the first incident (index 0) of the sequence (or timeline)
  // if the scene that contains it is NOT NULL,
  if (!(se_aux.nt.equals("NULL"))) {  
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, se.id, se.nt, "TS"));
      color fill_color = assign_fill_color ("T", "SEQUENCE", t.type, "NULL"); 
      color text_color = assign_text_color ("T", "SEQUENCE", t.type, "NULL");
      t.d_t = new DrawingObj_v(0, 0, fill_color, text_color, t.id, t.print, false);  // DEBUG t.id; DELIVERY t.print
    } // END for 
    float sequence_element_size = (float) timeline_width / sequence.size();
    unit_width = sequence_element_size - unit_border*2; // / (1 + unit_border); // actual side of a story incident
    println("IF THE FIRST UNIT IS NOT NULL ...");
    println("display_width = " + timeline_width + "; sequence_element_size = " + sequence_element_size + "; sequence.size() = " + sequence.size());
    println("unit_border = " + unit_border + "; unit_width = " + unit_width);
    if (unit_width < min_unit_width) {println("\nDRAWING IS NOT POSSIBLE: MIN SIZE OF " + min_unit_width + " PIXEL PER ELEMENT IS NOT SATISFIED! \n"); exit();}
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, se.id, se.nt, "TS"));
      t.d_t.x_coord = (i*unit_width) + (i*unit_border*2) + timeline_left_x + (unit_border) + (unit_width/2); 
      t.d_t.y_coord = timeline_top_y + ((timeline_bottom_y - timeline_top_y)/2);
      t.d_t.x_width = unit_width; t.d_t.y_height = unit_height;
    } // END for 
  // if the scene that contains it is NULL,
  } else {
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, se.id, "NTS"));
      color fill_color = assign_fill_color ("T", "SEQUENCE", nt.type, "NULL"); 
      color text_color = assign_text_color ("T", "SEQUENCE", nt.type, "NULL");
      nt.d_unit = new DrawingObj_v(0, 0, fill_color, text_color, nt.id, nt.print, false);  // DEBUG t.id; DELIVERY t.print
    } // END for 
    float sequence_element_size = (float) timeline_width / sequence.size();
    unit_width = sequence_element_size - unit_border*2; /// (1 + unit_border); // actual side of a story incident
    println("IF THE FIRST UNIT IS NULL ...");
    println("display_width = " + timeline_width + "; sequence_element_size = " + sequence_element_size + "; sequence.size() = " + sequence.size());
    println("unit_border = " + unit_border + "; unit_width = " + unit_width);
    if (unit_width < min_unit_width) {println("\nDRAWING IS NOT POSSIBLE: MIN SIZE OF " + min_unit_width + " PIXEL PER ELEMENT IS NOT SATISFIED! \n"); exit();}
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, se.id, "NTS"));
      nt.d_unit.x_coord = (i*unit_width) + (i*unit_border*2) + timeline_left_x + (unit_border) + (unit_width/2); 
      nt.d_unit.y_coord = timeline_top_y + ((timeline_bottom_y - timeline_top_y)/2);
      nt.d_unit.x_width = unit_width; nt.d_unit.y_height = unit_height;
    } // END for 
  } // END IF

  println("\n ----------- END CREATE SEQUENCE NODES -----------\n \n"); 
} 
