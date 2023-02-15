<?php

//get_plan.php?plan=idPlan
//restituisce un array associativo con tutte le caratteristiche
//del piano tranne le precondizioni e gli effetti
include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];

$plan = array();

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//prima recupero dalla tabella Unit la timeline della Unit in questione
$sql = "SELECT * FROM Plan WHERE idPlan = $id_plan";
$result = $conn->query($sql);

$row = $result->fetch_assoc(); 
// un array associativo per ogni piano

// agent
// goal
// mapping init
// mapping end
// accomplished

$id_agent = $row["Agent_idAgent"];
$id_goal = $row["Goal_idGoal"];
$id_mapping_init = $row["mappingInit"];
$id_mapping_end = $row["mappingEnd"];


$plan["id"] = $row["idPlan"];
$plan["print"] = utf8_encode($row["printPlan"]);
$plan["name"] = utf8_encode($row["namePlan"]);
$plan["type"] = $row["typePlan"];
$plan["timeline"] = $row["Timeline_idTimeline"];

$id_accomplished =  $row["accomplished"]; 
if ($id_accomplished == 1) {$plan["accomplished"] = "yes";} else {$plan["accomplished"] = "no";}


// ora recupero agent, goal, mappingInit e mappingEnd usando le chiavi esterne

if ($id_agent != ""){
$sql_agent = "SELECT nameAgent FROM Agent  WHERE idAgent = $id_agent";
$result = $conn->query($sql_agent);
$row = $result->fetch_assoc(); 
$plan["agent"] = utf8_encode($row["nameAgent"]);} else {$plan["agent"] = "";} 

if ($id_goal != ""){
$sql_goal = "SELECT nameGoal FROM Goal  WHERE idGoal = $id_goal";
$result = $conn->query($sql_goal);
$row = $result->fetch_assoc(); 
$plan["goal"] = utf8_encode($row["nameGoal"]);} else {$plan["goal"] = "";}


if ($id_mapping_init != ""){
$sql_mapping_init = "SELECT printUnit FROM Unit  WHERE idUnit = $id_mapping_init";
$result = $conn->query($sql_mapping_init);
$row = $result->fetch_assoc(); 
$plan["mappingInit"] = utf8_encode($row["printUnit"]);} else {$plan["mappingInit"] = "";}


if ($id_mapping_end != ""){
$sql_mapping_end = "SELECT printUnit FROM Unit  WHERE idUnit = $id_mapping_end";
//echo $sql_mapping_end;
$result = $conn->query($sql_mapping_end);
$row = $result->fetch_assoc(); 
$plan["mappingEnd"] = utf8_encode($row["printUnit"]);} else {$plan["mappingEnd"] = "";}

    

$conn->close();

echo json_encode($plan);


?>