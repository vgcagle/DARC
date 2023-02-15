<?php
//delete_agent_from_repository.php?goal=3
include 'include/connessione.php';

$id_goal = $_REQUEST["goal"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

//devo cercare tutti i piani a cui è associato l'agente per cancellarlo
$sql = "SELECT idPlan FROM Plan WHERE Goal_idGoal = $id_goal";
$result = $conn->query($sql);

//cancello il goal da tutti piani trovati
while ($row = $result->fetch_assoc()) {
	$plan = $row["idPlan"];
	
	$sql = "UPDATE Plan SET Goal_idGoal = null WHERE idPlan = $plan ";
	$result2 = $conn->query($sql);	
}


$sql = "DELETE FROM Goal WHERE idGoal = $id_goal";
$result = $conn->query($sql);

?>