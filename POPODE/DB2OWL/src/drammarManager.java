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
import java.nio.charset.StandardCharsets;

/********CLASSE DRAMMAR MANAGER******/
public class drammarManager {		

  private String dbServer;
  private String dbPort;
  private String dbName;
  private String dbUser;
  private String dbPassword;
  private Connection conn;
	
  //costruttore che prende i parametri di connessione al DB
  public drammarManager(String dbServer, String dbPort, String dbName, String dbUser, String dbPassword) {
    this.dbServer = dbServer;
	this.dbPort = dbPort;
	this.dbName = dbName;
	this.dbUser = dbUser;
	this.dbPassword = dbPassword;
  }
		
  //questo metodo consente di tenere i parametri di connessione su file XML e caricarli all'avvio
  public static drammarManager getManagerFromProperties() {
	Properties params = new Properties();
	String dbServer = "";
	String dbPort = "";
	String dbName = "";
	String dbUser = "";
	String dbPassword = "";
		
	try {
	  //nome del file XML di configurazione
	  params.loadFromXML(new FileInputStream("DrammarSQLManager.xml"));
	} catch(Exception e) { 
	  System.out.println("!!!! ERROR with DB configuration file DrammarSQLManager.xml! Using local values."); 
	  System.exit(1);
	// }
	  // se il file di connessione non c'è o non va bene, i parametri vengono valorizzati coi valori di default
	  // È utile se non si vuole usare il file xml coi parametri e si preferisce tenerli nel codice java
	  // params.setProperty("SERVER", "localhost");
	  // params.setProperty("PORT", "8889");
	  // params.setProperty("DB_NAME", "DrammarGiacomo");
	  // params.setProperty("USER", "root");
	  // params.setProperty("PASSWORD", "root");	
	  // try {
	    //scrive file xml contenente i valori di default
	    // params.storeToXML(new FileOutputStream("DrammarSQLManager.xml"), "File di configurazione per la connessione al DB");
	  // } catch (IOException ioe) {}
	}	
	//qui mette i parametri nelle variabili della classe
	dbServer = params.getProperty("SERVER"); // , "localhost");
	dbPort = params.getProperty("PORT"); // , "8889");
	dbName = params.getProperty("DB_NAME"); // , "DrammarGiacomo");
	dbUser = params.getProperty("USER"); //, "root");
	dbPassword = params.getProperty("PASSWORD"); //, "root");
		
	return new drammarManager(dbServer, dbPort, dbName, dbUser, dbPassword);
  } // END public static drammarManager getManagerFromProperties()

		
  public static String fixName(String s) {
	String s1 = s.replaceAll(" ", "_");
	s1 = s1.replaceAll("/", "_");
	s1 = s1.replaceAll("\"", "\'");
	s1 = s1.replaceAll("l`", "");
	return s1;
  }	

  public static String createName(int n, String suffix) {
	return fixName(String.valueOf(n)) + suffix;
  }	

  public static String createTypeObj1Obj2Name(String type, String obj1, String obj2) {
	return type + "_" + obj1 + "_" + obj2;
  }	

  public static Boolean isMUC (int idUnit, ResultSet unit) {
    try {
  	unit.beforeFirst();
  	while (unit.next()) {
  	  if (unit.getInt("idUnit") == idUnit && unit.getString("isReference").equals("reference")) {return true;}
  	}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return false;
  }

  public static Boolean isPrintUnitNULL (int idUnit, ResultSet unit) {
    try {
  	unit.beforeFirst();
  	while (unit.next()) {
  	  if (unit.getInt("idUnit") == idUnit && unit.getString("printUnit").equals("")) {return true;}
  	}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return false;
  }

  public static int getInitMUC (int unit_n, ResultSet unit2reference, ResultSet unit) {
    try {
  	unit2reference.beforeFirst();
  	while (unit2reference.next()) {
  	  if (unit2reference.getInt("Unit_idUnit") == unit_n && unit2reference.getInt("position") == 1) {
  	  	return unit2reference.getInt("idReferenceUnit");
  	  }
  	}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return -1;
  }

  public static int getEndMUC (int unit_n, ResultSet unit2reference, ResultSet unit) {
    try {
  	boolean found = false; int cur_position = 0; int row = -1; 
  	unit2reference.beforeFirst();
  	while (unit2reference.next()) {
  	  if (unit2reference.getInt("Unit_idUnit") == unit_n && unit2reference.getInt("position") > cur_position) {
  	  	found = true; cur_position = unit2reference.getInt("position"); row = unit2reference.getRow();
  	  }
  	}
   	unit2reference.absolute(row);
   	// System.out.println("\n @@@@ End MUC: " + unit2reference.getInt("idReferenceUnit") + " has row " + row);
    // System.out.println("\n @@@@ End MUC for unit " + unit_n + " is reference unit " + unit2reference.getInt("idReferenceUnit"));
 	if (found) {return unit2reference.getInt("idReferenceUnit");}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return -1;
  }

/*
  public static int getMappingInit (int nonMUCunit_n, ResultSet unit2reference, ResultSet unit) {
  	int unit_n = -1; int return_int = -1;
    try {
  	unit.beforeFirst();
  	while (unit.next()) {
      if (unit.getInt("idUnit") == nonMUCunit_n) { unit_n = unit.getInt("idUnit");}
  	}
  	return_int = getInitMUC(unit_n, unit2reference, unit);
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
	return return_int;
  }

  public static int getMappingEnd (int nonMUCunit_n, ResultSet unit2reference, ResultSet unit) {
  	int unit_n = -1;
    try {
  	unit.beforeFirst();
  	while (unit.next()) {
      if (unit.getInt("TimelineUnit") == nonMUCunit_n) { unit_n = unit.getInt("idUnit");}
  	}
    return getEndMUC(unit_n, unit2reference, unit);
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
	return -1;
  }
*/
  public static int getUnitRow (ResultSet unit, int idUnit) {
    try {
  	unit.beforeFirst();
  	while (unit.next()) {
  	  if (unit.getInt("idUnit") == idUnit) {return unit.getRow();}
  	}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return -1;
  } 

  public static int getPlanRow (ResultSet plan, int idPlan) {
    try {
  	plan.beforeFirst();
  	while (plan.next()) {
  	  if (plan.getInt("idPlan") == idPlan) {return plan.getRow();}
  	}
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  	return -1;
  } 

  public static String[] retrieveTimelineForAbstractPlan (int row, ResultSet plan, ResultSet unit, ResultSet unit2reference, ResultSet pair, Hashtable<String,String> repository) {
    String[] returnTimeline = {"NULL", "NULL"};
  	try { 
  	plan.absolute(row);
    // retrieve the declared timeline (isMotivationFor)
    String keyPlan = drammarManager.createName(plan.getInt("idPlan"), "Pl"); String namePlan = repository.get(keyPlan);
	// String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
	int unit_row = getUnitRow (unit, plan.getInt("Timeline_idTimeline")); unit.absolute(unit_row);
	String keyUnit = drammarManager.createName(unit.getInt("idUnit"), "Un"); 
	String keyTimeline = drammarManager.createName(unit.getInt("TimelineUnit"), "Tl"); // keyUnit + "Tl"; 

	if (plan.getString("typePlan").equals("Rec")) { 
	  // take the extreme units of the timeline  
	  // int mappingInit = drammarManager.getMappingInit(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  // int mappingEnd = drammarManager.getMappingEnd(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  int mappingInit = drammarManager.getInitMUC(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
	  int mappingEnd = drammarManager.getEndMUC(plan.getInt("Timeline_idTimeline"), unit2reference, unit); 
      System.out.println("  #### opa iMF; retrieveTimelineForAbstractPlan: mappingInit " + mappingInit + ", mappingEnd " + mappingEnd + ", in timeline " + plan.getInt("Timeline_idTimeline"));
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

      // if timeline is complete from init to end, proceed to retrieve the timeline
      if (currentUnit == mappingEnd) {
	    returnTimeline[0] = keyTimeline; returnTimeline[1] = repository.get(keyTimeline);
	    return returnTimeline;
	  } else { System.out.println("!!!! ERROR: " + namePlan + " has no continuity in the timeline! "); return returnTimeline; }
	} else {System.out.println("!!!! ERROR: " + namePlan + " is not an abstract plan! "); return returnTimeline; }
    } catch (SQLException sqle) { sqle.printStackTrace(); System.exit(1);}		
    return returnTimeline;
  }

  /**********METODO MAIN***************/				
  public static void main(String[] args) throws IOException {
  	DBparse dbp = new DBparse(); 
  	Declaration decl = new Declaration();
  	ClassAssertion ca = new ClassAssertion();
  	ObjectPropertyAssertion opa = new ObjectPropertyAssertion();
  	DataPropertyAssertion dpa = new DataPropertyAssertion();

	try {	
	  //istanzia il manager
	  drammarManager manager = getManagerFromProperties();
	  //System.out.println("\n @@@@ \n @@@@ START!!! @@@@ \n @@@@ \n @@@@ ");
	  //connette al DB
	  manager.conn = dbp.initConnection(manager.dbServer, manager.dbPort, manager.dbName, manager.dbUser, manager.dbPassword);
	  //estrae il resultset della tabella
	  ResultSet agent = dbp.retrieveAgent(manager.conn);
	  ResultSet attitude = dbp.retrieveAttitude(manager.conn);
	  ResultSet goal = dbp.retrieveGoal(manager.conn);
	  ResultSet action = dbp.retrieveAction(manager.conn);
	  ResultSet cssplan = dbp.retrieveCSSPlan(manager.conn);
	  ResultSet csstimeline = dbp.retrieveCSSTimeline(manager.conn);
	  ResultSet pair = dbp.retrievePair(manager.conn);
	  ResultSet plan = dbp.retrievePlan(manager.conn);
	  ResultSet state = dbp.retrieveState(manager.conn);
	  ResultSet subplanof = dbp.retrieveSubplanOf(manager.conn);
	  ResultSet timeline = dbp.retrieveTimeline(manager.conn);
	  ResultSet unit = dbp.retrieveUnit(manager.conn);
	  ResultSet unit2reference = dbp.retrieveUnit2Reference(manager.conn);
	  ResultSet value = dbp.retrieveValue(manager.conn);
	  //una volta estratti tutti i dati, si disconnette dal DB
	  dbp.terminateConnection(manager.conn);
	  //esce senza segnalare errori
				
	  Hashtable<String, String> repository = new Hashtable<String, String>(); // repository for all identifiers
	  //inizializzo reader e writer per creare il mio file a partire da ontologia senza individui
	  BufferedReader br = null; BufferedWriter bw = null;
	  String sCurrentLine;
	  try {
	    br = new BufferedReader(new FileReader("Drammar12_no_individuals.owl"));
		// System.out.println("\n @@@@ \n @@@@ FOUND KB!!! @@@@ \n @@@@ \n @@@@ ");
	  } catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	  }
	  boolean firstDeclarationFound = false; boolean firstClassAssertionFound = false; boolean firstDataPropertyAssertionFound = false;
	  File file = new File("drammar_ct.owl");
	  //System.out.println("\n @@@@ \n @@@@ CREATED FILE!!! @@@@ \n @@@@ \n @@@@ ");
	  if (!file.exists()) { try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}}
	  FileWriter fw = null;
      try { fw = new FileWriter(file);} catch (IOException e) {e.printStackTrace(); }
	  // bw = new BufferedWriter(fw);
	  bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));				   
	  try {
	    System.out.println("\n @@@@ \n @@@@ DECLARATIONS!!! @@@@ \n @@@@ \n @@@@ ");
	    while ((sCurrentLine = br.readLine())!=null) {
		//////// ***************************************************************************** ////////
		//////// DECLARATION ////////
		//////// ***************************************************************************** ////////
		//scrive Declaration sopra le classi
		  if(sCurrentLine.contains("<EquivalentClasses>") && firstDeclarationFound==false) {
		    firstDeclarationFound= true; // this is the first declarations; all declarations here

		    repository = decl.declareUnitsTimelines(unit, pair, unit2reference, repository, bw); //CREO INDIVIDUI Unit, OLE_TL_Unit, TL_Unit 
		    repository = decl.declareActions(action, unit, repository, bw); //CREO INDIVIDUI Action
		    repository = decl.declareAgents(agent, repository, bw); //CREO INDIVIDUI Agent
		    repository = decl.declareGoals(goal, repository, bw); // CREO INDIVIDUI Goal 					    
		    repository = decl.declareValues(value, repository, bw); // CREO INDIVIDUI Value					    
		    repository = decl.declarePlans(plan, unit, unit2reference, pair, subplanof, repository, bw); //CREO INDIVIDUI Plans
		    // repository = decl.declareStates(state, repository, bw); // CREO INDIVIDUI State 
		    // repository = decl.declareCSSPlans(plan, cssplan, state, repository, bw); // CREO INDIVIDUI CSSPLAN 
		    // repository = decl.declareCSSTimelines(timeline, csstimeline, state, repository, bw); // CREO INDIVIDUI CSSTIMELINE 
		    // for (Map.Entry<String,String> entry : repository.entrySet()) {System.out.println(entry.getKey() + " " + entry.getValue());}
		    
            //try {
              //bw.write("\t<Declaration>\n\t\t<NamedIndividual IRI=\"#TL_total"+"\"/>\n\t</Declaration>\n");
              bw.write(sCurrentLine+ "\n");
            //} catch (IOException e) {e.printStackTrace(); }
            
          }	// END if Equivalent classes 
          else				
		//////// ***************************************************************************** ////////
		//////// CLASS ASSERTIONS ////////
		//////// ***************************************************************************** ////////
		//scrive sopra le SubObjectPropertyOf
		  if (sCurrentLine.contains("<SubObjectPropertyOf>") && firstClassAssertionFound == false) {
	        System.out.println("\n @@@@ \n @@@@ CLASS ASSERTIONS!!! @@@@ \n @@@@ \n @@@@ ");
			firstClassAssertionFound = true;

		    ca.assertUnitsTimelines(unit, pair, unit2reference, repository, bw); // ClassAssertion delle Unit 
		    ca.assertAgents(agent, repository, bw); // ClassAssertion Agent
		    ca.assertActions(action, unit, repository, bw); // ClassAssertion Action
		    ca.assertValues(value, repository, bw); // ClassAssertion Value					    
		    ca.assertPlans(plan, unit, pair, subplanof, repository, bw); // ClassAssertion dei plans
		    ca.assertGoals(goal, repository, bw); // ClassAssertion goals					    
		    // ca.assertStates(state, repository, bw); // ClassAssertion states
		    // ca.assertCSSPlans(plan, cssplan, state, repository, bw); // ClassAssertion CSS plans 
		    // ca.assertCSSTimelines(timeline, csstimeline, state, repository, bw); // ClassAssertion CSS Timelines
 		//////// ***************************************************************************** ////////
		//////// OBJECT PROPERTY ASSERTIONS ////////
		//////// ***************************************************************************** ////////
	        System.out.println("\n @@@@ \n @@@@ OBJECT PROPERTY ASSERTIONS!!! @@@@ \n @@@@ \n @@@@ ");

		    opa.assertUnitsTimelines(unit, pair, unit2reference, repository, bw); // Obj Property Assertion delle Unit 
		    opa.assertAgents(agent, repository, bw); // Obj Property Assertion Agent
		    opa.assertActions(action, unit, repository, bw); // Obj Property Assertion Action
		    opa.assertValues(value, repository, bw); // Obj Property Assertion Value					    
		    opa.assertPlans(plan, unit, pair, unit2reference, subplanof, repository, bw); // Obj Property Assertion dei plans
		    opa.assertGoals(goal, plan, repository, bw); // Obj Property Assertion goals					    
		    // opa.assertStates(state, repository, bw); // Obj Property Assertion states
		    // opa.assertCSSPlans(plan, cssplan, state, repository, bw); // Obj Property Assertion CSS plans 
		    // opa.assertCSSTimelines(timeline, csstimeline, state, repository, bw); // Obj Property Assertion CSS Timelines

 		//////// ***************************************************************************** ////////
		//////// DATA PROPERTY ASSERTIONS ////////
		//////// ***************************************************************************** ////////
	        System.out.println("\n @@@@ \n @@@@ DATA PROPERTY ASSERTIONS!!! @@@@ \n @@@@ \n @@@@ ");

		    // dpa.assertUnitsTimelines(unit, pair, unit2reference, repository, bw); // Obj Property Assertion delle Unit 
		    // dpa.assertAgents(agent, repository, bw); // Obj Property Assertion Agent
		    // dpa.assertActions(action, unit, repository, bw); // Obj Property Assertion Action
		    // dpa.assertValues(value, repository, bw); // Obj Property Assertion Value					    
		    dpa.assertPlansAccomplished(plan, repository, bw); // DATA Property Assertion dei plans
		    // dpa.assertGoals(goal, plan, repository, bw); // Obj Property Assertion goals					    
		    // dpa.assertStates(state, repository, bw); // Obj Property Assertion states
		    // dpa.assertCSSPlans(plan, cssplan, state, repository, bw); // Obj Property Assertion CSS plans 
		    // dpa.assertCSSTimelines(timeline, csstimeline, state, repository, bw); // Obj Property Assertion CSS Timelines
					    
			bw.write(sCurrentLine+ "\n");	
							    	 
		  } // END if SubObjectPropertyOf
		//////// ***************************************************************************** ////////
		//////// ANNOTATION PROPERTY ////////
		//////// ***************************************************************************** ////////						
		  else 
		  if (sCurrentLine.contains("</Ontology>") && firstDataPropertyAssertionFound == false) {
			unit.beforeFirst();
			while (unit.next()) {
			  String name = repository.get(String.valueOf(unit.getInt("idUnit"))+"Un");
			  String annUnit = drammarManager.fixName(unit.getString ("nameUnit"));
			  try {
			  bw.write("\t<AnnotationAssertion>\n\t\t<AnnotationProperty abbreviatedIRI=\"dc:description"+"\"/>\n\t\t<IRI>#"+name+"</IRI>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral"+"\">"+annUnit+"\"</Literal>\n\t</AnnotationAssertion>\n");
			  } catch (IOException e) { e.printStackTrace(); }
			} 
			action.beforeFirst();
			while (action.next()) {
			  if (action.getString("nameAction")!="") {
			  	String name = repository.get(String.valueOf(action.getInt("idAction"))+"Ac");
				String annAction = drammarManager.fixName(action.getString("printAction"));
			    try {
				bw.write("\t<AnnotationAssertion>\n\t\t<AnnotationProperty abbreviatedIRI=\"dc:description"+"\"/>\n\t\t<IRI>#"+name+"</IRI>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral"+"\">"+annAction+"\"</Literal>\n\t</AnnotationAssertion>\n");
			    } catch (IOException e) { e.printStackTrace(); }
			  }
			}
			agent.beforeFirst();
			while (agent.next()) {
			  String name = repository.get(String.valueOf(agent.getInt("idAgent"))+"Ag");
			  String annAgent = drammarManager.fixName(agent.getString("printAgent"));
			  try {
			  bw.write("\t<AnnotationAssertion>\n\t\t<AnnotationProperty abbreviatedIRI=\"dc:description"+"\"/>\n\t\t<IRI>#"+name+"</IRI>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral"+"\">"+annAgent+"\"</Literal>\n\t</AnnotationAssertion>\n");
			  } catch (IOException e) { e.printStackTrace(); }
			}
			goal.beforeFirst();
			while (goal.next()) {
			  String name = repository.get(String.valueOf(goal.getInt("idGoal"))+"Go");
			  String annGoal = drammarManager.fixName(goal.getString("printGoal"));
			  try {
			  bw.write("\t<AnnotationAssertion>\n\t\t<AnnotationProperty abbreviatedIRI=\"dc:description"+"\"/>\n\t\t<IRI>#"+name+"</IRI>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral"+"\">"+annGoal+"\"</Literal>\n\t</AnnotationAssertion>\n");
			  } catch (IOException e) { e.printStackTrace(); }
			}
			state.beforeFirst();
			while (state.next() && !state.getString("typeState").equals("NIL")) {
              if ((state.getString("typeState").equals("SOA"))||(state.getString("typeState").equals("BEL"))||(state.getString("typeState").equals("VAS"))||(state.getString("typeState").equals("NEG"))){
				String name = repository.get(String.valueOf(state.getInt("idState"))+"St");
				String annState = drammarManager.fixName(state.getString("printState"));
			    try {
				bw.write("\t<AnnotationAssertion>\n\t\t<AnnotationProperty abbreviatedIRI=\"dc:description"+"\"/>\n\t\t<IRI>#"+name+"</IRI>\n\t\t<Literal datatypeIRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral"+"\">"+annState+"\"</Literal>\n\t</AnnotationAssertion>\n");
			    } catch (IOException e) { e.printStackTrace(); }
			  }
			}
			try {
			bw.write(sCurrentLine + "\n");
			} catch (IOException e) { e.printStackTrace(); }
		  } // END sCurrentLine.contains("</Ontology>")

		  else { 
			try {
		    bw.write(sCurrentLine+ "\n"); //stampa il resto del file
			} catch (IOException e) { e.printStackTrace(); }
		  } 
		} // END WHILE sCurrentLine	
	    br.close();
	    bw.close();
	  } catch (IOException e) { e.printStackTrace(); } 
	  System.exit(0);
	} catch (SQLException sqle) {sqle.printStackTrace(); System.exit(1); }//stampa stacktrace ed esce segnalando errore
  } // END Main

} // END Class drammarManager