<?php

include 'include/connessione.php';

$id_action = $_REQUEST["action"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");


$sql = "SELECT * FROM Action WHERE idAction = $id_action";
$result = $conn->query($sql);
//die("Non riesco a inserire la nuova azione nella tabella Action");

$riga = $result->fetch_assoc();

$dati_action = array();
$name_action = utf8_encode($riga["nameAction"]);
$print_action = utf8_encode($riga["printAction"]);
$unit_action = $riga["Unit_idUnit"];

$dati_action["name"] = $name_action;
$dati_action["print"] = $print_action; 
$dati_action["idunit"] =  $unit_action;
$dati_action["id"] = $riga["idAction"];

//recupero la print della unit
$sql = "SELECT printUnit FROM Unit WHERE idUnit = '$unit_action'";
$result = $conn->query($sql);
//die("Non riesco a trovare il nome della unit nella tabella Unit");
//echo $sql; 

$riga = $result->fetch_assoc();
$name_unit = $riga["printUnit"];
$dati_action["nameunit"] =  $name_unit;

echo json_encode($dati_action);


?>