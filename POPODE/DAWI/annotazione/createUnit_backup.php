<?php

include 'include/connessione.php';

//create_state.php?id= ...&type=...&status=...&print=
$print = addslashes($_REQUEST["print"]);
$description = addslashes($_REQUEST["description"]);



// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

// creazione della timeline della unit (ogni unit ha una timeline che contiene solo lei)
// creo due id, per effetti e precond della unit sulla sua timeline
$id_timeline_effects = rand(10000,20000);
$id_timeline_preconditions = rand(20000,30000);		 	
	 	
// creo la nuova timeline della unit con precondizioni e effetti	 	
$sql = "INSERT INTO Timeline (effectsTimeline, preconditionsTimeline) VALUES ($id_timeline_effects,  $id_timeline_preconditions)";
//echo $sql;


// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$id_timeline_unit = $last_id;


// metto la unit nuova nella tabella Unit
$sql = "INSERT INTO Unit (printUnit, TimelineUnit, nameUnit) VALUES ('$print', $id_timeline_unit, '$description')";
//echo $sql;

	 	
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$id_unit = $last_id;
 	
echo $id_unit;


$conn->close();	


?>