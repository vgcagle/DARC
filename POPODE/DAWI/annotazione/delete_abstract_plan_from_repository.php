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

//devo cercare tutti i piani a cui è associato l'agente per cancellarlo
$sql = "DELETE FROM Plan WHERE idPlan = $id_plan";
echo $sql;

$result = $conn->query($sql);

echo "<br/>";

$sql= "DELETE FROM SubplanOf WHERE idFatherPlan = $id_plan OR idChildPlan = $id_plan ";
echo $sql;


$result = $conn->query($sql);

?>