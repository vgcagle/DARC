<?php

include 'include/connessione.php';
//get_state_features.php?state=5&timeline=4$flag=eff

$id_state = $_REQUEST["state"];
$id_timeline_unit = $_REQUEST["timeline"];
//$flag = $_REQUEST["flag"];

if ($_REQUEST["flag"] == "pre") {$field = "Timeline.preconditionsTimeline"; }
if ($_REQUEST["flag"] == "eff") {$field = "Timeline.effectsTimeline"; }

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");


// query per recuperare lo stato giusto
$sql = "SELECT printState, State.idState, typeState, statusState, CSSTimeline.id, printSet, nameState FROM (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) " . 
"JOIN Timeline ON $field = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit AND State.idState = '$id_state'" ;    
//echo $sql;

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");


$dati_stato = array();
$riga = $result->fetch_assoc();
$id_state = $riga["idState"];   
// JSON only parses utf-8 encoded data... (to be decoded client side)
$print_state =  utf8_encode($riga["printState"]);  
$print_set = utf8_encode($riga["printSet"]);
$type_state = $riga["typeState"];  
$status_state = $riga["statusState"];    
$description_state = utf8_encode($riga["nameState"]);
$dati_stato = array("id" => $id_state, "print"=>$print_state, "set" => $print_set, "type" => $type_state, "status" => $status_state, "description" => $description_state); 
// il set è l'id del CSS ... aggiungere  

// restituisco il Json perché all'onchange del menu nella pagina client possa essere visualizzato nel box apposito.
// il box esiste già ma è vuoto fino a quando non arriva questo Json

echo json_encode($dati_stato);

?>