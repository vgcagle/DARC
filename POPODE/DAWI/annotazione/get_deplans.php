<?php


//questa è la get_plans per i piani direttamente eseguibili (de_plans) con la condizione che il piano sia tale nella query al db

//get_plans.php (no parametri)
//restituisce un array di agent, ogni agent è un array associativo con tutte le caratteristiche
include 'include/connessione.php';

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//prima recupero dalla tabella Unit la timeline della Unit in questione
$sql = "SELECT * FROM Plan WHERE typePlan = 'Base' ORDER BY `namePlan`";
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