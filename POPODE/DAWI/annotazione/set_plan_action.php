<?php

//set_plan_action.php
include 'include/connessione.php';

/* 
API

plan=

new_action

*/


// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");

$new_action_id = 0;
$sql ="";

$id_plan = $_REQUEST["plan"];
$new_action = $_REQUEST["new_action"];


//leggo l'id dell'azione da sostituire
$sql = "SELECT Action_idAction FROM Plan WHERE idPlan = $id_plan"; 
$result = $conn->query($sql);
$row = $result->fetch_assoc(); 
$old_action = $row["Action_idAction"];


// creo la nuova azione e ne prendo l'id
// INSERT INTO `DrammarAnnotation`.`Action` (`idAction`, `printAction`, `Unit_idUnit`, `nameAction`) VALUES (NULL, 'prova', NULL, 'azione prova');
$sql = "INSERT INTO Action (nameAction) VALUES ('$new_action)";

if ($conn->query($sql) === TRUE) {
    $new_action_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;	
    }


// aggiorno il piano con la nuova azione
$sql = "UPDATE Plan SET Action_idAction = '$new_action_id' WHERE idPlan = $id_plan ";	
$conn->query($sql);

// cancello la vecchia azione
$sql = "DELETE FROM Action WHERE idAction = '$old_action'";
$conn->query($sql);

echo json_encode("ok");

?>

