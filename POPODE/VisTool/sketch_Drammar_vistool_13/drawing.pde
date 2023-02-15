
// ************* HIGH-LEVEL DRAWING FUNCTIONS ****************

// draw a large header
void draw_header_L(float x, float y, float x_w, float y_h, color f, color t, String txt) {
  noStroke();
  fill (f); // stroke(0); strokeWeight(3); 
  rectMode(CORNER); rect (x, y, x_w, y_h); flush();
  stroke(200); strokeWeight(3);
  line(x, y+y_h, x+x_w, y+y_h);
  fill(t);
  // if () {}
  // PFont sansserif_h = createFont(default_font_type, y_h*0.75, true); 
  // textFont(sansserif_h); textAlign(CENTER, CENTER); 
  // text(txt, x+(x_w/2), y+(y_h/2));
  // --- flex_write_text_in_box(txt, default_font_type, "CENTER", "CENTER", x+(x_w/2), y+(y_h/2), x_w, y_h);
  flex_write_lines_in_box(txt, default_font_type, "CENTER", "CENTER", x+(x_w/2), y+(y_h/2), x_w, y_h);
}

// draw the track headers, on the left. three horizontal sections: scenes, timeline, agents 
void draw_track_headers() { 
  // println("--- START draw_track_headers() ---");
  
  // *** 1) DRAW SCENES TRACK HEADERS
  PFont sansserif_10 = loadFont("SansSerif-10.vlw"); textFont(sansserif_10); textAlign(CENTER, CENTER); 
  strokeWeight(1); stroke(200);
  // println("track_header_height = " + track_header_height + "; scenes_top_y = " + scenes_top_y);
  // from bottom to top through increasing rec level scenes
  float offset = 0.0; line(track_header_left_x, scenes_bottom_y, track_header_right_x, scenes_bottom_y);
  if (scenes_layer_heights!=null) {
    for (int k=0; k<scenes_layer_heights.length; k++) {
      if (k<scenes_layer_heights.length-1) {
        line(track_header_left_x, scenes_bottom_y - offset - scenes_layer_heights[k], track_header_right_x, scenes_bottom_y - offset - scenes_layer_heights[k]);
      }
      flex_write_lines_in_box("scenes " + k, default_font_type, "CENTER", "CENTER", 
                            (int) ((track_header_right_x - track_header_left_x)/2), (int) (scenes_bottom_y - offset -(scenes_layer_heights[k]/2)), 
                            (int) track_header_width, (int) scenes_layer_heights[k]);
      // text("scenes " + k, (track_header_right_x - track_header_left_x)/2, scenes_bottom_y - offset -(scenes_layer_heights[k]/2));
      offset = offset + scenes_layer_heights[k]; 
    }
  }
  // println("--- DIFFERENCE = " + (scenes_height - offset));
  strokeWeight(1); stroke(200);
  //line(track_header_left_x, track_header_top_y+play_layer_height, track_header_right_x, track_header_top_y+play_layer_height);
  
  // *** 2) DRAW TIMELINE TRACK HEADER
  flex_write_lines_in_box("timeline", default_font_type, "CENTER", "CENTER", 
                            (int)(track_header_width/2), (int)(timeline_top_y+(timeline_height/2)), 
                            (int) track_header_width, (int) timeline_height);
  // text("timeline", (track_header_right_x-track_header_left_x)/2, timeline_top_y+(timeline_height/2));
  line(track_header_left_x, timeline_bottom_y, track_header_right_x, timeline_bottom_y);
  
  // *** 3) DRAW AGENT TRACKS HEADERS: mute toggle + circle per agent -> last row for mute agents
  for (int j=0; j<mute_boxes.size(); j++) {mute_boxes.remove(j);} // remove mute boxes from previous frame

  offset = 0.0; // from top border of the agent track header
  for (int i=0; i < hierarchy_clusters_in_display.size(); i++) { // for each agent in display
    strokeWeight(1); stroke(200); // draw a separation line
    line(track_header_left_x, plans_top_y + offset, track_header_right_x, plans_top_y + offset);
    HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i); // select agent
    // draw the agent circle in the middle of the track header space
    float diameter = 1.0; // determine the diameter of the agent circle: 90% of height or width
    if (plans_layer_heights[i] < track_header_width) {diameter = plans_layer_heights[i]*0.9;} else {diameter = track_header_width*0.9;}
    DrawingObj_r d_o = new DrawingObj_r (track_header_left_x+(track_header_width/2), // track_header_left_x+2*(track_header_width/3),
                                         plans_top_y + offset + plans_layer_heights[i]/2, 
                                         diameter, diameter, diameter/2, 
                                         hc.c, #000000, hc.idShort);
    d_o.draw_r_obj(0,0);  
    // draw the mute toggle, if in interactive mode
    if (interactive) {
      float mute_toggle_side = 1.0; if (diameter/2 >= 2.0) {mute_toggle_side = diameter/2;};
      strokeWeight(1); stroke(127); fill(230);
      rectMode(CENTER); rect(track_header_left_x+(mute_toggle_side/2), plans_top_y+offset+(mute_toggle_side/2), mute_toggle_side, mute_toggle_side); 
      // PFont sansserif_8 = loadFont("SansSerif-8.vlw"); textFont(sansserif_8);
      // textAlign(CENTER, CENTER); 
      fill(0);
      // text("M", track_header_left_x+8, plans_top_y+offset+8, 10, 10);
      flex_write_lines_in_box("M", default_font_type, "CENTER", "CENTER", 
                              (int)(track_header_left_x+(mute_toggle_side/2)), (int)(plans_top_y+offset+(mute_toggle_side/2)), 
                              (int) mute_toggle_side, (int) mute_toggle_side);
      // Mute_box(String i, String a, float xc, float yc, float xw, float yh)
      Mute_box mb = new Mute_box("mb_"+hc.id, hc.id, 
                                 (int)(track_header_left_x+(mute_toggle_side/2)), (int)(plans_top_y+offset+(mute_toggle_side/2)), 
                                 (int) mute_toggle_side, (int) mute_toggle_side); 
      mute_boxes.add(mb);
    }
    strokeWeight(1); stroke(200);
    line(track_header_left_x, plans_top_y+offset+plans_layer_heights[i], track_header_right_x, plans_top_y+offset+plans_layer_heights[i]);
    offset = offset + plans_layer_heights[i];
  } // END for each agent
  strokeWeight(1); stroke(200);
  line(track_header_left_x, plans_top_y+offset, track_header_right_x, plans_top_y+offset);
  // DRAW MUTES TRACK HEADER
  if (mute_clusters.size() > 0) {
    textFont(sansserif_10);
    text("mutes", (track_header_right_x-track_header_left_x)/2, mutes_top_y+(mutes_height/2));
  } 
  line(track_header_right_x, track_header_top_y, track_header_right_x, track_header_bottom_y);
}

void draw_sequence() { 
  // println("\n---------------- DRAWING SEQUENCE ----------------\n"); 
  strokeWeight(1); stroke(127); 
  line(timeline_left_x, timeline_top_y, timeline_right_x, timeline_top_y);
  strokeWeight(0);
  Sequence_element se_aux = (Sequence_element) sequence.get(0); 
  if (!(se_aux.nt.equals("NULL"))) {
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, se.id, se.nt, "TS"));
      // println(i + " Drawing terminal " + t.id + " at x=" + t.d_t.x_coord + " and y=" + t.d_t.y_coord);
      t.d_t.draw_v_obj (0, 0); 
    } // END for 
  } else {
    for (int i = 0; i < sequence.size(); i++) { // for each sequence (terminal) element 
      Sequence_element se = (Sequence_element) sequence.get(i);
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, se.id, "NTS"));
      // println(i + " Drawing Nonterminal " + nt.id + " at x=" + nt.d_unit.x_coord + " and y=" + nt.d_unit.y_coord);
      nt.d_unit.draw_v_obj (0, 0); 
    } // END for 
  }
  strokeWeight(1); stroke(127);
  line(timeline_left_x, timeline_bottom_y, timeline_right_x, timeline_bottom_y);
}


void draw_plans() { 
  // println("\n\n plans_top_y = " + plans_top_y + "; hierarchy_clusters_in_display.size() = " + hierarchy_clusters_in_display.size() + "; layer_height = " + layer_height);
  println("\n\n Drawing plans ... ");
  int i=0;
  // for each agent, sort the plans through increasing recursive levels and compute the max rec levels
  int max_rec_levels = -1;
  for (i=0; i < hierarchy_clusters_in_display.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i);  
    hc.compute_rec_level (); 
    if (max_rec_levels < hc.rec_levels) {max_rec_levels = hc.rec_levels;}
  }
  // for each agent, draw the plans
  float offset_from_top = 0;
  for (i=0; i < hierarchy_clusters_in_display.size(); i++) { 
    // current layer height
    float layer_height = plans_layer_heights[i]; // / hierarchy_clusters_in_display.size();
    float sub_layer_height = layer_height / max_rec_levels;
    strokeWeight(1); stroke(200); line(plans_left_x, plans_top_y+offset_from_top, plans_right_x, plans_top_y+offset_from_top);
    // println("--- y = " + (plans_top_y+layer_height*i));
    HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i);  
    hc.compute_rec_level(); float cur_rec_levels = hc.rec_levels+1;
    // for each plan, assign a sublayer
    for (int k=0; k < hc.plans.size(); k++) {
      Hierel p = (Hierel) hc.plans.get(k); // println("Drawing plan = " + p.id);
      // assign x positions to each daughter and compute the span of the hierel from left to right 
      float[] lm_rm = {-1, -1};
      lm_rm = p.x_positioning (lm_rm[0], lm_rm[1]); // assign x positions to daughters and returns the extreme positions 
      if (lm_rm[1]-lm_rm[0]>0) {
        color f_color = assign_fill_color ("NT", "HIERARCHY", "T", p.hcluster); 
        color t_color = assign_text_color ("NT", "HIERARCHY", "T", p.hcluster); 
        int rec_level_n = Integer.parseInt(p.rec_level) + 1; // take the rec level of the plan + 1 (because of rec level 0)
        // CREATE OBJECT DrawingObj_h FOR DRAWING THE PLAN
        //              (float leftmost_x, float leftmost_y, float horizontal_width, float h_height=0.0, // HEIGHT TO BE COMPUTED
        //               float leftdeg=0, float rightdeg=180, // COVERS THE WHOLE ARC
        //               color fill_color, color text_color, String txt, boolean barred)
        float offset =  sub_layer_height * rec_level_n - (float) sub_layer_height/2; // (exp(rec_level_n) / exp(max_rec_levels));   
        float plan_height = offset; //- (float) - offset/8; //  (layer_height / max_rec_levels) / 2; // - offset/8; // + layer_height; // * ((exp(rec_level_n+1)-exp(rec_level_n)) / exp(max_rec_levels + 1)); // * 0.75;
        // float w_aux = lm_rm[1]-lm_rm[0]; println("Drawing plan = " + p.id + ", width = " + w_aux);
        if (!p.accomplished.equals("NULL")) { // leftmost_x and rightmost_x are ok, DRAW COMPLETE ARC
          p.hierel_mt.d_nt = new DrawingObj_h(lm_rm[0], plans_top_y+offset_from_top, lm_rm[1]-lm_rm[0]-(cur_rec_levels-rec_level_n), plan_height, // * 0.8, // (rec_level_n + 1), // * 0.75, 
                                              (float) (lm_rm[1]-lm_rm[0])/2, (float) sub_layer_height/2,
                                              f_color, t_color, p.id, p.print, parseInt(p.rec_level), p.accomplished, p.barred); // false
          p.hierel_mt.d_nt.draw_h_obj("down", true, 2, p.barred, "NON NULL");
        } else { // p.accomplished.equals("NULL"): only draw HALF arc (PI/2) from init to last mapping unit
          p.hierel_mt.d_nt = new DrawingObj_h(lm_rm[0], plans_top_y+offset_from_top, lm_rm[1]-lm_rm[0]-(cur_rec_levels-rec_level_n), plan_height, // * 0.8, // (rec_level_n + 1), // * 0.75, 
                                              (float) (lm_rm[1]-lm_rm[0])/2, (float) sub_layer_height/2,
                                              f_color, t_color, p.id, p.print, parseInt(p.rec_level), p.accomplished, p.barred); // true                                              
          p.hierel_mt.d_nt.draw_h_obj("down", true, 2, p.barred, "NULL");
        } 
      // println("Drawing " + p.print + ", with accomplished " + p.accomplished); 
      }
    } // END FOR EACH PLAN
    offset_from_top = offset_from_top + plans_layer_heights[i]; // update offset from plans top  
  } // END for each character
  strokeWeight(1); stroke(200); // SEPARATOR BETWEEN AGENTS
  line(plans_left_x, plans_top_y+offset_from_top, plans_right_x, plans_top_y+offset_from_top);
}


void draw_scenes() { 
  for (int i=0; i < scenes.size(); i++) { //<>//
    Scene s = (Scene) scenes.get(i);
    // println(" Scene " + s.print + " .return_lm_rm "); 
    // compute the span of the scene from left to right 
    float[] lm_rm = s.return_lm_rm();
    if (lm_rm[0] != -1) { // if everything ok (leftmost extreme found) 
      NonTerminalInSequence nt_s = (NonTerminalInSequence) 
                                         nonterminals_in_sequence.get(searchALindex_item(nonterminals_in_sequence, s.id, "NTS"));
      // DrawingObj_h (float x, float y, float h_w, float h_h, float ld, float rd, color f, color t, String txt, boolean b)
      // println("--- Drawing scene " + nt_s.id + " at x interval = <" + lm_rm[0] + ", " + lm_rm[1] + ">"); // + "> and y interval = <" + nt_s.d_nt.y_coord + ", " + (nt_s.d_nt.y_coord-nt_s.d_nt.h_height) + ">");
      float offset=0; for (int j=0; j<int(s.rec_level); j++) {offset = offset + scenes_layer_heights[j];}
      nt_s.d_nt = new DrawingObj_h (lm_rm[0], scenes_bottom_y, // x, y 
        lm_rm[1] - lm_rm[0], offset + scenes_layer_heights[int(s.rec_level)] * 0.5, // width, height 
        (float) (lm_rm[1]-lm_rm[0])/2, (float) scenes_layer_heights[int(s.rec_level)] * 0.5,
        128, 128, // fill color, text color
        s.id, s.print, // identifier, print
        int(s.rec_level), // rec_level
        "SCENE", false); // accomplished "SCENE", a non value, barred
      nt_s.d_nt.draw_h_obj("up", true, 2, false, "NON NULL");
    }  
  }
}

void draw_agents_in_scenes() { 
  // float min_degree = PI/10; 
  for (int i=0; i < scenes.size(); i++) {
    Scene s = (Scene) scenes.get(i);
    NonTerminalInSequence nt_s = s.scene_mt;
    if (s.agents.size() > 0) {
      float degree = (PI/2)/s.agents.size();
      // println("--- Drawing agents in scene: number " + s.agents.size() + " and degree " + degree);
      for (int j=0; j < s.agents.size(); j++) {
        String a = (String) s.agents.get(j);
        HCluster hc = (HCluster) hierarchy_clusters.get(searchALindex_item (hierarchy_clusters, a, "HCL"));
        // DrawingObj_h (float x, float y, float h_w, float h_h, float ld, float rd, color f, color t, String txt, boolean b)
        nt_s.d_nt_a = new DrawingObj_h (nt_s.d_nt.x_coord, scenes_bottom_y, 
                                        nt_s.d_nt.h_width-inner_radius, nt_s.d_nt.h_height-inner_radius, 
                                        // j*degree+offset_angle, (j+1)*degree+offset_angle, 
                                        (float) (nt_s.d_nt.h_width-inner_radius)/2, (float) (nt_s.d_nt.h_width-inner_radius)/2,
                                        hc.c, hc.c, s.id, s.print, parseInt(s.rec_level),
                                        "SCENE", false); 
        // println("--- Drawing scene " + s.id + " at x interval = <" + lm_rm[0] + ", " + lm_rm[1] + "> and y interval = <" + nt_s.d_nt.y_coord + ", " + (nt_h.d_nt.y_coord-nt_h.d_nt.h_height) + ">");
        nt_s.d_nt_a.draw_h_obj("up", false, 5, false, "NON NULL");
      }
    } // END IF AGENTS > 0
  } // END FOR EACH SCENE
}

// DRAWS THE GRID OF VERTICAL LINES FOR APPRECIATING THE HORIZONTAL ALIGNMENT OF ELEMENTS 
void drawGrid() {
  Sequence_element se_aux = (Sequence_element) sequence.get(0); 
  float x1 = 0.0; 
  float dots = (plans_bottom_y - scenes_top_y) / 5; 
  // if the first element in sequence (so, all elements) is a terminal (because it belongs to some non terminal)
  if (!(se_aux.nt.equals("NULL"))) { // print terminals
    for (int i=0; i < sequence.size() - 1; i++) {
      Sequence_element se = (Sequence_element) sequence.get(i);
      TerminalInSequence t = (TerminalInSequence) terminals_in_sequence.get(searchALindex_item_set (terminals_in_sequence, se.id, se.nt, "TS"));
      x1 = t.d_t.x_coord + (t.d_t.x_width/2) + (unit_border); 
      fill(127); dottedLine(x1, scenes_top_y, x1, plans_bottom_y, dots);
      // if (i%2==0) {fill(200);} else {fill(255);}
      // noStroke(); rect(x1 - (t.d_t.x_width/2) - (unit_border), scenes_top_y, t.d_t.x_width, plans_bottom_y-scenes_top_y);
    } // END for
  } else { // else, print non terminals (units)
    for (int i=0; i < sequence.size() - 1; i++) { 
      Sequence_element se = (Sequence_element) sequence.get(i);
      NonTerminalInSequence nt = (NonTerminalInSequence) nonterminals_in_sequence.get(searchALindex_item (nonterminals_in_sequence, se.id, "NTS"));
      x1 = nt.d_unit.x_coord + (nt.d_unit.x_width/2) + (unit_border); // float y1 = t.d_t.y_coord + t.d_t.y_height + unit_border * 2;
      fill(127);
      dottedLine(x1, scenes_top_y, x1, plans_bottom_y, dots);
      // if (i%2==0) {fill(200);} else {fill(255);}
      // noStroke(); rect(x1 - (nt.d_unit.x_width/2) - (unit_border), scenes_top_y, nt.d_unit.x_width, plans_bottom_y-scenes_top_y);
    } // END for
  }
}


void draw_mute_clusters() {
  // println (" Drawing mute clusters ");
  for (int j=0; j < unmute_boxes.size(); j++) {unmute_boxes.remove(j);}
  for (int i=0; i < mute_clusters.size(); i++) {
    HCluster hc = (HCluster) mute_clusters.get(i);
    float radius;
    if (mutes_height < mutes_width) {radius = mutes_height/3;} else {radius = mutes_width/3;}
    DrawingObj_r d_o = new DrawingObj_r (mutes_left_x+(i+1)*radius+(i+1)*(radius/2), 
                                         mutes_top_y + (mutes_height/3), 
                                         radius, radius, radius, 
                                         hc.c, #000000, hc.id);
    d_o.draw_r_obj (0, 0);  
    Mute_box mb = new Mute_box("mb_"+hc.id, hc.id, mutes_left_x+(i+1)*radius+(i+1)*(radius/2),  mutes_top_y+(2*(mutes_height/3)), 10, 10); 
    strokeWeight(1); stroke(127); fill(230);
    rectMode(CENTER);
    rect(mb.x_coord, mb.y_coord, 10, 10); 
    // mute toggle
    PFont sansserif_8 = loadFont("SansSerif-8.vlw"); textFont(sansserif_8);
    textAlign(CENTER, CENTER); fill(0);
    text("U", mb.x_coord, mb.y_coord, 10, 10);
    // Mute_box(String i, String a, float xc, float yc, float xw, float yh)
    unmute_boxes.add(mb);  
  }
}


// by Cedric 15-01-28
void dottedLine(float x1, float y1, float x2, float y2, float steps){
 for(int i=0; i<=steps; i++) {
   float x = lerp(x1, x2, i/steps);
   float y = lerp(y1, y2, i/steps);
   noStroke();
   ellipse(x,y,1,1);
 }
}


// dottedArc(100, 100, 100, 20, PI/2, TWO_PI);
// arc(x_coord+(h_width/2), y_coord, h_width, h_height*2, left_deg-PI/2, right_deg);
// draws a dotted arc
void dottedArc(float x1, float y1, float x_w, float y_h, float start_angle, float end_angle){
  float r = (sq(y_h) + sq(x_w)/4) / (2 * y_h); 
  float angle = end_angle - start_angle; 
  float dots = abs(angle) * r / 20;
  // println("dots = " + dots); 
   for(int i=0; i<=dots; i++) {
     float x = x1 + x_w/2 * cos(start_angle+i*angle/dots); 
     float y = y1 + y_h * sin(start_angle+i*angle/dots);
     // println("x = " + x + "; y = " + y); 
     ellipse(x,y,1,1);
 }
}

// collect the agents who are active in the scenes 
void assign_agents_to_scenes() {
  // for each scene, search the plans within the same span and assign the corresponding agents
  for (int i=0; i < scenes.size(); i++) {
    Scene s = (Scene) scenes.get(i); // retrieve a scene
    if (!s.mapping_init.equals("NULL")) {
      for (int j=0; j < hierels.size(); j++) {
        Hierel he = (Hierel) hierels.get(j); // search the plans 
        if (he.hierel_mt.d_nt!=null) {
          if (contains(he.hierel_mt.d_nt.x_coord, he.hierel_mt.d_nt.x_coord + he.hierel_mt.d_nt.h_width, 
                       s.scene_mt.d_nt.x_coord, s.scene_mt.d_nt.x_coord+s.scene_mt.d_nt.h_width) || 
              contained(he.hierel_mt.d_nt.x_coord, he.hierel_mt.d_nt.x_coord + he.hierel_mt.d_nt.h_width, 
                        s.scene_mt.d_nt.x_coord, s.scene_mt.d_nt.x_coord+s.scene_mt.d_nt.h_width) ||
              overlap(he.hierel_mt.d_nt.x_coord, he.hierel_mt.d_nt.x_coord + he.hierel_mt.d_nt.h_width, 
                      s.scene_mt.d_nt.x_coord, s.scene_mt.d_nt.x_coord+s.scene_mt.d_nt.h_width)) {
            // println("FOUND SOME AGENT!");
            if (searchALindex_item (s.agents, he.hcluster, "STR")==-1) {
              s.agents.add(he.hcluster); // in case the coordinates are coincident, include the agent in the scene
            }}} // END 3 IF's
      } // END FOR EACH PLAN
    }
  } // END FOR EACH SCENE

}
