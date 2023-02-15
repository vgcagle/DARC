<?php


//restituisce le precondizioni e gli effetti di una unit, insieme alla precondizioni e agli effetti candidati
//{
//"preconditions":[{"id":"1244","print":"cddcdcdc","type":"BEL","status":"0","name":"aggiunta"}],
//"effects":[],
//"candidate_preconditions":[{"id":"6796","print":"cddcdcdc","type":"BEL","status":"0","name":"anticlimax"},{"id":"6901","print":"descrizione statissimo","type":"BEL","status":"1","name":"statissimo"},
//"candidate_effects[]"
//]}





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

$array_preconditions = array(); 

//qui recupero gli stati precondizione attuali
//JOIN su Timeline e CSSTimeline su preconditionsTimeline e poi su State 
$sql = "SELECT printState, State.idState, typeState, statusState, nameState FROM (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) " . 
"JOIN Timeline ON Timeline.preconditionsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit ORDER BY nameState";    
//echo $sql;     

if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
        
while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];   
			// JSON only parses utf-8 encoded data... (to be decoded client side)
			$print_state =  utf8_encode($riga["printState"]);  
			$type_state = $riga["typeState"];  
			$status_state = $riga["statusState"];  
			$name_state =  utf8_encode($riga["nameState"]);  
			$dati_precondition = array("id" => $id_state, "print"=>$print_state, "type" => $type_state, "status" => $status_state, "name" => $name_state); 
			$array_preconditions[] = $dati_precondition;
}  

//print_r ($array_preconditions);
    	
    	
$array_effects = array(); 
//qui recupero gli stati effetto attuali
//JOIN su Timeline e CSSTimeline e poi su State 
$sql = "SELECT printState, State.idState, typeState, statusState, nameState FROM (State JOIN CSSTimeline ON CSSTimeline.idState = State.idState) " . 
        "JOIN Timeline ON Timeline.effectsTimeline = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit ORDER BY nameState";    
//echo $sql;     
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");
         
    	while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];   
			// JSON only parses utf-8 encoded data... (to be decoded client side)
			$print_state =  utf8_encode($riga["printState"]);  
			$type_state = $riga["typeState"];  
			$status_state = $riga["statusState"];  
			$name_state =  utf8_encode($riga["nameState"]);   
			$dati_effect = array("id" => $id_state, "print"=>$print_state, "type" => $type_state, "status" => $status_state, "name" => $name_state); 
			$array_effects[] = $dati_effect;
}   
//print_r ($array_effects);
    	 

$array_stati = array();
//ora estraggo la lista di tutti gli stati, per differenza otterro gli stati candidati per precondizioni effetti
$sql = "SELECT printState, State.idState, typeState, statusState, nameState FROM State ORDER BY nameState";
    	
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera le precondizioni");   
		 	
    	while ($riga = $result->fetch_assoc()) {
			$id_state = $riga["idState"];   
			// JSON only parses utf-8 encoded data... (to be decoded client side)
			$print_state = utf8_encode($riga["printState"]);  
			$type_state = $riga["typeState"];  
			$status_state = $riga["statusState"];   
			$name_state =  utf8_encode($riga["nameState"]); 
			$dati_precondition_candidata = array("id" => $id_state, "print"=>$print_state, "type" => $type_state, "status" => $status_state, "name" => $name_state); 
			$array_stati[] = $dati_precondition_candidata;
}   


$lista_candidate_prec = array();
$l = count($array_stati);
$l2 = count($array_preconditions);



$result_array_candidate_preconditions = array();
foreach ($array_stati as $a) {	
	 //echo "confronto "; print_r ($a);  echo " con <br/>";
	 $found = 'false';	
	 for ($i = 0; $i < $l2 ; $i++) {
	 	//print_r ($array_preconditions[$i]); echo "<br/>";
	 	$confronto = array_diff_assoc($a,$array_preconditions[$i]);  
	 	//echo "risultato " ; print_r ($confronto); echo "<br/>";
	 	if ( count ($confronto) == 0) {	
	 		$found = 'true'; 
	 		//echo "esco! <br/>"; 
	 		break;
	 		}
	 	}
	 if ($found == 'false') {
	 	$result_array_candidate_preconditions[] = $a;
	 	}	
	 //echo "<hr>";			
	 }	

$l2 = count($array_effects);
$result_array_candidate_effects = array();
foreach ($array_stati as $a) {	
	 //echo "confronto "; print_r ($a);  echo " con <br/>";
	 $found = 'false';	
	 for ($i = 0; $i < $l2 ; $i++) {
	 	//print_r ($array_preconditions[$i]); echo "<br/>";
	 	$confronto = array_diff_assoc($a,$array_effects[$i]);  
	 	//echo "risultato " ; print_r ($confronto); echo "<br/>";
	 	if ( count ($confronto) == 0) {	
	 		$found = 'true'; 
	 		//echo "esco! <br/>"; 
	 		break;
	 		}
	 	}
	 if ($found == 'false') {
	 	$result_array_candidate_effects[] = $a;
	 	}	
	 //echo "<hr>";			
	 }	




$lista_candidate_eff = array_values(array_diff($array_stati, $array_effects)); 
 
 
$array_dati ["preconditions"] = $array_preconditions;
$array_dati ["effects"] = $array_effects;
$array_dati ["candidate_preconditions"] = $result_array_candidate_preconditions;
$array_dati ["candidate_effects"] = $result_array_candidate_effects;

//print_r ($array_dati);
     	
echo json_encode($array_dati); 

?>