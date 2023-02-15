<?php

//map_plan_unit.php?plan=3&unit=5&flag=

include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];
$id_unit = $_REQUEST["unit"];
if ($_REQUEST["flag"] == 'init') {$flag = "mappingInit";}
if ($_REQUEST["flag"] == 'end') {$flag = "mappingEnd";}


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

//prima recupero dalla tabella Unit la timeline della Unit in questione
/*
$sql = "SELECT TimelineUnit FROM Unit WHERE idUnit = $id_unit";
$result = $conn->query($sql);


// output data of each row
$row = $result->fetch_assoc();
$id_timeline = $row["TimelineUnit"];
*/    

// ora inserisco la timeline nella tabella del piano
//$sql = "UPDATE Plan SET `Action_idAction` = '$id_action' WHERE idPlan = $idPlan ";

$sql = "UPDATE Plan SET $flag = $id_unit WHERE idPlan = $id_plan ";

//echo $sql;

$conn->query($sql);

$conn->close();	


?>