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

/********CLASSE DRAMMAR MANAGER******/
public class DBparse {		
	
  //inizializza la connessione al DB
  public Connection initConnection(String dbServer, String dbPort, String dbName, String dbUser, String dbPassword) {
  	Connection conn = null;
	try {
	  Class.forName("com.mysql.jdbc.Driver");			
	  conn = DriverManager.getConnection("jdbc:mysql://" + dbServer  + ":" + dbPort + "/" + dbName, dbUser, dbPassword);
	} catch (Exception e) {
	  System.err.println("Problemi nella connessione al database");
	  e.printStackTrace();
	}
	return conn;
  }	
  //termina la connessione al DB
  public void terminateConnection(Connection conn) {
	try {
	  conn.commit();
	  conn.close();
	} catch (Exception e) {}		
	conn = null;
  }	
  //metodo che estrae tutti i record della tabella Action
  public ResultSet retrieveAction(Connection conn) throws SQLException {
	String query = "SELECT idAction, printAction, Unit_idUnit, nameAction FROM Action;";			
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);
	return rs;
  }			
  //metodo che estrae tutti i record della tabella Agent
  public ResultSet retrieveAgent(Connection conn) throws SQLException {
	String query = "SELECT idAgent, printAgent, nameAgent, pleasantAgent FROM Agent;";		
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);
	return rs;
  }		
  //metodo che estrae tutti i record della tabella Attitude
  public ResultSet retrieveAttitude(Connection conn) throws SQLException {
	String query = "SELECT idAttitude, Attitude_idAgent, objectAttitude, typeAttitude FROM Attitude;";		
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);			
	return rs;
  }
		
  //metodo che estrae tutti i record della tabella Goal
  public ResultSet retrieveGoal(Connection conn) throws SQLException {
	String query = "SELECT idGoal, printGoal, nameGoal FROM Goal;";			
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);		
	return rs;
  }				
  //metodo che estrae tutti i record della tabella CSSPlan
  public ResultSet retrieveCSSPlan(Connection conn) throws SQLException {
	// String query = "SELECT id, SetPlan, idState FROM CSSPlan;";
	String query = "SELECT id, `Set`, idState FROM CSSPlan;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }								
  //metodo che estrae tutti i record della tabella CSSTimeline
  public ResultSet retrieveCSSTimeline(Connection conn) throws SQLException {
	// String query = "SELECT id, SetTimeline, printSet, idState FROM CSSTimeline;";				
	String query = "SELECT id, `Set`, printSet, idState FROM CSSTimeline;";				
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }							
  //metodo che estrae tutti i record della tabella Pair
  public ResultSet retrievePair(Connection conn) throws SQLException {
	String query = "SELECT idPair, precedesPair, followsPair, Timeline_idTimeline FROM Pair;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);			
	return rs;
  }									
  //metodo che estrae tutti i record della tabella Plan
  public ResultSet retrievePlan(Connection conn) throws SQLException {
    String query = "SELECT idPlan, printPlan, Agent_idAgent, Goal_idGoal, Action_idAction, Timeline_idTimeline, typePlan, preconditionsPlan, effectsPlan, namePlan, mappingInit, mappingEnd, accomplished, inConflictWithPlan, inSupportOfPlan, valueAtStakePlan_p, valueBalancedPlan_p, valueAtStakePlan_e, valueBalancedPlan_e FROM Plan;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }												
  //metodo che estrae tutti i record della tabella State
  public ResultSet retrieveState(Connection conn) throws SQLException {
	String query = "SELECT idState, typeState, printState, statusState, nameState, idValueAtStake, idValueBalanced FROM State;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }						
  //metodo che estrae tutti i record della tabella Unit
  public ResultSet retrieveUnit(Connection conn) throws SQLException {
	String query = "SELECT idUnit, printUnit, TimelineUnit, nameUnit, isReference FROM Unit;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);						
	return rs;
  }
  //metodo che estrae tutti i record della tabella Unit2Reference
  public ResultSet retrieveUnit2Reference(Connection conn) throws SQLException {
	String query = "SELECT idUnit2Reference, Unit_idUnit, idReferenceUnit, position FROM Unit2Reference;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);						
	return rs;
  }
  //metodo che estrae tutti i record della tabella SubplanOf
  public ResultSet retrieveSubplanOf(Connection conn) throws SQLException {
	// String query = "SELECT idSubplanOf, idFatherPlan, idChildPlan, orderPlan FROM SubplanOf;";
	String query = "SELECT idSubplanOf, idFatherPlan, idChildPlan, `order` FROM SubplanOf;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }
  //metodo che estrae tutti i record della tabella Timeline
  public ResultSet retrieveTimeline(Connection conn) throws SQLException {
    String query = "SELECT idTimeline, effectsTimeline, preconditionsTimeline FROM Timeline;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);				
	return rs;
  }		
  //metodo che estrae tutti i record della tabella Value
  public ResultSet retrieveValue(Connection conn) throws SQLException {
	String query = "SELECT idValue, nameValue, descriptionValue, Agent_idAgent FROM Value;";
	Statement st = conn.createStatement();
	ResultSet rs = st.executeQuery(query);						
	return rs;
  }
} // end class DBparse