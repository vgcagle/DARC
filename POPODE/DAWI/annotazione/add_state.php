<?php

include 'include/connessione.php';

//add_state.php?state=3&timeline=4&flag=eff
//add_state.php?state=2707&timeline=9952&flag=pre

$id_state = $_REQUEST["state"];
$id_timeline_unit = $_REQUEST["timeline"];
$flag = $_REQUEST["flag"];

if ($_REQUEST["flag"] == "pre") {$field = "preconditionsTimeline"; }
if ($_REQUEST["flag"] == "eff") {$field = "effectsTimeline"; }


$conn = new mysqli($host, $username, $password, $db);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$conn->set_charset("utf8");


//aggiungo lo stato al CSS della unit
//mi servono: stato, CSS

//recupero CSS della Unit
//$sql = "SELECT CSSTimeline.Set, printSet FROM CSSTimeline JOIN Timeline ON $field = CSSTimeline.Set WHERE idTimeline = $id_timeline_unit" ;    
$sql = "SELECT $field FROM Timeline WHERE idTimeline = $id_timeline_unit";
//echo $sql;


$result = $conn->query($sql); 
//if (!($result = mysql_query($sql, $connessione)))
//		 	die("Non riesco a eseguire la query che recupera il CSS della unit");

//$riga = mysql_fetch_array($result);
$riga = $result->fetch_assoc();

$id_CSS = utf8_encode($riga[$field]);

$printSet = "CSS $id_CSS";

//collego lo stato al CSS Timeline inserendo una nuova riga nella tabella CSS che punta allo stato dato
//INSERT INTO `DrammarAnnotation`.`CSSTimeline` (`id`, `Set`, `printSet`, `idState`) VALUES (NULL, '12', 'CSS 12', '3');

$sql = "INSERT INTO `CSSTimeline` (`Set`, `printSet`, `idState`) VALUES ($id_CSS, '$printSet', '$id_state')";
echo $sql;


if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a eseguire la query che aggiunge lo stato al CSS");




?>