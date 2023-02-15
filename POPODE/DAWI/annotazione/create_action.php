<?php

include 'include/connessione.php';
//add_action.php?unit=2

$name_action = $_REQUEST["action"];
$print_action = $_REQUEST["print"];
$id_unit = $_REQUEST["unit"];
//$print_action = $_REQUEST["print"];
//echo "print azione: $print_action";


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 



$conn->set_charset("latin1");

$id_action = rand(1,10000);
// inserisco la action nella tabella Unit con l'id della unit

//INSERT INTO `DrammarAnnotation`.`Action` (`idAction`, `printAction`, `Unit_idUnit`) VALUES (NULL, 'Ofelia scappa su un chopper', '2');
$sql = "INSERT INTO `Action` (`idAction`, `printAction`, `nameAction`) VALUES ($id_action, '$print_action', '$name_action');";
//echo $sql;

//if (!($result = mysql_query($sql, $connessione)))
//		 	die("Non riesco a inserire la nuova azione nella tabella Action");
$result = $conn->query($sql);

echo $id_action;

?>