<?php

include 'include/connessione.php';

//create_state.php?id= ...&type=...&status=...&print=
$my_text = addslashes($_REQUEST["text"]);
$start_line = $_REQUEST["start"];
$end_line = $_REQUEST["end"];
$unit = $_REQUEST["unit"];


$text = addslashes(file_get_contents('Texts/sample.txt'));
//$text = file_get_contents('Texts/sample.txt');
//$subtext = nl2br(substr($text, $start_line, $end_line)); 
$subtext = substr($text, $start_line, $end_line); 
//echo $subtext;



// Modulo sqli object-oriented with 
// Create connection
$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 





$conn->set_charset("utf8");
//update di Unit con startLine endLine e textUnit
$sql = "UPDATE `Unit` SET `startLine`= $start_line, `endLine` = $end_line, `textUnit` = '$my_text' WHERE `idUnit` = $unit";
//echo $sql;



if ($conn->query($sql) === TRUE) {
    $last_id = $conn->insert_id;
    } else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

//prelevare il testo nel db dalla start line alla end line

echo "ok";



$conn->close();	



?>