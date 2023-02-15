<?php

//API
//parameters 
//agent
//new_name = nuovo nome dell'agente (nameAgent)
//new_description = nuova descrizione agent (printAgent)
//la modifica dei values è gestita da change_agent_values.php
//change_agent_features.php?new_name=pippo&agent=...
//new_liking
//new_pleasant

//change_unit_features.php?
include 'include/connessione.php';

$id_agent = $_REQUEST["agent"];

// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql ="";

//modifica nome
if (isset ($_REQUEST["new_name"])) {	
	$new_name = addslashes($_REQUEST["new_name"]);
	$sql = "UPDATE Agent SET nameAgent = '$new_name' WHERE idAgent = $id_agent ";
	$conn->query($sql);	
}

//modifica descrizione
if (isset ($_REQUEST["new_description"])) {	
	$new_description = addslashes($_REQUEST["new_description"]);
	$sql = "UPDATE Agent SET printAgent = '$new_description' WHERE idAgent = $id_agent ";	
	$conn->query($sql);
}

//modifica descrizione
if (isset ($_REQUEST["new_pleasant"])) {	
	$new_pleasant = addslashes($_REQUEST["new_pleasant"]);
	$sql = "UPDATE Agent SET pleasantAgent = $new_pleasant WHERE idAgent = $id_agent ";	
	$conn->query($sql);
}



/*
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }
*/

//modifica liking / pleasant
/*    
if (isset ($_REQUEST["new_pleasant"])) {	
	$type = "pleasant";
	$attitudes = $_REQUEST["new_pleasant"];
	}
*/	
if (isset ($_REQUEST["new_liking"])) {	
	$type = "liking";
	$attitudes = addslashes($_REQUEST["new_liking"]);
	}
if (isset ($_REQUEST["new_disliking"])) {	
	$type = "disliking";
	$attitudes = addslashes($_REQUEST["new_disliking"]);
	}	
	


if (isset ($_REQUEST["new_disliking"]) || isset ($_REQUEST["new_liking"])){

	$array_attitudes = explode(",", $attitudes);

	//Cancellazione del tipo giusto di attitude
	$sql = "DELETE FROM Attitude WHERE Attitude_idAgent = $id_agent AND typeAttitude = '$type'";
	
	$conn->query($sql);

	//nuove insert delle attitudes del tipo dato
	foreach ($array_attitudes as $a) {
		
		if ($a != ""){
			$a = trim($a);
			$sql = "INSERT INTO Attitude (`typeAttitude`, `Attitude_idAgent`,`objectAttitude`) VALUES ('$type', '$id_agent', '$a')";
			//echo $sql;
			$conn->query($sql);
			}
	}	
}



//echo "ok";

echo json_encode("ok");

?>