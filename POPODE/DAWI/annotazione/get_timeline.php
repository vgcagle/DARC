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
  
$result = $conn->query($sql); 
       
//if (!($result = mysqli_query($sql, $connessione)))
//		 	die("Non riesco a eseguire la query che recupera la timeline della unit");
       
//if (mysqli_num_rows ($result) > 1) {echo "Attenzione, questa unit si trova in più di una timeline!";}
        
$riga = $result->fetch_assoc();
$id_timeline_unit = $riga["TimelineUnit"];  
$description_unit = utf8_encode($riga["nameUnit"]);  
$print_unit = utf8_encode($riga["printUnit"]); 
$start_line = $riga["startLine"];
$end_line = $riga["endLine"];
$isref = $riga["isReference"];
$text_slice = $riga["textUnit"];
//$reference = $riga["idReferenceUnit"];

//$annotator_unit = $riga["Annotator_idAnnotator"];

// prelevo il nome dell'annotatore mettendo in join la tabella Unit_Annotator con la tabella Annotator e selezionando solo la unit 
$sql = "SELECT Name FROM Unit_Annotator JOIN Annotator ON `idAnnotator` = `Annotator_idAnnotator` WHERE Unit_idUnit = $id_unit";

$result = $conn->query($sql); 
$riga = $result->fetch_assoc();
$annotator_name = utf8_encode($riga["Name"]);




/*
if ($annotator_unit != ""){
		$sql = "SELECT Name FROM Annotator WHERE idAnnotator = $annotator_unit";
		//echo $sql;
		$result = $conn->query($sql); 
		$riga = $result->fetch_assoc();
		$annotator_name = utf8_encode($riga["Name"]);
	} else {
			$annotator_name = "";
			}
*/


$text = file_get_contents('Texts/sample.txt');
//$subtext = nl2br(substr($text, $start_line, $end_line)); 
$length = $end_line - $start_line;
$subtext = substr($text, $start_line, $length); 

/*
if ($reference != 0){

	$sql = "SELECT printUnit FROM Unit WHERE idUnit = $reference";
	$result = $conn->query($sql); 
	$riga = $result->fetch_assoc();
	$reference_name = utf8_encode($riga["printUnit"]);


} else $reference_name = "";
*/

$array_reference = array();

// prendo la lista di tutte le reference, nell'ordine in cui sono mappate
// le metto in un array 
$sql = "SELECT Unit2Reference.idReferenceUnit, printUnit FROM (Unit2Reference JOIN Unit ON Unit2Reference.idReferenceUnit = idUnit ) WHERE Unit_idUnit = $id_unit ORDER BY position";
//echo $sql;
$result = $conn->query($sql); 


while ($riga = $result->fetch_assoc())  {
	
	$ref = array();
	$ref["ref_unit"] = $riga["idReferenceUnit"];
	$ref["id_ref"] = utf8_encode($riga["printUnit"]);
	$array_reference[] = $ref;
}




$dati_unit = array();

$dati_unit["timeline"] = $id_timeline_unit;
$dati_unit["description"] = $description_unit;
$dati_unit["name"] = $print_unit;
$dati_unit["annotator"]= $annotator_name;
$dati_unit["reference"]= $array_reference;
$dati_unit["isref"] = $isref;
//$dati_unit["start"] = $start_line;
//$dati_unit["end"]= $end_line;
//$dati_unit["text"]= $subtext;
$dati_unit["text"]= utf8_encode($text_slice);



echo json_encode($dati_unit); 

?>