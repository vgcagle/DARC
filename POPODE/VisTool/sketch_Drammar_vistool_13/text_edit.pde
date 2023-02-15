// split a string into strings of length < max_length
String[] split_text (String text, int max_length) {
  // boolean max_length_b = true;
  String[] words = split (text, " "); 
  if (words.length<=1) {
    words = split (text, "_");
  }
  //for (int i=0; i < words.length; i++) {if (words[i].length()>=max_length) {max_length_b = false;}}
  //if (!max_length_b) {
  //  for (int j=0; j < words.length; j++) {
  //    String[] words2 = split (text, " ");
  //  }
  //}
  return words;
}

// split a string into lines of certain length, returning an array of strings
String[] split_string_into_lines (String s, int line_size) {
  // println("\ns = " + s + " and line size = " + line_size); 
  // extract all the original lines, according to RETURNs
  String[] words = split_text (s, line_size);  // for (int k=0; k < words.length; k++) {if (words[k].length() > line_size) {String[] s_={" "}; return s_;}}
  String original_lines[] = split(s, "\n");
  String actual_lines1[] = new String[s.length()]; // print("\n actual_lines1.length = " + actual_lines1.length);
  // count actual lines: for each line, split the string according to BLANKs or UNDERSCOREs, then split into words, count characters
  int line_count = 0; 
  for (int i = 0; i < original_lines.length; i++) {
    // words = split (original_lines[i], " "); 
    words = split_text (original_lines[i], line_size); 
    int j = 0; 
    // print("\n number of words = " + words.length); for (int k = 0; k < words.length; k++) {print(" " + words[k]);} print("\n");
    while (j < words.length) { 
      int total_chars = 0; 
      String s_aux = "";
      while (j < words.length && total_chars + words[j].length()+1 < line_size) { // while there are words and do not exceed line size
        // println(j + " words[j] = " + words[j] + ", length " + words[j].length());
        total_chars = total_chars + words[j].length() + 1;  
        s_aux = s_aux + " " + words[j]; 
        j++; 
        s_aux = s_aux + " ";
      } // while char under single line size
      // print("\n line_count = " + line_count + " and j = " + j);
      if (s_aux.equals("")) {
        s_aux = " ..."; 
        j = words.length;
      }
      actual_lines1[line_count] = s_aux; 
      line_count++;
    } // END while all the words
  } // END for each line
  // fill actual lines to be returned
  String[] actual_lines = new String[line_count]; 
  for (int k = 0; k < line_count; k++) {
    actual_lines[k] = actual_lines1[k];
  } // END for each line
  return actual_lines;
}

// given some box, with x,y corner + width + height and some text and font size write the text in the box, if capable enough
void write_text_in_box(String txt, PFont tfont, int font_size, int x_corner, int y_corner, int x_width, int y_height) {
  String lines[] = split_string_into_lines (txt, x_width); 
  int[] box_w_h = box_size(lines, font_size);
  // println("write_text_in_box: lines.length = " + lines.length + "; box_w_h[0] = " + box_w_h[0] + "; x_width = " + x_width + "; box_w_h[1] = " + box_w_h[1] + "; y_height = " + y_height);
  if (box_w_h[0] <= x_width && box_w_h[1] <= y_height) {
    for (int i=0; i<lines.length; i++) {
      textAlign(LEFT); 
      textFont(tfont);
      text(lines[i], x_corner, y_corner+(i+1)*font_size);
    }
  }
}


// given some box, with x,y corner + width + height and some text and font size write the text in the box, if capable enough
void write_text_dots_in_box(String txt, String font_type, String x_align, String y_align, int x_corner, int y_corner, int x_width, int y_height) {
  if (x_align.equals("LEFT") && y_align.equals("CENTER")) {
    textAlign(LEFT, CENTER); 
    x_corner = x_corner - x_width/2;
  } else 
  if (x_align.equals("LEFT") && y_align.equals("TOP")) {
    textAlign(LEFT, TOP); 
    x_corner = x_corner - x_width/2; 
    y_corner = y_corner - y_height/2;
  } else 
  if (x_align.equals("CENTER") && y_align.equals("CENTER")) {
    textAlign(CENTER, CENTER);
  }  
  // String[] words = split_text_into_words (txt);
  int font_size = 12; //determine_font_size (words, default_font_aspect_ratio, x_width, y_height);
  String lines[] = split_string_into_lines (txt, x_width); 
  // println("number of lines = " + lines.length);
  if (font_size>0) {
    PFont tfont = createFont(font_type, font_size);
    textFont(tfont);
    for (int i=0; i<lines.length; i++) {        
      // text(lines[i],x_corner,y_corner+(i+1)*font_size);
      text(lines[i], x_corner, y_corner);
    }
  } else {
    PFont tfont = createFont(font_type, default_font_size);
    textFont(tfont);
    text("...", x_corner, y_corner+default_font_size);
  }
}

// gives some text lines, computes the size of the box that contains it
int[] box_size(String lines[], int font_size) {
  int[] total_w_h = {0, 0}; 
  int curWidth = 0; 
  for (int i=0; i<lines.length; i++) {
    total_w_h[1] = total_w_h[1] + font_size;
    curWidth = (int) textWidth(lines[i]);
    if (int(curWidth) > int(total_w_h[0])) {
      total_w_h[0] = curWidth;
    }
  }
  total_w_h[1] = total_w_h[1] + font_size;
  return total_w_h;
}

// split a string into words (" ", "_")
String[] split_text_into_words (String text) {
  String[] words = split (text, " "); 
  if (words.length<=1) {
    words = split (text, "_");
  }
  return words;
}

int longest_word (String[] words) {
  int max=0;
  for (int i=0; i<words.length; i++) {
    if (max < words[i].length()) {
      max = words[i].length();
    }
  }
  return max;
}

// combine words into lines of certain length, returning an array of strings
String[] combine_words_into_lines (String[] words, int line_size_in_characters) {
  // println("words: " + "; line_size_in_characters = " + line_size_in_characters); 
  // for (int aux=0; aux<words.length; aux++) {print(words[aux] + " ");};
  // initialize a temporary array of lines that is as large as the number of words
  String[] actual_lines1 = new String[words.length+1]; 
  for (int i=0; i<words.length; i++) {
    actual_lines1[i]="";
  }
  // count actual lines: for each line, assemble words, count characters
  int line_count = 0;  
  for (int i = 0; i < words.length; i++) { // for each word    
    if (actual_lines1[line_count].length() + words[i].length() + 1 <= line_size_in_characters) { // while there are words and do not exceed line size
      actual_lines1[line_count] = actual_lines1[line_count] + words[i] + " "; 
      //println(actual_lines1[line_count]);
    } else { 
      line_count = line_count+1; 
      actual_lines1[line_count] = words[i] + " "; // actual_lines1[line_count] + words[i] + " ";
    }
  } // END for each word
  // fill actual lines to be returned
  String[] actual_lines = new String[line_count+1]; 
  for (int k = 0; k < line_count+1; k++) {
    //println(actual_lines1[k]);
    actual_lines[k] = actual_lines1[k];
  } // END for each line
  return actual_lines;
}

// given some box, with x,y corner + width + height and some text and font size write the text in the box, if capable enough
void flex_write_lines_in_box(String text, String font_type, String x_align, String y_align, float x_center, float y_center, float x_width, float y_height) {
  // println(" = flex_write_lines_in_box = text: " + text + "; x_width: " + x_width);
  String[] words = split_text_into_words (text);
  //println(" = flex_write_lines_in_box = words[0]: " + words[0]);
  int font_size = determine_font_size (words, default_font_aspect_ratio, x_width, y_height);
  //println(" = flex_write_lines_in_box = font_size: " + font_size);
  int line_size = int (x_width / (font_size * default_font_aspect_ratio)); // in terms of characters 
  // println(" = flex_write_lines_in_box = line_size: " + line_size);
  String lines[] = combine_words_into_lines (words, line_size); 
  // println("number of lines = " + lines.length);
  float x = x_center; 
  float y = y_center; 
  if (x_align.equals("LEFT") && y_align.equals("CENTER")) {
    textAlign(LEFT, CENTER); 
    x = x_center - x_width/2; 
    y = y_center;
  } else // - (lines.length*font_size)/2;} else 
  if (x_align.equals("LEFT") && y_align.equals("TOP")) {
    textAlign(LEFT, TOP); 
    x = x_center - x_width/2; 
    y = y_center - y_height/2;
  } else 
  if (x_align.equals("CENTER") && y_align.equals("CENTER")) {
    textAlign(CENTER, CENTER); 
    x = x_center; 
    y = y_center;
  } // - (lines.length*font_size)/2;}  
  if (font_size>0) {
    PFont tfont = createFont(font_type, font_size); textFont(tfont);
    for (int i=0; i<lines.length; i++) {        
      text(lines[i], x, y+i*font_size);
      // text(lines[i],x_corner,y_corner);
    }
  } else {
    PFont tfont = createFont(font_type, default_font_size); textFont(tfont);
    text("...", x, y);
  }
}

// given some text and font size, write the text in the box
void write_lines_in_fixed_fontsize(String text, String font_type, int font_size, String x_align, String y_align, int x_center, int y_center) {
  // println(" = write_lines_in_fixed_fontsize = text: " + text + "; font_size: " + font_size);
  int x = x_center; 
  int y = y_center; 
  String[] words = split_text_into_words (text);
  float[] box_size = determine_box_size(words, default_font_aspect_ratio, font_size);
  String lines[] = combine_words_into_lines (words, (int)(box_size[0]/(font_size*default_font_aspect_ratio))); 
  // println("number of lines = " + lines.length);
  if (x_align.equals("LEFT") && y_align.equals("CENTER")) {
    textAlign(LEFT, CENTER); 
    x = x_center - (int) box_size[0]/2; 
    rectMode(CORNER);
  } else 
  if (x_align.equals("LEFT") && y_align.equals("TOP")) {
    textAlign(LEFT, TOP); 
    x = x_center - (int) box_size[0]/2; 
    y = y_center - (int) box_size[1]/2; 
    rectMode(CORNER);
  } else 
  if (x_align.equals("CENTER") && y_align.equals("CENTER")) {
    textAlign(CENTER, CENTER); 
    rectMode(CENTER);
  }  
  fill(0, 0, 100, 10); 
  noStroke(); // rectMode(CORNER);  
  rect(x, y, box_size[0], box_size[1]); 
  //println(" = write_lines_in_fixed_fontsize = box_size[0]: " + box_size[0] + "; box_size[1]: " + box_size[1]);
  fill(0); 
  if (font_size>0) {
    PFont tfont = createFont(font_type, font_size); 
    textFont(tfont);
    for (int i=0; i<lines.length; i++) {        
      text(lines[i], x, y+i*font_size);
      // text(lines[i],x_corner,y_corner);
    }
  } else {
    PFont tfont = createFont(font_type, default_font_size); 
    textFont(tfont);
    text("...", x, y);
  }
}


// given some box, with x,y corner + width + height and some text and font size write the text in the box, if capable enough
void flex_write_text_in_box(String txt, String font_type, String x_align, String y_align, int x_corner, int y_corner, int x_width, int y_height) {
  if (x_align.equals("LEFT") && y_align.equals("CENTER")) {
    textAlign(LEFT, CENTER); 
    x_corner = x_corner - x_width/2;
  } else 
  if (x_align.equals("LEFT") && y_align.equals("TOP")) {
    textAlign(LEFT, TOP); 
    x_corner = x_corner - x_width/2; 
    y_corner = y_corner - y_height/2;
  } else 
  if (x_align.equals("CENTER") && y_align.equals("CENTER")) {
    textAlign(CENTER, CENTER);
  }  
  String[] words = split_text_into_words (txt);
  int font_size = determine_font_size (words, default_font_aspect_ratio, x_width, y_height);
  String lines[] = split_string_into_lines (txt, x_width); 
  // println("number of lines = " + lines.length);
  if (font_size>0) {
    PFont tfont = createFont(font_type, font_size);
    textFont(tfont);
    for (int i=0; i<lines.length; i++) {        
      // text(lines[i],x_corner,y_corner+(i+1)*font_size);
      text(lines[i], x_corner, y_corner);
    }
  } else {
    PFont tfont = createFont(font_type, default_font_size);
    textFont(tfont);
    text("...", x_corner, y_corner+default_font_size);
  }
}

int determine_font_size (String[] words, float font_aspect_ratio, float x_width, float y_height) {
  int font_size = 1; // start with font size 1
  int longest_word_length = longest_word(words); // compute the longest word length (minimum for line size)
  boolean max_size_found = false; // boolean for finding the possible max font size 
  while (!max_size_found) { // while not found the max font size
    int line_size = int (x_width / (font_size * font_aspect_ratio)); // compute the current line size in number of chars
    if (line_size > longest_word_length) { // if it is more than the longest word length
      String lines[] = combine_words_into_lines (words, line_size); // form the lines of the text 
      // println("font_size = " + font_size + "; line_size = " + line_size + "; lines[0] = " + lines[0]);
      if (lines.length * font_size <= y_height) {
        font_size=font_size+1;
      } else {
        max_size_found=true;
      }
    } else {
      max_size_found=true;
    }
  }
  if (font_size>1) {
    font_size = font_size - 1;
  } 
  //println("font_size = " + str(font_size-1));
  return font_size;
}


float[] determine_box_size(String[] words, float font_aspect_ratio, int font_size) {
  float[] box_size = {0, 0}; // set to max size
  float box_aspect_ratio = 25/9;
  int line_size_in_chars = longest_word(words) + 1; // longest word length (minimum for line size) + blank after word
  float line_size = font_size * font_aspect_ratio * line_size_in_chars; // compute the current line size in number of pixels
  boolean box_size_found = false;
  while (!box_size_found) {
    line_size_in_chars = line_size_in_chars+1;
    line_size = font_size * font_aspect_ratio * line_size_in_chars;
    // if (line_size <= width/3) { // if it is more than the longest word length 
    String lines[] = combine_words_into_lines (words, line_size_in_chars); // form the lines of the text 
    //println("line_size = " + line_size + "; lines[0] = " + lines[0] + "; longest_word_length = " + longest_word_length);
    if (lines.length * font_size <= line_size/box_aspect_ratio) { 
      box_size[0] = line_size; 
      box_size[1] = line_size/box_aspect_ratio;
      box_size_found = true;
      //println("box_size[0] = " + box_size[0] + "; box_size[1] = " + box_size[1]);
    } 
    //}
  }
  //println("font_size = " + str(font_size-1));
  return box_size;
}
