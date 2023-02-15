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
//////// Class CLASS ASSERTION ////////
//////// ***************************************************************************** ////////

public class ClassAssertion {		

  private static void assertTimelinesFromUnits (ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository, BufferedWriter bw) {    
    // assert single-unit timelines from MUC units + assert multiple-unit timelines from NON MUC units
  	Hashtable<String, Boolean> timelines = new Hashtable<String, Boolean>(); // repository for all identifiers
  	try { 
	unit.beforeFirst();
	while (unit.next()) {  // for each unit, take key and name 
	  int currentRow = unit.getRow();	
	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit);
	  String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl");
	  String keyScene = drammarManager.createName(unit.getInt("TimelineUnit"), "Sc"); 
	  // ASSERT a timeline from MUC units 
	  if (drammarManager.isMUC(unit.getInt("idUnit"), unit)) {
	    unit.absolute(currentRow);
	    String nameTimeline = repository.get(keyTimeline);
	    String nameScene = repository.get(keyScene);
	    String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); String nameOLE = repository.get(keyOLE);
	    System.out.println("\n @@@@ Class Assertion of Timeline MUC " + nameTimeline + ", OLE " + nameOLE);
        try { 
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Timeline"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</ClassAssertion>\n");
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ClassAssertion>\n");
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" +"OLE_TL_total_"+ nameUnit + "\"/>\n\t</ClassAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
      else { // unit.getString("isReference").equals("annotation")
	  	unit.absolute(currentRow);
	    // for each unit, find the MUC (or reference) units, create a (sub-)timeline  
        int init = drammarManager.getInitMUC(unit.getInt("idUnit"), unit2reference, unit); // get the INIT and the END MUC
        // System.out.println("\n @@@@ Init MUC of " + nameUnit + " is " + init); 
        if (init != -1) {
          int end = drammarManager.getEndMUC(unit.getInt("idUnit"), unit2reference, unit);
          // System.out.println("\n @@@@ End MUC of " + nameUnit + " is " + end);
          if (end != -1) { // if this unit has both init and end MUC units
          	if (!timelines.containsKey(String.valueOf(init)+String.valueOf(end))) {
           	  timelines.put(String.valueOf(init)+String.valueOf(end),true);
	          // System.out.println("\n @@@@ Class Assertion of Timeline key " + String.valueOf(init)+String.valueOf(end));
	          String nameTimeline = repository.get(keyTimeline);
	          String nameScene = repository.get(keyScene); 
	          System.out.println("\n @@@@ Class Assertion of Scene " + nameScene + " and Timeline NON MUC " + nameTimeline + " and all its OLEs");
              unit.absolute(drammarManager.getUnitRow (unit, init)); // position cursor at the init row
	          keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); nameUnit = repository.get(keyUnit); 
	          String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); String nameOLE = repository.get(keyOLE);
              try { // assert the timeline and the first OLE
		      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Scene"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameScene + "\"/>\n\t</ClassAssertion>\n");
		      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Timeline"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</ClassAssertion>\n");
		      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ClassAssertion>\n");
	          } catch (IOException e) {e.printStackTrace();}
	          pair.beforeFirst(); int currentUnit = init; // loop to declare all the OLE's
	          while (pair.next() && currentUnit != end) { // for each adjacent pair of units
		        if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		      	  int followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
	              // System.out.println("\n @@@@ Class Assertion of Timeline NON MUC: followsRow " + followsRow);
	          	  if (followsRow!=-1) {
		      	    keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); nameUnit = repository.get(keyUnit);
	                keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); nameOLE = repository.get(keyOLE);
                    try { // declare the timeline and the first OLE
		            bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ClassAssertion>\n");
	                } catch (IOException e) {e.printStackTrace();}
			        currentUnit = pair.getInt("followsPair"); // update current with followsPair
			        pair.beforeFirst(); // start from beginning again to find next
			      } // END created OLE
		          else { System.out.println(" !!!! Error in Class Assertion of Timeline " + nameTimeline + ". ROW of unit " + pair.getInt("followsPair") + " does not exist in DB !!!");}
		        } // END if precedes found
	          } // END for each pair - exit for NO MORE NEXT OR map end REACHED
	          if (currentUnit != end) {System.out.println("ERROR: Unit " + nameUnit + " has no continuation, but should have!!!");}
	        } // END timelines containsKey
	      } else { System.out.println("\n @@@@ Error: NON MUC unit: " + unit.getInt("idUnit") + " has no end MUC!!!");}
        } else { System.out.println("\n @@@@ Error: NON MUC unit: " + unit.getInt("idUnit") + " has no initial MUC!!!");}
      } // END ELSE == annotation UNIT
      unit.absolute(currentRow);
    } // END while units
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertUnitsTimelines(ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository, BufferedWriter bw) {
  	// write class assertions of Unit, OLE_TL_Unit, TL_Unit, TL_total
  	try { 
	unit.beforeFirst();
	while (unit.next()) {
	  // System.out.println("\n @@@@ Class Assertion of Unit: " + unit.getInt("idUnit") + " with printUnit " + drammarManager.fixName(unit.getString ("printUnit")));
      if (unit.getString("isReference").equals("reference")) {
	    if (unit.getString("printUnit")!="") {
	  	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un");
	      String nameUnit = repository.get(keyUnit);
		  // System.out.println("\n @@@@ \n @@@@ nameUnit: \n " + nameUnit);
		  try { 
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Unit"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameUnit + "\"/>\n\t</ClassAssertion>\n");
		  } catch (IOException e) {e.printStackTrace();}
	    } // END IF non empty unit
	    else { System.out.println("\n @@@@ Error: unit: " + unit.getInt("idUnit") + " has no content!!!");}
      } // END IF MUC unit
	} // END OF while UNITS
  	//create instance of TL_total
	try { 
	bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Timeline"+"\"/>\n\t\t<NamedIndividual IRI=\"#TL_total"+"\"/>\n\t</ClassAssertion>\n");
    } catch (IOException e) {e.printStackTrace();}
    assertTimelinesFromUnits (unit, pair, unit2reference, repository, bw);    
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}			
  }

  public void assertAgents(ResultSet agent, Hashtable<String,String> repository, BufferedWriter bw) {
    // create instances of Agent
  	try { 
  	agent.beforeFirst();
	while (agent.next()) {	
	  if (agent.getString("nameAgent")!="") { // if this agent has content 
	    String nameAgent = repository.get(drammarManager.createName(agent.getInt("idAgent"),"Ag"));  
	    try { 
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Agent"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameAgent + "\"/>\n\t</ClassAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	}
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertActions(ResultSet action, ResultSet unit, Hashtable<String,String> repository, BufferedWriter bw) {
    // create instances of Agent
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
			bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#SetMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#" +"SM_"+nameUnit+"_"+ nameAction + "\"/>\n\t</ClassAssertion>\n");	
		  }
		}
		} catch (IOException e) {e.printStackTrace();}
	  }
	} // END While 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertValues(ResultSet value, Hashtable<String,String> repository, BufferedWriter bw) {
    // create instances of Agent
  	try { 
  	value.beforeFirst();
	while (value.next()){
	  if (value.getString("nameValue")!="") {
		String nameValue = repository.get(String.valueOf(value.getInt("idValue"))+"Va");
	    try { 
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#ValueEngaged"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameValue + "\"/>\n\t</ClassAssertion>\n");
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#ValueSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameValue + "_Schema" + "\"/>\n\t</ClassAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertGoals(ResultSet goal, Hashtable<String,String> repository, BufferedWriter bw) {
    // create instances of Agent
  	try { 
  	goal.beforeFirst();
	while (goal.next()) {
	  if (goal.getString("nameGoal")!="") {
	    String nameGoal = repository.get(String.valueOf(goal.getInt("idGoal"))+"Go");
	    try { //scrive Declaration dei goal
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Goal"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameGoal + "\"/>\n\t</ClassAssertion>\n");
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#GoalSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameGoal +"_Schema"+ "\"/>\n\t</ClassAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertStates(ResultSet state, Hashtable<String,String> repository, BufferedWriter bw) {
    // create instances of Agent
  	try { 
  	state.beforeFirst();
	while (state.next()){	
	  if (!state.getString("typeState").equals("NIL") && !state.getString("nameState").isEmpty()) {
	    String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); 
		String typeState = drammarManager.fixName(state.getString("typeState"));
	    try { //scrive Declaration degli state
		if(typeState.equals("SOA")) {
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#StateOfAffairs"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameState + "\"/>\n\t</ClassAssertion>\n");
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#FrameNetSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameState + "_Schema"+"\"/>\n\t</ClassAssertion>\n");
		} else 
		if (typeState.equals("NEG")) {
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#NegatedState"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameState + "\"/>\n\t</ClassAssertion>\n");
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#NegationSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameState + "_Schema"+"\"/>\n\t</ClassAssertion>\n");
		} else 
		if (typeState.equals("BEL")) {
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Belief"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameState + "\"/>\n\t</ClassAssertion>\n");
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#BeliefSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameState + "_Schema"+"\"/>\n\t</ClassAssertion>\n");
		} else 
		if (typeState.equals("VAS")) {
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#ValueEngaged"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameState + "\"/>\n\t</ClassAssertion>\n");
		  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#ValueSchema"+"\"/>\n\t\t<NamedIndividual IRI=\"#"+ nameState + "_Schema"+"\"/>\n\t</ClassAssertion>\n");
		}
	    } catch (IOException e) {e.printStackTrace();}
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
	    // if (cssplan.getInt("SetPlan")==pre) { // if this is the right pre CSS 
	    if (cssplan.getInt("Set")==pre) { // if this is the right pre CSS 
		  int idState = cssplan.getInt("idState"); // get the state within this pre CSS
		  state.beforeFirst();
		  while (state.next()) { // for each state, if this is the non-null current state (in the pre CSS)
		    if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			  String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			  try {
			    bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#ConsistentStateSet"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "CSS"+namePlan+"_"+nameState+"_Pre" + "\"/>\n\t</ClassAssertion>\n");
				bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#SetMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSS"+namePlan+"_"+nameState+"_Pre" + "\"/>\n\t</ClassAssertion>\n");
			  } catch (IOException e) {e.printStackTrace();}
			}
		  }
		}
  		// CREO INDIVIDUI CSSPLAN effects + SM_CSSPLAN
		// if (cssplan.getInt("SetPlan")==eff) {
		if (cssplan.getInt("Set")==eff) {
		  int idState = cssplan.getInt("idState");
		  state.beforeFirst();
		  while (state.next()) {
		    if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			  String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			  try {
			  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#ConsistentStateSet"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "CSS"+namePlan+"_"+nameState+"_Eff" + "\"/>\n\t</ClassAssertion>\n");
			  bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#SetMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSS"+namePlan+"_"+nameState+"_Eff" + "\"/>\n\t</ClassAssertion>\n");
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
    // create instances of Agent
  	try { 
	timeline.beforeFirst();
	while (timeline.next()) { // for each timeline
	  if (timeline.getInt("idTimeline")!=0) {nameTL = repository.get(String.valueOf(timeline.getInt("idTimeline"))+"Tl");}
	  else {nameTL = "TL_total";}
	  System.out.println("\n @@@@ CLASS ASSERTION OF Timeline " + nameTL);
	  if (nameTL!=null) {
	  int pre = timeline.getInt("preconditionsTimeline"); // get preconditions CSS - System.out.println(pre);
	  int eff = timeline.getInt("effectsTimeline"); // get effects CSS - System.out.println(eff);
 	  // CREO INDIVIDUI CSSTIMELINE preconditions + SM_CSSTIMELINE
	  csstimeline.beforeFirst(); // for each CSS timeline
	  while (csstimeline.next()) {
	  	if (pre > 0) {
	      try {
	      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#ConsistentStateSet"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "CSSTL_"+nameTL+"_Pre" + "\"/>\n\t</ClassAssertion>\n");
	      } catch (IOException e) {e.printStackTrace();}
	      // if (csstimeline.getInt("SetTimeline")==pre) { // if this is the right pre CSS 
	      if (csstimeline.getInt("Set")==pre) { // if this is the right pre CSS 
		    int idState = csstimeline.getInt("idState"); // get the state within this pre CSS
		    state.beforeFirst();
		    while (state.next()) { // for each state, if this is the non-null current state (in the pre CSS)
		      if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			    String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			    if (nameState!=null) {
			      try {
			      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#SetMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSSTL_"+nameTL+"_"+nameState+"_Pre" + "\"/>\n\t</ClassAssertion>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    } else {System.out.println("\n !!!! ERROR: State " + idState + " is NULL ");}
			  }
		    }
		  }
		} // END pre > 0
  		// CSSTIMELINE effects + SM_CSSTIMELINE
		// if (csstimeline.getInt("SetTimeline")==eff){
	  	if (eff > 0) {
	      try {
	      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#ConsistentStateSet"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "CSSTL_"+nameTL+"_Eff" + "\"/>\n\t</ClassAssertion>\n");
	      } catch (IOException e) {e.printStackTrace();}
		  if (csstimeline.getInt("Set")==eff){
		    int idState = csstimeline.getInt("idState");
		    state.beforeFirst();
		    while (state.next()) {
		      if (state.getInt("idState")==idState && !state.getString("typeState").equals("NIL")) {
			    String nameState = repository.get(String.valueOf(state.getInt("idState"))+"St"); // declare the CSS and the set member
			    if (nameState!=null) {
			      try {
			      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#SetMember"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSSTL_"+nameTL+"_"+nameState+"_Eff" + "\"/>\n\t</ClassAssertion>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    } else {System.out.println("\n !!!! ERROR: State " + idState + " is NULL ");}			      
			  }
		    }
		  }
		} // END eff > 0 
	  } // END while csstimelines
	  } // END if NOT null
	} // END while timelines 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }	    
*/

  private static void assertTimelineFromPlan (int row, ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, Hashtable<String,String> repository, BufferedWriter bw) {    
    // assert multiple-unit timelines from rec plans
  	try { 
  	plan.absolute(row);
    // retrieve the declared timeline for this abstract plan
	if (plan.getString("typePlan").equals("Rec")) { 
      String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); String namePlan = repository.get(keyPlan);
	  String returnTimeline[] = drammarManager.retrieveTimelineForAbstractPlan (row, plan, unit, unit2reference, pair, repository);
      if (returnTimeline[0]!="null") {
      	String keyTimeline = returnTimeline[0];  String nameTimeline = returnTimeline[1];
        int mappingInit = plan.getInt("mappingInit"); int mappingEnd = plan.getInt("mappingEnd");
	    int mappingInit_unit_row = drammarManager.getUnitRow (unit, mappingInit); unit.absolute(mappingInit_unit_row);
        String printInitUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
	    int mappingEnd_unit_row = drammarManager.getUnitRow (unit, mappingEnd); unit.absolute(mappingEnd_unit_row);
        String printEndUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
        int currentUnit = mappingInit;
	    System.out.println(" #### Class assertion of Timeline " + nameTimeline + " from Plan " + namePlan);
	    // take the row of the map init unit and position the unit cursor
      	unit.absolute(mappingInit_unit_row);
        String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit); 
	    String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	    String nameOLE = repository.get(keyOLE);
	    System.out.println(" #### Assert class Timeline: " + nameTimeline);
	    System.out.println(" #### Assert class OLE for first OLE: " + nameOLE);
        try { // assert timeline and the first OLE
	    bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#Timeline"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</ClassAssertion>\n");
	    bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ClassAssertion>\n");
	    } catch (IOException e) {e.printStackTrace();}
	    pair.beforeFirst(); currentUnit = mappingInit; int followsRow = 0; // loop to assert all the OLE's
	    while (pair.next() && currentUnit != mappingEnd) { // for each adjacent pair of units
	      if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		    followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
		    if (followsRow!=-1) {
		      keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); nameUnit = repository.get(keyUnit);
	          keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); nameOLE = repository.get(keyOLE);
	          System.out.println(" #### Assert class OLE for OLE: " + nameOLE);
              try { // Assert the current OLE
		      bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</ClassAssertion>\n");
	          } catch (IOException e) {e.printStackTrace();}
			  currentUnit = pair.getInt("followsPair"); // update current with followsPair
			  pair.beforeFirst(); // start from beginning again to find next
		    } // END created OLE for follows
          }
        }
      }
	  else { System.out.println("!!!! ERROR in creating Timeline: No continuity");} // from " + printInitUnit + " to " + printEndUnit);}
	} // END if Abstract Plan
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
  }

  public void assertPlans (ResultSet plan, ResultSet unit, ResultSet pair, ResultSet subplanof, Hashtable<String,String> repository, BufferedWriter bw) {
  	try { 
 	System.out.println("\n@@@@ Class assertions of all plans ");
  	plan.beforeFirst();
	while (plan.next()) { // for each plan 
	  String keyPlan = "NULL";
	  if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
	  else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
	  String namePlan = repository.get(keyPlan); // store in the has table 
	  try { // print the plan declaration 
 	  System.out.println("\n  @@@@ Class assertion of plan " + namePlan);
	  if (plan.getString("typePlan").equals("Base")) {
	  	bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"#DirectlyExecutablePlan"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + namePlan + "\"/>\n\t</ClassAssertion>\n");
	  } else 
	  if (plan.getString("typePlan").equals("Rec")) {
		bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#AbstractPlan"+"\"/>\n\t\t<NamedIndividual IRI=\"#" + namePlan + "\"/>\n\t</ClassAssertion>\n");
	  }  
	  } catch (IOException e) {e.printStackTrace(); }
	  // *** if an abstract plan, find map init and map end over units
      // assertTimelineFromPlan (plan.getRow(), plan, unit, pair, repository, bw); 
    } // END While 
	// Class assertions OLE_PlanFather_PlanChild
 	System.out.println("\n@@@@ Class assertion of all subplan OLEs ");
	subplanof.beforeFirst();
	while (subplanof.next()) {
	  int father = subplanof.getInt("idFatherPlan"); int child = subplanof.getInt("idChildPlan");
	  plan.beforeFirst();
	  while (plan.next()) {
	    if (plan.getString("typePlan").equals("Rec")) {
		  if (plan.getInt("idPlan")==father) {
	        String keyFatherPlan = "NULL";
	        if (plan.getString("typePlan").equals("Rec")) { keyFatherPlan = drammarManager.createName(father, "AbsPl"); }
	        else { keyFatherPlan = drammarManager.createName(father, "DePl"); }
	        String fathername = repository.get(keyFatherPlan); // store in the has table 
		    // String fathername = repository.get(String.valueOf(plan.getInt("idPlan"))+"Pl");
		    plan.beforeFirst();
		    while (plan.next()) {
			  if (plan.getInt("idPlan")==child) {
	            String keyChildPlan = "NULL";
	            if (plan.getString("typePlan").equals("Rec")) { keyChildPlan = drammarManager.createName(child, "AbsPl"); }
	            else { keyChildPlan = drammarManager.createName(child, "DePl"); }
	            String childname = repository.get(keyChildPlan); // store in the has table 
			    // String childname = repository.get(String.valueOf(plan.getInt("idPlan"))+"Pl");
	            System.out.println("    #### Class assertion of subplan OLE: father: " + fathername + "; child: " + childname);
	            try { //stampa OLE for subplan 
				bw.write("\t<ClassAssertion>\n\t\t<Class IRI=\"/drammar/2012/4/drammar.owl#OrderedListElement"+"\"/>\n\t\t<NamedIndividual IRI=\"#OLE_father"+fathername+"_child"+childname  + "\"/>\n\t</ClassAssertion>\n");
	            } catch (IOException e) {e.printStackTrace(); }
			  }
		    }	
		  } // END IF father
	    } // END IF ABSTRACT PLAN
	  } // END FOR each plan
	} // END FOR each subplan	 
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}					
  }

} // end class Declaration