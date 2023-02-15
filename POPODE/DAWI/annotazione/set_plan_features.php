<?php

//set_plan_features.php
include 'include/connessione.php';

/* 
API

plan=

più uno dei seguenti

accomplished=
confl=
supp=
stake_p=
bal_p=
stake_e=
bal_e=
new_name=
new_description=


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

//accomplished or not
if (isset ($_REQUEST["accomplished"])) {	
	$accomp = $_REQUEST["accomplished"];
	$sql = "UPDATE Plan SET accomplished = '$accomp' WHERE idPlan = $id_plan ";	
}

//conflict with other plans
if (isset ($_REQUEST["confl"])) {	
	$confl = $_REQUEST["confl"];
	$sql = "UPDATE Plan SET inConflictWithPlan = '$confl' WHERE idPlan = $id_plan ";	
}


//in support of other plans
if (isset ($_REQUEST["supp"])) {	
	$supp = $_REQUEST["supp"];
	$sql = "UPDATE Plan SET inSupportOfPlan = '$supp' WHERE idPlan = $id_plan ";	
}

//mapping init
if (isset ($_REQUEST["mapping_init"])) {	
	$mapping_init = $_REQUEST["mapping_init"];
	$sql = "UPDATE Plan SET mappingInit = '$mapping_init' WHERE idPlan = $id_plan ";	
}

//mapping end
if (isset ($_REQUEST["mapping_end"])) {	
	$mapping_end = $_REQUEST["mapping_end"];
	$sql = "UPDATE Plan SET mappingEnd = '$mapping_end' WHERE idPlan = $id_plan ";	
}


// modificata 
// ora i valori sono gestiti da set_plan_values.php

/*
if (isset ($_REQUEST["stake_p"])) {	
	$stake_p = $_REQUEST["stake_p"];
	$sql = "UPDATE Plan SET valueAtStakePlan_p = '$stake_p' WHERE idPlan = $id_plan ";	
}



if (isset ($_REQUEST["bal_p"])) {	
	$bal_p = $_REQUEST["bal_p"];
	$sql = "UPDATE Plan SET valueBalancedPlan_p = '$bal_p' WHERE idPlan = $id_plan ";	
}


if (isset ($_REQUEST["stake_e"])) {	
	$stake_e = $_REQUEST["stake_e"];
	$sql = "UPDATE Plan SET valueAtStakePlan_e = '$stake_e' WHERE idPlan = $id_plan ";	
}


if (isset ($_REQUEST["bal_e"])) {	
	$bal_e = $_REQUEST["bal_e"];
	$sql = "UPDATE Plan SET valueBalancedPlan_e = '$bal_e
	' WHERE idPlan = $id_plan ";	
}
*/

if (isset ($_REQUEST["new_name"])) {	
	$new_name = addslashes($_REQUEST["new_name"]);
	$sql = "UPDATE Plan SET namePlan = '$new_name' WHERE idPlan = $id_plan ";	
}

if (isset ($_REQUEST["new_description"])) {	
	$new_description = addslashes($_REQUEST["new_description"]);
	$sql = "UPDATE Plan SET printPlan = '$new_description' WHERE idPlan = $id_plan ";	
}	

if (isset ($_REQUEST["new_action"])) {	
	$action = $_REQUEST["action"];
	$new_action = addslashes($_REQUEST["new_action"]);
	$sql = "UPDATE Action SET nameAction = '$new_action' WHERE idAction = $action";	
}	

if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }

echo json_encode("ok");


?>