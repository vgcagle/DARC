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
//////// Class OBJECT PROPERTY ASSERTION ////////
//////// ***************************************************************************** ////////

public class ObjectPropertyAssertion {	


  private void 	assertUnitsTimelineTotal(ResultSet unit, ResultSet pair, Hashtable<String,String> repository, BufferedWriter bw) {
  	// inserts units in TL_total
	System.out.println("\n\n@@@@ OBJ PROPERTY ASSERTION FOR UNITS IN TL_total");
  	try { 
	unit.beforeFirst();
	while (unit.next()) {
	  int currentRow = unit.getRow();
	  if (unit.getString("printUnit")!="") {
	    // System.out.println("\n  #### OBJ PROPERTY ASSERTION for Unit with printUnit " + unit.getString("printUnit") + " and idUnit " + unit.getString("idUnit"));
	  	if (drammarManager.isMUC(unit.getInt("idUnit"), unit)) {
	  	  unit.absolute(currentRow);
	  	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit);
	      String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); String nameTimeline = repository.get(keyTimeline);
	      System.out.println("  #### OBJ PROPERTY ASSERTION containsOLE + hasData FOR Unit in TL_total: " + nameUnit);
	      try { 
	      //TL_total containsFirstOLE, containsOLE, containsLastOLE OLE_TL_Unit
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#TL_total"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_TL_total_"+nameUnit+"\"/>\n\t</ObjectPropertyAssertion>\n");
		  //OLE_TL_total_unitX hasData Unit
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_TL_total_"+nameUnit+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameUnit+"\"/>\n\t</ObjectPropertyAssertion>\n");
	      } catch (IOException e) {e.printStackTrace();}
	    }
	    else { unit.absolute(currentRow); System.out.println(" !!!! Warning in ObjPropAss! Unit: " + unit.getInt("idUnit") + " is not MUC. Unit not in TL_total.");}
	  } // END if printUnit NOT NULL
	  else { unit.absolute(currentRow); System.out.println(" !!!! Warning in ObjPropAss! Unit: " + unit.getInt("idUnit") + " has no content. Unit not in TL_total.");}
	  unit.absolute(currentRow);
	} // END OF while UNITS
    // OLE_TL_total_unitX precedes OLE_TL_total_unitY
	System.out.println("\n  @@@@ OBJ PROPERTY ASSERTION precedes over OLEs");
	pair.beforeFirst();
	while (pair.next()) {
	  // ONLY CONSIDER PAIRS WITH UNITS THAT ARE MUC AND printUnit not NULL
	  if (drammarManager.isMUC(pair.getInt("precedesPair"), unit) && !drammarManager.isPrintUnitNULL(pair.getInt("precedesPair"), unit) && 
	  	  drammarManager.isMUC(pair.getInt("followsPair"), unit) && !drammarManager.isPrintUnitNULL(pair.getInt("followsPair"), unit)) {
	    String keyUnitP = drammarManager.createName(pair.getInt("precedesPair"), "Un"); String nameUnitP = repository.get(keyUnitP);
	    String nameOLE_P = "OLE_TL_total_" + nameUnitP;
	    String keyUnitF = drammarManager.createName(pair.getInt("followsPair"), "Un"); String nameUnitF = repository.get(keyUnitF); 
	    String nameOLE_F = "OLE_TL_total_" + nameUnitF;
        try { 
	    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#precedes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameOLE_P + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");		
	    } catch (IOException e) {e.printStackTrace();}
	  }
	}
	System.out.println("\n@@@@ OBJ PROPERTY ASSERTION firstOLE and lastOLE, if possible");
	// FIND FIRST AND LAST OLE IN TL_TOTAL
  	int firstOLETotal = -1; int lastOLETotal = -1; int countFirstOLETotal = 0; int countLastOLETotal = 0;
    unit.beforeFirst(); // loop over units
	while (unit.next()) {  // for each unit, check if it never precedes (so, it is last) or if it never follows (so, it is first)
	  int currentRow = unit.getRow(); // unit.absolute(currentRow);
	  if (drammarManager.isMUC(unit.getInt("idUnit"), unit) && !drammarManager.isPrintUnitNULL(unit.getInt("idUnit"), unit)) {
	  // if (unit.getString("printUnit")!="") { // if not null
	    pair.beforeFirst(); boolean foundPrecedes = false; boolean foundFollows = false; // loop over pairs
	    while (pair.next()) { // for each pair
	      // ONLY CONSIDER PAIRS WITH UNITS THAT ARE MUC AND printUnit not NULL
	      // if (drammarManager.isMUC(pair.getInt("precedesPair"), unit) && drammarManager.isPrintUnitNULL(pair.getInt("precedesPair"), unit)) {
	  	  unit.absolute(currentRow); 
	      if (unit.getInt("idUnit") == pair.getInt("precedesPair")) {foundPrecedes = true; }
	      // } 
	      // if (drammarManager.isMUC(pair.getInt("followsPair"), unit) && drammarManager.isPrintUnitNULL(pair.getInt("followsPair"), unit)) {
	  	  //   unit.absolute(currentRow); 
	      if (unit.getInt("idUnit") == pair.getInt("followsPair")) {foundFollows = true;}
	      // }
	    } // END WHILE pairs
	    // SET FIRST AND LAST, IF THIS IS THE CASE
        if (foundPrecedes&&!foundFollows) { firstOLETotal = unit.getInt("idUnit"); countFirstOLETotal=countFirstOLETotal+1;};
        if (!foundPrecedes&&foundFollows) { lastOLETotal = unit.getInt("idUnit"); countLastOLETotal=countLastOLETotal+1;};
      } // ENF IF unit not null and MUC
    } // END WHILE UNITS (maybe found FIRST and LAST OLE in TL_total)
	System.out.println("  #### Number of firstOLE: " + countFirstOLETotal);
	//TL_total containsFirstOLE
    if (countFirstOLETotal==1) {
	  String keyUnit = drammarManager.createName(firstOLETotal, "Un"); String nameUnit = repository.get(keyUnit);
	  String nameOLE = "OLE_TL_total_" + nameUnit;
	  try {
      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#TL_total"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
	  } catch (IOException e) {e.printStackTrace();}
    } else {System.out.println("\n !!!! ERROR: TL_total has no FIRST OLE!!!");}
	//TL_total containsLastOLE
	System.out.println("  #### Number of lastOLE: " + countLastOLETotal);
    if (countLastOLETotal==1) {
	  String keyUnit = drammarManager.createName(lastOLETotal, "Un"); String nameUnit = repository.get(keyUnit);
	  String nameOLE = "OLE_TL_total_" + nameUnit;
	  try {
      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#TL_total"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
	  } catch (IOException e) {e.printStackTrace();} 
     } else {System.out.println("\n !!!! ERROR: TL_total has no LAST OLE!!!");}
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  private void 	assertUnitsTimelineMUC(ResultSet unit, ResultSet pair, Hashtable<String,String> repository, BufferedWriter bw) {
	System.out.println("\n\n@@@@ OBJ PROPERTY ASSERTIONS (OLE, firstOLE, lastOLE) FOR TIMELINES FROM MUC UNITS");
  	try { 
	unit.beforeFirst();
	while (unit.next()) {
      int currentRow = unit.getRow();	
	  if (unit.getString("printUnit")!="") {
	  	// For MUC units, OLE_TL_Unit hasData Unit + TL_Unit containsOLE OLE_TL_Unit
	    if (drammarManager.isMUC(unit.getInt("idUnit"), unit)) {
	      unit.absolute(currentRow);
	      String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit);
	      String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); String nameTimeline = repository.get(keyTimeline);
	      String keyScene = drammarManager.createName(unit.getInt("TimelineUnit"), "Sc"); String nameScene = repository.get(keyScene);
	      System.out.println("  @@@@ OBJ PROPERTY ASSERTIONS FOR MUC TIMELINE " + nameTimeline);
	      String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); String nameOLE = repository.get(keyOLE);
	      // System.out.println("\n @@@@ ObjectPropertyAssertion of MUC unit " + unit.getInt("idUnit") + " " + nameUnit + "; timeline " + nameTimeline + ", ole " + nameOLE); 
          try { 
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnit + "\"/>\n\t</ObjectPropertyAssertion>\n");
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#spans"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameScene + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</ObjectPropertyAssertion>\n");
	      } catch (IOException e) {e.printStackTrace();}
	    }
	  }
	  unit.absolute(currentRow);	
	} // END While units
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  private void 	assertUnitsTimelineNONMUC(ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository, BufferedWriter bw) {
   	Hashtable<String, Boolean> timelines = new Hashtable<String, Boolean>(); // repository for all identifiers
	System.out.println("\n\n@@@@ OBJ PROPERTY ASSERTIONS (OLEs, firstOLE, lastOLE, precedes) FOR TIMELINES FROM NON-MUC UNITS");
 	try { 
	unit.beforeFirst();
	while (unit.next()) {
	  int currentRow = unit.getRow();	
	  if (unit.getString("printUnit")!="") {
	  	if (!drammarManager.isMUC(unit.getInt("idUnit"), unit)) { 
	  	  unit.absolute(currentRow);
 	      int firstOLE = -1; int lastOLE = -1; int countFirstOLE = 0; int countLastOLE = 0;
 	  	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit);
          String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); String nameTimeline = repository.get(keyTimeline);
	      String keyScene = drammarManager.createName(unit.getInt("TimelineUnit"), "Sc"); String nameScene = repository.get(keyScene);
	      System.out.println("\n @@@@ OBJ PROPERTY ASSERTION FOR TIMELINE " + nameTimeline + " FROM Unit NON MUC: " + nameUnit);
	      // find the MUC (or reference) units, create a (sub-)timeline  
          int init = drammarManager.getInitMUC(unit.getInt("idUnit"), unit2reference, unit); // get the INIT and the END MUC
          // System.out.println("\n @@@@ Init MUC of " + nameUnit + " is " + init); 
          if (init != -1) {
            int end = drammarManager.getEndMUC(unit.getInt("idUnit"), unit2reference, unit);
            // System.out.println("\n @@@@ End MUC of " + nameUnit + " is " + end);
            if (end != -1) { // if this unit has both init and end MUC units
          	  if (!timelines.containsKey(String.valueOf(init)+String.valueOf(end))) {
          	    try { // assert containsFirstOLE, containsOLE, hasData
		          bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#spans"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameScene +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</ObjectPropertyAssertion>\n");
	            } catch (IOException e) {e.printStackTrace();}
           	    timelines.put(String.valueOf(init)+String.valueOf(end),true);
                unit.absolute(drammarManager.getUnitRow (unit, init)); // position cursor at the init row
	            keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); nameUnit = repository.get(keyUnit); 
	            String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); String nameOLE = repository.get(keyOLE);
 	            System.out.println("  #### ObjectPropertyAssertion containsOLE + hasData " + nameOLE); 
                try { // assert containsFirstOLE, containsOLE, hasData
		        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
		        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
		        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnit + "\"/>\n\t</ObjectPropertyAssertion>\n");
	            } catch (IOException e) {e.printStackTrace();}
                String keyOLE_F = keyOLE; String nameOLE_F = nameOLE;
	            pair.beforeFirst(); int currentUnit = init; // loop to declare all the OLE's
	            while (pair.next() && currentUnit != end) { // for each adjacent pair of units
		          if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		      	    int followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
		      	    if (followsRow!=-1) {
		      	      String keyUnitF = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnitF = repository.get(keyUnitF);
	                  keyOLE_F = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnitF); nameOLE_F = repository.get(keyOLE_F);
 	                  System.out.println("  #### ObjectPropertyAssertion containsOLE + hasData + precedes " + nameOLE_F); 
                      try { // declare the timeline and the first OLE
		              bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");
		              bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnitF + "\"/>\n\t</ObjectPropertyAssertion>\n");
		              bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#precedes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameOLE + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");		
	                  } catch (IOException e) {e.printStackTrace();}
			          currentUnit = pair.getInt("followsPair"); // update current with followsPair
			          pair.beforeFirst(); // start from beginning again to find next
			          keyUnit = keyUnitF; nameUnit = nameUnitF; keyOLE = keyOLE_F; nameOLE = nameOLE_F;
			        } // END created OLE
		            else { System.out.println("ERROR: Unit " + nameUnit + " has no continuation, but should have!!!");}
		          } // END if precedes found
	            } // END for each pair - exit for NO MORE NEXT OR map end REACHED
	            if (currentUnit == end) {
	              try { // declare the timeline and the first OLE
	              bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");
	              } catch (IOException e) {e.printStackTrace();}
	            } else {System.out.println("ERROR: Unit " + nameUnit + " has no continuation, but should have!!!");}
              } 
	        } else { System.out.println("\n @@@@ ERROR: unit: " + nameUnit + " has no end MUC!!!");}
          } else { System.out.println("\n @@@@ ERROR: unit: " + nameUnit + " has no initial MUC!!!");}
        }
      } // END ELSE == annotation UNIT
      unit.absolute(currentRow);
    } // END WHILE UNITS
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertUnitsTimelines(ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository, BufferedWriter bw) {
    assertUnitsTimelineTotal(unit, pair, repository, bw);
    assertUnitsTimelineMUC(unit, pair, repository, bw);
    assertUnitsTimelineNONMUC(unit, pair, unit2reference, repository, bw);
  }

  public void assertActions(ResultSet action, ResultSet unit, Hashtable<String,String> repository, BufferedWriter bw) {
    // SM_Unit_Action hasData Action
  	try { 
  	action.beforeFirst();
	while (action.next()) {	
	//System.out.println("\n @@@@ \n @@@@ Action @@@@ \n --> $" + action.getString("nameAction") + "$");
	  if (action.getString("nameAction")!="") {
 	    // System.out.println("\n @@@@ \n @@@@ Action2 @@@@ \n --> $" + action.getString("nameAction") + "$");
	    String nameAction = repository.get(String.valueOf(action.getInt("idAction"))+"Ac");
	    try { 
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Action"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameAction + "\"/>\n\t</ClassAssertion>\n");
		unit.beforeFirst();
		while (unit.next()) {
		  if (unit.getInt("idUnit")==action.getInt("Unit_idUnit")) {
			String nameUnit = repository.get(String.valueOf(unit.getInt("idUnit"))+"Un");
			//String nameUnit = fixName(unit.getString("nameUnit"));
			bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#SM_"+nameUnit+"_"+nameAction+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameAction+"\"/>\n\t</ObjectPropertyAssertion>\n");
			bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameUnit+"\"/>\n\t\t<NamedIndividual IRI=\"#SM_"+nameUnit+"_"+nameAction+"\"/>\n\t</ObjectPropertyAssertion>\n");
		  }
		}
		} catch (IOException e) {e.printStackTrace();}
	  }
	} // END While 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  private void assert_isMotivationFor (int row, ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, Hashtable<String,String> repository, BufferedWriter bw) {
    String namePlan = "NULL"; String nameTimeline = "NULL"; int unitRow=-1;
    // Plan isMotivationFor TL_mapInitXXX_mapEndYYY_tot
  	try { 
  	plan.absolute(row);
	String keyPlan = "NULL";
	if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
	else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
	namePlan = repository.get(keyPlan); // store in the has table 
    // String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "Pl"); namePlan = repository.get(keyPlan);   
	// when Plan is Directly Executable Plan, timeline motivated is from a unit (MUC or not) 
	unitRow = drammarManager.getUnitRow(unit, plan.getInt("Timeline_idTimeline")); 
	if (unitRow!=-1) {
	  unit.absolute(unitRow);
	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
	  String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); // keyUnit + "Tl"; 
	  nameTimeline = repository.get(keyTimeline); 
	  if (plan.getString("typePlan").equals("Base")) {
	    // take timeline (column Timeline_idTimeline): this is a unit primary key 
        System.out.println("  #### ObjectPropertyAssertion isMotivationFor for DE Plan: " + namePlan + " is timeline " + nameTimeline);
      } else if (plan.getString("typePlan").equals("Rec")) {
	  // when Plan is Abstract Plan, timeline motivated is directly retrievable  from timeline
	  // String returnTimeline[] = drammarManager.retrieveTimelineForAbstractPlan (row, plan, unit, unit2reference, pair, repository);
	  // String keyTimeline = returnTimeline[0]; 
	  // nameTimeline = returnTimeline[1]; 
	  // unitRow = drammarManager.getUnitRow (unit, plan.getInt("Timeline_idTimeline")); 
	  // if (unitRow!=-1) {
	    // unit.absolute(unitRow);
	    // String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
	    // String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); // keyUnit + "Tl"; 
        // nameTimeline = repository.get(keyTimeline);
        System.out.println("  #### ObjectPropertyAssertion isMotivationFor for Abs Plan: " + namePlan + " is timeline " + nameTimeline);	  	
	  }
    } 
    if (nameTimeline==null || nameTimeline.equals("NULL")) {System.out.println("  #### ERROR OPA isMotivationFor for Abs Plan: " + namePlan + " is not possible because no NON MUC unit associated! ");}
    else if (!namePlan.equals("NULL") && !nameTimeline.equals("NULL")) {
 	  try { 
	  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"#isMotivationFor"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameTimeline+"\"/>\n\t</ObjectPropertyAssertion>\n"); 
      } catch (IOException e) {e.printStackTrace(); }
    }
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}					
  }

  private static void assertTimelineFromPlan (int row, ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, Hashtable<String,String> repository, BufferedWriter bw) {    
    // assert multiple-unit timelines from rec plans
  	try { 
  	plan.absolute(row);
    // retrieve the declared timeline for this abstract plan
	String keyPlan = "NULL";
	if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
	else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
	String namePlan = repository.get(keyPlan); // store in the has table 
    // String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "Pl"); String namePlan = repository.get(keyPlan);
	if (plan.getString("typePlan").equals("Rec")) { 
	  String returnTimeline[] = drammarManager.retrieveTimelineForAbstractPlan (row, plan, unit, unit2reference, pair, repository);
      if (returnTimeline[0]!="null") {
      	String keyTimeline = returnTimeline[0];  String nameTimeline = returnTimeline[1];
        int mappingInit = plan.getInt("mappingInit"); int mappingEnd = plan.getInt("mappingEnd");
	    int mappingInit_unit_row = drammarManager.getUnitRow (unit, mappingInit); unit.absolute(mappingInit_unit_row);
        String printInitUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
	    int mappingEnd_unit_row = drammarManager.getUnitRow (unit, mappingEnd); unit.absolute(mappingEnd_unit_row);
        String printEndUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
        int currentUnit = mappingInit;
	    System.out.println(" #### Object property assertion of Timeline " + nameTimeline + " from Plan " + namePlan);
	    // take the row of the map init unit and position the unit cursor
      	unit.absolute(mappingInit_unit_row);
        String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit); 
	    String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	    String nameOLE = repository.get(keyOLE);
	    System.out.println(" #### Assert containsFirstOLE: " + nameOLE);
        try { // assert containsFirstOLE, containsOLE, hasData
		bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
		bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ObjectPropertyAssertion>\n");
		bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnit + "\"/>\n\t</ObjectPropertyAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
        // loop over the timeline
        String keyOLE_F = keyOLE; String nameOLE_F = nameOLE;
        pair.beforeFirst(); currentUnit = mappingInit; int followsRow = 0; // loop to declare all the OLE's
	    while (pair.next() && currentUnit != mappingEnd) { // for each adjacent pair of units
		  if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		    followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
		    if (followsRow!=-1) {
		      String keyUnitF = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnitF = repository.get(keyUnitF);
	          keyOLE_F = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnitF); nameOLE_F = repository.get(keyOLE_F);
	          System.out.println(" #### object property assertion for OLE: " + nameOLE);
              try { // declare the timeline and the first OLE
		      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");
		      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnitF + "\"/>\n\t</ObjectPropertyAssertion>\n");
		      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#precedes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameOLE + "\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");		
	          } catch (IOException e) {e.printStackTrace();}
			  currentUnit = pair.getInt("followsPair"); // update current with followsPair
			  pair.beforeFirst(); // start from beginning again to find next
			  keyUnit = keyUnitF; nameUnit = nameUnitF; 
			} // END IF followsRow 
		    else { System.out.println("ERROR: Unit " + nameUnit + " has no continuation, but should have!!!");}
		  } // END if precedes found
	    } // END for each pair - exit for NO MORE NEXT OR map end REACHED
        try { // Assert the lastOLE
		bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline +"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE_F + "\"/>\n\t</ObjectPropertyAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
      } // Timeline exists 
	  else { System.out.println("!!!! ERROR! No Timeline!");} // 
	} // END if Abstract Plan
	else { System.out.println("!!!! This plan is not abstract!");} // 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }




  public void assertPlans (ResultSet plan, ResultSet unit, ResultSet pair, ResultSet unit2reference, ResultSet subplanof, Hashtable<String,String> repository, BufferedWriter bw) {
  	// obj prop assertions for plans
	System.out.println("\n\n@@@@ OBJ PROPERTY ASSERTION FOR PLANS");
   	try { 
  	plan.beforeFirst(); // loop over plans
	while (plan.next()) { // for each plan 
	  int currentRow = plan.getRow();
	  String keyPlan = "NULL";
	  if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
	  else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
	  String namePlan = repository.get(keyPlan); // store in the has table 
	  // String namePlan = repository.get(String.valueOf(plan.getInt("idPlan"))+"Pl"); // get name from hashtable 
	  System.out.println("\n@@@@ OBJ PROPERTY ASSERTIONS FOR Plan: " + namePlan);
	  // agent
	  if (plan.getInt("Agent_idAgent") > 0) {
	  	String nameAgent  = repository.get(drammarManager.createName(plan.getInt("Agent_idAgent"),"Ag")); // get name from hashtable
	    try { // Plan isIntendedBy Agent
	    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#isIntendedBy"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameAgent+"\"/>\n\t</ObjectPropertyAssertion>\n"); 
        } catch (IOException e) {e.printStackTrace(); }	
	    System.out.println(" #### ObjPropAss of Agent of Plan: " + nameAgent);
	  } else {System.out.println(" !!!! ERROR: plan: " + namePlan + " has no agent!!!");}
	  // goal
	  if (plan.getInt("Goal_idGoal") > 0) {
	  	String nameGoal  = repository.get(drammarManager.createName(plan.getInt("Goal_idGoal"),"Go")); // get name from hashtable
	    try { // Plan achieves Goal
	    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#achieves"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"\"/>\n\t</ObjectPropertyAssertion>\n");
        } catch (IOException e) {e.printStackTrace(); }	
        System.out.println(" #### ObjPropAss of Goal of Plan: " + nameGoal);
	  } else {System.out.println(" !!!! WARNING: plan: " + namePlan + " has no goal!!!");}
	  // Plan isMotivationFor some TIMELINE 
      assert_isMotivationFor (plan.getRow(), plan, unit, unit2reference, pair, repository, bw);
      // subplans
      if (plan.getString("typePlan").equals("Rec")) {
        // assertTimelineFromPlan (plan.getRow(), plan, unit, unit2reference, pair, repository, bw);     
        System.out.println(" #### ObjPropAss of subplans ");
  	    //OLE_PlanFather_PlanChild precedes OLE_PlanFather_PlanChild	
	    // Vector<Integer> fatherspre = new Vector <Integer>();
	    // *** get all children of the current plan and print
	    // collect all children and their order from the DB
	    Vector<String> children = new Vector <String>();
	    Vector<Integer> children_order = new Vector <Integer> ();
	    // Object property assertions OLE_PlanFather_PlanChild
	    subplanof.beforeFirst(); // loop on all subplan relations

	    while (subplanof.next()) {
	      int father = subplanof.getInt("idFatherPlan"); int child = subplanof.getInt("idChildPlan");
	      // public static int getPlanRow (ResultSet plan, int idPlan)
	      plan.absolute(drammarManager.getPlanRow (plan, father));
	      String keyFatherPlan = "NULL";
	      if (plan.getString("typePlan").equals("Rec")) { keyFatherPlan = drammarManager.createName(father, "AbsPl"); }
	      else { keyFatherPlan = drammarManager.createName(father, "DePl"); }
	      String fathername = repository.get(keyFatherPlan); // store in the has table 
		  // String fathername = repository.get(String.valueOf(plan.getInt("idPlan"))+"Pl");
	      plan.absolute(drammarManager.getPlanRow (plan, child));
	      String keyChildPlan = "NULL";
	      if (plan.getString("typePlan").equals("Rec")) { keyChildPlan = drammarManager.createName(child, "AbsPl"); }
	      else { keyChildPlan = drammarManager.createName(child, "DePl"); }
	      String childname = repository.get(keyChildPlan); // store in the has table 
	      if (namePlan.equals(fathername)) {
		    children.add(childname); System.out.println("    #### subplan " + childname);
			children_order.add(subplanof.getInt("order"));
	      }
		} // END WHILE subplans 
 	    System.out.println("   #### in number ... " + children.size());
		if (testContinuityOverSubplans(namePlan, subplanof, children, children_order)) {
 	      System.out.println("\n@@@@ Object property assertion of all subplan OLEs ");
          writeSubplans(namePlan, subplanof, children, children_order, bw);
        /*
		  int curr = -1; int order = 1; 
		  for (int i=0; i<children.size(); i++) {if (children_order.get(i)==order) {curr=i;}}
		  if (curr!= -1) {
	        try { // print firstOLE
	        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
            //OLE_PlanFather_PlanChild hasData PlanChild
	        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t\t<NamedIndividual IRI=\"#"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
            System.out.println(" #### ObjPropAss of subplan " + curr + " of order " + order + ": " + children.get(curr));
	        for (int i=0; i<children.size()-1; i++) { 
	      	  int prev = curr; curr = -1; order++;
	      	  for (int j=0; j<children_order.size(); j++) { if (children_order.get(j)==order) {curr=j;}} 
	      	  if (curr!= -1) {
	            bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
		        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#precedes"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(prev)+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");													
		        // OLE_PlanFather_PlanChild hasData PlanChild
		        bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t\t<NamedIndividual IRI=\"#"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
                System.out.println(" #### ObjPropAss of subplan " + curr + " of order " + order + ": " + children.get(curr));
	            bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
	      	  } else {System.out.println(" !!!! ERROR: abstract plan: " + namePlan + " has no subplan of order " + order + " !!!");}
	        } // END FOR
	        } catch (IOException e) {e.printStackTrace(); }
		  } else {System.out.println(" !!!! ERROR: abstract plan: " + namePlan + " has no first subplan!!!");}
		  */
		} else {System.out.println(" !!!! Warning: abstract plan: " + namePlan + " has no continuity over subplans!!!");}
	  } // END if abstract plan
	  plan.absolute(currentRow);
	} // END While 
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}					
  }

  private int nextOrder(Vector<Integer> children_order, int prevOrder) {
    int curNext = 1000;
	if (children_order.size()>0) {
	  for (int j=0; j<children_order.size(); j++) { if (children_order.get(j)<curNext && children_order.get(j)>prevOrder) {curNext=children_order.get(j);}}
    } 
    if (curNext == 1000) {curNext=-1;}
    return curNext; 
  }

  private boolean testContinuityOverSubplans(String namePlan, ResultSet subplanof, Vector<String> children, Vector<Integer> children_order) {
    boolean continuity = true; // continuity with no children is ok
    int curr = -1;  
	if (children.size()>0) {
 	  System.out.println("\n@@@@ Object property assertion of all subplan OLEs: test of continuity ... ");
	  // int order = 1; 
	  int i=0; 
	  while (i<children.size()-1 && continuity) {
	  	curr = nextOrder(children_order, curr);
	  	// for (int j=0; j<children_order.size(); j++) { if (children_order.get(j)==order) {curr=j;}}
	  	if (curr == -1) {return false;}
	    // order++; 
	    i++;
	  } // END while
	} // else {continuity = false;}// END IF children
 	System.out.println("  #### ... curr is " + curr);
    return continuity;
  }

  private int nextIndexOrder(Vector<Integer> children_order, int prevOrder) {
    int curNext = 1000; int returnIndex = -1;
	if (children_order.size()>0) {
	  for (int j=0; j<children_order.size(); j++) { if (children_order.get(j)<curNext && children_order.get(j)>prevOrder) {
	  	curNext=children_order.get(j);
	    returnIndex=j;}
	  }
    } 
    if (curNext == 1000) {returnIndex=-1;}
    return returnIndex; 
  }

  private void writeSubplans(String namePlan, ResultSet subplanof, Vector<String> children, Vector<Integer> children_order, BufferedWriter bw) {
	if (children.size()>0) {
 	  // System.out.println("\n@@@@ Object property assertion of all subplan OLEs: write ");
	  // int order = 1; int i=0; int prev=-1; int curr = -1; 
	  int i=0; int prevOrder = -1; int prev = -1; int curr = -1; 
	  try { 
	  while (i<children.size()) {
	  	curr = nextIndexOrder(children_order, prevOrder);
	  	// curr = -1; 
	  	// for (int j=0; j<children_order.size(); j++) { if (children_order.get(j)==order) {curr=j;}}
	    // if (order==1) { // print firstOLE
	    if (prevOrder==-1) { // print firstOLE
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsFirstOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
          //OLE_PlanFather_PlanChild hasData PlanChild
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t\t<NamedIndividual IRI=\"#"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
          // System.out.println(" #### ObjPropAss of subplan " + curr + " of order " + order + ": " + children.get(curr));
          System.out.println(" #### ObjPropAss of first subplan: " + curr + ", " + children.get(curr) + ", of " + children.size() + " subplans");
	      prevOrder = children_order.get(curr); prev=curr; i++; // order++; 
	    } else {
	      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#precedes"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(prev)+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");													
		  // OLE_PlanFather_PlanChild hasData PlanChild
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t\t<NamedIndividual IRI=\"#"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
          // System.out.println(" #### ObjPropAss of subplan " + curr + " of order " + order + ": " + children.get(curr));
          System.out.println(" #### ObjPropAss of subplan " + curr + ": " + children.get(curr));
	      prevOrder = children_order.get(curr); prev=curr;  // order++; 
	      i++; // order++; i++;
	  }
	  } // END while
	  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#containsLastOLE"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+namePlan+"_child"+children.get(curr)+"\"/>\n\t</ObjectPropertyAssertion>\n");
	  } catch (IOException e) {e.printStackTrace(); }
	} // else {continuity = false;}// END IF children
  }


  public void assertValues(ResultSet value, Hashtable<String,String> repository, BufferedWriter bw) {
    // VALUE isValueOf AGENT
  	try { 
  	value.beforeFirst();
	while (value.next()){
	  if (value.getString("nameValue")!="") {
		String nameValue = repository.get(String.valueOf(value.getInt("idValue"))+"Va");
	    try { 
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#describes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameValue+"_Schema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameValue+"\"/>\n\t</ObjectPropertyAssertion>\n");
          String nameAgent = repository.get(String.valueOf(value.getInt("Agent_idAgent"))+"Ag");
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#isValueOf"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameValue+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameAgent+"\"/>\n\t</ObjectPropertyAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertGoals(ResultSet goal, ResultSet plan, Hashtable<String,String> repository, BufferedWriter bw) {
  	try { 
  	goal.beforeFirst();
	while (goal.next()) {
	  if (goal.getString("nameGoal")!="") {
	    String nameGoal = repository.get(drammarManager.createName(goal.getInt("idGoal"),"Go"));
	    try { // GoalSchema describes Goal
	    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#describes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"_Schema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"\"/>\n\t</ObjectPropertyAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	    // Agent hasGoal Goal
		plan.beforeFirst();
		while(plan.next()) { // through the plan, get the agent of the goal
		  int agentgoal = plan.getInt("Agent_idAgent"); 
		  if (agentgoal > 0 && plan.getInt("Goal_idGoal")==goal.getInt("idGoal")) {
            String nameAgent = repository.get(drammarManager.createName(plan.getInt("Agent_idAgent"),"Ag"));
	        try { 
		    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasGoal"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameAgent+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"\"/>\n\t</ObjectPropertyAssertion>\n");
	        } catch (IOException e) {e.printStackTrace();}
		  }
		}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertAgents(ResultSet agent, Hashtable<String,String> repository, BufferedWriter bw) {
  	try { 
  	agent.beforeFirst();
	while (agent.next()) {
	  if (agent.getString("nameAgent")!="") {
	    String nameAgent = repository.get(String.valueOf(agent.getInt("idAgent"))+"Ag");
        // GoalSchema describes Goal
	    // try { //scrive Declaration dei goal
	    // bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#describes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"_Schema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameGoal+"\"/>\n\t</ObjectPropertyAssertion>\n");
	    // } catch (IOException e) {e.printStackTrace();}
	  } // END if agent non empty
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }
				
  public void assertStates(ResultSet state, Hashtable<String,String> repository, BufferedWriter bw) {
    // StateSchema describes State
  	try { 
  	state.beforeFirst();
	while (state.next()){	
	  if (!state.getString("nameState").isEmpty()) {
	    if ((state.getString("typeState").equals("SOA"))||(state.getString("typeState").equals("BEL"))||(state.getString("typeState").equals("VAS"))||(state.getString("typeState").equals("NEG"))){
	      String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); 
	      try { 
		    bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#describes"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"_Schema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"\"/>\n\t</ObjectPropertyAssertion>\n");
	      } catch (IOException e) {e.printStackTrace();}
	    }
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

/*
  public void assertCSSPlans(ResultSet plan, ResultSet cssplan, ResultSet state, Hashtable<String,String> repository, BufferedWriter bw) {
  	try {
	plan.beforeFirst();
	while (plan.next()) { // for each plan
	  String namePlan = repository.get(String.valueOf(plan.getInt("idPlan"))+"Pl");
	  int pre = plan.getInt("preconditionsPlan"); // get preconditions CSS - System.out.println(pre);
	  int eff = plan.getInt("effectsPlan"); // get effects CSS - System.out.println(eff);
 	  // CREO INDIVIDUI CSSPLAN preconditions + SM_CSSPLAN
	  cssplan.beforeFirst(); // for each CSS plan
	  while (cssplan.next()) {
        // PLAN hasPrecondition CSSPLAN_Pre + CSSPLAN_Pre hasMember SM_CSSPLAN_Pre + SM_CSSPLAN_Pre hasData STATE
	    // if (cssplan.getInt("SetPlan")==pre) { // if this is the right pre CSS 
	    if (cssplan.getInt("Set")==pre) { // if this is the right pre CSS 
		  int idState = cssplan.getInt("idState"); // get the state within this pre CSS
		  state.beforeFirst();
		  while (state.next()) { // for each state, if this is the non-null current state (in the pre CSS)
		    if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			  String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			  try {
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasPrecondition"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSS"+namePlan+"_"+nameState+"_Pre"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSS"+namePlan+"_"+nameState+"_Pre"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSS"+namePlan+"_"+nameState+"_Pre"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSS"+namePlan+"_"+nameState+"_Pre"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  } catch (IOException e) {e.printStackTrace();}
			}
		  }
		}
  		// PLAN hasEffect CSSPLAN_Eff + CSSPLAN_Eff hasMember SM_CSSPLAN_Eff + SM_CSSPLAN_Eff hasData STATE
		// if (cssplan.getInt("SetPlan")==eff) {
		if (cssplan.getInt("Set")==eff) {
		  int idState = cssplan.getInt("idState");
		  state.beforeFirst();
		  while (state.next()) {
		    if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			  String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			  try {
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasEffect"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+namePlan+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSS"+namePlan+"_"+nameState+"_Eff"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSS"+namePlan+"_"+nameState+"_Eff"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSS"+namePlan+"_"+nameState+"_Eff"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSS"+namePlan+"_"+nameState+"_Eff"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"\"/>\n\t</ObjectPropertyAssertion>\n");
			  } catch (IOException e) {e.printStackTrace();}
			}
		  }
		}
	  }
	} // END while plans	    0
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }
*/

  /*
  public void assertCSSTimelines(ResultSet timeline, ResultSet csstimeline, ResultSet state, Hashtable<String,String> repository, BufferedWriter bw) {
    String nameTL = "NULL TIMELINE";
  	try { 
	timeline.beforeFirst();
	while (timeline.next()) { // for each timeline
	  if (timeline.getInt("idTimeline")!=0) {nameTL = repository.get(String.valueOf(timeline.getInt("idTimeline"))+"Tl");}
	  else {nameTL = "TL_total";}
	  System.out.println("\n @@@@ DECLARATION OF Timeline " + nameTL);
	  if (nameTL!=null) {
	  int pre = timeline.getInt("preconditionsTimeline"); // get preconditions CSS - System.out.println(pre);
	  int eff = timeline.getInt("effectsTimeline"); // get effects CSS - System.out.println(eff);
	  csstimeline.beforeFirst(); // for each CSS timeline
	  while (csstimeline.next()) {
	  	// TIMELINE hasPrecondition CSSTL_Pre + CSSTL_Pre hasMember SM_CSSTL_Pre + SM_CSSTL_Pre hasData STATE
	    // if (csstimeline.getInt("SetTimeline")==pre) { // if this is the right pre CSS 
	  	if (pre > 0) {
		  try {
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasPrecondition"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameTL+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSSTL_"+nameTL+"_Pre"+"\"/>\n\t</ObjectPropertyAssertion>\n");
		  } catch (IOException e) {e.printStackTrace();}
	      if (csstimeline.getInt("Set")==pre) { // if this is the right pre CSS 
		    int idState = csstimeline.getInt("idState"); // get the state within this pre CSS
		    state.beforeFirst();
		    while (state.next()) { // for each state, if this is the non-null current state (in the pre CSS)
		      if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			    String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			    if (nameState!=null) {
			      try {
			      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSSTL_"+nameTL+"_Pre"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSSTL_"+nameTL+"_"+nameState+"_Pre"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSSTL_"+nameTL+"_"+nameState+"_Pre"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"\"/>\n\t</ObjectPropertyAssertion>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    }
			  }
			}
		  }
		} // END pre > 0
  		// TIMELINE hasEffect CSSTL_Eff + CSSTL_Eff hasMember SM_CSSTL_Eff + SM_CSSTL_Eff hasData STATE
	  	if (eff > 0) {
		  try {
		  bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasEffect"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameTL+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSSTL_"+nameTL+"_Eff"+"\"/>\n\t</ObjectPropertyAssertion>\n");
		  } catch (IOException e) {e.printStackTrace();}
		  // if (csstimeline.getInt("SetTimeline")==eff){
		  if (csstimeline.getInt("Set")==eff){
		    int idState = csstimeline.getInt("idState");
		    state.beforeFirst();
		    while (state.next()) {
		      if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			    String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			    if (nameState!=null) {
			      try {
			      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"CSSTL_"+nameTL+"_Eff"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSSTL_"+nameTL+"_"+nameState+"_Eff"+"\"/>\n\t</ObjectPropertyAssertion>\n");
			      bw.write("\t<ObjectPropertyAssertion>\n\t\t<ObjectProperty IRI=\"/drammar/2012/4/drammar.owl#hasData"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+"SM_CSSTL_"+nameTL+"_"+nameState+"_Eff"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+nameState+"\"/>\n\t</ObjectPropertyAssertion>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    }
			  } 
			}
		  }
		} // END eff > 0
	  } // END while css timelines
	  } // END if NOT Null
	} // END while timelines 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }	    
  */

} // end class 