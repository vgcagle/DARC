<?php

//detach_state_from_plan.php?plan=3&state=1700&flag=eff

include 'include/connessione.php';

include 'include/connessione.php';


$id_plan = $_REQUEST["plan"];
$id_state = $_REQUEST["state"];

// flag per gestire precondizioni effetti
if ($_REQUEST["flag"] == "pre") {$field = "preconditionsPlan"; }
if ($_REQUEST["flag"] == "eff") {$field = "effectsPlan"; }




// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql = "SELECT $field FROM Plan WHERE idPlan = $id_plan";


// prima ricavo il CCS a cui lo stato appartiene prendendolo dalla tabella del piano

$result = $conn->query($sql);

$row = $result->fetch_assoc();
$id_CSS = $row[$field];


$sql = "DELETE FROM `CSSPlan` WHERE `Set` = $id_CSS AND `idState` = $id_state ";

$result = $conn->query($sql);

$conn->close();

?>