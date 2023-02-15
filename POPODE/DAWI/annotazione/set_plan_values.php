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
$values = $_REQUEST["values"];
$relation = $_REQUEST["relation"];



//cancello tutti i values esistenti per il tipo di relazione per quel piano
$sql = "DELETE FROM Plan2Value WHERE Plan_idPlan = $id_plan AND relationType = '$relation' ";	
if ($conn->query($sql) === TRUE) {
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }


//ora inserisco quelli nuovi
$num_values = count($values);

for ($i = 0; $i < $num_values; $i++) {
		$value = $values[$i];
		$sql = "INSERT INTO `Plan2Value` (`Plan_idPlan`, `Value_idValue`, `relationType`) VALUES ($id_plan, $value, '$relation')";
		$conn->query($sql);
}





?>