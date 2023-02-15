// ALGEBRA OF TEMPORAL INTERVALS

boolean contained (float l1, float r1, float l2, float r2) {
  if (l1 >= l2 && r1 <= r2) {return true;}
  return false; 
}

boolean contains (float l1, float r1, float l2, float r2) {
  if (l1 <= l2 && r1 >= r2) {return true;}
  return false; 
}

boolean proper_contains (float l1, float r1, float l2, float r2) {
  if (l1 < l2 && r1 >= r2) {return true;}
  if (l1 <= l2 && r1 > r2) {return true;}
  return false; 
}

boolean overlap (float l1, float r1, float l2, float r2) {
  if (l1 >= l2 && r1 >= r2 && l1 < r2) {return true;}
  if (l1 <= l2 && r1 <= r2 && l2 < r1) {return true;}
  return false; 
}