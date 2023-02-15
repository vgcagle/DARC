<?php

include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

$sql = "SELECT * FROM Unit WHERE idUnit = $id_unit";
//echo $sql;echo "<br/>";


//$sql = "SELECT DISTINCT Timeline_idTimeline FROM Pair WHERE ((precedesPair = $id_unit OR followsPair = $id_unit) AND Timeline_idTimeline != 0)";
//echo $sql;
       
if (!($result = $conn->query($sql)))
		 	die("Non riesco a eseguire la query che recupera la timeline della unit");
       
//if (mysqli_num_rows ($result) > 1) {echo "Attenzione, questa unit si trova in più di una timeline!";}
  
       
$riga = $result->fetch_assoc() ;
$id_timeline_unit = $riga["TimelineUnit"];  
$description_unit = utf8_encode($riga["nameUnit"]); 
$name_unit = utf8_encode($riga["printUnit"]); 
$annotator_unit = utf8_encode($riga["Annotator_idAnnotator"]);


$dati_unit = array();

$dati_unit["timeline"] = $id_timeline_unit;
$dati_unit["description"] = $description_unit;
$dati_unit["name"] = $name_unit;
$dati_unit["annotator"]= $annotator_unit;

echo json_encode($dati_unit); 

?>