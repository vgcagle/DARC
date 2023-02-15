<?php

include 'include/connessione.php';
//delete_action.php?unit=2

$id_action = $_REQUEST["action"];

$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("latin1");	

//cancello la unit da dov'è
$sql ="UPDATE Action SET Unit_idUnit = NULL WHERE idAction = $id_action";
//echo $sql;

$result = $conn->query($sql);
//die("Non riesco a cancellare l'azione dalla unit in cui si trova");

?>