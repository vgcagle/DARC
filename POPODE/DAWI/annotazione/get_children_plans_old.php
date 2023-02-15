<?php

//get_children_plans.php?plan=3
//restituisce un array di agent, ogni agent è un array associativo con tutte le caratteristiche
include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];



$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

$all_plans = array();
$children_plans = array();

$sql = "SELECT * FROM SubplanOf JOIN Plan ON idChildPlan = idPlan   WHERE idFatherPlan = $id_plan ORDER BY `order`";


$result = $conn->query($sql);

while ($row = $result->fetch_assoc()) {
	    // un array associativo per ogni piano
    	$plan = array();
        $plan["id"] = $row["idPlan"];
        $plan["print"] = utf8_encode($row["printPlan"]);
        $plan["name"] = utf8_encode($row["namePlan"]);
        $plan["type"] = $row["typePlan"];
        $plan["timeline"] = $row["Timeline_idTimeline"];
        $plan["agent"] = $row["Agent_idAgent"];
        $plan["goal"] = $row["Goal_idGoal"];
        $plan["action"] = $row["Action_idAction"];     
        $children_plans[] = $plan;  
    }

$all_plans["children"] = $children_plans;

$candidate_plans = array();

//estrai tutti i piani che non sono già figli di un qualche piano
//$sql = "SELECT * FROM SubplanOf JOIN Plan ON idChildPlan = idPlan  WHERE idFatherPlan != $id_plan";

$sql = "SELECT * FROM Plan LEFT OUTER JOIN SubplanOf ON idChildPlan = idPlan WHERE idFatherPlan IS NULL ORDER BY printPlan";

//echo $sql;

$result = $conn->query($sql);

while ($row = $result->fetch_assoc()) {
	    // un array associativo per ogni piano
    	$plan = array();
        $plan["id"] = $row["idPlan"];
        $plan["print"] = $row["printPlan"];
        $plan["name"] = $row["namePlan"];
        $plan["type"] = $row["typePlan"];
        $plan["timeline"] = $row["Timeline_idTimeline"];
        $plan["agent"] = $row["Agent_idAgent"];
        $plan["goal"] = $row["Goal_idGoal"];
        $plan["action"] = $row["Action_idAction"];     
        $candidate_plans[] = $plan;  
    }


$all_plans["candidate"] = $candidate_plans;


$conn->close();

//print_r ($plans);

echo json_encode($all_plans);


?>