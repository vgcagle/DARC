<?php

//API
//parameters 
//goal
//new_name = nuovo nome dell'agente (nameAgent)
//new_description = nuova descrizione agent (printAgent)
//change_goal_features.php?new_name=pippìi&goal=...

include 'include/connessione.php';

$id_goal = $_REQUEST["goal"];

// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql ="";


if (isset ($_REQUEST["new_name"])) {	
	$new_name = addslashes($_REQUEST["new_name"]);
	$sql = "UPDATE Goal SET nameGoal = '$new_name' WHERE idGoal = $id_goal ";	
	//echo $sql;
	$conn->query($sql);
}


if (isset ($_REQUEST["new_description"])) {	
	$new_description = addslashes($_REQUEST["new_description"]);
	$sql = "UPDATE Goal SET printGoal = '$new_description' WHERE idGoal = $id_goal ";	
	$conn->query($sql);
}




echo json_encode("ok");

?>