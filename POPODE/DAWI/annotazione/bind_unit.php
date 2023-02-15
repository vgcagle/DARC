<?php

include 'include/connessione.php';

/*

inserisce una nuova reference unit tra i binding della unit data nella posizione di
una unit già presente

$before vale 

-first se non ci sono reference già posizionate
-id della reference prima della quale metterla
-last se va per ultima

*/


$unit = $_REQUEST["unit"];
$bind_unit = $_REQUEST["bind"];
$before = $_REQUEST["order"];
$new_pos = 0;

// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("utf8");


//update di Unit con startLine endLine e textUnit
//$sql = "UPDATE `Unit` SET `idReferenceUnit`= $bind_unit WHERE `idUnit` = $unit";
//echo $sql;

// solo se la unit $before è diversa da 0 devo far slittare le reference già presenti


if ($before == "first") {$new_pos = 1;} 


if ($before == "last") { 
		//sono nel caso "last"
		//recupero l'ordine più altro 
		$sql = "SELECT MAX(`position`) as maxpos FROM Unit2Reference WHERE `Unit_idUnit` = '$unit'";
		//echo $sql;
		
		$result = $conn->query($sql); 
		$riga = $result->fetch_assoc();
		$new_pos = $riga["maxpos"];
		$new_pos = $new_pos + 1;
		
	}

if (($before != "first") && ($before != "last")){

	// siamo nel caso in cui c'è una unit "before"
	// 0. trovo la posizione della unit before
	$sql = "SELECT position FROM Unit2Reference WHERE Unit_idUnit = $unit AND idReferenceUnit = $before";
	$result = $conn->query($sql); 
	$riga = $result->fetch_assoc();
	$new_pos = $riga["position"];

	// 1. faccio slittare in avanti tutte le reference units successive a quella da insierire 
	$sql = "UPDATE Unit2Reference SET `position` =  `position` + 1 WHERE Unit_idUnit = $unit and `position` >= $new_pos";
	//echo $sql;

	if ($conn->query($sql) === TRUE) {
    	$last_id = $conn->insert_id;
    	} else {
    	echo "Error: " . $sql . "<br>" . $conn->error;
	}	

} 
	



// 2. ora inserisco la nuova reference
$sql = "INSERT INTO `Unit2Reference`(`Unit_idUnit`, `idReferenceUnit`, `position`) VALUES ($unit,$bind_unit,$new_pos)";
echo $sql;

$result = $conn->query($sql); 


/* 
JOIN Unit2Reference e Unit 
per recuperare i nomi delle unit che sono bound a quella corrente

$sql = "SELECT printUnit, idUnit FROM Unit JOIN Unit2Reference ON idReferenceUnit = idUnit  WHERE idUnit = $bind_unit";

$result = $conn->query($sql); 
$lista_reference = array();

while ($riga = $result->fetch_assoc()){
	$lista_reference[] = $riga;
}

//generare lista di units 
$reference_list = json_encode($lista_reference);

*/

$conn->close();	

//echo $reference_list;
echo "ok";



?>