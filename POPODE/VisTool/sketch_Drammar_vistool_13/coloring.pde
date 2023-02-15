// colors 

void assign_colors_to_agents() {
  float next_hue = 0;
  if (hierarchy_clusters.size() > 0) {
    for (int i=0; i < hierarchy_clusters.size(); i++) {
      HCluster hc = (HCluster) hierarchy_clusters.get(i);
      //if (hc.plans.size() > 0) {
        // hc.c = color((i*23)%360, 40, 100); 
        hc.c = color(next_hue, 100, 75, 100);  
        next_hue = (next_hue+(360/hierarchy_clusters.size())+19)%360;
      //}
    }
  }
}

color assign_fill_color (String t_or_nt, String seq_or_hie, String t_or_xt, String hcluster) {
  // if (t_or_nt.equals("T") && seq_or_hie.equals("SEQUENCE") && t_or_xt.equals("T")) {return #FFFFFF;}; // WHITE  //<>//
  if (t_or_nt.equals("T") && t_or_xt.equals("T")) {return #004000;}; // green (16 SVG)  #008000
  if (t_or_nt.equals("T") && t_or_xt.equals("XT")) {return #880000;}; // red  #ff0000  255,0,0
  if (t_or_nt.equals("T") && seq_or_hie.equals("SEQUENCE") && t_or_xt.equals("UN")) {return #C0C0C0;}; // silver (16 SVG)  #C0C0C0  192,192,192
  if (t_or_nt.equals("NT") && seq_or_hie.equals("SEQUENCE") && t_or_xt.equals("NULL")) {return #00FF00;}; // green  #00ff00  0,255,0
  if (t_or_nt.equals("NT") && seq_or_hie.equals("HIERARCHY") && t_or_xt.equals("T")) {
    int index = searchALindex_item (hierarchy_clusters, hcluster, "HCL");
    HCluster hc = (HCluster) hierarchy_clusters.get(index);
    return hc.c;
  }; 
  return #000000; // black  #000000  0,0,0
}

color assign_text_color (String t_or_nt, String seq_or_hie, String t_or_xt, String hcluster) {
  //if (t_or_nt.equals("T") && t_or_xt.equals("T")) {return #ffffff;}; // white  #ffffff  255,255,255    && seq_or_hie.equals("SEQUENCE") && //<>//
  //// if (t_or_nt.equals("T") && seq_or_hie.equals("SEQUENCE") && t_or_xt.equals("T")) {return #000000;}; // black      
  //// if (t_or_nt.equals("T") && seq_or_hie.equals("HIERARCHY") && t_or_xt.equals("T")) {return #ffffff;}; // white      
  //if (t_or_nt.equals("T") && t_or_xt.equals("XT")) {return #ffffff;}; // white  #ffffff  255,255,255
  //if (t_or_nt.equals("T") && t_or_xt.equals("UN")) {return #000000;} // black  #000000  0,0,0
  //// if (t_or_nt.equals("T") && t_or_xt.equals("UN")) {return #ff0000;} // red  #ff0000  255,0,0
  //if (t_or_nt.equals("NT") && seq_or_hie.equals("SEQUENCE") && t_or_xt.equals("NULL")) {return #ffffff;}; // white #ffffff  255,255,255
  //if (t_or_nt.equals("NT") && seq_or_hie.equals("HIERARCHY") && t_or_xt.equals("T")) {return #000000;}; // black  #000000  0,0,0
  return #FFFFFF; // #C0C0C0; // silver (16 SVG)  #C0C0C0  192,192,192
}
