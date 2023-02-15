<?php


//get_unit_plans.php?Unit=idUnit
//restituisce (un array di) piani associati a una unit
//cioè che hanno mapping init e mapping end associati alla unit in input
//esempio:
//[{"id":"120","print":"guarda che bello","name":"Piano prova 2","type":"Base","timeline":"5","agent":"1","goal":"45","action":null}]

include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//recupero dalla tabella Plan i piani mappati sulla unit corrente
//SELECT * FROM Plan WHERE `mappingInit` = $id_unit AND `mappingEnd` = $id_unit
//$sql = "SELECT * FROM Plan WHERE mappingInit = $id_unit AND mappingEnd = $id_unit";
$sql = "SELECT * FROM Plan WHERE `Timeline_idTimeline` = $id_unit ORDER BY `namePlan`";
//`Timeline_idTimeline`

$result = $conn->query($sql);

$result = $conn->query($sql);

$plans = array();

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
        $plans[] = $plan;  
    }

$conn->close();

//print_r ($plans);

echo json_encode($plans);


?>



