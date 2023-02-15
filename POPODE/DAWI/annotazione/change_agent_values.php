<?php


//change_agent_values.php
include 'include/connessione.php';

$id_agent = $_REQUEST["agent"];
$values = addslashes($_REQUEST["new_values"]);

// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");


// 1. cancello i vecchi valori dove erano atstake o balanced
// CI VUOLE UN BEL JOIN 
// valueAtStakePlan 
// valueBalancedPlan

/*
EX:
DELETE prodotti.*
FROM prodotti INNER JOIN ripiani
ON prodotti.ripiano_id = ripiani.id
WHERE ripiani.scaffale_id = 1;
*/

//UPDATE `Plan` SET `valueAtStakePlan` = '1' WHERE `plan`.`idPlan` = 128;
/*
update ud u
inner join sale s on
    u.id = s.udid
set u.assid = s.assid
*/
 
//questo è il JOIN normale per le tre tabelle (Agent, Value, Plan) 
//SELECT * FROM Plan INNER JOIN (Value INNER JOIN Agent ON Agent.idAgent = Value.Agent_idAgent) ON valueAtStakePlan = Value.idValue WHERE Value.Agent_idAgent = 110

// 21/11/16 aggiornato con tutti i campi del piano, inclusi quelli aggiunti in precondizioni e effetti
// azzero i values nei piani che fanno riferimento ai valori del'agente dato
$sql = "UPDATE Plan INNER JOIN (Value INNER JOIN  Agent ON Agent.idAgent = Value.Agent_idAgent) ON valueAtStakePlan_p = Value.idValue   SET valueAtStakePlan_p = 0 WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);

$sql = "UPDATE Plan INNER JOIN (Value INNER JOIN  Agent ON Agent.idAgent = Value.Agent_idAgent) ON valueAtStakePlan_e = Value.idValue   SET valueAtStakePlan_e = 0 WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);

$sql = "UPDATE Plan INNER JOIN (Value INNER JOIN  Agent ON Agent.idAgent = Value.Agent_idAgent) ON valueBalancedPlan_p = Value.idValue   SET valueBalancedPlan_p = 0 WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);

$sql = "UPDATE Plan INNER JOIN (Value INNER JOIN  Agent ON Agent.idAgent = Value.Agent_idAgent) ON valueBalancedPlan_e = Value.idValue   SET valueBalancedPlan_e = 0 WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);

// TODO azzero i values anche nelle unit
$sql = "UPDATE State INNER JOIN (Value INNER JOIN Agent Agent.idAgent = Value.Agent_idAgent) ON idValueAtStake =  Value.idValue SET  idValueAtStake = 0  WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);

// TODO azzero i values anche nelle unit
$sql = "UPDATE State INNER JOIN (Value INNER JOIN Agent Agent.idAgent = Value.Agent_idAgent) ON idValueBalanced =  Value.idValue SET  idValueBalanced = 0  WHERE Value.Agent_idAgent = $id_agent";
$conn->query($sql);


// $sql = "DELETE Value.* FROM Value INNER JOIN Agent ON Agent.idAgent = Value.Agent_idAgent WHERE Agent_idAgent = $id_agent ";
// $conn->query($sql);


// 2. poi cancello vecchi valori
$sql = "DELETE FROM Value WHERE Agent_idAgent = $id_agent";
$conn->query($sql);


// 3.infine inserisco i nuovi valori
if ($values != "type values separated by comma") {
	
	$array_values = explode(",", $values);
	
	foreach ($array_values as $v) {
		
		if ($v != ""){
	
			$v = trim($v);
			$sql = "INSERT INTO Value (`nameValue`, `Agent_idAgent`) VALUES ('$v', '$id_agent')";
			//echo $sql;
			$conn->query($sql);
			}
	}	
}

echo json_encode("ok");


?>