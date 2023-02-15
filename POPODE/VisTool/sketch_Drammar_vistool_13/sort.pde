
// computes the recursive sublayers and orders scenes through increasing rec level
void sort_scenes_increasing_rec_sublayers () {
  // println("sort_scenes_increasing_rec_sublayers"); 
  ArrayList ordered_scenes = new ArrayList(); int i=0; int rec_level = 0;
  for (i=0; i<scenes.size(); i++) {Scene s = (Scene) scenes.get(i); s.rec_level="0";}
  boolean more_rec_level = true; 
  // for each scene, find its rec level
  while (more_rec_level) {
    rec_level++; more_rec_level=false;
    for (int scenes_cursor = 0; scenes_cursor < scenes.size(); scenes_cursor++) {       
      Scene s = (Scene) scenes.get(scenes_cursor); 
      // println(scenes_cursor + " Scene " + s.print + " .return_lm_rm "); 
      float s_lm_rm[] = s.return_lm_rm(); 
      // println(scenes_cursor + " Scene s " + s.print + ", with lm=" + s_lm_rm[0] + " and rm=" + s_lm_rm[1] + " and rec level " + s.rec_level); 
      // if such scene contains scenes of the immediately previous level, assign this scene the current rec level 
      for (i=0; i < scenes.size(); i++) {
        Scene s1 = (Scene) scenes.get(i); 
        // println(" Scene " + s1.print + " .return_lm_rm "); 
        float s1_lm_rm[] = s1.return_lm_rm();
        // println("---" + i + " Scene s1 " + s1.print + ", with lm=" + s1_lm_rm[0] + " and rm=" + s1_lm_rm[1] + " and rec level " + s1.rec_level); 
        if (int(s1.rec_level)==rec_level-1) {
          // println("------- CONFRONTO"); 
          if (proper_contains(s_lm_rm[0], s_lm_rm[1], s1_lm_rm[0], s1_lm_rm[1])) {s.rec_level=str(int(s1.rec_level) + 1); more_rec_level=true;}
        }
      }
      // println(scenes_cursor + " Scene s " + s.print + ", with rec_level=" + s.rec_level); 
    }
  } // END WHILE !stop_rec_level
  // store scenes in increasing order 
  for (int rec_level_cursor = 0;  rec_level_cursor < rec_level + 1; rec_level_cursor++) {
    for (i=0; i<scenes.size(); i++) {
      Scene s = (Scene) scenes.get(i);
      if (s.rec_level.equals(str(rec_level_cursor))) {ordered_scenes.add(s);} ;}
  }
  scenes = ordered_scenes; // scenes incorrectly annotated remain out of the list
}

// computes the recursive sublayers and orders scenes through increasing rec level
void sort_agents_decreasing_reclevel () { 
  // println("sort_agents_decreasing_reclevel"); 
  ArrayList ordered_hclusters = new ArrayList(); int i=0; int max_rec_level = -1;
  for (i=0; i<hierarchy_clusters_in_display.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters_in_display.get(i); 
    if(hc.rec_levels>max_rec_level) {max_rec_level=hc.rec_levels;}
  }
  println("max_rec_level = " + max_rec_level);
  // store scenes in increasing order 
  for (int rec_level_cursor = max_rec_level;  rec_level_cursor >=0; rec_level_cursor--) {
    for (i=0; i<hierarchy_clusters.size(); i++) {
      HCluster hc = (HCluster) hierarchy_clusters.get(i);
      if (hc.rec_levels==rec_level_cursor) {ordered_hclusters.add(hc);}
    }
  }
  hierarchy_clusters_in_display = ordered_hclusters; // hclusters incorrectly annotated remain out of the list
}

// computes the recursive sublayers and orders plans through increasing rec level
void sort_plans_increasing_rec_sublayers () {
  // println("sort_plans_increasing_rec_sublayers"); 
  ArrayList ordered_plans = new ArrayList(); int i=0; int rec_level = 0;
  for (i=0; i<hierels.size(); i++) {Hierel p = (Hierel) hierels.get(i); p.rec_level="0";}
  boolean more_rec_level = true; 
  // for each plan, find its rec level
  while (more_rec_level) {
    rec_level++; more_rec_level=false;
    for (int plans_cursor = 0; plans_cursor < hierels.size(); plans_cursor++) {       
      Hierel p = (Hierel) hierels.get(plans_cursor); 
      // println(" Plan " + p.print + " .return_lm_rm "); 
      float p_lm_rm[] = p.return_lm_rm(); 
      // println(plans_cursor + " Plan p " + p.print + ", with lm=" + p_lm_rm[0] + " and rm=" + p_lm_rm[1] + " and rec level " + p.rec_level); 
      // if such plan contains plans of the immediately previous level, assign this plan the current rec level 
      for (i=0; i < hierels.size(); i++) {
        Hierel p1 = (Hierel) hierels.get(i); 
        println(" Plan " + p1.print + " .return_lm_rm "); 
        float p1_lm_rm[] = p1.return_lm_rm();
        // println("---" + i + " Plan p1 " + p1.print + ", with lm=" + p1_lm_rm[0] + " and rm=" + p1_lm_rm[1] + " and rec level " + p1.rec_level); 
        if (p.hcluster.equals(p1.hcluster) && int(p1.rec_level)==rec_level-1) {
          // println("------- CONFRONTO"); 
          if (proper_contains(p_lm_rm[0], p_lm_rm[1], p1_lm_rm[0], p1_lm_rm[1])) {p.rec_level=str(int(p1.rec_level) + 1); more_rec_level=true;}
        }
      }
      // println(scenes_cursor + " Scene s " + s.print + ", with rec_level=" + s.rec_level); 
    }
  } // END WHILE !stop_rec_level
  // store plans in increasing order 
  for (int rec_level_cursor = 0;  rec_level_cursor < rec_level + 1; rec_level_cursor++) {
    for (i=0; i<hierels.size(); i++) {
      Hierel p = (Hierel) hierels.get(i);
      if (p.rec_level.equals(str(rec_level_cursor))) {ordered_plans.add(p);} ;}
  }
  hierels = ordered_plans; // plans incorrectly annotated remain out of the list
}
