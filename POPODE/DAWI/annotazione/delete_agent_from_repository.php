<?php
//delete_agent_from_repository.php?agent=7
include 'include/connessione.php';

$id_agent = $_REQUEST["agent"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

//devo cercare tutti i piani a cui è associato l'agente per cancellarlo
$sql = "SELECT idPlan FROM Plan WHERE Agent_idAgent = $id_agent";
$result = $conn->query($sql);

//cancello l'agente da tutti piani trovati
while ($row = $result->fetch_assoc()) {
	$plan = $row["idPlan"];
	
	$sql = "UPDATE Plan SET Agent_idAgent = null WHERE idPlan = $plan ";
	$result2 = $conn->query($sql);	
}

$sql = "DELETE FROM Agent WHERE idAgent = $id_agent";
$result = $conn->query($sql);



// ottengo la lista dei valori dell'agente per cancellarli dai piani
$sql = "SELECT idValue FROM Value WHERE Agent_idAgent = $id_agent";
echo $sql;
$result = $conn->query($sql);


while ($row = $result->fetch_assoc()) {
	$value = $row["idValue"];
	
	
	// cancello i valori dell'agente dai piani
	$sql = "UPDATE Plan SET valueAtStakePlan_p = 0 WHERE valueAtStakePlan_p = $value ";	
	$conn->query($sql);	
	$sql = "UPDATE Plan SET valueBalancedPlan_p = 0 WHERE valueBalancedPlan_p = $value ";
	$conn->query($sql);	
	$sql = "UPDATE Plan SET valueAtStakePlan_e = 0 WHERE valueAtStakePlan_e = $value ";	
	$conn->query($sql);	
	$sql = "UPDATE Plan SET valueBalancedPlan_e = 0 WHERE valueBalancedPlan_e = $value ";
	$conn->query($sql);	

	// cancello i valori dell'agente dalle unit
	$sql = "UPDATE State SET idValueAtStake = 0 WHERE idValueAtStake = $value";
	$conn->query($sql);
	$sql = "UPDATE State SET idValueBalanced = 0 WHERE idValueBalanced = $value";
	$conn->query($sql);
}


//CANCELLO TUTTI I VALUE DELL'AGENTE
$sql = "DELETE FROM Value WHERE Agent_idAgent = $id_agent";
$result = $conn->query($sql);

// cancello attitudes (pleasant e liking)
$sql = "DELETE FROM Attitude WHERE Attitude_idAgent = $id_agent";
$result = $conn->query($sql);



?>