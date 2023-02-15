<?php

include 'include/connessione.php';

// esempio di chiamata
// js: get_units(2,4);
// get_units.php?unit=2&timeline=4
		
$id_unit = $_REQUEST["unit"];
$id_timeline = $_REQUEST["timeline"];
$units = array();

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");
     
// la unit che attualmente precede/segue quella corrente (nella Timeline generale, quella con id =0 ) 
$sql1 = "SELECT * FROM (Unit JOIN Pair ON `idUnit` = precedesPair) WHERE (followsPair = $id_unit AND Timeline_idTimeline = 0)";
$sql2 = "SELECT * FROM (Unit JOIN Pair ON `idUnit` = followsPair) WHERE (precedesPair = $id_unit AND Timeline_idTimeline = 0)";	

if (!($result = $conn->query($sql1)))
		 	die("Non riesco a eseguire la query che recupera la unit precedente");
    
$riga =  $result->fetch_assoc();
$print = utf8_encode($riga["printUnit"]);
$pre_descr = utf8_encode($riga["nameUnit"]);
$units["pre"] = $print;
$units["pre_description"] = $pre_descr;

    	
if (!($result = $conn->query($sql2)))
		 	die("Non riesco a eseguire la query che recupera la unit successiva");
    
$riga =  $result->fetch_assoc();
$print = utf8_encode($riga["printUnit"]);
$eff_descr = utf8_encode($riga["nameUnit"]);
$is_reference = $riga["isReference"];
$units["eff"] = $print;  
$units["eff_description"] = $eff_descr; 
	

echo json_encode($units);

    	      
?>