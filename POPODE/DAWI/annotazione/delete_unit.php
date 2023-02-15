<?php

//delete_unit.php?unit=41&timeline=5877

include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];
$id_timeline = $_REQUEST["timeline"];





$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("latin1");	
	 		

// cancello l'annotatore
//DELETE Annotator.* FROM Annotator INNER JOIN Unit ON idAnnotator = Annotator_idAnnotator WHERE idUnit = 1

$sql = "DELETE Unit_Annotator.* FROM Unit_Annotator INNER JOIN Unit ON Unit_idUnit = idUnit WHERE idUnit = $id_unit";
$result = $conn->query($sql);

//cancellazione della unit	 		
$sql = "DELETE FROM `Unit` WHERE `idUnit` = $id_unit";
//echo $sql;

$result = $conn->query($sql);
//die("Non riesco a cancellare la unit dalla tabella Unit");
		 	
		 		 	
//1. query su CSS precondizioni e effetti (per cancellare i CSS da CSSTimeline)
$sql = "SELECT * FROM `Timeline` WHERE `idTimeline` = $id_timeline";
//
$result = $conn->query($sql);
//die("Non riesco a recuperare precondizioni e effetti della unit da cancellare");
		 	
$riga = $result->fetch_assoc();
$CSS_preconditions = $riga["preconditionsTimeline"];		 	
$CSS_effects = $riga["effectsTimeline"];


//2. cancellazione dei piani della Unit (prima venivano conservati nell'ipotesi che fossero multi-unit)
//i piani della unit sono quelli associati alla timeline della unit
$sql = "DELETE Plan.* FROM Plan WHERE Timeline_idTimeline = $id_unit";
$result = $conn->query($sql);



//3. cancellazione della timeline
$sql = "DELETE FROM `Timeline` WHERE `idTimeline` = $id_timeline";
//echo $sql;
$result = $conn->query($sql);
//		 	die("Non riesco a cancellare la timeline della unit cancellata");

//4. cancellazione dei CSS della unit
$sql = "DELETE FROM `CSSTimeline` WHERE `Set` = $CSS_preconditions OR `Set` = $CSS_effects";
//echo $sql;
$result = $conn->query($sql);
//		 	die("Non riesco a cancellare le precondizioni e effetti della unit da cancellare");


// pezzo rimasto in sospeso... non vengono create più actions per le unit		 	
//5. cancello il contenuto della unit (forse mettere NULL)
$sql = "UPDATE `Action` SET `Unit_idUnit` = 0 WHERE `Unit_idUnit` = $id_unit;";		 
//echo $sql;
$result = $conn->query($sql);
// die("Non riesco a cancellare gli incident dalla unit");	


//6. cancello i riferimenti alla unit nel campo idReferenceUnit
$sql = "UPDATE `Unit` SET `idReferenceUnit` = 0 WHERE `idReferenceUnit` = $id_unit;";	

//7. cancello la unit dalla tabella Pair
// cancello tutte le righe dove la unit è precedesPair o followsPair
$sql = "DELETE FROM `Pair` WHERE `precedesPair` = $id_unit OR `followsPair` = $id_unit";
$result = $conn->query($sql);


?>