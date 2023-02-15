<?php
//delete_action_from_repository.php?action=5
include 'include/connessione.php';

$id_action = $_REQUEST["action"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

//devo cercare tutti i piani a cui è associato l'azione per cancellarla
$sql = "SELECT idPlan FROM Plan WHERE Action_idAction = $id_action";
$result = $conn->query($sql);

//cancello l'azione da tutti piani trovati
while ($row = $result->fetch_assoc()) {
	$plan = $row["idPlan"];
	
	$sql = "UPDATE Plan SET Action_idAction = null WHERE idPlan = $plan ";
	$result2 = $conn->query($sql);	
}

$sql = "DELETE FROM Action WHERE idAction = $id_action";
$result = $conn->query($sql);


?>