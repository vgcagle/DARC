<?php

include 'include/connessione.php';

$id_action = $_REQUEST["action"];
$id_unit = $_REQUEST["unit"];
$print_action = $_REQUEST["print"];
//echo "print azione: $print_action";


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("latin1");

//cerco la print e il name perché
//devo fare l'inserimento da capo, quindi non mi basta l'id dell'azione

$sql ="SELECT printAction, nameAction FROM Action WHERE idAction = $id_action";
//echo $sql;

if (!($result = $conn->query($sql)))
		 	die("Non riesco a trovare la print di questa action");
$riga = $result->fetch_assoc();
$print_action = $riga["printAction"];
$name_action = $riga["nameAction"];
	 	


//cancello la unit da dov'è
$sql ="DELETE FROM Action WHERE idAction = $id_action";


if (!($result = $conn->query($sql)))
		 	die("Non riesco a cancellare l'azione dalla unit in cui si trova");


// ora la attacco dove dev'essere
$sql = "INSERT INTO Action (idAction, printAction, Unit_idUnit, nameAction) VALUES ($id_action, '$print_action', $id_unit, '$name_action')";
echo $sql;

if (!($result = $conn->query($sql)))
		 	die("Non riesco a inserire l'azione nella unit");

?>