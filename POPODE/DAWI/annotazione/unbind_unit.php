<?php

include 'include/connessione.php';

/*
rimuove da una unit il riferimento a una lista di reference units
chiamata dal pulsante delete
*/


$unit = $_REQUEST["unit"];
$bound_units = $_REQUEST["bound"];




// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("utf8");

//per ogni unit
//leggo il numero d'ordine della unit
//faccio slittare indietro tutte le unit dopo quella posizione

$end = count($bound_units);
//echo $end;

for ($i=0; $i < $end; $i++){
	
	//echo "entro qui";

	$to_del = $bound_units[$i];
	$sql = "SELECT position FROM Unit2Reference WHERE idReferenceUnit = $to_del AND Unit_idUnit = $unit";
	//echo $sql;
	$result = $conn->query($sql); 
	$riga = $result->fetch_assoc();
	$pos = $riga["position"];

	$sql = "DELETE FROM Unit2Reference WHERE idReferenceUnit = $to_del AND Unit_idUnit = $unit";
	//echo $sql;
	$conn->query($sql); 

	$sql = "UPDATE Unit2Reference SET `position` =  `position` - 1 WHERE Unit_idUnit = $unit and `position` > $pos";
	//echo $sql;
	//echo $sql;

	$conn->query($sql);

}


$conn->close();	

echo "ok";




?>