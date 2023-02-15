<?php

//parameters 
//new_name = nuovo titolo (printUnit)
//new_description = nuova descrizione (nameUnit)
//change_unit_features.php?new_name=pippo&unit=1

//change_unit_features.php?
include 'include/connessione.php';

// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql ="";

$id_unit = $_REQUEST["unit"];


if (isset ($_REQUEST["new_name"])) {	
	$new_name = addslashes($_REQUEST["new_name"]);
	$sql = "UPDATE Unit SET printUnit = '$new_name' WHERE idUnit = $id_unit";	
}


if (isset ($_REQUEST["new_description"])) {	
	$new_description = addslashes($_REQUEST["new_description"]);
	$sql = "UPDATE Unit SET nameUnit = '$new_description' WHERE idUnit = $id_unit";	
}	

//eseguo la query
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }

//echo "ok";

echo json_encode("ok");

?>