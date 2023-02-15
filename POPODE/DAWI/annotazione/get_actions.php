<?php

include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];

		
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//per ogni action mi servono print e id 

// cerco le action del piano
$sql = "SELECT * FROM Plan JOIN Action ON  Action_idAction = idAction WHERE idPlan = $id_plan ORDER BY nameAction";
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


//echo "<br/>";

// ora cerco le action che non fanno già parte del piano (in questo momento le prendo tutte)
$sql = "SELECT * FROM Action";
//echo $sql;      
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