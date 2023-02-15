// package jena.examples.rdf ;

package KB2Vistool_translator;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.datatypes.*; 
import com.hp.hpl.jena.datatypes.xsd.*;

// import edu.stanford.smi.protege.model.*;

import java.io.*;
import java.util.*;

public class KB_parse { 

  static final String drammar_prefix = "http://www.cirma.unito.it/drammar/drammar.owl#"; 
  static final String drammar2012_prefix = "http://www.cadmos.cirma.unito.it/drammar/2012/4/drammar.owl#"; 
  static final String rdf_prefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"; 
  static final String rdfs_prefix = "http://www.w3.org/2000/01/rdf-schema#";
  static final String www_prefix = "http://www.cadmos.cirma.unito.it#"; 

  static final String freeDescription_property = drammar_prefix + "freeDescription"; 
  static final String plantype_property = drammar_prefix + "Plan_type"; 
  static final String plan_accomplishment_property = drammar_prefix + "accomplished"; 
  static final String comment_property = rdfs_prefix + "comment";  

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@  ERROR MESSAGE + NULL ASSIGNMENT  @@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // print an error message with a structure and returns "NULL" as output
  public static String printErrorReturnNull (String incipit, String object, String claim) {
    System.out.println("\n ERROR: " + incipit + " " + object + " " + claim);
    return "NULL";
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@ SEARCH VALUE OF DATATYPE PROPERTY @@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // search the literal value of a datatype property (property_s) of a resource (subj) in a set of statements (iter_s)
  public static Literal search_datatype_value (String property_s, Resource subj, Model m) {
    // System.out.println("\n @@@@ search_datatype_value \n @@@@ " + property_s + "   "  + subj.toString());  
    Property dp_aux = m.getProperty(property_s);
    // System.out.println("\n @@@@ search_datatype_value 2" + dp_aux.toString());  
    if (dp_aux == null) {return null;}
    StmtIterator iter_s = m.listStatements(new SimpleSelector(subj, null, (String) null)); 
    while (iter_s.hasNext()) {
      // System.out.println("\n @@@@ search_datatype_value 3");  
      Statement stmt2 = iter_s.nextStatement();  // get next statement
      Property prop = stmt2.getPredicate();
      RDFNode   object     = stmt2.getObject();      // get the object
      // System.out.println("\n @@@@ search_datatype_value 4 " + prop.toString() + "   "  + object.toString());  
      if (prop.toString().equals(property_s)) {
        if(object.isLiteral()) { // it is a datatype property 
          // ATTENZIONE: DATATYPE FUNZIONANO SOLO SE NON IN LABEL ... OK FREE-DESCRIPTION !!! 
          Literal l = (Literal) object;
          // System.out.println("\n \n       search_datatype_value  " + l);
          return l; // return the literal (can be boolean or string --- casting outside)
        }
      }
    }
    return null;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@ SEARCH OBJECT VALUE OF OBJECT PROPERTY @@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // returns the value of an object property (property_s) of a resource (subj); 
  // in case of multiple values, it returns the first
  public static String search_object_value (Resource subj, String property_s, Model m) {
    // System.out.println("\n @@@@ search_object_value \n @@@@ " + property_s + "   "  + subj.toString());      
    Property p_aux = m.getProperty(property_s);
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector(subj, p_aux, (RDFNode) null)); // select stmts with subj as subject
    if (iter_stmts.hasNext()) { // for each statement (with subject, predicate, and object)
      Statement stmt2 = iter_stmts.nextStatement();  // get next statement
      // System.out.println("\n RETURN OBJECT " + stmt2.getObject().toString());
      return stmt2.getObject().toString();
    } // END of IF  
    return "NULL";
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@ SEARCH SUBJECT VALUE OF OBJECT PROPERTY @@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // search the subject value of an object property (property_s) given the resource object (obj) 
  public static String search_subject_value (String property_s, RDFNode obj, Model m) {
    // System.out.println("\n @@@@ search_subject_value \n @@@@ " + property_s + "   "  + obj.toString());      
    Property p_aux = m.getProperty(property_s);
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector((Resource) null, p_aux, obj)); // select stmts with obj as object
    if (iter_stmts.hasNext()) { // for each statement (with subject, predicate, and object)
      Statement stmt2 = iter_stmts.nextStatement();  // get next statement
      Resource subj = stmt2.getSubject();
      // System.out.println("\n RETURN SUBJECT " + subj.toString());
      return subj.toString();
    } // END of IF  
    return "NULL";
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SEARCH FOR NO PROPERTY @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // check whether some subject does not have some property in the model
  public static Boolean search_no_property (Resource subj, String property_s, Model m) {
    // System.out.println("\n @@@@ search_no_property \n @@@@ " + subj.toString() + "   "  + property_s );      
    // returns T/F on the existence of property_s 
    Property p_aux = m.getProperty(property_s);
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector(subj, p_aux, (RDFNode) null)); // select stmts with current bp as subject
    while (iter_stmts.hasNext()) {          	
      Statement st = iter_stmts.nextStatement();  // get next statement (actually one)
      RDFNode   ob = st.getObject();      // get the object
      if (ob!=null) {return false;}          
    } // END WHILE
    return true;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@ IS SOME PROPERTY VALUED AS ? @@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // check whether some subject has a property valued as the object in the model
  public static Boolean is_property_valued_as (Resource subj, String property_s, Resource obj, Model m) {
    // System.out.println("\n @@@@ search_no_property \n @@@@ " + subj.toString() + "   "  + property_s );      
    // returns T/F on the existence of property_s 
    Property p_aux = m.getProperty(property_s);
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector(subj, p_aux, (RDFNode) null)); // select stmts with subj as subject
    while (iter_stmts.hasNext()) {          	
      Statement st = iter_stmts.nextStatement();  // get next statement 
      RDFNode   ob = st.getObject();      // get the object
      if (ob.toString().equals(obj.toString())) {return true;}  // if the object equals obj, return true    
    } // END WHILE
    return false;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ IS PLAN ACCOMPLISHED @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // check whether a plan is accomplished
  public static String is_plan_accomplished (Resource b_r_plan, Model m) {
    Boolean accomplished = true; String accomplished_s = "NULL";
    Literal l = KB_parse.search_datatype_value (plan_accomplishment_property, b_r_plan, m); 
    if (l==null) {accomplished_s = "NULL";} else {accomplished = (Boolean) l.getValue(); accomplished_s = (accomplished) ? "T" : "F";}

    return accomplished_s;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      PARSE AGENT       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void parse_agent (Statement stmt, Resource subject, Property predicate, RDFNode object, 
                                                 Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!";
    System.out.println("\n ------ PARSE " + subject.toString() + " OF TYPE " + object.toString());

    // Literal l = search_datatype_value (freeDescription_property, subject, model); 
    Literal l = search_datatype_value (comment_property, subject, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    System.out.println("@@@@ PARSE AGENT: id: " + subject.toString().substring(drammar_prefix.length()) + "\"" +
                       " type=\"AGT\"" + 
                       " d: " + d + "\"" +
                       " @@@@ ");
    KB2Vistool_translator.repository.put(subject.toString().substring(drammar_prefix.length()), 
                   " id=\"" + subject.toString().substring(drammar_prefix.length()) + "\"" +
                   " type=\"AGT\"" + 
                   " print=\"" + d + "\""); 
    KB2Vistool_translator.agents.put(subject.toString().substring(drammar_prefix.length()), 
                   " id=\"" + subject.toString().substring(drammar_prefix.length()) + "\"" +
                   " type=\"AGT\"" + 
                   " print=\"" + d + "\"");
    } 
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      PARSE STATE       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void parse_state (Statement stmt, Resource state, Property predicate, RDFNode object, 
                                   Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!"; 
    String bs = "NULL"; // status of some SOA or DONE
    String mapping_state = "NULL";
    String mapping_set = "NULL";
    String seq_or_hie = "NULL"; 
    
    Statement s;
    Resource state_set, unit_state, plan_state;
    Resource plan, unit;
    
    System.out.println("\n ------ PARSE " + state.toString() + " OF TYPE " + object.toString());

    // ... retrieve the description
    //Literal l = search_datatype_value (drammar_prefix + "freeDescription", state, model); 
    Literal l = search_datatype_value (comment_property, state, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    // If such type is SOA or Done, ... 
    if (t.equals("SOA") || t.equals("ACC") || t.equals("BEL") || t.equals("VAS") || t.equals("NEG")) { // || t.equals("EMO"))  
  	// then retrieve the state status (only existing for SOA and DONE) or atStake boolean value (for VAS) or true (for BEL)
      System.out.println("@@@@ PARSE SOA/DONE/BEL/VAS/NEG @@@@ type: " + t + " d: " + d + " @@@@ ");
      Boolean status; 
      if (t.equals("SOA") || t.equals("ACC")) { // if SOA or ACC or NEG, check the boolean State_status
        Property sSp = model.getProperty(drammar_prefix + "State_status");  
        l = search_datatype_value (drammar_prefix + "State_status", state, model); 
        if (l == null) {bs = "NULL";} else {status = (Boolean) l.getValue(); bs = (status) ? "T" : "F";}
      } else if (t.equals("VAS")) {
        Property aSp = model.getProperty(drammar_prefix + "atStake");  
        l = search_datatype_value (drammar_prefix + "atStake", state, model); 
        if (l==null) {bs = "NULL";} else {status = (Boolean) l.getValue(); bs = (status) ? "T" : "F";}
      } else { // if BEL 
        bs = "T";
      }
    // @@@ CREATE AS MANY OBJECTS AS CONTAINMENTS IN DRAMA STRUCTURES  
      Property iISp = model.getProperty(drammar_prefix + "isMemberOf"); 
      Resource planstate = model.getResource(drammar_prefix + "ConsistentStateSet");
      // retrieve the stmts isInSet for this state
      StmtIterator iter_inSet_stmts = model.listStatements(new SimpleSelector(state, iISp, (RDFNode) null));   
      // for each stmt of membership (isInSet)
      while (iter_inSet_stmts.hasNext()) {  
        mapping_state = "NULL"; mapping_set = "NULL";
        s = iter_inSet_stmts.nextStatement();  
        // retrieve one plan state that contains the state
        state_set = (Resource) s.getObject();  // the state set can be plan state or unit state
        
        String id = state.toString().substring(drammar_prefix.length())+"_"+state_set.toString().substring(drammar_prefix.length());
        
        seq_or_hie = "SEQUENCE"; // initialize to "SEQUENCE"; changed to "HIERARCHY" if it is the case
        // if the state set is a plan state 
        if (is_property_valued_as (state_set, rdf_prefix + "type", planstate, model)) { 
          // if it is precondition/effect of a plan and the plan spans a unit, retrieve the unit state that is precondition/effect of the unit 
          seq_or_hie = "HIERARCHY"; // initialize to "SEQUENCE"; changed to "HIERARCHY" if it is the case
          if (search_object_value(state_set, drammar_prefix + "isPlanPreconditionOf", model).equals("NULL")) {
            plan = model.getResource(search_object_value(state_set, drammar_prefix + "isPlanEffectOf", model));
            Resource ole_unit = model.getResource(search_object_value(plan, drammar_prefix + "containsLastOLE", model));
            unit = model.getResource(search_object_value(ole_unit, drammar_prefix + "hasData", model));
            unit_state = model.getResource(search_object_value(unit, drammar_prefix + "hasTimelineEffect", model));
          } else {
            plan = model.getResource(search_object_value(state_set, drammar_prefix + "isPlanPreconditionOf", model));
            Resource ole_unit = model.getResource(search_object_value(plan, drammar_prefix + "containsFirstOLE", model));
            unit = model.getResource(search_object_value(ole_unit, drammar_prefix + "hasData", model));
            unit_state = model.getResource(search_object_value(unit, drammar_prefix + "hasTimelinePrecondition", model));
          }
          // if such unit_state contains the same state 
          if (is_property_valued_as (state, drammar_prefix + "isMemberOf", unit_state, model)) {
            // set mapping in the state to the state in the unit state
            mapping_state = state.toString().substring(drammar_prefix.length());
            mapping_set = unit_state.toString().substring(drammar_prefix.length());
          }
        } // END if in planstate 
                
        System.out.println("@@@@ PARSE STATE: " +
                   " id=\"" + state.toString().substring(drammar_prefix.length()) + "\"" + 
                   " set=\"" + state_set.toString().substring(drammar_prefix.length()) + "\"" + 
                   " type=\"" + t + "\"" + 
                   " seq_or_hie=\"" + seq_or_hie + "\"" + 
                   " status=\"" + bs + "\"" + 
                   " mapping=\"" + mapping_state + "\"" +
                   " mapping_set=\"" + mapping_set + "\"" + 
                   " print=\"" + d + "\"" + 
                           " @@@@ ");
        KB2Vistool_translator.repository.put(id, 
                   " id=\"" + state.toString().substring(drammar_prefix.length()) + "\"" + 
                   " set=\"" + state_set.toString().substring(drammar_prefix.length()) + "\"" + 
                   " type=\"" + t + "\"" + 
                   " seq_or_hie=\"" + seq_or_hie + "\"" + 
                   " status=\"" + bs + "\"" + 
                   " mapping=\"" + mapping_state + "\"" +
                   " mapping_set=\"" + mapping_set + "\"" + 
                   " print=\"" + d + "\""); 
      }
    } 
  }
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                
  
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@      PARSE ACTION      @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // parse action

  private static void parse_action (Statement stmt, Resource action, Property predicate, RDFNode object, 
                                    Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!";
    String mapping_incident = "NULL";
    String mapping_set = "NULL";
    String seq_or_hie = "NULL";

    System.out.println("\n ------ PARSE ACTION " + action.toString() + " OF TYPE " + object.toString());
    // ... retrieve the description
    //Literal l = search_datatype_value (freeDescription_property, action, model); 
    Literal l = search_datatype_value (comment_property, action, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  

    
    Statement s; Resource ole_or_sm;

    // @@@ CREATE AS MANY OBJECTS AS CONTAINMENTS IN DRAMA STRUCTURES  
    Property iIDOp = model.getProperty(drammar_prefix + "isDataOf"); // to select the statements of property isDataOf
    StmtIterator iter_isDataOf_stmts = model.listStatements(new SimpleSelector(action, iIDOp, (RDFNode) null)); // retrieve the stmt isDataOf for this action  
    while (iter_isDataOf_stmts.hasNext()) {
      s = iter_isDataOf_stmts.nextStatement(); 
      ole_or_sm = (Resource) s.getObject(); // retrieve the ole or setmember of the action    
      System.out.println("------ " + ole_or_sm.toString());
      Property tt = model.getProperty(rdf_prefix + "type"); 
      StmtIterator iter_tt_stmts = model.listStatements(new SimpleSelector(ole_or_sm, tt, (RDFNode) null)); // retrieve the stmt type for this ole or setmember  
      if (iter_tt_stmts.hasNext()) { // if a type for this ole or setmember
        s = iter_tt_stmts.nextStatement(); 
        Resource ole_or_sm_type = (Resource) s.getObject(); // retrieve the type of this ole or setmember
        System.out.println("------ " + ole_or_sm_type.toString());
        if (ole_or_sm_type.toString().equals(drammar_prefix + "SetMember")) {
          Resource unit = model.getResource(search_object_value(ole_or_sm, drammar_prefix + "isMemberOf", model)); // retrieve the unit that contains the setmember
          mapping_set = unit.toString().substring(drammar_prefix.length());

          String print_string;
          print_string = " id=\"" + action.toString().substring(drammar_prefix.length()) + "\"" +  
                         " type=\"" + t + "\"" +
                         " set=\"" + unit.toString().substring(drammar_prefix.length()) + "\"" +
                         " seq_or_hie=\"" + "SEQUENCE" + "\"" +
                         " mapping=\"" + "NULL" + "\"" +
                         " mapping_set=\"" + "NULL" + "\"" +
                         " print=\"" + d + "\"";

          System.out.println("@@@@ PARSE ACTION: " + print_string + " @@@@ ");
          KB2Vistool_translator.repository.put(action.toString().substring(drammar_prefix.length()), print_string);
        }
      }
    }
  }
/*
      Property iOCp = model.getProperty(drammar_prefix + "isOLEContained"); // to select the statement of property isOLEContained
      StmtIterator iter_OC_stmts = model.listStatements(new SimpleSelector(ole, iOCp, (RDFNode) null)); // retrieve the stmt isOLEContained for this action  
      if (iter_OC_stmts.hasNext()) { // if a plan contains this ole 
        s = iter_OC_stmts.nextStatement();  
        Resource plan = (Resource) s.getObject(); // retrieve the plan that contains the action 
        String print_string, id_long;
        if ((!search_object_value(action, drammar_prefix + "isDataOf", model).equals("NULL"))) {
      	  // RETRIEVE THE UNIT THAT CONTAINS THE ACTION
          Resource setmember = model.getResource(search_object_value(action, drammar_prefix + "isDataOf", model)); // retrieve the setmember that contains the action
          Resource unit = model.getResource(search_object_value(setmember, drammar_prefix + "isMemberOf", model)); // retrieve the unit that contains the setmember
          mapping_set = unit.toString().substring(drammar_prefix.length());
        
          print_string = " id=\"" + action.toString().substring(drammar_prefix.length()) + "\"" +  
                         " type=\"" + t + "\"" +
                         " set=\"" + unit.toString().substring(drammar_prefix.length()) + "\"" +
                         " seq_or_hie=\"" + "SEQUENCE" + "\"" +
                         " mapping=\"" + "NULL" + "\"" +
                         " mapping_set=\"" + "NULL" + "\"" +
                         " print=\"" + d + "\"";
          System.out.println("@@@@ PARSE ACTION: " + print_string + " @@@@ ");
          id_long = action.toString().substring(drammar_prefix.length())+"_"+unit.toString().substring(drammar_prefix.length()); 
          KB2Vistool_translator.repository.put(id_long, print_string);
        }
        print_string = " id=\"" + action.toString().substring(drammar_prefix.length()) + "\"" +
                       " type=\"" + t + "\"" +
                       " set=\"" + plan.toString().substring(drammar_prefix.length()) + "\"" +
                       " seq_or_hie=\"" + "HIERARCHY" + "\"" +
                       " mapping=\"" + action.toString().substring(drammar_prefix.length()) + "\"" +
                       " mapping_set=\"" + mapping_set + "\"" +
                       " print=\"" + d + "\"";
        System.out.println("@@@@ PARSE ACTION: " + print_string + " @@@@ ");
        id_long = action.toString().substring(drammar_prefix.length())+"_"+plan.toString().substring(drammar_prefix.length()); 
        KB2Vistool_translator.repository.put(id_long, print_string);
      } // END if a plan contains this ole 

    } // END if ANY CONTAINMENT FOR THE ACTION
  }
          */

  
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       PARSE PLAN       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  private static void parse_plan (Statement stmt, Resource plan, Property predicate, RDFNode object, 
                                                 Model model, PrintWriter write_rep, String t) {
    String d = "NULL";
    String accomplished_s = "NULL";
    String goal_s = "NULL";
    String agent_s = "NULL";
    String mapping_init = "NULL";
    String mapping_end = "NULL";
    
    System.out.println("\n ------ PARSE " + plan.toString() + " OF TYPE " + object.toString());

    // @@@ RETRIEVE VALUES OF DATATYPE PROPERTIES (see String casting in retrieval of statements) and set variables              

    // ... retrieve the description
    //Literal l = search_datatype_value (freeDescription_property, plan, model); 
    Literal l = search_datatype_value (comment_property, plan, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    // @@ accomplished T or F --- i.e., accomplishment datatype property 
    accomplished_s = is_plan_accomplished (plan, model);   
    // System.out.println("\n Plan " + plan.toString().substring(drammar_prefix.length()) + ", accomplished " + accomplished_s);       
    if (!search_object_value(plan, drammar_prefix + "achieves", model).equals("NULL")) {
      Resource goal = model.getResource(search_object_value(plan, drammar_prefix + "achieves", model));
      goal_s =  goal.toString().substring(drammar_prefix.length());  
    } else {System.out.println("\n ERROR: PLAN " + plan.toString().substring(drammar_prefix.length()) + " HAS NO GOAL!");}
    // System.out.println("\n @@@@ \n @@@@ After goal: " + goal_s);
    // @@@ RETRIEVE VALUES OF OBJECT PROPERTIES (cf. RDFNode casting in retrieval of statements)
    // @@ isIntendedBy AGENT
    agent_s = search_object_value (plan, drammar_prefix + "isIntendedBy", model); 
    // System.out.println("\n Plan " + plan.toString().substring(drammar_prefix.length()) + ", agent " + agent_s);       
    if (agent_s.equals("NULL")) {System.out.println("\n ERROR: PLAN " + plan.toString().substring(drammar_prefix.length()) + " HAS NO AGENT!");}
    else {
      // to select the statements of property mapping (init and end)
      if (!search_object_value(plan, drammar_prefix + "isMotivationFor", model).equals("NULL")) {
    	  // System.out.println("@@@@ HABEMUS MOTIVATIONEM!");
        Resource timeline = model.getResource(search_object_value(plan, drammar_prefix + "isMotivationFor", model)); 
        if (!search_object_value(timeline, drammar_prefix + "containsFirstOLE", model).equals("NULL")) {
          Resource ole_unit_init = model.getResource(search_object_value(timeline, drammar_prefix + "containsFirstOLE", model));
          if (!search_object_value(ole_unit_init, drammar_prefix + "hasData", model).equals("NULL")) {
            Resource unit_init = model.getResource(search_object_value(ole_unit_init, drammar_prefix + "hasData", model));
            mapping_init = unit_init.toString().substring(drammar_prefix.length());
            if (!search_object_value(timeline, drammar_prefix + "containsLastOLE", model).equals("NULL")) {
              Resource ole_unit_end = model.getResource(search_object_value(timeline, drammar_prefix + "containsLastOLE", model));
              if (!search_object_value(ole_unit_end, drammar_prefix + "hasData", model).equals("NULL")) {
                Resource unit_end = model.getResource(search_object_value(ole_unit_end, drammar_prefix + "hasData", model));
                mapping_end = unit_end.toString().substring(drammar_prefix.length());
                // @@@ PRINT THE PLAN HEADER and insert in repository
                String repository_entry = " id=\"" + plan.toString().substring(drammar_prefix.length()) + "\"" +
                                          " type=\"" + t + "\"" +
                                          " agent=\"" + agent_s.substring(drammar_prefix.length()) + "\"" +
                                          " goal=\"" + goal_s + "\"" +
                                          " accomplished=\"" + accomplished_s + "\"" +
                                          " mapping_init=\"" + mapping_init + "\"" +
                                          " mapping_end=\"" + mapping_end + "\"" +
                                          " print=\"" + d + "\"";
                                          // System.out.println("\n @@@@ \n @@@@ Before put: " + plan.toString().substring(drammar_prefix.length()));
                                          KB2Vistool_translator.repository.put(plan.toString().substring(drammar_prefix.length()), repository_entry);     
                                          // System.out.println("\n @@@@ \n @@@@ After put: " + plan.toString().substring(drammar_prefix.length()));
                                          System.out.println("@@@@ PARSE PLAN: id: " + plan.toString().substring(drammar_prefix.length()) + "\"" +
                                                                                   " type=\"" + t + "\"" +
                                                                                   " agent=\"" + agent_s.substring(drammar_prefix.length()) + "\"" +
                                                                                   " goal=\"" + goal_s + "\"" +
                                                                                   " accomplished=\"" + accomplished_s + "\"" +
                                                                                   " mapping_init=\"" + mapping_init + "\"" +
                                                                                   " mapping_end=\"" + mapping_end + "\"" +
                                                                                   " print=\"" + d + "\"" +
                                                                                   " @@@@ ");
              } else {System.out.println("\n ERROR: OLE UNIT END " + ole_unit_end.toString().substring(drammar_prefix.length()) + " HAS NO DATA!");}
            } else {System.out.println("\n ERROR: TIMELINE " + timeline.toString().substring(drammar_prefix.length()) + " HAS NO LAST OLE!");}  
          } else {System.out.println("\n ERROR: OLE UNIT INIT " + ole_unit_init.toString().substring(drammar_prefix.length()) + " HAS NO DATA!");}
        } else {System.out.println("\n ERROR: TIMELINE " + timeline.toString().substring(drammar_prefix.length()) + " HAS NO FIRST OLE!");}
      } else {System.out.println("\n ERROR: PLAN " + plan.toString().substring(drammar_prefix.length()) + " HAS NO TIMELINE!");}
    // to select the statements of goal 
  }
  } 


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       PARSE SCENE       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void parse_scene (Statement stmt, Resource scene, Property predicate, RDFNode object, 
                                                  Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!";
    String mapping_init = "NULL";
    String mapping_end = "NULL";
    
    System.out.println("\n ------ PARSE " + scene.toString() + " OF TYPE " + object.toString());

    // @@@ RETRIEVE VALUES OF DATATYPE PROPERTIES (see String casting in retrieval of statements) and set variables              
    // ... retrieve the description
    //Literal l = search_datatype_value (freeDescription_property, scene, model); 
    Literal l = search_datatype_value (comment_property, scene, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    // @@@ RETRIEVE MAPPING INIT AND MAPPING END
    if (!search_object_value(scene, drammar_prefix + "spans", model).equals("NULL")) {
      System.out.println("@@@@ SCENE SPANS TIMELINE!");
      Resource timeline = model.getResource(search_object_value(scene, drammar_prefix + "spans", model)); 
      Resource ole_unit_init = model.getResource(search_object_value(timeline, drammar_prefix + "containsFirstOLE", model));
      Resource unit_init = model.getResource(search_object_value(ole_unit_init, drammar_prefix + "hasData", model));
      mapping_init = unit_init.toString().substring(drammar_prefix.length());
      if (!search_object_value(timeline, drammar_prefix + "containsLastOLE", model).equals("NULL")) {
        Resource ole_unit_end = model.getResource(search_object_value(timeline, drammar_prefix + "containsLastOLE", model));
        Resource unit_end = model.getResource(search_object_value(ole_unit_end, drammar_prefix + "hasData", model));
        mapping_end = unit_end.toString().substring(drammar_prefix.length());
      }
    }

    // @@@ PRINT THE SCENE HEADER and insert in repository
    String repository_entry = " id=\"" + scene.toString().substring(drammar_prefix.length()) + "\"" +
                              " type=\"" + t + "\"" +
                              " mapping_init=\"" + mapping_init + "\"" +
                              " mapping_end=\"" + mapping_end + "\"" +
                              " print=\"" + d + "\"";

    KB2Vistool_translator.repository.put(scene.toString().substring(drammar_prefix.length()), repository_entry);
                  
    System.out.println("@@@@ PARSE SCENE: id: " + scene.toString().substring(drammar_prefix.length()) + "\"" +
                              " type=\"" + t + "\"" +
                              " mapping_init=\"" + mapping_init + "\"" +
                              " mapping_end=\"" + mapping_end + "\"" +
                              " print=\"" + d + "\"" +
                              " @@@@ ");
  } 

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@    PARSE CONSISTENT STATE SET     @@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void parse_stateset (Statement stmt, Resource stateset, Property predicate, RDFNode object, 
                                      Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!";
    String pre_or_eff = "NULL"; 
    System.out.println("\n ------ PARSE " + stateset.toString() + " OF TYPE " + object.toString());

    // ... retrieve the description
    //Literal l = search_datatype_value (freeDescription_property, stateset, model); 
    Literal l = search_datatype_value (comment_property, stateset, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    if ((!search_object_value(stateset, drammar_prefix + "isPlanPreconditionOf", model).equals("NULL")) ||
        (!search_object_value(stateset, drammar_prefix + "isTimelinePreconditionOf", model).equals("NULL"))) {pre_or_eff="PRE";}
    else if ((!search_object_value(stateset, drammar_prefix + "isPlanEffectOf", model).equals("NULL")) || 
            (!search_object_value(stateset, drammar_prefix + "isTimelineEffectOf", model).equals("NULL"))) {pre_or_eff="EFF";}
    else {pre_or_eff="NULL";};

    
    System.out.println("@@@@ PARSE CONSISTENT STATE SET: " +
                       " id: " + stateset.toString().substring(drammar_prefix.length()) + "\"" +
                       " type=\"" + t + "\"" +
                       " pre_or_eff=\"" + pre_or_eff + "\"" +
                       " print=\"" + d + "\"" +
                       " @@@@ ");
    KB2Vistool_translator.repository.put(stateset.toString().substring(drammar_prefix.length()), 
                   " id=\"" + stateset.toString().substring(drammar_prefix.length()) + "\"" +
                   " type=\"" + t + "\"" +
                   " pre_or_eff=\"" + pre_or_eff + "\"" +
                   " print=\"" + d + "\"");
  } 

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       PARSE TIMELINE   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void parse_timeline (Statement stmt, Resource timeline, Property predicate, RDFNode object, 
                                                 Model model, PrintWriter write_rep, String t) {
    String init = "NULL";
    String end = "NULL";
    
    System.out.println("\n ------ PARSE " + timeline.toString() + " OF TYPE " + object.toString());

    // to select the statements of property mapping
    if (!search_object_value(timeline, drammar_prefix + "containsFirstOLE", model).equals("NULL")) {
      Resource ole_unit_init = model.getResource(search_object_value(timeline, drammar_prefix + "containsFirstOLE", model));
      // System.out.println("\n ------##### PARSE " + timeline.toString() + " 2: " + ole_unit_init.toString());
      if (!search_object_value(ole_unit_init, drammar_prefix + "hasData", model).equals("NULL")) {
        Resource unit_init = model.getResource(search_object_value(ole_unit_init, drammar_prefix + "hasData", model));
        // System.out.println("\n ------##### PARSE " + timeline.toString() + " 3: " + unit_init.toString());
        init = unit_init.toString().substring(drammar_prefix.length());
        // System.out.println("\n ------##### PARSE " + timeline.toString() + " 4: " + init);
        if (!search_object_value(timeline, drammar_prefix + "containsLastOLE", model).equals("NULL")) {
          Resource ole_unit_end = model.getResource(search_object_value(timeline, drammar_prefix + "containsLastOLE", model));
          if (!search_object_value(ole_unit_end, drammar_prefix + "hasData", model).equals("NULL")) {
            Resource unit_end = model.getResource(search_object_value(ole_unit_end, drammar_prefix + "hasData", model));
            end = unit_end.toString().substring(drammar_prefix.length());
            // @@@ PRINT THE TIMELINE HEADER and insert in repository
            String repository_entry = " id=\"" + timeline.toString().substring(drammar_prefix.length()) + "\"" +
                                      " type=\"" + t + "\"" +
                                      " init=\"" + init + "\"" +
                                      " end=\"" + end + "\"";
            KB2Vistool_translator.repository.put(timeline.toString().substring(drammar_prefix.length()), repository_entry);  
            System.out.println("@@@@ PARSE TIMELINE: id: " + timeline.toString().substring(drammar_prefix.length()) + "\"" +
                                                   " type=\"" + t + "\"" +
                                                   " init=\"" + init + "\"" +
                                                   " end=\"" + end + "\"" +
                                                   " @@@@ ");
          } else {System.out.println("\n ERROR: OLE UNIT END " + ole_unit_end.toString().substring(drammar_prefix.length()) + " HAS NO DATA!");}
        } else {System.out.println("\n ERROR: TIMELINE " + timeline.toString().substring(drammar_prefix.length()) + " HAS NO END!");}
      } else {System.out.println("\n ERROR: OLE UNIT INIT " + ole_unit_init.toString().substring(drammar_prefix.length()) + " HAS NO DATA!");}
    } else {System.out.println("\n ERROR: TIMELINE " + timeline.toString().substring(drammar_prefix.length()) + " HAS NO INIT!");}
  } 


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       PARSE UNIT       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // parse unit

  private static void parse_unit (Statement stmt, Resource subject, Property predicate, RDFNode object, 
                                                 Model model, PrintWriter write_rep, String t) {
    String d = "NO DESCRIPTION!!!";
    System.out.println("\n ------ PARSE " + subject.toString() + " OF TYPE " + object.toString());

    // ... retrieve the description
    // Literal l = search_datatype_value (freeDescription_property, subject, model); 
    Literal l = search_datatype_value (comment_property, subject, model);
    d = (l!=null) ? (String) l.getValue() : "NULL";  
    System.out.println("@@@@ PARSE UNIT: id: " + subject.toString().substring(drammar_prefix.length()) + "\"" +
                       " type=\"" + t + "\"" +
                       " print=\"" + d + "\"" +
                       " @@@@ ");
    KB2Vistool_translator.repository.put(subject.toString().substring(drammar_prefix.length()), 
                   " id=\"" + subject.toString().substring(drammar_prefix.length()) + "\"" +
                   " type=\"" + t + "\"" +
                   " print=\"" + d + "\"");
    } 

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@ INSERT ITEM IN REPOSITORY @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // insert neutral item in repository: mapping is added afterwords
  public static void insert_item_in_repository (Statement stmt, Resource subject, Property predicate, RDFNode object, 
                                                 Model model, PrintWriter write_rep) {
    System.out.println("\n \n @@@@ INSERT ITEM IN REPOSITORY @@@@");
    // INITIALIZATION 
    String t = "NO TYPE!!!"; 
    String s = "NO STATUS!!!";
    String d = "NO DESCRIPTION!!!";
        
    System.out.println("\n ------ PARSE " + subject.toString() + " OF TYPE " + object.toString());
    // for AGT and OBJ, we only need the featured element (otherwise all the individuals have type Agent and Object, resp.) 
    if (object.toString().equals(drammar_prefix + "Agent") && search_no_property(subject, drammar_prefix+"features", model)) 
      {System.out.println("\n SONO AGENT @@@@"); parse_agent (stmt, subject, predicate, object, model, write_rep, "AGT");} //  
    // else if (object.toString().equals(drammar_prefix + "Action")) // && search_no_property(subject, drammar_prefix+"features", model) 
      // {System.out.println("\n SONO ACTION @@@@"); parse_action (stmt, subject, predicate, object, model, write_rep, "ACT");}
    else if (object.toString().equals(drammar_prefix + "Goal")) 
      {System.out.println("\n SONO GOAL @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "GOA");}
    /*
   else if (object.toString().equals(drammar_prefix + "StateOfAffairs")) 
      {System.out.println("\n SONO SOA @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "SOA");}
    else if (object.toString().equals(drammar_prefix + "Done")) 
      {System.out.println("\n SONO DONE @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "ACC");}
    else if (object.toString().equals(drammar_prefix + "NegatedState")) 
      {System.out.println("\n SONO NEG @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "NEG");}
    else if (object.toString().equals(drammar_prefix + "Belief")) 
       {System.out.println("\n SONO BELIEF @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "BEL");}
    else if (object.toString().equals(drammar_prefix + "ValueEngaged)) 
      {System.out.println("\n SONO VALUE AT STAKE @@@@"); parse_state (stmt, subject, predicate, object, model, write_rep, "VAS");}
    else if (object.toString().equals(drammar_prefix + "ConsistentStateSet")) 
      {System.out.println("\n SONO CONSISTENT STATE SET @@@@"); parse_stateset (stmt, subject, predicate, object, model, write_rep, "CSS");}
*/
    else if (object.toString().equals(drammar_prefix + "Plan")) // AGGIUNGERE IL REC LEVEL DEI PLAN!!! 
      {System.out.println("\n SONO PLAN @@@@"); parse_plan (stmt, subject, predicate, object, model, write_rep, "PLA");}
    else if (object.toString().equals(drammar_prefix + "Scene"))  
      {System.out.println("\n SONO SCENE @@@@"); parse_scene (stmt, subject, predicate, object, model, write_rep, "SCE");}
    else if (object.toString().equals(drammar_prefix + "Unit")) 
      {System.out.println("\n SONO UNIT @@@@ + "); parse_unit (stmt, subject, predicate, object, model, write_rep, "UNI");}
    else if (object.toString().equals(drammar_prefix + "Timeline")) 
      {System.out.println("\n SONO TIMELINE @@@@"); parse_timeline (stmt, subject, predicate, object, model, write_rep, "TML");};
/*
      */
    // NO EMOTIONS YET! TO BE ACCOUNTED FOR AFTERWORDS!  ??? 
    // If one of the accepted types, search the description and print a line in the repository 
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@ PRINT ITEMS IN REPOSITORY @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // insert neutral item in repository: mapping is added afterwords
  public static void print_items_in_repository (Hashtable<String, String> rep, PrintWriter write_rep) {
  	
  	
    Enumeration<String> rep_enum = rep.elements();
  	
    System.out.println("\n \n @@@@ PRINT ITEMS IN REPOSITORY @@@@");

    while (rep_enum.hasMoreElements()) {
      // write_rep.append("\n <rep_elem" + rep_enum.nextElement() + "> </rep_elem> ") ;
      write_rep.append("\n <agent" + rep_enum.nextElement() + "> </agent> ") ;
    }
  }


} // END CLASS KB2Vistool_translator

