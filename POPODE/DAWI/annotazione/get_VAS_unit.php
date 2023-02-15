<?php


include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];
$id_timeline_unit = $_REQUEST["timeline"];
//$flag = $_REQUEST["flag"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

$array_dati = array();


// PER VAS DEVO OTTENERE UNA FUNZIONE CHE SELEZIONI SOLO GLI STATI ETICHETTATI COME VAS 
// E CHE RECUPERI LA LISTA DEI VAS PER PREC E EFFETTI


$array_VAS_before = array();

//JOIN su Timeline e CSSTimeline su preconditionsTimeline e poi su State 
$sql = "SELECT DISTINCT nameValue, idValue, State.idState, Agent_idAgent FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake)" . 
"JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit AND typeState = 'VAS'";    

//echo $sql;     
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake )  JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced )  JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera i valori at stake prima");
        
while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];
			$id_agent = $riga["Agent_idAgent"];   
			$id_value = utf8_encode($riga["idValue"]);	
			$name_value =  utf8_encode($riga["nameValue"]);  
			$dati_value = array("id_state" => $id_state, "id_agent"=>$id_agent, "id_value" => $id_value, "name_value" => $name_value); 
			$array_VAS_before[] = $dati_value;
}  

$array_VAB_before = array();

//JOIN su Timeline e CSSTimeline su preconditionsTimeline e poi su State 
$sql = "SELECT DISTINCT nameValue, idValue, State.idState, Agent_idAgent FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced)" . 
"JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit AND typeState = 'VAS'";    


//echo $sql;     
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake )  JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced )  JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera i valori bilanciati prima");
        
while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];
			$id_agent = $riga["Agent_idAgent"];   
			$id_value = utf8_encode($riga["idValue"]);	
			$name_value =  utf8_encode($riga["nameValue"]);  
			$dati_value = array("id_state" => $id_state, "id_agent"=>$id_agent, "id_value" => $id_value, "name_value" => $name_value); 
			$array_VAB_before[] = $dati_value;
}  


$array_VAS_after = array();

//JOIN su Timeline e CSSTimeline su preconditionsTimeline e poi su State 
$sql = "SELECT DISTINCT nameValue, idValue, State.idState, Agent_idAgent FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake)" . 
"JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit AND typeState = 'VAS'";    

//echo $sql;     
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake )  JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced )  JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera i valori at stake dopo");
        
while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];
			$id_agent = $riga["Agent_idAgent"];   
			$id_value = utf8_encode($riga["idValue"]);	
			$name_value =  utf8_encode($riga["nameValue"]);  
			$dati_value = array("id_state" => $id_state, "id_agent"=>$id_agent, "id_value" => $id_value, "name_value" => $name_value); 
			$array_VAS_after[] = $dati_value;
}  


$array_VAB_after = array();

//JOIN su Timeline e CSSTimeline su preconditionsTimeline e poi su State 
$sql = "SELECT DISTINCT nameValue, idValue, State.idState, Agent_idAgent FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced)" . 
"JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit AND typeState = 'VAS'";    

//echo $sql;     
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueAtStake )  JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";
//"SELECT * FROM (Value JOIN (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) ON idValue = idValueBalanced )  JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = 2 AND typeState = 'VAS'";

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera i valori balanced dopo");
        
while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];
			$id_agent = $riga["Agent_idAgent"];   
			$id_value = utf8_encode($riga["idValue"]);	
			$name_value =  utf8_encode($riga["nameValue"]);  
			$dati_value = array("id_state" => $id_state, "id_agent"=>$id_agent, "id_value" => $id_value, "name_value" => $name_value); 
			$array_VAB_after[] = $dati_value;
}  


$array_dati ["VAS_before"] = $array_VAS_before;
$array_dati ["VAB_before"] = $array_VAB_before;
$array_dati ["VAS_after"] = $array_VAS_after;
$array_dati ["VAB_after"] = $array_VAB_after;

echo json_encode($array_dati); 


?>