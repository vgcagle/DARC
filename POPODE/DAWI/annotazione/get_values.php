<?php


//get_values.php
//restituisce (un array di) values estreandoli dalla tabella Value dove id = quello di agent
//[{"id":"120","print":"guarda che bello","name":"Piano prova 2","type":"Base","timeline":"5","agent":"1","goal":"45","action":null}]

include 'include/connessione.php';

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//recupero dalla tabella Plan i piani mappati sulla unit corrente
//SELECT * FROM Plan WHERE `mappingInit` = $id_unit AND `mappingEnd` = $id_unit
//$sql = "SELECT * FROM Plan WHERE mappingInit = $id_unit AND mappingEnd = $id_unit";
$sql = "SELECT * FROM Value ORDER BY `nameValue`";
//`Timeline_idTimeline`

$result = $conn->query($sql);

$values = array();

while ($row = $result->fetch_assoc()) {
	    // un array associativo per ogni piano
    	$value = array();
        $value["id"] = $row["idValue"];
        $value["description"] = utf8_encode($row["descriptionValue"]);
        $value["name"] = utf8_encode($row["nameValue"]);   
        
        //query su tabella agenti per recuperare il nome dell'agente
        $id_agent = $row["Agent_idAgent"];
        	
        	$sql = "SELECT * FROM Agent WHERE idAgent = $id_agent";
        	$result_agent = $conn->query($sql);
        	$row_agent = $result_agent->fetch_assoc();
        	$name_agent = utf8_encode($row_agent["nameAgent"]);
        $value["nameAgent"] = $name_agent;	
        
        
        $values[] = $value;  
        
        
        
        
    }

$conn->close();

//print_r ($plans);

echo json_encode($values);


?>
