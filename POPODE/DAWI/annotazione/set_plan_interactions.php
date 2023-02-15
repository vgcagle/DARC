<?php

//set_plan_features.php
include 'include/connessione.php';

/* 
API

plan=
values= array
relation={support_p, support_p, conflict_e, support_e}


*/

// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql ="";

$id_plan = $_REQUEST["plan"];
$plans = $_REQUEST["confl"];
$relation = $_REQUEST["relation"];



//cancello tutti i values esistenti per il tipo di relazione per quel piano
$sql = "DELETE FROM Plan2Plan WHERE (idPlan1 = $id_plan OR idPlan2 = $id_plan) AND relationType = '$relation' ";	
if ($conn->query($sql) === TRUE) {
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }

echo $sql;

//ora inserisco quelli nuovi
$num_plans = count($plans);

for ($i = 0; $i < $num_plans; $i++) {
		$p = $plans[$i];
		$sql = "INSERT INTO `Plan2Plan` (`idPlan1`, `idPlan2`, `relationType`) VALUES ($id_plan, $p, '$relation')";
		//echo $sql;
		$conn->query($sql);
}








?>