<?php

include 'include/connessione.php';

$id_action = $_REQUEST["action"];
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$conn->set_charset("latin1");
	 		
//cancellazione della action	 		
$sql = "DELETE FROM `Action` WHERE `idAction` = $id_action";
//echo $sql;
$result = $conn->query($sql);
//die("Non riesco a cancellare l'azione dalla tabella Action");


?>