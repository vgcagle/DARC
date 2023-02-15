    // <DataPropertyAssertion>
    //     <DataProperty IRI="#accomplished"/>
    //     <NamedIndividual IRI="#_allontanare_Hamlet_e_commissionare_la_sua_esecuzione476Pl"/>
    //     <Literal datatypeIRI="http://www.w3.org/2001/XMLSchema#boolean">true</Literal>
    // </DataPropertyAssertion>

import java.io.*;
import java.sql.*;
import java.util.*;
// import com.mysql.*;
import com.mysql.jdbc.Driver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

//////// ***************************************************************************** ////////
//////// Class DATA PROPERTY ASSERTION ////////
//////// ***************************************************************************** ////////

public class DataPropertyAssertion {	

  public void assertPlansAccomplished (ResultSet plan, Hashtable<String,String> repository, BufferedWriter bw) {
  	try { 
  	plan.beforeFirst();
	  while (plan.next()) { // for each plan 
      String keyPlan = "NULL";
      if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
      else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
      String namePlan = repository.get(keyPlan); // store in the has table 
	    // String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "Pl"); String namePlan = repository.get(keyPlan); // get name from hashtable 
	    String accomplished = "NULL";
	    if (plan.getInt("accomplished")==0) {accomplished="false";}
	    if (plan.getInt("accomplished")==1) {accomplished="true";}
	    try {
      // Plan accomplished
	    bw.write("\t<DataPropertyAssertion>\n\t\t<DataProperty IRI=\"#accomplished"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/2001/XMLSchema#boolean\">"+accomplished+"</Literal>\n\t</DataPropertyAssertion>\n"); 
      // 
      } catch (IOException e) {e.printStackTrace(); }	
	  } // END While 
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}					
  }

} // end class 