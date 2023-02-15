// package jena.examples.rdf ;

package KB2Vistool_translator;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.datatypes.*; 
import com.hp.hpl.jena.datatypes.xsd.*;
import org.apache.commons.io.*;

// import edu.stanford.smi.protege.model.*;

import java.io.*;
import java.util.*;

public class KB2Vistool_translator extends Object { // from Tutorial 5

  static final String inputFileName  = "drammar_ct_inferred_2.owl";  //   "vc-db-1.rdf"
  static final String repositoryFileName  = "drama_test.xml"; 

  static final String drammar_prefix = "http://www.cirma.unito.it/drammar/drammar.owl#";
  static final String drammar2012_prefix = "http://www.cadmos.cirma.unito.it/drammar/2012/4/drammar.owl#"; 
  static final String rdf_prefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"; 
  static final String rdfs_prefix = "http://www.w3.org/2000/01/rdf-schema#"; 
  static final String www_prefix = "http://www.cadmos.cirma.unito.it#"; 

  static final String freeDescription_property = drammar_prefix + "freeDescription"; 

  
  static final int max_rec_level = 100; // provisionary constant, later to be replaced by compute rec level procedure

  static final Hashtable<String, String> repository = new Hashtable<String, String>();
  static final Hashtable<String, String> agents = new Hashtable<String, String>();
  

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@     COMPUTE REC LEVEL    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // compute recursively the level of a plan: DirectlyExecutablePlan  0, AbstractPlan >= 1
  private static int compute_rec_level (Resource b_r_plan, Model m, int level) {
  	int return_level = level;
  	// if b_r_plan is a DirectlyExecutablePlan, return 0
    Resource deplan = m.getResource(drammar_prefix + "DirectlyExecutablePlan");
    if (KB_parse.is_property_valued_as(b_r_plan, rdf_prefix + "type", deplan, m)) {return return_level;}
    //} else 
    // if (plan_type_of(b_r_plan, m).equals("Recursive")) { // else call recursively that adds +1 for each subplan encountered
    // retrieve all the OLEPlan of rec_plan
  	Statement s;
    StmtIterator iter_subplan_stmts = m.listStatements(new SimpleSelector(b_r_plan, m.getProperty(drammar_prefix + "containsOLE"), (RDFNode) null)); 
    // for each OLEPlan
    while (iter_subplan_stmts.hasNext()) { 
      s = iter_subplan_stmts.nextStatement();  Resource ole_subplan = (Resource) s.getObject(); // retrieve the OLEPlan resource
  	  // retrieve the corresponding (sub)Plan
      StmtIterator iter_subplan_data_stmt = m.listStatements(new SimpleSelector(ole_subplan, m.getProperty(drammar_prefix + "hasData"), (RDFNode) null)); 
      if (iter_subplan_data_stmt.hasNext()) {
        s = iter_subplan_data_stmt.nextStatement();  Resource subplan = (Resource) s.getObject(); // retrieve the Plan resource
        int aux = compute_rec_level (subplan, m, level+1);
        if (aux > return_level) {return_level = aux;}
      }
    }
    // }
    //System.out.println("\n Plan " + b_r_plan.toString().substring(drammar_prefix.length()) + " has level " + return_level);
    return return_level;
  }  
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@     COMPUTE MIN REC LEVEL FOR A STATE   @@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // compute the min rec level of a state in a unit state: depends on the plan state at the min rec level

  private static String compute_min_rec_level (Resource state_in_us, Resource ustate, Model model, KB_parse KB_p) {
    String min_rec_level_s = "NULL";
  	int min_rec_level = max_rec_level; 
    // search all the plan states that contain such a state
  	Statement s;
    StmtIterator iter_isinplanstate_stmts = model.listStatements(new SimpleSelector(state_in_us, model.getProperty(drammar_prefix + "isInSet"), (RDFNode) null)); 
    // for each stateset
    while (iter_isinplanstate_stmts.hasNext()) { 
      s = iter_isinplanstate_stmts.nextStatement();  Resource state_set = (Resource) s.getObject(); // retrieve the stateset resource
      Resource planstate = model.getResource(drammar_prefix + "ConsistentStateSet"); 
      String id = state_in_us.toString().substring(drammar_prefix.length())+"_"+state_set.toString().substring(drammar_prefix.length());   
      if (KB_parse.is_property_valued_as (state_set, rdf_prefix + "type", planstate, model)==true) { // if the state set is a plan state
        // if it is precondition or effect of a plan, retrieve the rec level of the plan  
        Resource plan; int plan_rec_level;
        if (!KB_parse.search_object_value(state_set, drammar_prefix + "isPlanPreconditionOf", model).equals("NULL") && // if planstate is precondition of a plan
            !KB_parse.search_object_value(ustate, drammar_prefix + "isTimelinePreconditionOf", model).equals("NULL")) { // unistate is precondition of a unit
          plan = model.getResource(KB_parse.search_object_value(state_set, drammar_prefix + "isPlanPreconditionOf", model)); // retrieve the plan
          plan_rec_level = compute_rec_level (plan, model, 0); // retrieve the rec_level of the plan
          if (plan_rec_level < min_rec_level) { min_rec_level = plan_rec_level; min_rec_level_s = "-" + min_rec_level;} // update the min_rec_level
        } else 
        if (!KB_parse.search_object_value(state_set, drammar_prefix + "isPlanEffectOf", model).equals("NULL") && // if planstate is precondition of a plan
            !KB_parse.search_object_value(ustate, drammar_prefix + "isTimelineEffectOf", model).equals("NULL")) { // unistate is precondition of a unit
          plan = model.getResource(KB_parse.search_object_value(state_set, drammar_prefix + "isPlanEffectOf", model)); // if it is effect of a plan
          plan_rec_level = compute_rec_level (plan, model, 0); // retrieve the rec_level of the plan
          if (plan_rec_level < min_rec_level) { min_rec_level = plan_rec_level; min_rec_level_s = "+" + min_rec_level;} // update the min_rec_level
        };
      };
    }
    return min_rec_level_s;
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
 
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ NUMBER_OF_OLE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // return the number of ole (the length) of a timeline or of a plan (in general, an Ordered List)
  private static int number_of_ole (Resource tl_or_plan, Model m) {
  	int i=0;
    // retrieve the stmts with the timeline or plan as subject and containsOLE as property
    Property cOLEEp = m.getProperty(drammar_prefix + "containsOLE");
    StmtIterator iter_ole_stmts = m.listStatements(new SimpleSelector(tl_or_plan, cOLEEp, (RDFNode) null)); 
    // find the first OLE (the one without a precedes property)
    while (iter_ole_stmts.hasNext()) {Statement s = iter_ole_stmts.nextStatement(); i++; }
    System.out.println("\n number of ole: " + i); 
  	return i; 
  }
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
 
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ LIST_OF_OLE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // return the list of oles of an Ordered List 
  private static Resource[] list_of_ole (Resource tl_or_plan, Model m) {
  	int number_of_ole = number_of_ole(tl_or_plan, m);
    System.out.println("\n PLAN OR TIMELINE: " + tl_or_plan.toString() + " number of ole: " + number_of_ole); 
  	Resource[] array_of_ole = new Resource[number_of_ole]; int i=0;
    for (i=0; i < number_of_ole; i++) {array_of_ole[i]=tl_or_plan;}
    // retrieve the stmts with tl_or_plan as subject and containsOLE as property
    Property cOLEEp = m.getProperty(drammar_prefix + "containsOLE");
    StmtIterator iter_ole_stmts = m.listStatements(new SimpleSelector(tl_or_plan, cOLEEp, (RDFNode) null)); 
    // find the first OLE plan (the one without a precedes property)
    Property pre = m.getProperty(drammar_prefix + "precedes"); // <drammar:precedes rdf:resource="&drammar;OLE_P_H_Kill_Claudius_in_P_H_Revenge"/>
    while (iter_ole_stmts.hasNext()) { // for each ole
      Statement s = iter_ole_stmts.nextStatement();  Resource ole = (Resource) s.getObject(); // retrieve the OLE resource
      StmtIterator iter_precedes_stmt = m.listStatements(new SimpleSelector(null, pre, ole)); // retrieve stmt with ole that precedes such ole
      if (!iter_precedes_stmt.hasNext()) { array_of_ole[0] = ole; } // if no such ole exists, it is the first
    }
    // build the rest of the array from the first
    i=0;
      // retrieve stmts of oles such that the first precedes 
    StmtIterator next_ole_stmt = m.listStatements(new SimpleSelector(array_of_ole[0], pre, (RDFNode) null)); 
      // while we find such statements
    while (next_ole_stmt.hasNext()) {
      Statement s = next_ole_stmt.nextStatement(); Resource ole = (Resource) s.getObject(); // retrieve the OLE resource that follows
      array_of_ole[++i] = ole; // update the array with the new ole and update the index
      next_ole_stmt = m.listStatements(new SimpleSelector(array_of_ole[i], pre, (RDFNode) null)); // retrieve stmts of oles such that the current ole precedes
    }
    System.out.println("\n OLEs: "); for (i=0; i<array_of_ole.length; i++) {System.out.println(" " + array_of_ole[i].toString()); }

  	return array_of_ole; 
  }
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PLAN_TYPE_OF @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // check whether a plan is a base or a recursive plan
  /*
  private static String plan_type_of (Resource b_r_plan, Model m) {
  	// select stmts with b_r_plan as subject and Plan_type as property
    Property p_aux = m.getProperty(drammar_prefix + "Plan_type");
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector(b_r_plan, p_aux, (String) null)); 
    while (iter_stmts.hasNext()) { // for each statement
      Statement st = iter_stmts.nextStatement();
  	  RDFNode object = st.getObject();
  	  Literal l = (Literal) object; // retrieve the plan type (Base or Recursive)
  	  String s = (String) l.getValue();
  	  if (s.equals("Base") || s.equals("base") || s.equals("Recursive")) {return s;} // to discover errors in encoding
    }
    return "NULL";
  }
  */

  // check whether a plan is DE or ABS plan
  private static String plan_type_of (Resource b_r_plan, Model m) {
    Property tt = m.getProperty(rdf_prefix + "type");
    // select stmts with b_r_plan as subject and "type" (tt) as property
    Resource deplan = m.getResource(drammar_prefix + "DirectlyExecutablePlan");      
    // ResIterator iter_plans = model.listSubjectsWithProperty(tt, deplan);
    StmtIterator iter_stmts = m.listStatements(new SimpleSelector(b_r_plan, tt, deplan)); 
    if (iter_stmts.hasNext()) { return "deplan";} // for each statement
    Resource absplan = m.getResource(drammar_prefix + "AbstractPlan");      
    // ResIterator iter_plans = model.listSubjectsWithProperty(tt, deplan);
    iter_stmts = m.listStatements(new SimpleSelector(b_r_plan, tt, absplan)); 
    if (iter_stmts.hasNext()) { return "absplan";} // for each statement
    return "NULL";
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PRINT SUBPLANS  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  private static void print_subplans (Resource b_r_plan, Model model, PrintWriter w_r) { 
    Property hDp = model.getProperty(drammar_prefix + "hasData"); // to select the statements of property hasData

    // retrieve the stmts with b_r_plan as subject and containsOLEPlan as property
    int number_of_ole = number_of_ole(b_r_plan, model);
    if (number_of_ole!=0) {
      Resource[] array_of_ole_subplans = list_of_ole(b_r_plan, model);
      for (int i=0; i < array_of_ole_subplans.length; i++) {
        // <drammar:hasData rdf:resource="&drammar;P_H_007"/>
        Resource ole_subplan = array_of_ole_subplans[i]; // retrieve the ole subplan  
        // System.out.println("\n @@@@ SUBPLAN 1: " + ole_action_subplan.toString());
        StmtIterator iter_subplan_stmt = model.listStatements(new SimpleSelector(ole_subplan, hDp, (RDFNode) null)); // retrieve hasData from OLE       
        if (iter_subplan_stmt.hasNext()) {  
          Statement s = iter_subplan_stmt.nextStatement(); Resource subplan = (Resource) s.getObject(); // retrieve the object 
          // System.out.println("\n @@@@ SUBPLAN 2: " + action_subplan.toString());
          String sa = repository.get(subplan.toString().substring(drammar_prefix.length())); // retrieve the whole description from the repository hashtable
          w_r.append("\n   <pe " + sa + " set=\"" + b_r_plan.toString().substring(drammar_prefix.length()) + "\" />"); // print the corresponding sa
        } // END IF hasData
      } // END for each OLE subplan 
    } // END if (number_of_ole!=0)                     
  }
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PRINT PLANSTATE  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  private static void print_css_pl (String has_precond_or_eff, Resource b_r_plan, Model model, 
                                       PrintWriter w_r, int rec_level, String reached) { 
    Statement s; 
    // retrieve the one stmt with current b_r_plan as subject and hasPlanPrecondition as property
    StmtIterator iter_plan_stmts = model.listStatements(new SimpleSelector(b_r_plan, model.getProperty(drammar_prefix + has_precond_or_eff), (RDFNode) null)); 
    if (iter_plan_stmts.hasNext()) {
      s = iter_plan_stmts.nextStatement(); 
      Resource pstate = (Resource) s.getObject(); // retrieve the precondition plan state 
      String pstate_s = repository.get(pstate.toString().substring(drammar_prefix.length())); // retrieve whole description from the repository 
      w_r.append("\n   <pe " + pstate_s + " >") ;
      // retrieve all the stmts hasMember from this planstate 
      iter_plan_stmts = model.listStatements(new SimpleSelector(pstate, model.getProperty(drammar_prefix + "hasMember"), (RDFNode) null));        
      // @@ RETRIEVE STATES FROM PLAN STATE
      while (iter_plan_stmts.hasNext()) { // for each statement, i.e. for each precondition state
        s = iter_plan_stmts.nextStatement(); 
        Resource state = (Resource) s.getObject(); // retrieve the state, object of hasMember
        String id = state.toString().substring(drammar_prefix.length()) + "_" + pstate.toString().substring(drammar_prefix.length()); 
        String rep_new;
        if (has_precond_or_eff.equals("hasPlanPrecondition")) { 
          rep_new = repository.get(id) + " rec_level=\"-" + rec_level + "\" reached=\"" + reached + "\""; }
        else { rep_new = repository.get(id) + " rec_level=\"+" + rec_level + "\" reached=\"" + reached + "\""; }
        repository.put(id, rep_new); 
        System.out.println("\n @@@@ " + has_precond_or_eff + ": " + id);
        // @@ PRINT STATE
        // w_r.append("\n     <state " + repository.get(id) + " rec_level = " + rec_level + " />");
        w_r.append("\n     <state " + repository.get(id) + " />");
      } // END WHILE there are members of the planstate
      w_r.append("\n   </pe>") ;
    } // END IF plan statements exist               
  }
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PRINT UNITSTATE  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void print_unitstate (String has_precond_or_eff, Resource unit, Model model, PrintWriter w_r, KB_parse KB_p) { 
    Statement s;
    // retrieve the unit state that is precondition or effect of the unit
    StmtIterator iter_hP_stmt = model.listStatements(new SimpleSelector(unit, model.getProperty(drammar_prefix + has_precond_or_eff), (RDFNode) null)); 
    if (iter_hP_stmt.hasNext()) { // given the one statement of hasUnitPrecondition
      s = iter_hP_stmt.nextStatement(); Resource ustate = (Resource) s.getObject(); // retrieve the unit state, precondition or effect of unit 
      String ustate_s = repository.get(ustate.toString().substring(drammar_prefix.length())); // retrieve whole description from the repository 
      w_r.append("\n   <te " + ustate_s + " >"); 
      // for each state of the unit state (property hasSetMember)  
      StmtIterator iter_state_stmts = model.listStatements(new SimpleSelector(ustate, model.getProperty(drammar_prefix + "hasSetMember"), (RDFNode) null));  
      // retrieve stmts of unit state  
      while (iter_state_stmts.hasNext()) { // for each statement of hasSetMember of state from the unit state                
        s = iter_state_stmts.nextStatement(); Resource state = (Resource) s.getObject(); // retrieve the state ID 
        // retrieve the whole description from the repository hashtable
        String id = state.toString().substring(drammar_prefix.length()) + "_" + ustate.toString().substring(drammar_prefix.length()); 
        // String state_print = repository.get(id) + " rec_level=\"" + compute_min_rec_level (state, pstate, ustate, model, KB_p) + "\" reached=\"T\""; 
        String state_print = repository.get(id) + " rec_level=\"" + compute_min_rec_level (state, ustate, model, KB_p) + "\" reached=\"T\""; 
        repository.put(id, state_print);
        System.out.println("\n @@@@ TIMELINE PRECONDITION: " + state.toString().substring(drammar_prefix.length()));
        // @@ PRINT STATE
        // w_r.append("\n     <state " + state_print + " rec_level=\"" + compute_min_rec_level (state, ustate, model, KB_p) + "\" reached=\"T\" />") ;
        w_r.append("\n     <state " + state_print + " />") ;
      } // END WHILE each stmt of hasMember (state in plan state)
      w_r.append("\n   </te>");// </te> closes the "te" of the story state PRECONDITION
    } // END IF unit has precondition (a story state) 
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@ PRINT CONSISTENT STATE SET  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

  private static void print_css_tl (String has_precond_or_eff, Resource timeline, Model model, PrintWriter w_r, KB_parse KB_p) { 
    Statement s;
    // retrieve the consistent state set that is precondition or effect of the timeline
    StmtIterator iter_hP_stmt = model.listStatements(new SimpleSelector(timeline, model.getProperty(drammar_prefix + has_precond_or_eff), (RDFNode) null)); 
    if (iter_hP_stmt.hasNext()) { // given the one statement of hasTimelinePrecondition or hasTimelineEffect
      s = iter_hP_stmt.nextStatement(); Resource css = (Resource) s.getObject(); // retrieve the css, precondition or effect of timeline 
      String css_s = repository.get(css.toString().substring(drammar_prefix.length())); // retrieve whole description from the repository 
      w_r.append("\n   <te " + css_s + " >"); 
      // for each state of the css (property hasMember)  
      StmtIterator iter_state_stmts = model.listStatements(new SimpleSelector(css, model.getProperty(drammar_prefix + "hasMember"), (RDFNode) null));  
      // retrieve stmts of unit state  
      while (iter_state_stmts.hasNext()) { // for each statement of hasMember of state from the unit state                
        s = iter_state_stmts.nextStatement(); Resource state = (Resource) s.getObject(); // retrieve the state ID 
        // System.out.println("\n +++ FIN QUA +++ " + state.toString());

        // retrieve the whole description from the repository hashtable
        String id = state.toString().substring(drammar_prefix.length()) + "_" + css.toString().substring(drammar_prefix.length()); 
        String state_print = repository.get(id) + " rec_level=\"" + compute_min_rec_level (state, css, model, KB_p) + "\" reached=\"T\""; 
        repository.put(id, state_print);
        // System.out.println("\n @@@@ TIMELINE PRECONDITION: " + state.toString().substring(drammar_prefix.length()));
        // @@ PRINT STATE
        w_r.append("\n     <state " + state_print + " />") ;
      } // END WHILE each stmt of hasMember (state in plan state)
      w_r.append("\n   </te>");// </te> closes the "te" of the story state PRECONDITION
    } // END IF timeline has precondition (a story state) 
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    MAIN    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                              
  
  // @@@@@@ MAIN @@@@@@@                              
  public static void main (String args[]) {
    // create an empty model
    Model model = ModelFactory.createDefaultModel();
    KB_parse KB_p = new KB_parse();

    try { 
      IOUtils iou = new IOUtils();
      String content = "NULL";
      content = IOUtils.toString(new FileInputStream("drammar_ct_inferred.owl"), "UTF-8");
      content = content.replaceAll("&www;", "&drammar;");
      content = content.replaceAll("<www:", "<drammar:");
      content = content.replaceAll("</www:", "</drammar:");
      content = content.replaceAll(www_prefix, drammar_prefix);
      content = content.replaceAll(drammar2012_prefix, drammar_prefix);
      IOUtils.write(content, new FileOutputStream("drammar_ct_inferred_2.owl"), "UTF-8");
    } catch (Exception e) { System.out.println("Error: File not generated" + e); System.exit(1); }    
    
    
    // Reader not with FileReader it is necessary to set the character encoding; FileReader only assumes default
    try { InputStreamReader in = new InputStreamReader(new FileInputStream (inputFileName), "UTF-8"); 
          FileWriter rep = new FileWriter(repositoryFileName);    
          PrintWriter write_rep = new PrintWriter(rep);

      // read the RDF/XML file
      model.read(in, "");
      Property tt = model.getProperty(rdf_prefix + "type"); 
      // Property comment = model.getProperty(rdfs_prefix + "comment"); 

      System.out.println("\n @@@@ \n @@@@ PARSE REPOSITORY BEGIN @@@@ \n @@@@ \n @@@@ ");

      // loop over statements, looking for types and print the relevant information
      StmtIterator iter = model.listStatements(new SimpleSelector(null, tt, (RDFNode) null));
      while (iter.hasNext()) { // for each statement, store subject, predicate, and object
        Statement stmt      = iter.nextStatement();  // get next statement
        Resource  subject   = stmt.getSubject();     // get the subject
        Property  predicate = stmt.getPredicate();   // get the predicate
        RDFNode   object    = stmt.getObject();      // get the object

        KB_parse.insert_item_in_repository (stmt, subject, predicate, object, model, write_rep);
        System.out.println("\n @@@@ \n @@@@ After insert_item_in_repository: " + subject.toString());
      } // END while (per ogni statement)    

      System.out.println("\n @@@@ \n @@@@ PARSE REPOSITORY END @@@@ \n @@@@ \n @@@@ ");

      // FILE INITIALIZATION 
      write_rep.append(
        "<?xml version=\"1.0\"?> \n \n" + 
        "\n<drama> \n"  
      ) ;

      write_rep.append("<title id=\"Nozze di Figaro - encoded from Web Interface\" /> \n \n") ;

      Statement s;
      write_rep.append(
        "<!-- ############################### PLANS ################################# --> \n" +
        "\n<plans> \n"        
      ) ;
      System.out.println("\n @@@@ \n @@@@ PARSE PLANS @@@@ \n @@@@ \n @@@@ ");      

      // @@@@ RETRIEVE ALL PLANS, PUT IN A HASH TABLE
      Resource plan = model.getResource(drammar_prefix + "Plan");      
      ResIterator iter_plans = model.listSubjectsWithProperty(tt, plan); // select all plans, i.e. subjects with property type "Plan"
      // put all the plans in a list (hash table); necessary for bottom up processing of plans
      Hashtable<Resource, String> list_of_plans = new Hashtable<Resource, String>(); // insert all plans in a list
      while (iter_plans.hasNext()) { list_of_plans.put(iter_plans.nextResource(), "NULL"); }  
      // System.out.println("\n @@@@ CHECK NUMBER OF PLANS: @@@@ " + list_of_plans.size());  

      // Abstract (Recursive) plans are processed bottom up; subplans are retrieved from the repository; 

      // @@@@ FOR EACH DIRECTLY EXECUTABLE PLAN OR ABSTRACT PLAN, PRINT ALL ELEMENTS
      // while list (hash table) is not empty (we work by deleting plans while they are processed)
      while (!list_of_plans.isEmpty()) { // retrieve all plans still to process        
        iter_plans = model.listSubjectsWithProperty(tt, plan); // plan iterator, i.e. subject with property "type" valued "Plan"
        while (iter_plans.hasNext()) { // for each plan ...
          // @@@ RETRIEVE THE PLAN
          // @@ if it is in the list (still to be processed) 
          Resource b_r_plan = iter_plans.nextResource(); 
          // System.out.println("\n  Plan " + b_r_plan.toString() + " of type " + plan_type_of(b_r_plan, model) );
          if (list_of_plans.containsKey((Object) b_r_plan)) {                	
            // @@@@ COMPUTE THE REC LEVEL FOR EACH PLAN AND INSERT IN THE REPOSITORY
            int rec_level = compute_rec_level (b_r_plan, model, 0);
            System.out.println("\n  Plan " + b_r_plan.toString() + " of type " + plan_type_of(b_r_plan, model) + " and rec level " + rec_level);
          	
          	StmtIterator iter_plan_stmts; // Statement s;              

            // @@@ PRINT THE PLAN HEADER from the repository
            String repository_entry = repository.get(b_r_plan.toString().substring(drammar_prefix.length()));
            if (repository_entry!=null) {
              write_rep.append("\n <plan " + repository_entry + " rec_level=\"" + rec_level + "\" >");
              // @@@ PRINT PRECONDITIONS 
              // print_css_pl ("hasPlanPrecondition", b_r_plan, model, write_rep, rec_level, "T"); 
            
              // System.out.println("\n E' un piano " + KB_parse.search_object_value(b_r_plan, tt.toString(), model));
              // @@@ PRINT ACTIONS OR SUBPLANS
              // if (plan_type_of(b_r_plan, model).equals("Base") || plan_type_of(b_r_plan, model).equals("base")) {
              // Boolean is_property_valued_as (Resource subj, String property_s, Resource obj, Model m)
              Resource de_plan = model.getResource(drammar_prefix + "DirectlyExecutablePlan"); 
              Resource a_plan = model.getResource(drammar_prefix + "AbstractPlan"); 
              if (KB_parse.is_property_valued_as(b_r_plan, tt.toString(), a_plan, model)) {
                print_subplans (b_r_plan, model, write_rep);
              } // NOTHING TO PRINT IN CASE OF UNDERSPECIFIED PLANS (NO BASE, NO RECURSIVE)

              // @@@ PRINT EFFECTS 
              String plan_acc = KB_parse.is_plan_accomplished (b_r_plan, model);
              //print_css_pl ("hasPlanEffect", b_r_plan, model, write_rep, rec_level, plan_acc); 
              write_rep.append("\n </plan> \n");
            }

            // remove from list 
            String result = list_of_plans.remove(b_r_plan);
            // System.out.println("\n @@@@ LIST REMOVED!!! @@@@ " + list_of_plans.size());  

          } // END IF list contains current plan (if removed, already processed) 
        } // END WHILE there are plans 
      } // END WHILE list of plans is not empty --- i.e. there are plans to process
      
      write_rep.append("\n</plans> \n \n") ;


// ###########################################################################################
// ###########################################################################################
//  ######################################## SCENES ########################################
// ###########################################################################################
// ###########################################################################################           

      write_rep.append(
        "<!-- ############################### SCENES ################################# --> \n" +
        "\n<scenes> \n"        
      ) ;
      System.out.println("\n @@@@ \n @@@@ PARSE SCENES @@@@ \n @@@@ \n @@@@ ");      

      // put all the scenes in a list (hash table); select all scenes, i.e. subjects with property type "Scene"
      ResIterator iter_scenes = model.listSubjectsWithProperty(tt, model.getResource(drammar_prefix + "Scene"));
      Hashtable<Resource, String> list_of_scenes = new Hashtable<Resource, String>(); // insert all scenes in a list
      while (iter_scenes.hasNext()) { list_of_scenes.put(iter_scenes.nextResource(), "NULL"); }  

      // while list (hash table) is not empty (we work by deleting scenes while they are processed)
      while (!list_of_scenes.isEmpty()) { // retrieve all scenes still to process        
        iter_scenes = model.listSubjectsWithProperty(tt, model.getResource(drammar_prefix + "Scene")); // scene iterator, i.e. subject with "type" "Scene"
        while (iter_scenes.hasNext()) { // for each plan ...
          // @@@ RETRIEVE THE SCENE
          // @@ if it is in the list (still to be processed) 
          Resource scene = iter_scenes.nextResource(); // System.out.println("\n  Scene " + scene.toString() );
          if (list_of_scenes.containsKey((Object) scene)) {                	
            // int rec_level = compute_rec_level (b_r_plan, model, 0);
            System.out.println("\n  Scene " + scene.toString()); // + " of type " + plan_type_of(b_r_plan, model) + " and rec level " + rec_level);
          	
          	StmtIterator iter_scene_stmts;               

            // @@@ PRINT THE SCENE HEADER from the repository
            // TO BE COMMENTED FOR NON ADEQUATE SCENES
            String repository_entry = repository.get(scene.toString().substring(drammar_prefix.length()));
            write_rep.append("\n <scene " + repository_entry + " >") ; // " rec_level=\"" + rec_level + 
            write_rep.append("\n </scene> \n");
            

          // remove from list 
          String result = list_of_scenes.remove(scene);
          // System.out.println("\n @@@@ LIST REMOVED!!! @@@@ " + list_of_scenes.size());  
          } // END IF scene to be processed yet               
        } // END WHILE there are scenes 
      } // END WHILE list of scenes is not empty --- i.e. there are plans to process
      write_rep.append("\n</scenes> \n \n") ;

// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################
// ###########################################################################################           

            
      System.out.println("\n @@@@ \n @@@@ PARSE TIMELINE @@@@ \n @@@@ \n @@@@ ");
      
      write_rep.append(
        "<!-- ############################### TIMELINES ################################# --> \n" + 
        "\n<timelines> \n"
      );
      
      // @@@@ consider the timeline
      Resource timeline = model.getResource( drammar_prefix + "Timeline" );
      ResIterator iter_timelines = model.listSubjectsWithProperty(tt, timeline); // select all timelines
      while (iter_timelines.hasNext()) { 
      // @@@ print the timeline header
        Resource tl = iter_timelines.nextResource(); 
        write_rep.append("\n<timeline id=\""  + tl.toString().substring(drammar_prefix.length()) + "\"> \n");

        // @@@@ PRECONDITION UNIT STATE: parse the unit state that precedes the unit @@@@
        // print_css_tl ("hasTimelinePrecondition", tl, model, write_rep, KB_p);

        // retrieve all the units in order 
        if (number_of_ole(tl, model) > 0) {
          Resource[] array_of_ole_units = list_of_ole(tl, model); // retrieve the OLE units of the timeline  
          // System.out.println("\n @@ ... AFTER LIST OF OLE");
          // @@@@ for each unit in order @@@@
          for (int i=0; i < array_of_ole_units.length; i++) { // for each OLEUnit
            Resource ole_unit = array_of_ole_units[i]; // retrieve the OLE unit  
            // retrieve hasData from OLE
            // Statement s;              
            StmtIterator iter_unit_stmt = model.listStatements(new SimpleSelector(ole_unit, model.getProperty(drammar_prefix + "hasData"), (RDFNode) null));        
            if (iter_unit_stmt.hasNext()) {  
              s = iter_unit_stmt.nextStatement(); Resource unit = (Resource) s.getObject(); // retrieve the object unit          
              // System.out.println("\n @@ UNIT: " + unit.toString().substring(drammar_prefix.length()));

              // @@@@ UNIT: parse the incidents of the unit @@@@
              String unit_print = repository.get(unit.toString().substring(drammar_prefix.length()));
              write_rep.append("\n   <te " + unit_print + ">"); // print the corresponding "te" 
              // retrieve stmts of unit incidents
              StmtIterator iter_member_stmts = model.listStatements(new SimpleSelector(unit, model.getProperty(drammar_prefix + "hasMember"), (RDFNode) null));  
              boolean has_incidents = false; // boolean: true if there are incidents (ActionInUnit) in the unit 
              // for each member of the unit --- 
              while (iter_member_stmts.hasNext()) { // for each member of the unit                
                s = iter_member_stmts.nextStatement(); 
                Resource member = (Resource) s.getObject(); // retrieve the member ID 
                StmtIterator iter_incidents_stmts = model.listStatements(new SimpleSelector(member, tt, (RDFNode) null));
                // System.out.println("\n @@@@ MEMBER: " + member.toString());
                while (iter_incidents_stmts.hasNext()) { // for each statement of member of the unit                
                  s = iter_incidents_stmts.nextStatement(); 
                  Resource t = (Resource) s.getObject(); // retrieve the member ID 
                  if (t.toString().equals(drammar_prefix + "Action")) {            
                    // retrieve the description from the repository hashtable
                    //COMMENTED: NOT PRINTING ABOUT INCIDENTS
                    //String incident_print = repository.get(member.toString().substring(drammar_prefix.length()) + "_" + unit.toString().substring(drammar_prefix.length())); 
                    // System.out.println("\n @@@@ INCIDENT: " + member.toString());
                    //write_rep.append("\n     <incident " + incident_print + " />"); // print in file   
                    has_incidents = true;
                  } // END if type = ActionInUnit
                } // END WHILE each stmt type of member
              } // END WHILE each stmt of hasMember (state in plan state)
              // if this unit has no incidents, create a fake incident
              if (!has_incidents) {                 
                write_rep.append("\n     <incident " + unit_print + " set=\""+unit.toString().substring(drammar_prefix.length())+"\"" + " />"); // print in file    
              } // END if NO INCIDENT
              write_rep.append("\n   </te>"); // print the end of the "te" UNIT 
            } // END if NEXT  
          } // END FOR each OLE unit
          // @@@@ EFFECTS: parse the story state of EFFECTS that follows the timeline @@@@
          // print_css_tl ("hasTimelineEffect", tl, model, write_rep, KB_p);
        }
        write_rep.append("\n</timeline> \n \n") ;  
      } // END TIMELINE Iterator

      write_rep.append("\n</timelines> \n \n") ;

// ###########################################################################################
// ###########################################################################################
//  ##################################### REPOSITORY ########################################
// ###########################################################################################
// ###########################################################################################           

      write_rep.append(
        "\n<!-- ############################### REPOSITORY ################################# --> \n" +
        "\n<repository> \n"        
      ) ;
      KB_parse.print_items_in_repository (agents, write_rep);
      write_rep.append("\n</repository> \n \n") ;

      
      write_rep.append("\n</drama> \n \n") ;

      write_rep.close() ;
     // write it to standard out
     // model.write(System.out); 
    } catch (Exception e) { System.out.println("Errore: " + e); System.exit(1); }          
  } // END MAIN

} // END CLASS KB2Vistool_translator

