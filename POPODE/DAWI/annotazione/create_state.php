<?php

include 'include/connessione.php';


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");



if (isset($_REQUEST["balanced"])) {
	$column = "idValueBalanced";
	$value = $REQUEST["balanced"];
}

if (isset($_REQUEST["at_stake"])) {
	$column = "idValueAtStake";
	$value = $REQUEST["at_stake"];
}

//IF per gestire il caso particolare dei VAS nelle precondizioni e effetti delle unit
if (isset($_REQUEST["balanced"]) || isset($_REQUEST["at_stake"])) {
	//query per stati con VAS
	//per ora, tutti gli altri campi sono messi a '' per evitare problemi con i null values sul server Altervista
	//optionals: at_stake, balanced (only one)
	//create_state.php?at_stake=70
	$sql = "INSERT INTO State (typeState, printState, statusState, nameState, $column) VALUES ('VAS', '', 0, '', '$value')";
	}

	else {
			//create_state.php?type=...&status=...&print=
			$name_state = $_REQUEST["id"];
			$type = $_REQUEST["type"];
			$status = $_REQUEST["status"];
			$print = $_REQUEST["print"];
		 //query generica senza VAS
		 //"INSERT INTO `DrammarAnnotation`.`State` (`typeState`, `printState`, `statusState`) VALUES ('VAS', 'Valore in gioco di Ofelia', '0');"
		 $sql = "INSERT INTO State (typeState, printState, statusState, nameState) VALUES ('$type', '$print', $status, '$name_state')";
		 }

// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $id_state = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}


		 	
// mi restituisco l'id dello stato creato
echo json_encode($value); 		 	
		 	

		 	



?>