<?php

//add_state2plan.php?state=1700&plan=3&flag=pre

//lo stato non si aggiunge al piano ma si aggiunge al CSSPlan che è la precondizione/fffetto del piano
//trovare prima il CSS


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


$result = $conn->query($sql);

$row = $result->fetch_assoc();
$id_CSS = $row[$field];

$sql = "INSERT INTO `CSSPlan` (`Set`,`idState`) VALUES ($id_CSS, '$id_state')";



$result = $conn->query($sql);

?>