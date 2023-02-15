// *********** ROUND DRAWING OBJECTS **************
// AGENTS IN THE TRACK HEADER 

class DrawingObj_r {
  float x_coord, y_coord, x_width, y_height, radius; 
  color fill_color, text_color;
  String text;
  int layer;

  DrawingObj_r (float x, float y, float x_w, float y_h,  float r, color f, color t, String txt) {
    x_coord = x; y_coord = y; x_width = x_w; y_height = y_h; radius=r; fill_color=f; text_color=t; text=txt;
  }
 
  // draw a circle : void draw_h_obj (int x, int y, int x_w, int y_h, color f, color t, String txt) {
  void draw_r_obj (int base_x, int base_y) {
    float x_coord_translated = x_coord + base_x;
    float y_coord_translated = y_coord + base_y;
    fill (fill_color);
    stroke(0);
    // rectMode(CENTER);
    // roundrect (x_coord_translated, y_coord_translated, x_width, y_height, radius);
    ellipseMode(CENTER);
    ellipse (x_coord_translated, y_coord_translated, x_width, y_height);
    fill(text_color);
    // PFont sans_serif_12 = loadFont("SansSerif-12.vlw");
    // textFont(sans_serif_12);
    // textAlign(CENTER,CENTER);
    // text(text.substring(0,2), x_coord_translated, y_coord_translated);
    // flex_write_lines_in_box(text.substring(0,2), default_font_type, "CENTER", "CENTER", (int) x_coord_translated, (int)y_coord_translated, (int)x_width,(int) y_height);
    flex_write_lines_in_box(text, default_font_type, "CENTER", "CENTER", (int) x_coord_translated, (int)y_coord_translated, (int)x_width,(int) y_height);
    // Tooltip_box(String i, String t, float xc, float yc, float xw, float yh)
    Tooltip_box tb = new Tooltip_box(text, text, x_coord_translated, y_coord_translated, x_width, y_height); //, x_width, y_height); //+(x_width/2), +(y_height/2)
    tooltip_boxes.add(tb);
    if (text.length() > max_text_length) {max_text_length = text.length();}
    // println("text = " + text +"; text length = " + text.length() + "; max_text_length = " + max_text_length);
  }
    
} // end class DrawingObj_r


// *********** HORIZONTAL DRAWING OBJECTS **************


class DrawingObj_h {
  float x_coord; float y_coord; 
  float h_width; float h_height; 
  float box_width; float box_height;
  float left_deg, right_deg;
  color fill_color; color text_color;
  String id; 
  String text; 
  float rec_level; 
  String accomplished; 
  boolean barred; 

  DrawingObj_h (float x, float y, float h_w, float h_h, float bw, float bh, color f, color t, String i, String txt, int rl, String acc, boolean b) {
    x_coord = x; y_coord = y; h_width = h_w; h_height = h_h;  // left_deg = ld; right_deg = rd;
    fill_color=f; text_color=t; id=i; text=txt; rec_level = rl; accomplished = acc; barred=b;
    box_width=bw; // h_w/2; // 8;  
    box_height=bh; // h_height / (float) (rec_level + 0.5); // h_height/4; 
  }
  
  // draw the curve
  void draw_h_obj (String dir, boolean rectangle, int sw, boolean barred, String acc) {
    if (dir.equals("up")) { // == SCENES
      left_deg = PI; right_deg = PI + PI;
      noFill(); strokeWeight(sw); stroke(fill_color); ellipseMode(CENTER);
      edge(x_coord+(h_width/2), y_coord, h_width, h_height, left_deg, right_deg);
      // arc(x_coord+(h_width/2), y_coord, h_width, h_height*2, left_deg, right_deg);
      if (rectangle) {
        fill(fill_color); rectMode(CENTER);
        float x_rect = x_coord+h_width/2; float y_rect = y_coord-h_height; float w_rect = h_width/8; float h_rect = h_height/4;
        rect(x_rect, y_rect, box_width, box_height);
        if (barred) { // draw the cross (bar)
          stroke(0);  
          line(x_coord, y_coord-h_height/4, x_coord+h_width/8, y_coord); 
          line(x_coord, y_coord, x_coord+h_width/8, y_coord-h_height/4); 
        } 
        // Tooltip_box(String i, String t, float xc, float yc, float xw, float yh)
        // Tooltip_box tb = new Tooltip_box(text, text, x_coord+h_width/2, y_coord-h_height, h_width/8, h_height/4, h_width, h_height/2); //y_coord-h_height/2
        Tooltip_box tb = new Tooltip_box(text, text, x_rect, y_rect, w_rect, h_rect); //, h_width, h_height/2); //y_coord-h_height/2
        tooltip_boxes.add(tb);
        if (text.length() > max_text_length) {max_text_length = text.length();}  
        // PFont sansserif_12 = loadFont("SansSerif-12.vlw"); fill(#FFFFFF);   
        // write_text_in_box(id, sansserif_12, 12, int(x_coord+h_width/2-h_width/16), int(y_coord-h_height-h_height/8), int(h_width/8), int(h_height/4)); 
        fill(#000000);
        // flex_write_text_in_box(id, "SansSerif", "CENTER", "CENTER", int(x_coord+h_width/2), int(y_coord-h_height-h_height/8), int(h_width/8), int(h_height/4));
        // flex_write_lines_in_box(id, default_font_type, "LEFT", "TOP", int(x_coord+h_width/2), int(y_coord-h_height), int(h_width/8), int(h_height/4) );
        flex_write_lines_in_box(id, default_font_type, "LEFT", "TOP", int(x_coord+h_width/2), int(y_coord-h_height), box_width, box_height);
      }
    } else { // dir.equals("down") == PLANS
      noFill(); strokeWeight(sw); stroke(fill_color); ellipseMode(CENTER);
      left_deg = 0; right_deg = PI; 
      if (acc.equals("NULL")) {
        left_deg = HALF_PI; right_deg = PI; 
        dottedArc(x_coord+(h_width/2), y_coord, h_width, h_height, 0, HALF_PI);
      }
      // arc(x_coord+(h_width/2), y_coord, h_width, h_height*2, left_deg, right_deg);
      edge(x_coord+(h_width/2), y_coord, h_width, h_height, left_deg, right_deg);
      if (rectangle) {
        fill(fill_color);
        rectMode(CENTER);
        rect(x_coord+h_width/2, y_coord+h_height, box_width, box_height);
        // PFont sansserif_10 = loadFont("SansSerif-10.vlw"); fill(#000000);  
        // write_text_in_box(id, sansserif_10, 10, int(x_coord+h_width/2-h_width/16), int(y_coord+h_height-h_height/8), int(h_width/8), int(h_height/4)); 
        fill(#000000);
        // flex_write_text_in_box(id, "SansSerif", "CENTER", "CENTER", int(x_coord+h_width/2), int(y_coord+h_height), int(h_width/8), int(h_height/4));
        // flex_write_lines_in_box(id, default_font_type, "LEFT", "TOP", int(x_coord+h_width/2), int(y_coord+h_height), int(h_width/8), int(h_height/4));
        flex_write_lines_in_box(id, default_font_type, "LEFT", "TOP", int(x_coord+h_width/2), int(y_coord+h_height), box_width, box_height);
        if (barred) {
          stroke(0); 
          line(x_coord+h_width/2-h_width/16, y_coord+h_height-h_height/8, x_coord+h_width/2+h_width/16, y_coord+h_height+h_height/8);
          line(x_coord+h_width/2-h_width/16, y_coord+h_height+h_height/8, x_coord+h_width/2+h_width/16, y_coord+h_height-h_height/8);
        };
        Tooltip_box tb = new Tooltip_box(text, text, x_coord+h_width/2, y_coord+h_height, h_width/8, h_height/4); //, h_width, h_height/2); //y_coord+h_height/2
        tooltip_boxes.add(tb);
        if (text.length() > max_text_length) {max_text_length = text.length();}
      }
    }
    
  }

} // end class DrawingObj_h



// *********** VERTICAL FIXED WIDTH DRAWING OBJECTS **************

class DrawingObj_v {
  float x_coord; float y_coord; 
  float x_width; float y_height;
  color fill_color; 
  color text_color;
  String id;
  String print;
  boolean barred;

  DrawingObj_v (int x, int y, color f, color t, String idt, String pri, boolean b) {
    x_coord = x; y_coord = y; fill_color=f; text_color=t; id=idt; print = pri; barred=b;
  }

  // draw the vertical rectangle
  // void draw_v_obj(int x, int y, int x_w, int y_h, color f, color t, String desc) {  
  void draw_v_obj(float base_x, float base_y) {  
    // println("... drawing vobj id: " + id + ", print: " + print);
    float x_coord_translated = x_coord + base_x;
    float y_coord_translated = y_coord + base_y;
    fill(fill_color); 
    stroke(0);
    rectMode(CENTER);
    rect (x_coord_translated, y_coord_translated, x_width, y_height); 
    Tooltip_box tb = new Tooltip_box(print, print, x_coord_translated, y_coord_translated, x_width, y_height); //, x_width, y_height/2);// y_coord_translated-y_height/4
    tooltip_boxes.add(tb);
    if (print.length() > max_text_length) {max_text_length = print.length();}
    
    // PFont sansserif_10 = loadFont("SansSerif-10.vlw"); fill(#FFFFFF);  
    // write_text_in_box(id, sansserif_10, 10, int(x_coord_translated-x_width/2), int(y_coord_translated-y_height/2), int(x_width), int(y_height));
    fill(#FFFFFF);
    //flex_write_text_in_box(id, "SansSerif", "LEFT", "CENTER", int(x_coord_translated), int(y_coord_translated), int(x_width), int(y_height));
    flex_write_lines_in_box(id, default_font_type, "LEFT", "CENTER", int(x_coord_translated), int(y_coord_translated), int(x_width), int(y_height));
    
    // print the vertical text, in the center of the rectangle
    // PFont sans_serif_12 = loadFont("SansSerif-12.vlw"); 
    //if (fill_color!=#C0C0C0) { // COMMENT FOR DEBUG
      //textMode(CENTER);
      // horizontal_text_box(id, sans_serif_12, text_color, x_coord_translated, y_coord_translated, x_width, y_height);
    // } // COMMENT FOR DEBUG    
    if (barred) {stroke(text_color); line(x_coord_translated, y_coord_translated, x_coord_translated+x_width, y_coord_translated+y_height);};
  } // end draw_v_obj

} // end class DrawingObj_v

// *********** VERTICAL VARIABLE WIDTH DRAWING OBJECTS **************

//class DrawingObj_v_var_w {
//  int x_coord; int y_coord; 
//  int var_width; 
//  color fill_color; color text_color;
//  String text;
//  boolean barred; 

//  DrawingObj_v_var_w (int x, int y, int v_w, color f, color t, String txt, boolean b) {
//    x_coord = x; y_coord = y; var_width = v_w; fill_color=f; text_color=t; text=txt; barred=b;
//  }

//  // draw the vertical rectangle
//  void draw_v_var_w_obj(int base_x, int base_y) {  
//    int x_coord_translated = x_coord + base_x;
//    int y_coord_translated = y_coord + base_y;
//    fill(fill_color); 
//    stroke(0);
//    rect (x_coord_translated, y_coord_translated, var_width, unit_height); 
//    // print the vertical text, in the center of the rectangle
//    PFont sans_serif_12 = loadFont("SansSerif-12.vlw"); 
//    vertical_text_box(text, sans_serif_12, text_color, x_coord_translated, y_coord_translated, var_width, unit_height);
//    if (barred) {stroke(text_color); line(x_coord_translated, y_coord_translated, x_coord_translated+v_width, y_coord_translated+unit_height);};
//  } // end draw_v_var_w_obj

//} // end class DrawingObj_v_var_w 



// *********** VERTICAL TEXT **************

// writing vertically
void vertical_text(String phrase, PFont font, color c, float x, float y) {
  fill(c);
  textAlign(CENTER, CENTER);
  textFont(font);

  pushMatrix();
  translate(x, y);
  rotate(-HALF_PI);
  text(phrase, 0, 0);
  popMatrix();
}

// writing vertically
void vertical_text_box(String phrase, PFont font, color c, float x, float y, float x_w, float y_h) {
  fill(c);
  textAlign(CENTER, CENTER);
  // rectMode(CENTER);
  textFont(font);

  pushMatrix();
  translate(x, y+y_h);
  rotate(-HALF_PI);
  text(phrase, 0, 0, y_h, x_w);
  popMatrix();
}

// writing horizontally
void horizontal_text_box(String phrase, PFont font, color c, float x, float y, float x_w, float y_h) {
  fill(c);
  textAlign(CENTER, CENTER);
  // rectMode(CENTER);
  textFont(font);

  pushMatrix();
  // translate(x, y+y_h);
  translate(x, y);
  text(phrase, 0, 0, x_w, y_h);
  popMatrix();
}


// ********* SPECIAL SHAPES ***************
// rounded rectangle, by ajstarks (jun 18th 2008), http://processing.org/discourse/beta/num_1213696787.html
void roundrect(int x, int y, int w, int h, int r) {
 noStroke();
 rectMode(CORNER);

 int  ax, ay, hr;

 ax=x+w-1;
 ay=y+h-1;
 hr = r/2;

 rect(x, y, w, h);
 arc(x, y, r, r, radians(180.0), radians(270.0));
 arc(ax, y, r,r, radians(270.0), radians(360.0));
 arc(x, ay, r,r, radians(90.0), radians(180.0));
 arc(ax, ay, r,r, radians(0.0), radians(90.0));
 rect(x, y-hr, w, hr);
 rect(x-hr, y, hr, h);
 rect(x, y+h, w, hr);
 rect(x+w,y,hr, h);

}

//       left_deg = left_deg + PI; right_deg = right_deg + PI;
//       noFill(); strokeWeight(sw); stroke(fill_color); ellipseMode(CENTER);
//       arc(x_coord+(h_width/2), y_coord, h_width, h_height*2, left_deg, right_deg);

// edge(x_coord+(h_width/2), y_coord, h_width, h_height*2, left_deg, right_deg)
void edge (float x, float y, float w, float h, float ld, float rd) {
  float radius = h/10; 
  if (ld == PI) {edgeRoundRectUp (x, y, w, h, radius); } // direction UP
  if (ld == 0) {edgeRoundRectDown (x, y, w, h, radius); } // direction UP
}

void edgeRoundRectUp (float center_x, float center_y, float w, float h, float r) {
  float left_x = center_x - (float) w/2; float right_x = center_x + (float) w/2;
  line(left_x, center_y, left_x, (center_y-h)+r);
  arc(left_x + r, (center_y-h)+r, 2*r, 2*r, PI, PI+HALF_PI);
  line(left_x+r, center_y-h, right_x-r, center_y-h);
  arc(right_x-r, (center_y-h)+r, 2*r, 2*r, PI+HALF_PI, TWO_PI);
  line(right_x, (center_y-h)+r, right_x, center_y);
}

void edgeRoundRectDown (float center_x, float center_y, float w, float h, float r) {
  float left_x = center_x - (float) w/2; float right_x = center_x + (float) w/2;
  line(left_x, center_y, left_x, (center_y+h)-r);
  arc(left_x+r, (center_y+h)-r, 2*r, 2*r, HALF_PI, PI);
  line(left_x+r, center_y+h, right_x-r, center_y+h);
  arc(right_x-r, (center_y+h)-r, 2*r, 2*r, 0, HALF_PI);
  line(right_x, (center_y+h)-r, right_x, center_y);
}
