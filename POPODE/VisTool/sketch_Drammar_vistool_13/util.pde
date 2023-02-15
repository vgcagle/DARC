
void update_counters() {
  if (u_counter == 9) {
    u_counter = 0;
    d_counter = d_counter + 1;
    if (d_counter == 9) {
      d_counter = 0;
      c_counter = c_counter + 1;
      if (c_counter == 9) {
        c_counter = 0;
        m_counter = m_counter + 1;
        if (m_counter == 9) {
          m_counter = 0;
          dm_counter = dm_counter + 1;
        }
      }
    }
  }
  else {u_counter = u_counter + 1;}
}