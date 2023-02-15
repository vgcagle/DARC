<?php

//get_plan.php?plan=idPlan
//restituisce un array associativo con tutte le caratteristiche
//del piano tranne le precondizioni e gli effetti
include 'include/connessione.php';

$id_plan = $_REQUEST["plan"];

if ($id_plan == "Select plan") {die();}

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
$plan = array();
$plan["id"] = $row["idPlan"];
$plan["print"] = utf8_encode($row["printPlan"]);
$plan["name"] = utf8_encode($row["namePlan"]);
$plan["type"] = $row["typePlan"];
$plan_unit = $row["Timeline_idTimeline"];
$plan["timeline"] = $row["Timeline_idTimeline"];
$plan["agent"] = $row["Agent_idAgent"];
$plan["goal"] = $row["Goal_idGoal"];
$plan["action"] = $row["Action_idAction"];   
$plan["mappingInit"]= $row["mappingInit"];    
$plan["mappingEnd"]= $row["mappingEnd"];     
//aggiunti: accomplished, inConflictWithPlan, inSupportOfPlan, valueAtStakePlan, valueBalancedPlan
$plan["accomplished"]= $row["accomplished"]; 



//aggiornamento 4 maggio 2018
// leggo mapping init e mapping end al volo
$sql = "SELECT n.*  FROM Unit2Reference n  INNER JOIN (  SELECT Unit_idUnit, MIN(position) AS position  FROM Unit2Reference GROUP BY Unit_idUnit) AS max USING (Unit_idUnit, position) WHERE Unit_idUnit = $plan_unit";
//echo $sql;
//echo "<br>";

$result = $conn->query($sql);

if ($result->num_rows > 0){
	$row = $result->fetch_assoc(); 
	$plan["mappingInit"]= $row["idReferenceUnit"]; 
}

$sql = "SELECT n.*  FROM Unit2Reference n  INNER JOIN (  SELECT Unit_idUnit, MAX(position) AS position  FROM Unit2Reference GROUP BY Unit_idUnit) AS max USING (Unit_idUnit, position) WHERE Unit_idUnit = $plan_unit";
//echo $sql;
//echo "<br>";
$result = $conn->query($sql);

if ($result->num_rows > 0){
	$row = $result->fetch_assoc(); 
	$plan["mappingEnd"]= $row["idReferenceUnit"]; 
}


$conflict_set = array();
$support_set = array();


$sql = "SELECT * FROM Plan2Plan WHERE idPlan1 = $id_plan AND relationType = 'conflict_p'";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera il conflict set");

//echo $sql;
//echo "<br/>";
        
while ($riga = $result->fetch_assoc()) {
	$conflict_set[] = $riga["idPlan2"];
	}

$sql = "SELECT * FROM Plan2Plan WHERE idPlan2 = $id_plan AND relationType = 'conflict_p'";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera il conflict set");
//echo $sql;
//echo "<br/>";		 
        
while ($riga = $result->fetch_assoc()) {
	$conflict_set[] = $riga["idPlan1"];
	}



$sql = "SELECT * FROM Plan2Plan WHERE idPlan1 = $id_plan AND relationType = 'support_p'";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera il support set");

//echo $sql;
//echo "<br/>";
        
while ($riga = $result->fetch_assoc()) {
	$support_set[] = $riga["idPlan2"];
	}

/*
$sql = "SELECT * FROM Plan2Plan WHERE idPlan2 = $id_plan AND relationType = 'support_p'";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera il support set");

//echo $sql;
//echo "<br/>";		 
        
while ($riga = $result->fetch_assoc()) {
	$support_set[] = $riga["idPlan1"];
	}
*/	

$plan["inConflictWithPlan"]= $conflict_set; 
$plan["inSupportOfPlan"]= $support_set; 

/*
$plan["valueAtStakePlan_p"]= $row["valueAtStakePlan_p"]; 
$plan["valueBalancedPlan_p"]= $row["valueBalancedPlan_p"]; 
$plan["valueAtStakePlan_e"]= $row["valueAtStakePlan_e"]; 
$plan["valueBalancedPlan_e"]= $row["valueBalancedPlan_e"];   
*/

$values_stake_p = array();
$values_balanced_p = array();
$values_stake_e = array();
$values_balanced_e = array();


$sql = "SELECT * FROM Plan2Value WHERE Plan_idPlan = $id_plan AND relationType = 'conflict_p' ";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
        
while ($riga = $result->fetch_assoc()) {
	$values_stake_p[] = $riga["Value_idValue"];
	}

$sql = "SELECT * FROM Plan2Value WHERE Plan_idPlan = $id_plan AND relationType = 'support_p' ";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
        
while ($riga = $result->fetch_assoc()) {
	$values_balanced_p[] = $riga["Value_idValue"];
	}

$sql = "SELECT * FROM Plan2Value WHERE Plan_idPlan = $id_plan AND relationType = 'conflict_e' ";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
        
while ($riga = $result->fetch_assoc()) {
	$values_stake_e[] = $riga["Value_idValue"];
	}

$sql = "SELECT * FROM Plan2Value WHERE Plan_idPlan = $id_plan AND relationType = 'support_e' ";
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
        
while ($riga = $result->fetch_assoc()) {
	$values_balanced_e[] = $riga["Value_idValue"];
	}			

$plan["valueAtStakePlan_p"]= $values_stake_p; 
$plan["valueBalancedPlan_p"]= $values_balanced_p; 
$plan["valueAtStakePlan_e"]= $values_stake_e; 
$plan["valueBalancedPlan_e"]= $values_balanced_e; 



$conn->close();

echo json_encode($plan);


?>