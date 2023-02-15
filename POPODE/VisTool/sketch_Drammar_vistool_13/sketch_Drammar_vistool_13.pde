// DRAWING CHARACTERS' INTENTIONS IN A STORY: THE ACTION MAP

// Interactive visualization tool for storyflow visualization in terms of characters' intentions in a story flow: 
// 1. fixed screen size to 1024 x 576
// 2. represent characters' intentions with colored arcs
// 3. characters' participation to story motivations aligned with story incidents and scenes

import processing.video.*;

// counters for the frame indices in capturing a video 
int u_counter = 0; int d_counter = 0; int c_counter = 0; int m_counter = 0; int dm_counter = 0;

// ##################################################################################
// ###########################  GLOBAL VARIABLES   ##################################
// ##################################################################################

// interactive VS. print 
boolean interactive = false; // false, true //<>//

// ZOOM DATA
float xo, yo; float zoom = 1; float angle = 0;

// GENERAL VISUALIZATION SETTINGS

float size_x, size_y; float aspect_ratio = (float) 16/9; // size of the drawing screen 
float header_width_L, header_height_L; float height_L_factor = 0.07; // main header width and height; 7% of size_y
float header_width_S, header_height_S; float height_S_factor = 0.03; // secondary header width and height; 3% of size_y
String title = "NULL"; 

float separator_stroke; float separator_stroke_factor = 0.005; // stroke for header boundary; 5 out of 1000 of size_y for stroke 

// int min_track_headers_width = 30; // width of track header 
// int min_track_headers_height = 5; // height of track header 
// int min_track_headers_radius = 3; // radius of track header 

float track_header_width_quot = 20;
float track_header_left_x, track_header_right_x;
float track_header_top_y; float track_header_bottom_y; 
float track_header_width, track_header_height;

float scenes_height_factor = 0.3; // scenes are 3/10 of visualization (except header);
float scenes_left_x, scenes_right_x; 
float scenes_width, scenes_height;  
float scenes_top_y, scenes_bottom_y;  
float scenes_layer_heights[]; 

float timeline_height_factor = 0.1; // timeline is 1/10 of visualization (except header);
float timeline_left_x, timeline_right_x; 
float timeline_width , timeline_height;  
float timeline_top_y, timeline_bottom_y; 

float plans_height_factor = 0.6; // plans are 6/10 of visualization (except header);
float plans_left_x, plans_right_x; 
float plans_width, plans_height;  
float plans_top_y, plans_bottom_y; 
float plans_layer_heights[];

float mutes_height_factor = 0.05; // mutes are 1/20 of visualization (except header);
float mutes_left_x, mutes_right_x; 
float mutes_width, mutes_height;  
float mutes_bottom_y, mutes_top_y; 

// CONSTANTS
// ELEMENTS OF VISUALIZATION 
float min_unit_width = 3.0; // 1 pixel is the min linear side of a story incident (square of some side)
float unit_border = 1.0; // border of unit  
float unit_width, unit_height; // actual side of a story incident, to be computed later

// print sizes
float minFontSize = 12; float minCharNumber = 40; float unitArea = minFontSize * minCharNumber; // determine area of unit box
// float unit_ext_size_x = sqrt(unitArea); // basic square side  
// float v_width = 0; // height of a story incident - to be decided later
// int h_height = 20; // height of a story incident

int min_plans_height = 20; // min height of char layer; actual height to be determined
int min_incr_plan_height = 10; // for each plan layer in a hierarchy
int min_scene_layer_height = 20; // height of scene layer

int inner_radius = 5;
float offset_angle = PI/4;
int margin = 3; // margin from borders

int max_text_length = 0;
float tooltipbox_width_factor = 0.4;
float tooltip_linewidth = 50;
float tooltip_linewidth_static = 25;

int default_font_size = 12;
String default_font_type = "SansSerif";
float default_font_aspect_ratio = 0.5; // 0.525;

/*
int sequence_size_x = 0;  int sequence_size_y = 0; // initialize x and y sizes of the sequence (to be computed)
int hierarchies_size_x = 0; int hierarchies_size_y = 0; // initialize x and y sizes of the hierarchies (to be computed)
*/

// DATA ELEMENTS
ArrayList hierarchy_clusters; // set of hierarchy clusters
ArrayList hierarchy_clusters_in_display; // set of hierarchy clusters that are currently displayed
ArrayList mute_clusters; // clusters (agents) that are mute

ArrayList sequence; // sequence of terminals (SEQEL)
ArrayList terminals_in_sequence; // set of terminal elements in sequence
ArrayList hierels; // set of hierarchy elements (plans)
ArrayList scenes; // set of scenes 

ArrayList terminals_in_hierarchies; // set of terminal elements in hierarchies
ArrayList nonterminal_daughters; // set of daughters, which are NON terminals
ArrayList nonterminals_in_sequence; // set of NON terminal elements in sequence
ArrayList nonterminals_in_hierarchies; // set of NON terminal elements in sequence

ArrayList tooltip_boxes; // all the boxes that have tooltips
ArrayList mute_boxes; // all the mute boxes in the track headers
ArrayList unmute_boxes; // all the mute boxes in the mute clusters, to be unmuted

// AUXILIARY CONSTANTS AND VARIABLES
int max_rec_level = 100;

// ##################################################################################
// #############################  SETTINGS   ########################################
// ##################################################################################

void settings() {
  // preparatory instructions, initializations
  sequence = new ArrayList(); 
  terminals_in_sequence = new ArrayList();  
  hierarchy_clusters = new ArrayList();
  hierarchy_clusters_in_display = new ArrayList();
  mute_clusters = new ArrayList(); // clusters (agents) that are mute

  hierels = new ArrayList();
  scenes = new ArrayList();
  terminals_in_hierarchies = new ArrayList();  
  nonterminal_daughters = new ArrayList();
  nonterminals_in_sequence = new ArrayList(); 
  nonterminals_in_hierarchies = new ArrayList(); 

  tooltip_boxes = new ArrayList();
  mute_boxes = new ArrayList();
  unmute_boxes = new ArrayList();

  // ****** LOADING ****** 
  XML xml_drama = loadXML("drama_test.xml"); // load the XML file    
  println("Drama file LOADED ... \n");
  loading_drama_repository1(xml_drama); // loading repository elements from XML file
  loading_drama(xml_drama); // loading plans, timelines, and mapping from XML file
  // what agents are in display on the tracks
  for (int i=0; i < hierarchy_clusters.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters.get(i);
    println(i + " Agent " + hc.id + " of " + hierarchy_clusters.size() + " agents "); 
    hierarchy_clusters_in_display.add(hc); // display agent's track
    // if (hc.plans.size() != 0) { hierarchy_clusters_in_display.add(hc); } // if this agent has plans, then display its track
  }; 
  // println(" Display for " + hierarchy_clusters_in_display.size() + " agents "); 

  // ############### NON INTERACTIVE ################
  if (!interactive) { 
    unit_width = 500;
    size_x = (int) sequence.size() * unit_width; // (float) sqrt(unitArea);
    size_y = (int) ((float) (1/aspect_ratio) * size_x);
    println("NON INTERACTIVE SIZE IS " + size_x + " x " + size_y); 
  } else {
    size_x = 1280; //1920; // 1440; //800; //   1024; // size of the drawing screen 
    size_y = size_x * (float) (1 / aspect_ratio); // 1080; // 810; // 450; //      576; 
    println("INTERACTIVE SIZE IS " + size_x + " x " + size_y); 
  }  

  header_width_L = size_x; header_height_L = size_y * height_L_factor; // / 15; // main header width and height
  header_width_S = size_x; header_height_S = size_y * height_S_factor; // /30; secondary header width and height
  separator_stroke = size_y * separator_stroke_factor; // 3

  track_header_left_x = 0; track_header_right_x = (float) size_x/track_header_width_quot;
  track_header_top_y = header_height_L; track_header_bottom_y = size_y; 
  track_header_width = track_header_right_x - track_header_left_x; 
  track_header_height = track_header_bottom_y - track_header_top_y;

  scenes_left_x = track_header_right_x; scenes_right_x = size_x; 
  scenes_width = scenes_right_x - scenes_left_x; 
  scenes_height = (float) track_header_height * scenes_height_factor;  // scenes are 3/10 of visualization;
  scenes_top_y = track_header_top_y; scenes_bottom_y = scenes_top_y + scenes_height;  

  timeline_left_x = track_header_right_x; timeline_right_x = size_x; 
  timeline_width = timeline_right_x - timeline_left_x; 
  timeline_height = track_header_height * timeline_height_factor;  // timeline is 1/10 of visualization;
  timeline_top_y = scenes_bottom_y; timeline_bottom_y = timeline_top_y + timeline_height; 

  plans_left_x = track_header_right_x; plans_right_x = size_x; 
  plans_width = plans_right_x - plans_left_x; 
  plans_height = track_header_height * plans_height_factor;  // plans are 6/10 of visualization;
  plans_top_y = timeline_bottom_y; plans_bottom_y = plans_top_y + plans_height; 

  mutes_left_x = track_header_right_x; mutes_right_x = size_x; 
  mutes_width = mutes_right_x - mutes_left_x; 
  mutes_height = track_header_height * mutes_height_factor;  // plans are 6/10 of visualization;
  mutes_bottom_y = size_y; mutes_top_y = mutes_bottom_y-mutes_height; 

  // CONSTANTS
  // ELEMENTS OF VISUALIZATION 
  unit_height = timeline_height - unit_border*2; // actual side of a story incident, to be computed later

  // unit_height = size_y / 10; // 20; // height of a story incident
  // v_width = unitArea / unit_height; // 20; // height of a story incident
  // h_height = 20; // height of a story incident

  min_plans_height = 50; // min height of char layer; actual height to be determined
  min_incr_plan_height = 10; // for each plan layer in a hierarchy
  min_scene_layer_height = 20; // height of scene layer

  max_text_length = 0;
  tooltipbox_width_factor = 0.4;
 //<>//
  size((int) size_x, (int) size_y); 

}

// ##################################################################################
// #############################  VOID SETUP   ######################################
// ##################################################################################

void setup() {  

  // frameRate(25);
  // prepare();
  // size(800, 450);

  colorMode(HSB, 360, 100, 100, 100);

  background(#FFFFFF);
  smooth();
  noStroke();

  assign_colors_to_agents();

  // compute the positions of each element in the sequence 
  create_sequence_nodes();
  
  sort_scenes_increasing_rec_sublayers();  // compute rec level of scenes and sort them
  compute_scenes_layer_heights(); // on the basis of rec level splits the drawing area
  // sort_plans_increasing_rec_sublayers (); // obsolete, now computed elsewhere
  for (int i=0; i < hierarchy_clusters.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters.get(i);
    hc.compute_rec_level();
  }
  // sort_agents_decreasing_reclevel();
  
  // println("###################### POSITIONING ... #######################");
  
  compute_plans_layer_heights();
  
  // DRAWING 
  println("###################### DRAWING ... #######################");
  if (interactive) {draw_header_L(0, 0, header_width_L, header_height_L, #FFFFFF, #000000, title + ": interactive chart");}
  else {draw_header_L(0, 0, header_width_L, header_height_L, #FFFFFF, #000000, title + ": static chart");}
  draw_track_headers(); 
  drawGrid();
  draw_sequence();
  // sort_plans_increasing_rec_sublayers ();
  draw_plans();
  if (scenes_layer_heights!=null) {
    draw_scenes();
    assign_agents_to_scenes(); 
    // draw_agents_in_scenes(); //<>//
  }
  if (!interactive) {// layover_static(); 
    save(title + "_visual.png"); exit();}
  /*
  */
  // layover_static();
}

// ##################################################################################
// #############################  VOID DRAW   ######################################
// ##################################################################################

void draw() {

  update_counters();
  // saveFrame("frames/test_iChart"+dm_counter+m_counter+c_counter+d_counter+u_counter+".tif");  // Add window's pixels to movie

  if (interactive) {
  System.gc();

  scale (zoom);
  translate (xo, yo);

  background(#FFFFFF); // set background to white
  
  // Scene p = (Scene) scenes.get(scenes.size()-1); // retrieve the play

  // sort_plans_increasing_rec_sublayers ();
  for (int i=0; i < hierarchy_clusters.size(); i++) {
    HCluster hc = (HCluster) hierarchy_clusters.get(i);
    hc.compute_rec_level();
  }  
  compute_plans_layer_heights();
  update_spaces(); 
  
  color fill_color = color(0,0,100,100); color text_color = color(0,0,0,100); 
  draw_header_L(0, 0, header_width_L, header_height_L,  fill_color, text_color, title + ": interactive chart"); //<>//
  draw_track_headers();
  drawGrid();
  draw_sequence();
  // sort_plans_increasing_rec_sublayers ();
  draw_plans();
  if (scenes_layer_heights!=null) {
    draw_scenes();
    assign_agents_to_scenes(); 
    // draw_agents_in_scenes();
  }
  draw_mute_clusters();
  
  layover();  // COMMENTED: for taking pictures

/*
  */
  } // END if interactive

} // END DRAW function


// ********************************** ******************************* **************************************
// ********************************** END DRAW **** START SUBROUTINES **************************************
// ********************************** ******************************* **************************************


// ********************************** AUXILIARY FUNCTIONS **************************************




// ********************************** INTERACTIONS **************************************

void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {yo += 30;} else 
    if (keyCode == DOWN) {yo -= 30;} else 
    if (keyCode == RIGHT) {xo -= 10;} else 
    if (keyCode == LEFT) {xo += 10;}
  }
  if (key == 105) {zoom += .5;}  
  if (key == 111) {zoom -= .5;}  
  if (key == 32) {
    angle = 0;
    zoom = 1;
    xo = 0; // width/2;
    yo = 0; // height/2;
  }
}
 
void mouseDragged() {
  xo = xo + (mouseX - pmouseX);
  yo = yo + (mouseY - pmouseY);
}

void mouseClicked() { 
  //PFont sans_serif_10 = loadFont("SansSerif-10.vlw"); 
  //textFont(sans_serif_10); 
  //for (int i=0; i<tooltip_boxes.size(); i++) {
  //  Tooltip_box tb = (Tooltip_box) tooltip_boxes.get(i);
  //  if (mouseX < tb.x_coord+tb.x_width/2 && mouseX > tb.x_coord-tb.x_width/2) {
  //    if (mouseY < tb.y_coord+tb.y_height/2 && mouseY > tb.y_coord-tb.y_height/2) {
  //      // float tw = textWidth(tb.tooltip);
  //      float tw = 200;
  //      float th = textWidth(tb.tooltip)/tw;
  //      fill(0, 0, 255);
  //      noStroke();
  //      rectMode(CENTER);
  //      rect(mouseX, mouseY-10, tw + 15, th + 30);
  //      fill(0);
  //      text(tb.tooltip, mouseX, mouseY-10, tw + 15, th + 30);
  //    }
  //  }
  //} // END FOR tooltip_boxes
  for (int i=0; i<mute_boxes.size(); i++) {
    Mute_box mb = (Mute_box) mute_boxes.get(i);
    if (mouseX < mb.x_coord + mb.x_width/2 && mouseX > mb.x_coord - mb.x_width/2) {
      if (mouseY < mb.y_coord + mb.y_height/2 && mouseY > mb.y_coord - mb.y_height/2) {
        HCluster hc = (HCluster) hierarchy_clusters.get(searchALindex_item(hierarchy_clusters, mb.hc, "HCL"));
        hierarchy_clusters_in_display.remove(searchALindex_item(hierarchy_clusters_in_display, hc.id, "HCL"));
        mute_clusters.add(hc);
        println (" Removed agent " + hc.id + " from display"); 
        mouseX = width + 1; mouseY = height + 1;
      }
    }
  } // END FOR mute_boxes
  for (int i=0; i<unmute_boxes.size(); i++) {
    Mute_box mb = (Mute_box) unmute_boxes.get(i);
    if (mouseX < mb.x_coord + mb.x_width/2 && mouseX > mb.x_coord - mb.x_width/2) {
      if (mouseY < mb.y_coord + mb.y_height/2 && mouseY > mb.y_coord - mb.y_height/2) {
        HCluster hc = (HCluster) hierarchy_clusters.get(searchALindex_item(hierarchy_clusters, mb.hc, "HCL"));
        mute_clusters.remove(searchALindex_item(mute_clusters, hc.id, "HCL"));
        hierarchy_clusters_in_display.add(hc);
        // println (" Added agent " + hc.id + " to display"); 
        mouseX = width + 1; mouseY = height + 1;
      }
    }
  } // END FOR unmute_boxes
  //layover();
}

// text layover through tooltip
void layover() {
  // println("max_text_length = " + max_text_length);
  // allocate the space for the tooltip
  // float tooltip_box_side = max_text_length * tooltipbox_width_factor; // max_text_length == 0, tooltipbox_width_factor == 0.4
  // search for the tooltips to display
  float x = mouseX; float y = mouseY; // capture mouse position
  for (int i=0; i<tooltip_boxes.size(); i++) { // for each box that includes a tooltip 
    Tooltip_box tb = (Tooltip_box) tooltip_boxes.get(i);
    if (x < tb.x_coord+tb.x_width/2 && x > tb.x_coord-tb.x_width/2) { // if the mouse is over such box
      if (y < tb.y_coord+tb.y_height/2 && y > tb.y_coord-tb.y_height/2) {
        // if (mouseX - tooltip_box_side/2 < 0) {x = tooltip_box_side/2;} 
        // if (mouseX + tooltip_box_side/2 > width) {x = width - tooltip_box_side/2;} 
        // if (mouseY - tooltip_box_side/2 < 0) {y = tooltip_box_side/2;} 
        // if (mouseY + tooltip_box_side/2 > height) {y = height - tooltip_box_side/2;}         
        // String tipText = "Alpha: "+tooltipAlpha+"\nR: "+tooltipR+"  G: "+tooltipG+"  B: "+tooltipB+"\nmouseX= "+x+" mouseY= "+y;  
        tb.tooltip.x= (int) x; tb.tooltip.y= (int) y; // new ToolTip((int) x, (int) y, tb.tooltip); // ToolTip((int) x, (int) y, tb.tooltip);
        color c = color(0, 0, 80, 10); // color(0, 80, 255, 30);
        tb.tooltip.setBackground(c); // color(0,80,255,30));
        tb.tooltip.display();
      }
    }
  } // END FOR tooltip_boxes
}

// text layover through tooltip
void layover_static() {
  // println("max_text_length = " + max_text_length);
  // allocate the space for the tooltip
  // float tooltip_box_side = max_text_length * tooltipbox_width_factor; // max_text_length == 0, tooltipbox_width_factor == 0.4
  // search for the tooltips to display
  for (int i=0; i<tooltip_boxes.size(); i++) { // for each box that includes a tooltip 
    Tooltip_box tb = (Tooltip_box) tooltip_boxes.get(i);
    // float x = tb.x_coord+tb.x_width/2; float y = tb.y_coord-tb.y_height/2; 
    float x = tb.x_coord; float y = tb.y_coord; 
    tb.tooltip.x= (int) x; tb.tooltip.y= (int) y; // new ToolTip((int) x, (int) y, tb.tooltip); // ToolTip((int) x, (int) y, tb.tooltip);
    color c = color(0, 0, 80, 10); // color(0, 80, 255, 30);
    tb.tooltip.setBackground(c); // color(0,80,255,30));
    tb.tooltip.displayStatic();
  } // END FOR tooltip_boxes
}
