<?php

include 'include/connessione.php';

// esempio di chiamata
// changePair (candidate_unit, unit, timeline, flag){
// change_pair.php?candidate_unit=3&unit=2&timeline=4&flag=pre

$id_candidate_unit = $_REQUEST["candidate_unit"];
$id_unit = $_REQUEST["unit"];
$id_timeline = $_REQUEST["timeline"];
$flag = $_REQUEST["flag"];

if ($flag == "pre") {$col = "followsPair"; } else {$col = "precedesPair";}


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");
     
// recupero l'id della coppia precede-segue sulla timeline 0 e quella della unit
$sql_id_pair_timeline_0 = "SELECT idPair, precedesPair FROM Pair WHERE ($col = $id_unit AND Timeline_idTimeline = 0)";
//echo $sql_id_pair_timeline_0; echo "<br />";

//NON SERVE
//$sql_id_pair_timeline_unit = "SELECT idPair, precedesPair FROM Pair WHERE ($col = $id_unit AND Timeline_idTimeline = $id_timeline)";
//echo $sql_id_pair_timeline_unit; echo "<br />";

// scrivo la nuova coppia in pair e aggiorno la timeline 0 
// idPair precedesPair followsPair Timeline_idTimeline
// NON AGGIORNO la timeline della unit con l'id della nuova coppia

$result = $conn->query($sql_id_pair_timeline_0);
//die("Non riesco a eseguire la query che recupera l'id della coppia da cambiare sulla timeline generale");


//se la unit non ha una unit che precede/segue, la query restituisce un id vuoto perché la coppia non esiste
//$num_rows = mysql_num_rows($result);

if ($result->num_rows > 0) {

	$riga = $result->fetch_assoc();
	$id_coppia_timeline_0 = $riga["idPair"];
 	
	/*
	if (!($result = mysql_query($sql_id_pair_timeline_unit, $connessione)))
		 	die("Non riesco a eseguire la query che recupera l'id della coppia  precedente sulla timeline della unit");
    
	$riga = mysql_fetch_array($result);
	$id_coppia_timeline_unit = $riga["idPair"]; 
	*/

	if ($flag == "pre") {
	$sql = "UPDATE Pair SET precedesPair = $id_candidate_unit WHERE idPair = $id_coppia_timeline_0";
	//echo $sql; ; echo "<br />";	
		} else {
		$sql = "UPDATE Pair SET followsPair = $id_candidate_unit WHERE idPair = $id_coppia_timeline_0";
	//echo $sql; ; echo "<br />";	
	}		 	


	//if (!($result = mysql_query($sql, $connessione)))
	//	 	die("Non riesco a eseguire la query che scrive la nuova unit precedente nella timeline generale");
	$result = $conn->query($sql);

} else {
	
	if ($flag == "pre") {
	$sql = "INSERT INTO Pair (`precedesPair`, `followsPair`, `Timeline_idTimeline`) VALUES ($id_candidate_unit, $id_unit, '0')";
	//echo $sql; ; echo "<br />";	
		} else {
		$sql = "INSERT INTO Pair (`precedesPair`, `followsPair`, `Timeline_idTimeline`) VALUES ($id_unit, $id_candidate_unit, '0')";
	//echo $sql; ; echo "<br />";	
	}		 	


	//if (!($result = mysql_query($sql, $connessione)))
	//	 	die("Non riesco a eseguire la query che scrive la nuova unit precedente nella timeline generale");
	$result = $conn->query($sql);
	
	
}

// NON AGGIORNO LA COPPIA DELLA TIMELINE
/*
if ($flag == "pre") {
$sql = "UPDATE Pair SET precedesPair = $id_candidate_unit WHERE idPair = $id_coppia_timeline_unit";
//echo $sql; echo "QUI $flag <br />";
} else {
$sql = "UPDATE Pair SET followsPair = $id_candidate_unit WHERE idPair = $id_coppia_timeline_unit";
//echo $sql; echo "QUA $flag <br />";	
}

if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a eseguire la query che che scrive la nuova unit precedente nella timeline della unit");
*/


// leggo la nuova unit che precede e la restituisco in formato Json
if ($flag == "pre") {
$sql_new_prec_unit_timeline_0 = "SELECT * FROM (Pair JOIN Unit ON precedesPair = idUnit) WHERE (followsPair = $id_unit AND Timeline_idTimeline = 0)";
//echo $sql_new_prec_unit_timeline_0; echo "<br/>";
} else {
$sql_new_prec_unit_timeline_0 = "SELECT * FROM (Pair JOIN Unit ON followsPair = idUnit) WHERE (precedesPair = $id_unit AND Timeline_idTimeline = 0)";
//echo $sql_new_prec_unit_timeline_0; echo "<br/>";	
}


//if (!($result = mysql_query($sql_new_prec_unit_timeline_0, $connessione)))
//		 	die("Non riesco a eseguire la query che che legge la nuova unit precedente nella timeline della unit");
$result = $conn->query($sql_new_prec_unit_timeline_0);

$riga = $result->fetch_assoc();

$dati_unit = array();

$nuovo_id = $riga["idUnit"]; 
$nuovo_print = $riga["printUnit"]; 
$nuova_description = $riga["nameUnit"];
$dati_unit["id"] = $nuovo_id; 
$dati_unit["print"] = $nuovo_print; 
$dati_unit["description"]= $nuova_description;
	
echo json_encode($dati_unit);

    	      
?>