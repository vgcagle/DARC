<?php

//delete_unit.php?unit=41&timeline=5877

include 'include/connessione.php';

$id_unit = $_REQUEST["unit"];
$id_timeline = $_REQUEST["timeline"];


$host = "localhost";
$username = "root";
$password = "root";
$db = "DrammarAnnotation";


if (!($connessione = mysql_connect($host, $username, $password)))
	 		die("Connessione fallita!");

if (!(mysql_select_db($db,$connessione)))
	 		die("Data base non trovato!");	
	 		

mysql_set_charset('utf8',$connessione);	 		
	 		
	 		
//cancellazione della unit	 		
$sql = "DELETE FROM `Unit` WHERE `idUnit` = $id_unit";
echo $sql;
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare la unit dalla tabella Unit");

		 	
		 		 	
//query su CSS precondizioni e effetti (per cancellare i CSS da CSSTimeline)
$sql = "SELECT * FROM `Timeline` WHERE `idTimeline` = $id_timeline";
echo $sql;
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a recuperare precondizioni e effetti della unit da cancellare");
		 	
$riga = mysql_fetch_array($result);
$CSS_preconditions = $riga["preconditionsTimeline"];		 	
$CSS_effects = $riga["effectsTimeline"];

//cancellazione della timeline
$sql = "DELETE FROM `Timeline` WHERE `idTimeline` = $id_timeline";
echo $sql;
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare la timeline della unit cancellata");

//cancellazione dei CSS
$sql = "DELETE FROM `CSSTimeline` WHERE `Set` = $CSS_preconditions OR `Set` = $CSS_effects";
echo $sql;
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare le precondizioni e effetti della unit da cancellare");
		 	
//cancello il contenuto della unit (forse mettere NULL)
$sql = "UPDATE `Action` SET `Unit_idUnit` = 0 WHERE `Unit_idUnit` = $id_unit;";		 
echo $sql;
if (!($result = mysql_query($sql, $connessione)))
		 	die("Non riesco a cancellare gli incident dalla unit");	

$sql = "DELETE FROM `Pair` WHERE `precedesPair` = $id_unit OR `followsPair` = $id_unit";
$result = $conn->query($sql)		 


?>