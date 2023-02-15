<?php
//create_plan.php?name= ... &print= ... &type= ... &agent= ... &goal= ... &unit= ... &action
//create_plan.php?name=Piano+prova&print=guarda+che+bello&type=Base&agent=7&unit=5&action=5
//create_plan.php?name=Piano+prova&print=guarda+che+bello&type=Base&agent=7&unit=5&goal=1
//agent, goal, action e mapping sono opzionali

include 'include/connessione.php';

$name_plan = addslashes($_REQUEST["name"]);
$print_plan = addslashes($_REQUEST["print"]);
$type_plan = addslashes($_REQUEST["type"]);
	


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$sql_agent_field = "";
$sql_agent_value = "";
$sql_agent_field = "";
$sql_agent_value = "";
$sql_action_field = "";
$sql_action_value = "";


if (isset ($_REQUEST["agent"])){
	
	$sql_agent_field = ", `Agent_idAgent`";
	$sql_agent_value = ", '$_REQUEST[agent]'";
}

if (isset ($_REQUEST["goal"])){
	
	$sql_goal_field = ", `Goal_idGoal`";
	$sql_goal_value = ", '$_REQUEST[goal]'";
}

if (isset ($_REQUEST["action"])){

	if ($_REQUEST["action"] != 0) {

		$sql_action_field = ", `Action_idAction`";
		$sql_action_value = ", '$_REQUEST[action]'";
	
	}	

}


if (isset ($_REQUEST["unit"])){
	
	// per gestire questo, devo recuperare la timeline della unit
	$id_unit = $_REQUEST["unit"];
	$sql_timeline_field = "`Timeline_idTimeline`";
	$sql = "SELECT TimelineUnit FROM Unit WHERE idUnit = $id_unit";
	$result = $conn->query($sql);
	// output data of each row
	$row = $result->fetch_assoc();
	
    $id_timeline = $row["TimelineUnit"];
    
    $sql_timeline_field = ", `Timeline_idTimeline`";
	$sql_timeline_value = ", '$id_timeline'";
}


//creo subito il CSS precondizioni e effetti del piano, anche se vuoto
$CSS_prec = rand(1,5000);
$CSS_eff = rand(5001,10000);


$sql = 	"INSERT INTO Plan (`namePlan`, `printPlan`, `preconditionsPlan`, `effectsPlan`, `typePlan` $sql_agent_field $sql_goal_field $sql_action_field $sql_timeline_field)" . 
		"VALUES ('$name_plan', '$print_plan', $CSS_prec, $CSS_eff, '$type_plan' $sql_agent_value $sql_goal_value $sql_action_value $sql_timeline_value) ";
				
//echo $sql;


// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

//echo $last_id;

echo json_encode($last_id);

$conn->close();	
	 		


?>