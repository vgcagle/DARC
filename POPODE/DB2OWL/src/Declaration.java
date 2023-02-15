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
//////// Class DECLARATION ////////
//////// ***************************************************************************** ////////

public class Declaration { 

  // FIRST 
  public Hashtable<String,String> declareUnitsTimelines(ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	//create instances of Unit, OLE_TL_Unit, TL_Unit
	System.out.println("\n \n@@@@ Declaration of Units \n \n ");
  	try { 
	unit.beforeFirst();
	while (unit.next()) {
      if (unit.getString("isReference").equals("reference")) {
        // if this unit has content, create a unit and OLE for timeline total
	    if (unit.getString("printUnit")!="") {
	      System.out.println("\n@@@@ Declaration of MUC Unit: " + unit.getInt("idUnit") + " with printUnit " + drammarManager.fixName(unit.getString ("printUnit")));
	  	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un");
	      String nameUnit = drammarManager.fixName(unit.getString ("printUnit")) + "_" + keyUnit;
		  repository.put(keyUnit, nameUnit);
		  // System.out.println(" #### Unit created in DIO: nameUnit " + nameUnit);
		  try { 
		  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameUnit + "\"/>\n\t</Declaration>\n");
		  } catch (IOException e) {e.printStackTrace();}
	    } // END IF non empty unit
	    else { System.out.println(" !!!! Warning in Declaration: unit: " + unit.getInt("idUnit") + " has no content!!! \n This unit is ignored!");}
      } // END IF MUC unit
	} // END OF while UNITS
	System.out.println("\n \n@@@@ Declaration of Timelines: TL_total and all the individual timelines \n \n ");
  	//create instance of TL_total and all the individual timelines  
	try { 
	bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" +"TL_total"+ "\"/>\n\t</Declaration>\n");
	System.out.println("\n@@@@ Declaration of Timeline TL_total");
    } catch (IOException e) {e.printStackTrace();}
    repository = declareTimelinesFromUnits (unit, pair, unit2reference, repository, bw);  
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

  private static Hashtable<String,String> declareTimelinesFromUnits (ResultSet unit, ResultSet pair, ResultSet unit2reference, Hashtable<String,String> repository1, BufferedWriter bw) {    
    // declare single-unit timelines from MUC units + declare multiple-unit timelines from NON MUC units (in this case, also scenes)
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	Hashtable<String, Boolean> timelines = new Hashtable<String, Boolean>(); // repository for all identifiers
  	try { 
	unit.beforeFirst();
	while (unit.next()) { // for each unit, take key and name
	  int currentRow = unit.getRow();	
	  String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit);
	  System.out.println("\n@@@@ Try Declaration of Timeline from Unit " + nameUnit + ", " + unit.getInt("idUnit") + " in data base"); 
	  String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); 
	  String keyScene = drammarManager.createName(unit.getInt("TimelineUnit"), "Sc"); 
	  // keyUnit + "Tl"; 
	  // DECLARE a timeline from MUC units, with non null printUnit
	  if (drammarManager.isMUC(unit.getInt("idUnit"), unit) && !drammarManager.isPrintUnitNULL(unit.getInt("idUnit"), unit)) { 
	    // System.out.println("\n@@@@ Declaration of Timeline from MUC Unit " + nameUnit + ", " + unit.getInt("idUnit") + " in data base"); 
	  	unit.absolute(currentRow);
	    String nameTimeline = "TL_" + keyTimeline;
	    String nameScene = "Sc_" + keyScene;
	    repository.put(keyTimeline, nameTimeline); repository.put(keyScene, nameScene);
	    String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	    String nameOLE = drammarManager.createTypeObj1Obj2Name("OLE", nameTimeline, nameUnit);
	    repository.put(keyOLE, nameOLE);
        try { 
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameScene + "\"/>\n\t</Declaration>\n");
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</Declaration>\n");
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</Declaration>\n");
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" +"OLE_TL_total_"+ nameUnit + "\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace();}
	    System.out.println(" #### OK! Declaration of Timeline " + nameTimeline + " from MUC Unit " + nameUnit + ", " + unit.getInt("idUnit") + " in data base"); 
	    // System.out.println(" #### Timeline from Unit created in DIO " + ); 
	  }
      else { // unit.getString("isReference").equals("annotation")
	  	unit.absolute(currentRow);
	    // for each unit, find the MUC (or reference) units, create a (sub-)timeline  
        int init = drammarManager.getInitMUC(unit.getInt("idUnit"), unit2reference, unit); // get the INIT and the END MUC 
        if (init != -1) {
          int end = drammarManager.getEndMUC(unit.getInt("idUnit"), unit2reference, unit);
          if (end != -1) { // if this unit has both init and end MUC units
            // System.out.print("\n #### NON-MUC Unit from init MUC " + init + "to end MUC " + end);
          	if (!timelines.containsKey(String.valueOf(init)+String.valueOf(end))) {
          	  timelines.put(String.valueOf(init)+String.valueOf(end),true);
	          // System.out.println("\n @@@@ Declaration of Timeline key " + String.valueOf(init)+String.valueOf(end));
	          String nameTimeline = "TL_Init_"+init+"_End_"+end+"_fromNonMUC";
	          String nameScene = "Sc_Init_"+init+"_End_"+end+"_fromNonMUC";
	          repository.put(keyTimeline, nameTimeline); repository.put(keyScene, nameScene);
              unit.absolute(drammarManager.getUnitRow (unit, init)); // position cursor at the init row
              keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
              nameUnit = repository.get(keyUnit);
	          // System.out.println("\n @@@@ Declaration of Timeline " + nameTimeline);
	          String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	          String nameOLE = drammarManager.createTypeObj1Obj2Name("OLE", nameTimeline, nameUnit);
	          repository.put(keyOLE, nameOLE);
              try { // declare the timeline and the first OLE, and the scene
	          bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameScene + "\"/>\n\t</Declaration>\n");
	          bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</Declaration>\n");
	          bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</Declaration>\n");
	          } catch (IOException e) {e.printStackTrace();}
	          pair.beforeFirst(); int currentUnit = init; // loop to declare all the OLE's
	          while (pair.next() && currentUnit != end) { // for each adjacent pair of units
		        if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		      	  int followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
		      	  if (followsRow!=-1) {
		      	    keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
		      	    nameUnit = repository.get(keyUnit);
	                keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	                nameOLE = drammarManager.createTypeObj1Obj2Name("OLE", nameTimeline, nameUnit);
	                repository.put(keyOLE, nameOLE);
                    try { // declare the timeline and the first OLE
	                bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</Declaration>\n");
	                } catch (IOException e) {e.printStackTrace();}
			        currentUnit = pair.getInt("followsPair"); // update current with followsPair
			        pair.beforeFirst(); // start from beginning again to find next
			      } // END created OLE
		          else { System.out.println(" !!!! Error in Declaration of Timeline " + nameTimeline + ". ROW of unit " + pair.getInt("followsPair") + " does not exist in DB !!!");}
		        } // END if precedes found
	          } // END for each pair - exit for NO MORE NEXT OR map end REACHED
	          if (currentUnit != end) {System.out.println(" !!!! Error in Declaration. EXIT CURRENT<>END. Unit " + nameUnit + " has no continuation, but should have!!!");}
	          System.out.println(" #### OK! Declaration of Timeline " + nameTimeline + " from NON-MUC Unit from init MUC " + init + "to end MUC " + end); 
	        }
	      } else { System.out.println(" !!!! Error in Declaration: NON-MUC unit: " + unit.getInt("idUnit") + " has no end MUC!!!");}
        } else { System.out.println(" !!!! Error in Declaration: unit: " + unit.getInt("idUnit") + " has no initial MUC!!!");}
      } // END ELSE == annotation UNIT
      unit.absolute(currentRow);
    }
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}	
    return repository;				
  }

  public Hashtable<String,String> declareAgents(ResultSet agent, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try { 
  	agent.beforeFirst();
	while (agent.next()) {	
	  if (agent.getString("nameAgent")!="") { // if this agent has content 
	  	String keyAgent = drammarManager.createName(agent.getInt("idAgent"), "Ag");
	    String nameAgent = drammarManager.fixName(agent.getString("nameAgent")) + keyAgent;
	    repository.put(keyAgent, nameAgent);
	    // System.out.println("\n @@@@ \n @@@@ nameAgent: \n " + nameAgent);
	    try { 
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameAgent + "\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	}
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

  public Hashtable<String,String> declareGoals(ResultSet goal, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try { 
  	goal.beforeFirst();
	while (goal.next()) {
	  if (goal.getString("nameGoal")!="") {
	  	String keyGoal = String.valueOf(goal.getInt("idGoal")) + "Go";
	    String nameGoal = drammarManager.fixName(goal.getString("nameGoal")) + keyGoal;
		repository.put(keyGoal, nameGoal);
	    try { //scrive Declaration dei goal
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameGoal + "\"/>\n\t</Declaration>\n");
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameGoal +"_Schema"+ "\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

  public Hashtable<String,String> declareActions(ResultSet action, ResultSet unit, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try { 
  	action.beforeFirst();
	while (action.next()) {	
	//System.out.println("\n @@@@ \n @@@@ Action @@@@ \n --> $" + action.getString("nameAction") + "$");
	  if (action.getString("nameAction")!="") {
 	    // System.out.println("\n @@@@ \n @@@@ Action2 @@@@ \n --> $" + action.getString("nameAction") + "$");
	  	String keyAction = String.valueOf(action.getInt("idAction")) + "Ac";
	    String nameAction = drammarManager.fixName(action.getString ("nameAction")) + keyAction;
	    repository.put(keyAction, nameAction);
	    try { 
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameAction + "\"/>\n\t</Declaration>\n");
		unit.beforeFirst();
		while (unit.next()) {
		  if (unit.getInt("idUnit")==action.getInt("Unit_idUnit")) {
			String nameUnit = repository.get(String.valueOf(unit.getInt("idUnit"))+"Un");
			//String nameUnit = fixName(unit.getString("nameUnit"));
			bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" +"SM_"+nameUnit +"_"+ nameAction + "\"/>\n\t</Declaration>\n");
		  }
		}
		} catch (IOException e) {e.printStackTrace();}
	  }
	} // END While 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

  public Hashtable<String,String> declareValues(ResultSet value, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try {
  	value.beforeFirst(); 
	while (value.next()){
	  if (value.getString("nameValue")!="") {
	  	String keyValue = String.valueOf(value.getInt("idValue")) + "Va";
		String nameValue = drammarManager.fixName(value.getString("nameValue")) + keyValue;
		repository.put(keyValue, nameValue);
	    try { 
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameValue + "\"/>\n\t</Declaration>\n");
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameValue + "_Schema"+ "\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

  public Hashtable<String,String> declareStates(ResultSet state, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try { 
  	state.beforeFirst();
	while (state.next()){	
	  if (!state.getString("typeState").equals("NIL") && !state.getString("nameState").isEmpty()) {
	  	String keyState = String.valueOf(state.getInt("idState")) + "St";
	    String nameState = drammarManager.fixName(state.getString ("nameState"));
	    repository.put(keyState, nameState); 
	    try { //scrive Declaration degli state
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameState + "\"/>\n\t</Declaration>\n");
		bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameState + "_Schema\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace();}
	  }
	} // END while
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }

/*
  public Hashtable<String,String> declareCSSPlans(ResultSet plan, ResultSet cssplan, ResultSet state, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
    // create instances of Agent
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
			  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "CSS"+namePlan+"_"+nameState+"_Pre" + "\"/>\n\t</Declaration>\n");
			  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSS"+namePlan+"_"+nameState+"_Pre" + "\"/>\n\t</Declaration>\n");
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
			  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "CSS"+namePlan+"_"+nameState+"_Eff" + "\"/>\n\t</Declaration>\n");
			  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSS"+namePlan+"_"+nameState+"_Eff" + "\"/>\n\t</Declaration>\n");
			  } catch (IOException e) {e.printStackTrace();}
			}
		  }
		}
	  }
	} // END while plans	    0
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }
*/
/*			 
  public Hashtable<String,String> declareCSSTimelines(ResultSet timeline, ResultSet csstimeline, ResultSet state, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
    // create instances of Agent
    String nameTL = "NULL TIMELINE";
  	try { 
	timeline.beforeFirst();
	while (timeline.next()) { // for each timeline
	  if (timeline.getInt("idTimeline")!=0) {nameTL = repository.get(String.valueOf(timeline.getInt("idTimeline"))+"Tl");}
	  else {nameTL = "TL_total";}
	  System.out.println("\n @@@@ DECLARATION OF Timeline " + nameTL);
	  if (nameTL!=null) {
 	  // CREO INDIVIDUI CSSTIMELINE preconditions + SM_CSSTIMELINE
	  int pre = timeline.getInt("preconditionsTimeline"); // get preconditions CSS - System.out.println(pre);
	  int eff = timeline.getInt("effectsTimeline"); // get effects CSS - System.out.println(eff);
	  csstimeline.beforeFirst(); // for each CSS timeline
	  while (csstimeline.next()) {
	  	if (pre > 0) {
	      try {
	      bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "CSSTL_"+nameTL+"_"+"Pre" + "\"/>\n\t</Declaration>\n");
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
			      bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSSTL_"+nameTL+"_"+nameState+"_Pre" + "\"/>\n\t</Declaration>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    }
			  }
		    }
		  }
		} // END pre > 0
  		// CSSTIMELINE effects + SM_CSSTIMELINE
	  	if (eff > 0) {
	      try {
	      bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "CSSTL_"+nameTL+"_"+"Eff" + "\"/>\n\t</Declaration>\n");
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
			      bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + "SM_CSSTL_"+nameTL+"_"+nameState+"_Eff" + "\"/>\n\t</Declaration>\n");
			      } catch (IOException e) {e.printStackTrace();}
			    }
			  }
		    }
		  }
	    } // END eff > 0
	  } // END WHILE csstimelines
	  } // END if NOT null 
	} // END while timelines 
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}					
    return repository;				
  }	    
*/
  private static Hashtable<String,String> declareTimelineFromPlan (int row, ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, Hashtable<String,String> repository1, BufferedWriter bw) {    
    // This is only for (multiple unit) timelines from REC plans; for BASE plans (single) timelines are already created in the database
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
  	try { 
  	plan.absolute(row);
	// in case of DE plan this is a unit id, so, it is already a timeline; nothing to be done
	// in case of ABSTRACT plan, timeline is to be built
	if (plan.getString("typePlan").equals("Rec")) { 
      String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); String namePlan = repository.get(keyPlan);
	  System.out.println("\n@@@@ Create and declare new timeline from Plan  " + namePlan);
	  // then take timeline (column Timeline_idTimeline); if empty (NULL) create from scratch
	  String keyTimeline = drammarManager.createName(plan.getInt("Timeline_idTimeline"), "Tl"); 
	  String nameTimeline = repository.get(keyTimeline); 
	  // String keyTimeline = ""; String nameTimeline = ""; 
	  // take the extreme units of the timeline 
	  int mappingInit = drammarManager.getInitMUC(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  int mappingEnd = drammarManager.getEndMUC(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  // int mappingInit = drammarManager.getMappingInit(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  // int mappingEnd = drammarManager.getMappingEnd(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  // int mappingInit = plan.getInt("mappingInit"); int mappingEnd = plan.getInt("mappingEnd");
	  int mappingInit_unit_row = drammarManager.getUnitRow (unit, mappingInit); unit.absolute(mappingInit_unit_row);
      String printInitUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
	  int mappingEnd_unit_row = drammarManager.getUnitRow (unit, mappingEnd); unit.absolute(mappingEnd_unit_row);
      String printEndUnit = drammarManager.fixName(unit.getString("printUnit") + "Un");  
      // check whether all the units from init to end are linked 
	  pair.beforeFirst(); int currentUnit = mappingInit; int followsRow = 0; // loop to declare all the OLE's	  
	  while (pair.next() && currentUnit != mappingEnd && followsRow != -1) { // for each adjacent pair of units
	    if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		  followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
          currentUnit = pair.getInt("followsPair"); // update current with followsPair
		  pair.beforeFirst(); // start from beginning again to find next		
		}
      } // END while check continuity
      // if timeline is complete from init to end, proceed to create the timeline
      if (currentUnit == mappingEnd) {
	    keyTimeline = drammarManager.fixName(printInitUnit + "_" + printEndUnit + "Tl");
	    nameTimeline = "TL_" + keyTimeline;
	    repository.put(keyTimeline, nameTimeline); 
	    System.out.println(" #### Declaration of new Timeline from Plan: " + nameTimeline);
	    // take the row of the map init unit and position the unit cursor
      	unit.absolute(mappingInit_unit_row);
        String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); String nameUnit = repository.get(keyUnit); 
	    String keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	    String nameOLE = drammarManager.createTypeObj1Obj2Name("OLE", nameTimeline, nameUnit);
	    System.out.println(" #### First OLE: " + nameOLE);
	    repository.put(keyOLE, nameOLE);
        try { // print the timeline declaration (from map init to map end)
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameTimeline + "\"/>\n\t</Declaration>\n");
	    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</Declaration>\n");
	    } catch (IOException e) {e.printStackTrace(); }	
	    pair.beforeFirst(); currentUnit = mappingInit; // loop to declare all the OLE's
	    while (pair.next() && currentUnit != mappingEnd) { // for each adjacent pair of units
	      if (pair.getInt("precedesPair") == currentUnit) { // if precedes is the idCurrent, take the follows
		    followsRow = drammarManager.getUnitRow (unit, pair.getInt("followsPair"));
		    if (followsRow!=-1) {
		      keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); nameUnit = repository.get(keyUnit);
	          keyOLE = drammarManager.createTypeObj1Obj2Name("OLE", keyTimeline, keyUnit); 
	          nameOLE = drammarManager.createTypeObj1Obj2Name("OLE", nameTimeline, nameUnit);
	          repository.put(keyOLE, nameOLE);
	          System.out.println(" #### OLE: " + nameOLE);
              try { // declare the timeline and the first OLE
		      bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#" + nameOLE + "\"/>\n\t</Declaration>\n");
	          } catch (IOException e) {e.printStackTrace();}
			  currentUnit = pair.getInt("followsPair"); // update current with followsPair
			  pair.beforeFirst(); // start from beginning again to find next
		    } // END created OLE for follows
          }
        }
      }
	  else { System.out.println("!!!! ERROR in creating Timeline: No continuity from " + printInitUnit + " to " + printEndUnit);}
	} // END if Abstract Plan
	else { // BASE plan

	}
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}		
    return repository;			
  }

  public Hashtable<String,String> declarePlans (ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, ResultSet subplanof, Hashtable<String,String> repository1, BufferedWriter bw) {
  	Hashtable<String,String> repository = new Hashtable<String,String>(); repository.putAll(repository1);
	System.out.println("\n \n@@@@ Declaration of Plans \n \n ");
	try {
  	plan.beforeFirst();
	while (plan.next()) { // for each plan 
	  String keyPlan = "NULL";
	  if (plan.getString("typePlan").equals("Rec")) { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "AbsPl"); }
	  else { keyPlan = drammarManager.createName(plan.getInt("idPlan"), "DePl"); }
	  String namePlan = drammarManager.fixName(plan.getString("namePlan")) + keyPlan; // create the name of the plan 
	  repository.put(keyPlan, namePlan); // store in the has table 
 	  System.out.println("\n@@@@ Declaration of Plan: " + namePlan);
	  try { // print the plan declaration 
	  bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#"+ namePlan + "\"/>\n\t</Declaration>\n");
	  } catch (IOException e) {e.printStackTrace(); }
      // repository = declareTimelineFromPlan (plan.getRow(), plan, unit, unit2reference, pair, repository, bw);
	  if (plan.getInt("Agent_idAgent")==0) {System.out.println("\n !!!! ERROR: Plan key " + keyPlan + " has no Agent!!!");}
	} // END While 
	// Declarations OLE_PlanFather_PlanChild
 	System.out.println("\n@@@@ Declaration of all subplan OLEs ");
	subplanof.beforeFirst();
	while (subplanof.next()) {
	  int father = subplanof.getInt("idFatherPlan"); int child = subplanof.getInt("idChildPlan");
	  plan.beforeFirst();
	  while (plan.next()) {
	    if (plan.getString("typePlan").equals("Rec")) {
		  if (plan.getInt("idPlan")==father) {
	        // int father = subplanof.getInt("idFatherPlan"); // String fathername = repository.get(String.valueOf(father)+"Pl");
	        String keyFatherPlan = "NULL";
	        if (plan.getString("typePlan").equals("Rec")) { keyFatherPlan = drammarManager.createName(father, "AbsPl"); }
	        else { keyFatherPlan = drammarManager.createName(father, "DePl"); }
	        String fathername = repository.get(keyFatherPlan); // store in the has table 
		    // String fathername = repository.get(plan.getInt(String.valueOf("idPlan"))+"Pl");
		    plan.beforeFirst();
		    while (plan.next()) {
			  if (plan.getInt("idPlan")==child) {
	            // int child = subplanof.getInt("idChildPlan"); // String childname = repository.get(String.valueOf(child)+"Pl");
	            String keyChildPlan = "NULL";
	            if (plan.getString("typePlan").equals("Rec")) { keyChildPlan = drammarManager.createName(child, "AbsPl"); }
	            else { keyChildPlan = drammarManager.createName(child, "DePl"); }
	            String childname = repository.get(keyChildPlan); // store in the has table 
			    // String childname = repository.get(plan.getInt(String.valueOf("idPlan"))+"Pl");
 	            System.out.println("    #### Declaration of subplan OLE: father: " + fathername + "; child: " + childname);
	            try { //stampa OLE for subplan 
			    bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#OLE_father"+fathername+"_child"+childname  + "\"/>\n\t</Declaration>\n");
	            } catch (IOException e) {e.printStackTrace(); }
			  }
		    }	
		  } // END IF father
	    } // END IF ABSTRACT PLAN
	  } // END FOR each plan
	} // END FOR each subplan	 
    } catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1);}	
    return repository;				
  }

} // end class Declaration