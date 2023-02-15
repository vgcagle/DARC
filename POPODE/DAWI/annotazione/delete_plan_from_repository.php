<?php
//delete_plan_from_repository.php?plan=7
include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");


// prima di cancellare il piano, lo cancello dal support e conflict set degli altri piani
$sql = "UPDATE Plan SET inConflictWithPlan = 0 WHERE inConflictWithPlan = $id_plan ";	
$conn->query($sql);	

$sql = "UPDATE Plan SET inSupportOfPlan = 0 WHERE inSupportOfPlan = $id_plan ";
$conn->query($sql);	

// occorrerebbe aggiornare anche order a voler fare le cose per bene!
$sql = "DELETE FROM SubplanOf WHERE idFatherPlan = $id_plan ";
$conn->query($sql);	

$sql = "DELETE FROM SubplanOf WHERE idChildPlan = $id_plan ";
$conn->query($sql);	


//ora cancello il piano
// NB i piani in questa versione del tool non hanno CSS come precondizioni e effetti
// gli effetti su value e in conflitti con altri piani sono nel Plan stesso.
$sql = "DELETE FROM Plan WHERE idPlan = $id_plan";
//echo $sql;




$result = $conn->query($sql);

echo json_encode(""); 


?>