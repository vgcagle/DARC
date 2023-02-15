// Tooltip boxes 

class Tooltip_box {
  String id; 
  ToolTip tooltip;
  float x_coord, y_coord;
  float x_width, y_height;
  // float tt_width, tt_height;

  Tooltip_box(String i, String tt, float xc, float yc, float xw, float yh) { // , float ttw, float tth) {
    id = i;
    x_coord = xc; y_coord = yc;
    x_width = xw; y_height = yh;
    // tt_width = ttw; tt_height = tth;
    tooltip = new ToolTip((int) x_coord, (int) y_coord, (int) x_width, (int) y_height, tt); // (int) tt_width, (int) tt_height, tt);
    // println("tooltip = " + t + "; width = " + tooltip.tooltipWidth); 
  } 
}


// ##########################################################
// Class to display a tooltip in a semi-transparent rectangle. 
// ##########################################################

class ToolTip{
  // main variables
  String mText; // text of the tooltip
  int x; // x coordinate center
  int y; // y coordinate center
  int max_w; // max width
  int max_h; // max_height
  String lines[]; // lines in which the text is broken for the tooltip display
  
  // boolean doClip = true; // ??? 
  int delta_x = 5; // horizontal space between mouse and tooltip 
  float tooltipWidth = 0; // width of the text
  float tooltipHeight = 0; // height of the text
  // int rectWidthMargin; // margin in width
  // int rectHeightMargin; // margin in height
  color tbackground = color(60, 60, 60, 10); //  // default background color   
  int fontSize = 12; // size of the tooltip font
  PFont ttfont = createFont("SansSerif", fontSize, true);
  
  PImage clip = null; int clipx; int clipy; // the background image and its coordinates
  
  // **** DEFINITION OF THE TOOLTIP OBJECT ****
  ToolTip(int _x, int _y, int _max_w, int _max_h, String tttext){
    // set main variables
    x = _x; y = _y; max_w = _max_w; max_h = _max_h; mText = tttext; // set coordinates and text

    if (interactive) {
      // lines = split_string_into_lines (mText, (int) tooltip_linewidth); // compute the lines of text
      lines = split_string_into_lines (mText, (int) max_w); // compute the lines of text
    } else {
      lines = split_string_into_lines (mText, (int) tooltip_linewidth_static); // compute the lines of text
    }
    String[] words = split_text_into_words (mText);
    float[] box_size = determine_box_size(words, default_font_aspect_ratio, default_font_size);
    tooltipWidth = box_size[0];
    tooltipHeight = box_size[1];
  }
  
  void setBackground(color c){
    tbackground = c;
  }
  
  void restoreClip(){
    imageMode(CORNER);
    image(clip,clipx,clipy);
  }
  
  void hide(){
    restoreClip();
  }
  
  // draw balloon with text
  void drawBalloon(int bx, int by, int w, int h){
    // println("\n DRAW BALLOON");
    //fill(tbackground); noStroke(); rectMode(CENTER); // rectMode(CORNER);  
    //rect(bx, by, w, h); 
    //fill(0); textFont(ttfont);  // textAlign(LEFT); // textAlign(CENTER);
    write_lines_in_fixed_fontsize(mText, default_font_type, default_font_size, "LEFT", "TOP", bx, by);
    // flex_write_lines_in_box(mText, default_font_type, "LEFT", "TOP", bx+margin, by, max_w-margin, max_h);
    //for (int i=0; i<lines.length; i++) {
      //text(lines[i], bx-w/2+margin, by-h/2+(i+1)*fontSize);
      // text(lines[i], bx+margin, by+(i)*fontSize);
      //println(lines[i]); 
    //}
    
    // write_text_dots_in_box(mText, default_font_type, "LEFT", "TOP", bx, by, max_w, max_h); 
    
  }
        
  void display() { // display the tooltip (2017 july 3)
    float bx = x+(tooltipWidth/2); //  //<>//
    float by = y-(tooltipHeight/2); //     
    // Grab whatever is going to be behind our tooltip...
    // imageMode(CORNER);
    // clip = get(bx,by,tooltipWidth,tooltipHeight); //bx-20, by-20, +rectWidthMargin+60, +rectHeightMargin+60
    // clipx = bx; // bx-20
    // clipy = by; // by-20; 
    if (bx+(tooltipWidth/2) >= width){bx = width-(tooltipWidth/2);} //  
    if (by-(tooltipHeight/2) <= 0){by = tooltipHeight/2;} //  
    // if (y-tooltipHeight <= 0){by = 0;} // int(y+tooltipHeight);} //+rectHeightMargin);
    write_lines_in_fixed_fontsize(mText, default_font_type, default_font_size, "LEFT", "TOP", (int)bx, (int)by);
    // drawBalloon(bx,by,tooltipWidth,tooltipHeight);  // by-fontSize
    // }
  } // END METHOD display

  void displayStatic() { // display the tooltip  
    int bx = x; // int(x+delta_x);
    int by = y; // -tooltipHeight/2; // int(y-tooltipHeight);    
    // if (x+tooltipWidth/2 >= width){bx = x-tooltipWidth/2;} // int(x-delta_x-tooltipWidth);} //-rectWidthMargin); 
    //if (y-tooltipHeight/2 <= 0){by = y+tooltipHeight/2;} // int(y+tooltipHeight);} //+rectHeightMargin);    
    // drawBalloon(bx,by,tooltipWidth,tooltipHeight);  // by-fontSize
    // fill(tbackground); noStroke(); rectMode(CENTER); // rectMode(CORNER);  
    // rect(bx+margin, by, max_w-2*margin, max_h); 
    fill(#000000);
    //flex_write_text_in_box(mText, default_font_type, "LEFT", "TOP", bx+margin, by, max_w, max_h);
    flex_write_lines_in_box(mText, default_font_type, "LEFT", "TOP", bx+margin, by, max_w-margin, max_h); 
    //write_text_dots_in_box(mText, default_font_type, "LEFT", "TOP", bx+margin, by, max_w-margin, max_h);
    
    // }
  } // END METHOD display

} // END CLASS ToolTip
