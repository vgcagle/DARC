<?php

include 'include/connessione.php';

//$id_unit = $_REQUEST["unit"];
//$id_timeline = $_REQUEST["timeline"];
$candidate_units = array();


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

// prendo tutte le unit

$sql = "SELECT * FROM Unit ORDER BY `printUnit`";

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera la unit precedente");
       
while ($riga = $result->fetch_assoc()) {
			$dati_unit = array();
			$print_candidate = utf8_encode($riga["printUnit"]);
			$id_candidate = $riga["idUnit"];    
			$dati_unit["id"] = $id_candidate;
			$dati_unit["print"] = $print_candidate;			
			$candidate_units[] = $dati_unit;  			
    	}     

echo json_encode($candidate_units);    	
    	
?>    