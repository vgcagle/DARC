<?php

include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];

		
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//per ogni action mi servono print e id 
//11/7/2016 modificato
//le azioni candidate sono solo quelle che non sono associate a NESSUNA unit.

$sql = "SELECT * FROM Action WHERE Unit_idUnit = $id_unit";
//echo $sql;
        
$result = $conn->query($sql);
  		
$actions = array();   
while ($riga = $result->fetch_assoc()) {		
		$id_action = $riga["idAction"];   
		$print_action = utf8_encode($riga["nameAction"]); 
		$array_dati = array();
		$array_dati["id"]= $id_action; 
		$array_dati["print"]= $print_action; 
		$actions[] = $array_dati;
    }

$sql = "SELECT * FROM Action WHERE Unit_idUnit IS NULL";
        
$result = $conn->query($sql);

$candidate_actions = array();
while ($riga = $result->fetch_assoc()) {
		
		$id_action = $riga["idAction"];   
		$print_action = utf8_encode($riga["nameAction"]); 
		$array_dati = array();
		$array_dati["id"]= $id_action; 
		$array_dati["print"]= $print_action; 
		$candidate_actions[] = $array_dati;
    }

$array_dati = array ("actions" => $actions, "candidate" => $candidate_actions);


echo json_encode($array_dati); 

?>