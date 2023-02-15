<?php

//call: http://localhost:8888/.../createUnit.php?unit=prova2&annotator=Sempronio&description=UnitProva2&print=Ciccio
//output: {"unit":129,"annotator":3}


include 'include/connessione.php';

//create_state.php?id= ...&type=...&status=...&print=
$print = addslashes($_REQUEST["print"]);
$description = addslashes($_REQUEST["description"]);
$annotator = addslashes($_REQUEST["annotator"]);
$reference = $_REQUEST["ref"];


// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

// creazione della timeline della unit (ogni unit ha una timeline che contiene solo lei)
// creo due id, per effetti e precond della unit sulla sua timeline
$id_timeline_effects = rand(10000,20000);
$id_timeline_preconditions = rand(20000,30000);		 	
	 	
// creo la nuova timeline della unit con precondizioni e effetti	 	
$sql = "INSERT INTO Timeline (effectsTimeline, preconditionsTimeline) VALUES ($id_timeline_effects,  $id_timeline_preconditions)";
//echo $sql;


// documentation says that last_id deals with concurrency
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$id_timeline_unit = $last_id;


//INSERT INTO `Annotator` (`Name`) VALUES ('Tizio');
$sql = "INSERT INTO `Annotator` (`Name`) VALUES ('$annotator')";

if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$id_annotator = $last_id;

// metto la unit nuova nella tabella Unit

$sql = "INSERT INTO Unit (printUnit, TimelineUnit, nameUnit, Project_idProject, isReference) VALUES ('$print', $id_timeline_unit, '$description', 1, '$reference')";
echo $sql;

	 	
if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$id_unit = $last_id; 	


//dopo aver creato la unit, creo una nuova riga della tabella che collega Unit e Annotator
//Unit_Annotator
$sql = "INSERT INTO Unit_Annotator (Unit_idUnit, Annotator_idAnnotator) VALUES ($id_unit, $id_annotator);";

if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$new_unit = array();
$new_unit["unit"] = $id_unit;
$new_unit["annotator"] = $id_annotator; 


echo json_encode($new_unit);


$conn->close();	


?>